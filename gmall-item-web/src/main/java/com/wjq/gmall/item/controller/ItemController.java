package com.wjq.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.wjq.gmall.service.AttrService;
import com.wjq.gmall.service.SkuService;

import com.wjq.gmall.service.SpuService;
import com.wjq.gmall.bean.PmsProductSaleAttr;
import com.wjq.gmall.bean.PmsSkuInfo;
import com.wjq.gmall.bean.PmsSkuSaleAttrValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@CrossOrigin
@Controller
public class ItemController {

    @Reference
    SkuService skuService;

    @Reference
    SpuService spuService;

    @Reference
    AttrService attrService;


    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map){
        //获取sku信息
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        map.put("skuInfo",pmsSkuInfo);
//        //获取平台属性和销售属性
//        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
//        pmsProductSaleAttr.setProductId(pmsSkuInfo.getProductId());
//        List<PmsProductSaleAttr> pmsProductSaleAttrs = attrService.selectByProductId(pmsProductSaleAttr);
//        //根据销售属性id 和 spuid 查询销售销售值
//        List<PmsProductSaleAttrValue> pmsProductSaleAttrValues = null;
//        PmsProductSaleAttrValue pmsProductSaleAttrValue = new PmsProductSaleAttrValue();
//
//        for(PmsProductSaleAttr pmsProductSaleAttr1 : pmsProductSaleAttrs){
//            pmsProductSaleAttrValue.setProductId(pmsProductSaleAttr1.getProductId());
//            pmsProductSaleAttrValue.setSaleAttrId(pmsProductSaleAttr1.getSaleAttrId());
//            pmsProductSaleAttrValues = attrService.getProductSaleAttrValueList(pmsProductSaleAttrValue);
//            pmsProductSaleAttr1.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
//        }

        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);

        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

        HashMap<String,String> skuSaleAttrHash = new HashMap<>();

        List<PmsSkuInfo> pmsSkuInfos = skuService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getProductId());
        //将pmsSkuInfos变成hash表
        for (PmsSkuInfo pmsSkuInfo1 : pmsSkuInfos){
            String k = "";
            String v = pmsSkuInfo1.getId();
            for(PmsSkuSaleAttrValue pmsSkuSaleAttrValue : pmsSkuInfo1.getSkuSaleAttrValueList()){
                k+=pmsSkuSaleAttrValue.getSaleAttrValueId()+"|";
            }
            skuSaleAttrHash.put(k,v);
        }

        String skuSaleAttrHashJsonStr = JSON.toJSONString(skuSaleAttrHash);

        map.put("skuSaleAttrHashJsonStr",skuSaleAttrHashJsonStr);
        return "item";
    }







    @RequestMapping("index")
    public String index(ModelMap modelMap){

        List<String> list = new ArrayList<String>();
        for (int i = 0; i <5 ; i++) {
            list.add("循环数据"+i);
        }
        modelMap.put("hello","hello thymeleaf!!!");
        modelMap.put("list",list);
        modelMap.put("check","0");
        return "index";
    }


}
