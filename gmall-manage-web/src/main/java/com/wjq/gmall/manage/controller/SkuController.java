package com.wjq.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wjq.gmall.bean.PmsSkuInfo;
import com.wjq.gmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@Controller
public class SkuController {

    @Reference
    SkuService skuService;

    @RequestMapping("saveSkuInfo")
    public String saveSkuInfo(@RequestBody PmsSkuInfo skuInfo){
        String msg = skuService.saveSkuInfo(skuInfo);
        return msg;
    }

}
