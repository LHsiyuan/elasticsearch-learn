package com._520xuzai.es7.service.impl;


import com._520xuzai.es7.annotation.ESMappingField;
import com._520xuzai.es7.service.IElasticsearchIndexService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.*;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.reindex.ReindexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 10:50
 * \* To change this template use File | Settings | File Templates.
 * \* Description: es索引相关操作实现
 * \
 */

@Service
public class ElasticsearchIndexServiceImpl implements IElasticsearchIndexService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public Boolean isIndexExists(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        getIndexRequest.humanReadable(true);
        return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public Boolean createIndexWithShards(String indexName, Integer numberOfShards, Integer numberOfReplicas) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        //设置分片信息
        numberOfShards = numberOfShards == null ? 1 : numberOfShards;
        numberOfReplicas = numberOfReplicas == null ? 1 : numberOfReplicas;
        createIndexRequest.settings(Settings.builder().
                put("index.number_of_shards", numberOfShards)
                .put("index.number_of_replicas", numberOfReplicas));
        //创建索引
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public Boolean createIndex(String indexName) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public Boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
        deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        return delete.isAcknowledged();
    }

    @Override
    public Boolean isAliasExists(String aliasName) throws IOException {
        GetAliasesRequest getAliasesRequest = new GetAliasesRequest(aliasName);
        return restHighLevelClient.indices().existsAlias(getAliasesRequest, RequestOptions.DEFAULT);
    }


    @Override
    public Boolean createIndexWithAlias(String indexName, String aliasName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        if (StringUtils.isNotEmpty(aliasName)) {
            request.alias(new Alias(aliasName));
        }
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    @Override
    public Boolean addAlias(String indexName, String aliasName) throws IOException {
        IndicesAliasesRequest aliasesRequest = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction =
                new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                        .index(indexName)
                        .alias(aliasName);
        aliasesRequest.addAliasAction(aliasAction);
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().updateAliases(aliasesRequest,RequestOptions.DEFAULT);
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public void reindex(String oldIndexname, String newIndexname) throws IOException {
        ReindexRequest request = new ReindexRequest();
        request.setSourceIndices(oldIndexname);
        request.setDestIndex(newIndexname);
        request.setSourceBatchSize(1000);
        request.setDestOpType("create");
        request.setConflicts("proceed");
//        request.setScroll(TimeValue.timeValueMinutes(10));
//        request.setTimeout(TimeValue.timeValueMinutes(20));
        request.setRefresh(true);
        restHighLevelClient.reindex(request, RequestOptions.DEFAULT);
    }

    @Override
    public Boolean deleteAlias(String indexName,String aliasName) throws IOException {
        DeleteAliasRequest deleteAliasRequest = new DeleteAliasRequest(indexName,aliasName);
        org.elasticsearch.client.core.AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().deleteAlias(deleteAliasRequest, RequestOptions.DEFAULT);
        return acknowledgedResponse.isAcknowledged();
    }

    @Override
    public Boolean changeAliasAfterReindex(String aliasname, String oldIndexname, String newIndexname) throws IOException {
        IndicesAliasesRequest.AliasActions addIndexAction = new IndicesAliasesRequest.AliasActions(
                IndicesAliasesRequest.AliasActions.Type.ADD).index(newIndexname).alias(aliasname);
        IndicesAliasesRequest.AliasActions removeAction = new IndicesAliasesRequest.AliasActions(
                IndicesAliasesRequest.AliasActions.Type.REMOVE).index(oldIndexname).alias(aliasname);

        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        indicesAliasesRequest.addAliasAction(addIndexAction);
        indicesAliasesRequest.addAliasAction(removeAction);
        AcknowledgedResponse indicesAliasesResponse = restHighLevelClient.indices().updateAliases(indicesAliasesRequest,
                RequestOptions.DEFAULT);
        return indicesAliasesResponse.isAcknowledged();
    }


    @Override
    public Boolean addMapping(String indexName, Class<?> clazz) throws IOException {
        PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
        Map<String, Object> jsonMap = Maps.newHashMap();
        Map<String, Object> properties = Maps.newHashMap();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ESMappingField esMappingField = field.getDeclaredAnnotation(ESMappingField.class);
            if (esMappingField != null) {
                String fieldname = esMappingField.fieldName();
                String datatype = esMappingField.dataType();
                String analyzer = esMappingField.setAnalyzer();
                boolean isanalye = esMappingField.isAnalyze();
                Map<String, Object> m = Maps.newHashMap();
                m.put("type", datatype);
                if (isanalye && StringUtils.isNotEmpty(analyzer)) {
                    m.put("analyzer", analyzer);
                    m.put("search_analyzer", analyzer);
                }
                properties.put(fieldname, m);
            }
        }
        jsonMap.put("properties", properties);
        putMappingRequest.source(jsonMap);
        AcknowledgedResponse putMappingResponse = restHighLevelClient.indices().putMapping(putMappingRequest,
                RequestOptions.DEFAULT);
        return putMappingResponse.isAcknowledged();
    }

}
