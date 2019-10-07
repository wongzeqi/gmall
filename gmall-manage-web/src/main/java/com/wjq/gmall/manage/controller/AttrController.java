package com.wjq.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wjq.gmall.bean.PmsBaseAttrInfo;
import com.wjq.gmall.bean.PmsBaseAttrValue;
import com.wjq.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@CrossOrigin
@Controller
public class AttrController {

    @Reference
    AttrService attrService;

    //查询三级分类中的所有属性的
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList (String catalog3Id){
        List<PmsBaseAttrInfo> pmsBaseAttrInfos =  attrService.attrInfoList(catalog3Id);
        return pmsBaseAttrInfos;
    }


    //添加属性 和  属性值
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo (@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        return attrService.saveAttrInfo(pmsBaseAttrInfo);
    }

    //获取属性对应的是所有属性值

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList (String attrId){
        List<PmsBaseAttrValue> pmsBaseAttrValues =  attrService.getAttrValueList(attrId);
        return pmsBaseAttrValues;
    }

    //修改属性值



}
