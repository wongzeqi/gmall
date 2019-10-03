package com.wjq.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjq.gmall.bean.PmsBaseAttrInfo;
import com.wjq.gmall.bean.PmsBaseAttrValue;
import com.wjq.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.wjq.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.wjq.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;


    //根据三级分类的id查询所有的属性名称
    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo info = new PmsBaseAttrInfo();
        info.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(info);
        return  pmsBaseAttrInfos;

    }

    //插入平台属性  以及平台属性的值

    @Override
    public void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
        for(PmsBaseAttrValue v :pmsBaseAttrValues){
            pmsBaseAttrValueMapper.insertSelective(v);
        }
    }


}
