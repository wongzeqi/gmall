package com.wjq.gmall.manage.mapper;

import com.wjq.gmall.bean.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {

    List<PmsBaseAttrInfo> selectAttrValueListByValueIds(@Param("valueIdStr") String valueIdStr);
}
