package com.wjq.gmall.service;

import com.wjq.gmall.bean.PmsSearchParam;
import com.wjq.gmall.bean.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}
