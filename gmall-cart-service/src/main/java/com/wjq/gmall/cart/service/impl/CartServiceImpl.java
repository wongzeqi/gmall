package com.wjq.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.wjq.gmall.bean.OmsCartItem;
import com.wjq.gmall.cart.mapper.OmsCartItemMapper;


import com.wjq.gmall.util.RedisUtil;
import com.wjq.gmall.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper cartMapper;
    @Autowired
    RedisUtil redisUtil;
    @Override
    public List<OmsCartItem> getCartsByUser(String memberId) {
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            List<String> hvals = jedis.hvals("user:"+memberId+":cart");
            for (String hval : hvals) {
                OmsCartItem omsCartItem = JSON.parseObject(hval,OmsCartItem.class);
                omsCartItems.add(omsCartItem);
            }
        }catch (Exception e){
            e.printStackTrace();
            //logService.addErrlog(e.getMessage);
        }finally {
            jedis.close();
        }
        return omsCartItems;
    }

    @Override
    public void addCart(OmsCartItem omsCartItem) {
        if(StringUtils.isNotBlank(omsCartItem.getMemberId())){
            cartMapper.insert(omsCartItem);
        }
    }

    @Override
    public void updateCart(OmsCartItem omsCartItem) {//根据id更新
        Example e  = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("id",omsCartItem.getId());
        cartMapper.updateByExample(omsCartItem,e);
    }

    @Override
    public void flushCartCache(String memberId) {
        OmsCartItem omsCartItemForSelect = new OmsCartItem();
        omsCartItemForSelect.setMemberId(memberId);
        List<OmsCartItem> omsCartItems = cartMapper.select(omsCartItemForSelect);
        Jedis jedis = null;
        try {
            //同步到redis缓存中
            jedis = redisUtil.getJedis();
            //设计redis的存储结构  hash 去存储  memberId skuId cartInfo    方便查询和修改
            Map<String, String> map = new HashMap<>();
            for (OmsCartItem omsCartItem : omsCartItems) {
                map.put(omsCartItem.getProductSkuId(), JSON.toJSONString(omsCartItem));
            }
            jedis.hmset("user:" + memberId + ":cart", map);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    @Override
    public OmsCartItem ifCartExitByUser(String memberId, String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        OmsCartItem omsCartItem1 = cartMapper.selectOne(omsCartItem); //这个如过查询出两条数据  可能存在异常
        return omsCartItem1;
    }

    @Override
    public void checkCart(OmsCartItem omsCartItem) {

        Example e = new Example(OmsCartItem.class);

        e.createCriteria().andEqualTo("memberId",omsCartItem.getMemberId()).andEqualTo("productSkuId",omsCartItem.getProductSkuId());

        cartMapper.updateByExampleSelective(omsCartItem,e);

        // 缓存同步
        flushCartCache(omsCartItem.getMemberId());

    }
}
