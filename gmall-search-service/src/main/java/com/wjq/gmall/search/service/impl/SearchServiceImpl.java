package com.wjq.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.wjq.gmall.bean.PmsSearchParam;
import com.wjq.gmall.bean.PmsSearchSkuInfo;

import com.wjq.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam){

        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        String dslStr = buildSearchDsl(pmsSearchParam);


        Search search = new Search.Builder(dslStr).addIndex("gmall").addType("PmsSkuInfo").build();

        SearchResult searchResult = null;
        try {
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);
        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo searchSkuInfo = hit.source;

            //高亮显示
            Map<String,List<String>> highlight = hit.highlight;
            if(highlight!=null) {
                String skuName = highlight.get("skuName").get(0);
                searchSkuInfo.setSkuName(skuName);
            }

            pmsSearchSkuInfos.add(searchSkuInfo);
        }
        return pmsSearchSkuInfos;
    }

    private String buildSearchDsl(PmsSearchParam pmsSearchParam) {
        String[] valueIds = pmsSearchParam.getValueId();
        String keywords = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        //jest的dsl工具

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        if(valueIds!=null&&valueIds.length>0) {
            for (String valueId : valueIds) {
                //filter                                                 field    field value
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", valueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        if(StringUtils.isNotBlank(catalog3Id)) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //must                                                       field   keyword
        if(StringUtils.isNotBlank(keywords)) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keywords);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        //query
        searchSourceBuilder.query(boolQueryBuilder);
        //from  分页查询
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(20);
        //排序 按照 id 降序  但是 es里的id是字符串类型的
        searchSourceBuilder.sort("id", SortOrder.DESC);
        //highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red;'><b>");
        highlightBuilder.postTags("</b></span>");
        highlightBuilder.field("skuName");
        searchSourceBuilder.highlight(highlightBuilder);

        String dslStr = searchSourceBuilder.toString();
        return dslStr;
    }
}
