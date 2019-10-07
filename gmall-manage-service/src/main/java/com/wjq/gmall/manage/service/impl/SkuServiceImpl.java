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
        pmsSkuInfoMapper.insertSelective(skuInfo);
        List<PmsSkuAttrValue> pmsSkuAttrValues = skuInfo.getSkuAttrValueList(); //获取sku平台属性值列表
        List<PmsSkuImage> pmsSkuImages =skuInfo.getSkuImageList();//获取sku图片列表
        List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = skuInfo.getSkuSaleAttrValueList(); //获取sku销售属性列表

        for(PmsSkuImage pmsSkuImage:pmsSkuImages ){
            pmsSkuImageMapper.insert(pmsSkuImage);
        }
        for(PmsSkuAttrValue pmsSkuAttrValue: pmsSkuAttrValues){
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue:pmsSkuSaleAttrValues){
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }

        return "success";
    }
}
