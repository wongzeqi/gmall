package com.wjq.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjq.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.wjq.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.wjq.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.wjq.gmall.bean.PmsBaseCatalog1;
import com.wjq.gmall.bean.PmsBaseCatalog2;
import com.wjq.gmall.bean.PmsBaseCatalog3;
import com.wjq.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {

        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        PmsBaseCatalog2 c2 = new PmsBaseCatalog2();
        c2.setCatalog1Id(catalog1Id);
        //selectByExample方法必须传入一个Example对象
        //必须使用select方法进行查询
        return pmsBaseCatalog2Mapper.select(c2);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 c3 = new PmsBaseCatalog3();
        c3.setCatalog2Id(catalog2Id);
        return pmsBaseCatalog3Mapper.select(c3);
    }
}
