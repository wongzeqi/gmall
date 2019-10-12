package com.wjq.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wjq.gmall.service.CatalogService;
import com.wjq.gmall.bean.PmsBaseCatalog1;
import com.wjq.gmall.bean.PmsBaseCatalog2;
import com.wjq.gmall.bean.PmsBaseCatalog3;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//跨域请求问题
@CrossOrigin
@Controller
public class CatalogController {


    @Reference
    CatalogService catalogService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> catalog1s = catalogService.getCatalog1();
        return catalog1s;
    }



    //根据一级分类查询所有的二级分类
    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        List<PmsBaseCatalog2> catalog2s = catalogService.getCatalog2(catalog1Id);
        return catalog2s;
    }


    //根据二级分类查询  三级分类
    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        List<PmsBaseCatalog3> catalog3s = catalogService.getCatalog3(catalog2Id);
        return catalog3s;
    }

}
