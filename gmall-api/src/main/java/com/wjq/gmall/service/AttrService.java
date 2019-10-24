package com.wjq.gmall.service;


import com.wjq.gmall.bean.PmsProductSaleAttr;
import com.wjq.gmall.bean.PmsProductSaleAttrValue;
import com.wjq.gmall.bean.PmsBaseAttrInfo;
import com.wjq.gmall.bean.PmsBaseAttrValue;

import java.util.List;
import java.util.Set;

public interface AttrService {
    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    String saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsProductSaleAttr> selectByProductId(PmsProductSaleAttr pmsProductSaleAttr);

    List<PmsProductSaleAttrValue> getProductSaleAttrValueList(PmsProductSaleAttrValue pmsProductSaleAttrValue);

    List<PmsBaseAttrInfo> getAttrValueListByValueIds(Set<String> valueIdSet);
}
