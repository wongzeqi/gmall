package com.wjq.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wjq.gmall.bean.PmsBaseSaleAttr;
import com.wjq.gmall.bean.PmsProductImage;
import com.wjq.gmall.bean.PmsProductInfo;
import com.wjq.gmall.bean.PmsProductSaleAttr;
import com.wjq.gmall.service.SpuService;
import com.wjq.util.PmsUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@Controller
public class SpuController {

    @Reference
    SpuService spuService;


    //根据商品的三级分类查询spu  标准的商品单元
    @RequestMapping("spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(String catalog3Id){

        List<PmsProductInfo> pmsProductInfos = spuService.spuList(catalog3Id);

        return  pmsProductInfos;
    }



    //根据商品的三级分类查询spu  标准的商品单元
    @RequestMapping("saveSpuInfo")
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        String msg = spuService.saveSpuInfo(pmsProductInfo);
        return msg;
    }


    //获取baseSaleAttrList 基本的销售属性  这个应该是归平台管理的
    @RequestMapping("baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList (){
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs = spuService.baseSaleAttrList();
        return pmsBaseSaleAttrs;
    }


    //文件上传
    @RequestMapping("fileUpload")
    @ResponseBody
    public String baseSaleAttrList (@RequestParam("file")MultipartFile multipartFile){
        //将图片上传到分布式文件系统上
        String imgUrl = PmsUploadUtil.uploadImage(multipartFile);
        System.out.println(imgUrl);
        //将
        return imgUrl;
    }


    //spuSaleAttrList
    @RequestMapping("spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){//spuId  就是productId
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    @RequestMapping("spuImageList")
    @ResponseBody
    public  List<PmsProductImage> spuImageList(String spuId){
        List<PmsProductImage> pmsProductImages = spuService.spuImageList(spuId);
        return pmsProductImages;
    }
}
