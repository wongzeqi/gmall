package com.wjq.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjq.gmall.bean.*;
import com.wjq.gmall.manage.mapper.*;
import com.wjq.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;
    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;
    @Autowired
    PmsProductImageMapper pmsProductImageMapper;




    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    //保存spu  其中包含  销售属性   以及 销售属性的值

    @Override
    public String saveSpuInfo(PmsProductInfo pmsProductInfo) {
        //将pmsProductInfo先保存到数据库中  之后主键返回
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
        //获取spu 销售属性列表
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductInfo.getSpuSaleAttrList();
        //获取spu 图片信息列表 并保存到数据库
        List<PmsProductImage> pmsProductImages = pmsProductInfo.getSpuImageList();
        for(PmsProductImage image:pmsProductImages){
            image.setProductId(pmsProductInfo.getId());
            pmsProductImageMapper.insertSelective(image);
        }

        for(PmsProductSaleAttr attr:pmsProductSaleAttrs){
            attr.setProductId(pmsProductInfo.getId());
            pmsProductSaleAttrMapper.insertSelective(attr);
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = attr.getSpuSaleAttrValueList();
            for (PmsProductSaleAttrValue attrValue:pmsProductSaleAttrValues){
                attrValue.setProductId(pmsProductInfo.getId());
                attrValue.setSaleAttrId(attr.getSaleAttrId());
                pmsProductSaleAttrValueMapper.insertSelective(attrValue);
            }
        }
        return "success";
    }

    //获取 销售属性列表
    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = pmsBaseSaleAttrMapper.selectAll();
        return pmsBaseSaleAttrs;
    }


    //根据spuId 即产品id  查询sup的销售属性  和销售属性值
    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        //先根据supId 也即product查询出所有的 sup的销售属性
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
        pmsProductSaleAttr.setProductId(spuId);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        //循环遍历 查询出所有的属性值
        PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
        for(PmsProductSaleAttr pmsProductSaleAttr1 : pmsProductSaleAttrs){
            //根据商品id和属性id 同时查询出属性值
            pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr1.getSaleAttrId());
            pmsProductSaleAttrValue.setProductId(pmsProductSaleAttr1.getProductId());
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
            pmsProductSaleAttr1.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }
        return pmsProductSaleAttrs;
    }


    //查询spuImage  查询所有的图片
    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage = new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }



    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.selectSpuSaleAttrListCheckBySku(productId,skuId);
        return pmsProductSaleAttrs;
    }




}
