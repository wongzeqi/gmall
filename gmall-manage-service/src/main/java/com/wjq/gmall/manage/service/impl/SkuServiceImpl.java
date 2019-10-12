package com.wjq.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjq.gmall.bean.PmsSkuAttrValue;
import com.wjq.gmall.bean.PmsSkuImage;
import com.wjq.gmall.bean.PmsSkuInfo;
import com.wjq.gmall.bean.PmsSkuSaleAttrValue;
import com.wjq.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.wjq.gmall.manage.mapper.PmsSkuImageMapper;
import com.wjq.gmall.manage.mapper.PmsSkuInfoMapper;
import com.wjq.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.wjq.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


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
    public PmsSkuInfo getSkuById(String skuId) {
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

        //sku平台属性列表
//        PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
//        pmsSkuAttrValue.setSkuId();
//        pmsSkuInfo.setSkuAttrValueList();
//        pmsSkuInfo.setSkuSaleAttrValueList();

        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(String productId) {

        return pmsSkuInfoMapper.selectSkuSaleAttrValueListBySpu(productId);
    }


}
