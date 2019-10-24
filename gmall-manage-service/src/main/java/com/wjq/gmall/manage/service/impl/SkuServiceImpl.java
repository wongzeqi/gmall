package com.wjq.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.wjq.gmall.bean.PmsSkuAttrValue;
import com.wjq.gmall.bean.PmsSkuImage;
import com.wjq.gmall.bean.PmsSkuInfo;
import com.wjq.gmall.bean.PmsSkuSaleAttrValue;
import com.wjq.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.wjq.gmall.manage.mapper.PmsSkuImageMapper;
import com.wjq.gmall.manage.mapper.PmsSkuInfoMapper;
import com.wjq.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.wjq.gmall.util.RedisUtil;
import com.wjq.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.UUID;


@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;
    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;
    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;
    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;
    //注入redisUtil
    @Autowired
    RedisUtil redisUtil;

    @Override
    public String saveSkuInfo(PmsSkuInfo skuInfo) {
        //我快要被这个给搞疯了  前台传来的 是spuId  数据库使用的是productId
        skuInfo.setProductId(skuInfo.getSpuId());
        //前台传来的居然是productName我快吐血了
        skuInfo.setSkuName(skuInfo.getProductName());
        //System.out.println(skuInfo.getSpuId());
        pmsSkuInfoMapper.insertSelective(skuInfo);
        List<PmsSkuAttrValue> pmsSkuAttrValues = skuInfo.getSkuAttrValueList(); //获取sku平台属性值列表
        List<PmsSkuImage> pmsSkuImages =skuInfo.getSkuImageList();//获取sku图片列表
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = skuInfo.getSkuSaleAttrValueList(); //获取sku销售属性列表

        for(PmsSkuImage pmsSkuImage:pmsSkuImages ){
            pmsSkuImage.setSkuId(skuInfo.getId()); //获取主键返回的主键值

            pmsSkuImageMapper.insert(pmsSkuImage);
        }
        for(PmsSkuAttrValue pmsSkuAttrValue: pmsSkuAttrValues){
            pmsSkuAttrValue.setSkuId(skuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue:pmsSkuSaleAttrValues){
            pmsSkuSaleAttrValue.setSkuId(skuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        return "success";
    }


    //根据id获取skuInfo
    @Override
    public PmsSkuInfo getSkuByIdFromDb(String skuId) {
        //sku商品信息对象
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        pmsSkuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        //查询出所有的图片
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        //sku图片集合
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo.setSkuImageList(pmsSkuImages);
        return pmsSkuInfo;
    }

    //上面一个版本的修改版  先从redis数据库中查询数据 查到了就返回给前端 如果没有查到则返回 再进行
    //数据库查询 再将查询到的结果存放到数据库中

    @Override
    public PmsSkuInfo getSkuById(String skuId) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        Jedis jedis = new Jedis();
        try {
            //连接redis数据库
            jedis = redisUtil.getJedis();
            //查询缓存
            String skuKey = "sku:" + skuId + ":info";
            String skuJson = jedis.get(skuKey);
            if (StringUtils.isNotBlank(skuJson)) {
                pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
            } else {
                //如果缓存没有 则查询Mysql
                //设置分布式锁  限制请求
                String token = UUID.randomUUID().toString();
                String OK = jedis.set("sku:"+skuId+":lock",token,"nx","px",10);//10秒的过期时间
                if(StringUtils.isNoneBlank()&&OK.equals("OK")){
                    pmsSkuInfo = getSkuByIdFromDb(skuId);//上锁成功进行数据库访问
                    if (pmsSkuInfo != null) {
                        jedis.set("sku:" + skuId + ":info", JSON.toJSONString(pmsSkuInfo));
                    } else {//防止缓存 穿透
                        jedis.setex("sku:" + skuId + ":info", 60 * 3, JSON.toJSONString(""));
                    }
                    String lockToken = jedis.get("sku:" + skuId + ":lock");
                    if(StringUtils.isNotBlank(lockToken)&&lockToken.equals(token)){ //确保删除的是自己的锁  并且确保我的锁过期
                        //判断前  还没过期 或者 还没被删除  ....
                        //当我要删除的时候  它过期了  或者删除了  怎么办   可以使用lua脚本 在查询到key的同时删除 改key 防止在高并发下的发生意外
                        //String script = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                        //jedis.eval(script);
                        jedis.del("sku:"+skuId+":lock");//释放锁
                    }
                }else{
                    //上锁失败  无权访问数据库   自旋 （该线程先睡几秒  在重新尝试访问此方法）
                    Thread.sleep(3000);
                    return getSkuById(skuId);//重写在发送一次请求
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return pmsSkuInfo;
    }


    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {
        return pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
    }


    @Override
    public List<PmsSkuInfo> getAllSkuInfo() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            String skuId = pmsSkuInfo.getId();
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();

            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }

        return pmsSkuInfos;
    }

}
