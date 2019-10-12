package com.wjq.gmall.manage.mapper;

import com.wjq.gmall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    //这里参数这一定要添加@Param注解
    List<PmsProductSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("productId") String productId, @Param("skuId") String skuId);

}
