package com.wjq.gmall.search;
import com.alibaba.dubbo.config.annotation.Reference;
import com.wjq.gmall.bean.PmsSearchSkuInfo;
import com.wjq.gmall.bean.PmsSkuInfo;
import com.wjq.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;

import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallSearchServiceApplicationTests {

    @Reference
    SkuService skuService;
    //注入elasticsearch客户端
    @Autowired
    JestClient jestClient;






    @Test
    public void searchUtils() throws IOException {
        //jest的dsl工具

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter                                                 field    field value
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId","39");
        boolQueryBuilder.filter(termQueryBuilder);

        //TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("","");
        //boolQueryBuilder.filter(termsQueryBuilder);

        //must                                                       field   keyword
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","华为");
        boolQueryBuilder.must(matchQueryBuilder);
        //query
        searchSourceBuilder.query(boolQueryBuilder);
        //from  分页查询
        searchSourceBuilder.from(0);
        //size
        searchSourceBuilder.size(30);
        //highlight
        searchSourceBuilder.highlight(null);
        String dslStr = searchSourceBuilder.toString();
        System.err.println(dslStr);
    }


    @Test
    public void search() throws IOException {

        Search search = new Search.Builder("{\n" +
                "  \"query\": {\n" +
                "    \n" +
                "    \"bool\": {\n" +
                "\n" +
                "\n" +
                "      \"filter\": [\n" +
                "        {\n" +
                "          \"terms\": {\n" +
                "            \"skuAttrValueList.valueId\":[\"39\",\"41\"]\n" +
                "          }\n" +
                "        },\n" +
                "        \n" +
                "        {\n" +
                "          \"term\":{\n" +
                "            \"skuAttrValueList.valueId\": \"64\"\n" +
                "          }\n" +
                "        },\n" +
                "        \n" +
                "        {\n" +
                "          \"term\":{\n" +
                "            \"skuAttrValueList.valueId\": \"43\"\n" +
                "          }\n" +
                "        }\n" +
                "        \n" +
                "        ],\n" +
                "        \"must\": [\n" +
                "          {\n" +
                "            \"match\": {\n" +
                "              \"skuName\": \"小米\"\n" +
                "            }\n" +
                "          }\n" +
                "        ]\n" +
                "      \n" +
                "      \n" +
                "    }\n" +
                "    \n" +
                "    \n" +
                "  }\n" +
                "}").addIndex("gmall").addType("PmsSkuInfo").build();

        SearchResult searchResult = jestClient.execute(search);

        List<SearchResult.Hit<PmsSearchSkuInfo,Void>> hits = searchResult.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;
            System.out.println(source.toString());
        }




    }

    //将数据库的内容进行导入
    @Test
    public void put() throws IOException {

        List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
        //查询mysql
        pmsSkuInfoList = skuService.getAllSkuInfo();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfos.add(pmsSearchSkuInfo);
            System.out.println(pmsSkuInfo);
        }

        //导入es
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {

            Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall").type("PmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
            jestClient.execute(put);
        }




    }

}
