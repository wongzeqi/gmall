package com.wjq.gmall.search.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.wjq.gmall.bean.*;
import com.wjq.gmall.service.AttrService;
import com.wjq.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @Reference
    AttrService attrService;

    //主页
    @RequestMapping("index")
    public String index (){
        return "index";
    }


    //点击三级分类  列表页
    @RequestMapping("list.html")
    public String list (PmsSearchParam pmsSearchParam , ModelMap modelMap){
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList",pmsSearchSkuInfos);
        //抽取平台属性值id集合
        Set<String> valueIdSet = new HashSet<>();

        //比使用聚合函数要快  必查数据库也要快
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }

        }

        List<PmsBaseAttrInfo> pmsBaseAttrInfos = new ArrayList<>();
        if(valueIdSet.size()>0){
            pmsBaseAttrInfos = attrService.getAttrValueListByValueIds(valueIdSet);
            modelMap.put("attrList",pmsBaseAttrInfos);
        }

        String[] delValueIds = pmsSearchParam.getValueId();
//删除不是这样写的   ArrayList删除效率很低 且索引会重排导致数组越界异常
//        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
//            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
//            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
//                String valueId = pmsSkuAttrValue.getId();
//                for (String delId : delValueIds){
//                    if(valueId.equals(delId)){
//                        //删除当前value所在的属性组
//
//                    }
//                }
//            }
//        }
        if (delValueIds != null) {
            // 面包屑
            // pmsSearchParam
            // delValueIds
            List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
            for (String delValueId : delValueIds) {
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                // 生成面包屑的参数
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchParam, delValueId));
                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        String valueId = pmsBaseAttrValue.getId();
                        if (delValueId.equals(valueId)) {
                            // 查找面包屑的属性值名称
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            //删除该属性值所在的属性组
                            iterator.remove();
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
            modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        }


        //获取  拼接url请求参数
        String urlParam = getUrlParam(pmsSearchParam);

        String keyword = pmsSearchParam.getKeyword();

        if(StringUtils.isNotBlank(keyword)){
            modelMap.put("keyword",keyword);
        }

        modelMap.put("urlParam",urlParam);







        return "list";
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam,String  delValueId) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueIds = pmsSearchParam.getValueId();

        String urlParam = "";
        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam = urlParam +"keyword="+keyword;
        }

        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam = urlParam +"catalog3Id="+catalog3Id;
        }

        if(valueIds!=null&&valueIds.length>0){
            for (String valueId : valueIds) {
                if (!valueId.equals(delValueId)) {
                    urlParam = urlParam + "&valueId=" + valueId;
                }

            }
        }
        return urlParam;
    }


    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String keyword = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String[] valueIds = pmsSearchParam.getValueId();

        String urlParam = "";
        if(StringUtils.isNotBlank(keyword)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam = urlParam +"keyword="+keyword;
        }

        if(StringUtils.isNotBlank(catalog3Id)){
            if(StringUtils.isNotBlank(urlParam)){
                urlParam=urlParam+"&";
            }
            urlParam = urlParam +"catalog3Id="+catalog3Id;
        }

        if(valueIds!=null&&valueIds.length>0){
            for (String valueId : valueIds) {
                urlParam = urlParam+"&valueId="+valueId;
            }
        }
        return urlParam;
    }



}
