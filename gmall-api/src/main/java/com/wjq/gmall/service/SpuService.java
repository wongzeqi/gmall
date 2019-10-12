package com.wjq.gmall.service;

import com.wjq.gmall.bean.PmsBaseSaleAttr;
import com.wjq.gmall.bean.PmsProductImage;
import com.wjq.gmall.bean.PmsProductInfo;
import com.wjq.gmall.bean.PmsProductSaleAttr;


import java.util.List;




public interface SpuService{
    List<PmsProductInfo> spuList(String catalog3Id);

    String saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);



    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String productId,String skuId);
}
