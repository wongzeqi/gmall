package com.wjq.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjq.gmall.service.AttrService;
import com.wjq.gmall.bean.PmsBaseAttrInfo;
import com.wjq.gmall.bean.PmsBaseAttrValue;
import com.wjq.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.wjq.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.wjq.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.wjq.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.wjq.gmall.bean.PmsProductSaleAttr;
import com.wjq.gmall.bean.PmsProductSaleAttrValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;


@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;
    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;
    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;
    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;


    //根据三级分类的id查询所有的属性名称  获取商品的属性值
    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
        PmsBaseAttrInfo info = new PmsBaseAttrInfo();
        info.setCatalog3Id(catalog3Id);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.select(info);

        //查询属性值
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        for(PmsBaseAttrInfo pmsBaseAttrInfo :pmsBaseAttrInfos){
            pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrInfo.setAttrValueList(pmsBaseAttrValueMapper.select(pmsBaseAttrValue));
        }

        return  pmsBaseAttrInfos;

    }

    //插入平台属性  以及平台属性的值

    @Override
    public String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        //有待考证
        //1  List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
        //2  pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
        //在使用主键返回策略的时候  上述代码中两条代码 位置存在问题 先获取其中的值  导致获取的内容中没有外键

        //如果是保存的话  传进来的参数是不带 主键的  如果是修改的话  传进来的参数是带  主键的
        String id = pmsBaseAttrInfo.getId();
        if(StringUtils.isBlank(id)){
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
            for(PmsBaseAttrValue v :pmsBaseAttrValues){
                v.setAttrId(pmsBaseAttrInfo.getId()); //设置属性值的  属性id
                pmsBaseAttrValueMapper.insertSelective(v);
            }
        }else{

            //根据id更新  PmsBaseAttrInfo
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo,example);

            //这样写是不对的
//            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
//            Example example2 = new Example(PmsBaseAttrValue.class);
//            for(PmsBaseAttrValue v : pmsBaseAttrValues){
//                example2.createCriteria().andEqualTo("id",v.getId());
//                pmsBaseAttrValueMapper.updateByExampleSelective(v,example2);
//            }
            //这样写会报错   不知道什么原因
            //org.apache.ibatis.ognl.NoSuchPropertyException: tk.mybatis.mapper.entity.Example$Criteria.oredCriter
//            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
//            Example example2 = new Example(PmsBaseAttrValue.class);
//            for(PmsBaseAttrValue v : pmsBaseAttrValues){
//                pmsBaseAttrValueMapper.updateByExampleSelective(v,new Example(PmsBaseAttrValue.class).createCriteria().andEqualTo("id",v.getId()););
//            }
            //1 先根据属性id删除所有的属性值

            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());

            pmsBaseAttrValueMapper.delete(pmsBaseAttrValue);

            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrInfo.getAttrValueList();
            //2 在对新数据进行保存
            for(PmsBaseAttrValue v:pmsBaseAttrValues){
                pmsBaseAttrValueMapper.insertSelective(v);
            }
            //有没有更好的办法.......


        }


        return "success";
    }


    //根据属性的id 获取全部的属性值
    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pv = new PmsBaseAttrValue();
        pv.setAttrId(attrId);
        return pmsBaseAttrValueMapper.select(pv);
    }

    @Override
    public List<PmsProductSaleAttr> selectByProductId(PmsProductSaleAttr pmsProductSaleAttr) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs =  pmsProductSaleAttrMapper.select(pmsProductSaleAttr);
        return pmsProductSaleAttrs;
    }

    @Override
    public List<PmsProductSaleAttrValue> getProductSaleAttrValueList(PmsProductSaleAttrValue pmsProductSaleAttrValue) {
        return pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueIds(Set<String> valueIdSet) {
        String valueIdstr = StringUtils.join(valueIdSet,",");
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrValueListByValueIds(valueIdstr);
        return pmsBaseAttrInfos;
    }


}
