package com._520xuzai.es7.service.impl;

import com._520xuzai.es7.service.IElasticsearchService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 10:50
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */

@Service
public class ElasticsearchServiceImpl implements IElasticsearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public Boolean isExist(String indexName, String id) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, id);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        return restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public Boolean deleteData(String indexName, String id){
        DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
        deleteRequest.timeout(TimeValue.timeValueMinutes(20));
        deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
        deleteRequest.setRefreshPolicy("wait_for");
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            ReplicationResponse.ShardInfo shardInfo = deleteResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    String reason = failure.reason();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public Boolean deleteByQuery(String indexName, QueryBuilder query) throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
        request.setConflicts("proceed");
        request.setQuery(query);
        request.setTimeout(TimeValue.timeValueMinutes(10));
        request.setRefresh(true);
        request.setIndicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
        restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        return true;
    }

    @Override
    public Boolean updateByQuery(String indexName, QueryBuilder query, Script script) throws IOException {
        UpdateByQueryRequest request = new UpdateByQueryRequest(indexName);
        request.setConflicts("proceed");
        request.setQuery(query);
        request.setScript(script);
        request.setTimeout(TimeValue.timeValueMinutes(10));
        request.setRefresh(true);
        restHighLevelClient.updateByQuery(request, RequestOptions.DEFAULT);
        return true;
    }

    @Override
    public <T> T getEntity(String indexName, String id, Class<T> clazz) {
        GetRequest getRequest = new GetRequest(indexName, id);
        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            if (getResponse.isExists()) {
                return JSONObject.parseObject(JSONObject.toJSONString(getResponse.getSourceAsMap()), clazz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> Boolean saveOrUpdateEntity(T entity, String indexName, String id) {
        try {
            if (StringUtils.isNotEmpty(id)) {
                if (this.isExist(indexName, id)) {
                    UpdateRequest updateRequest = new UpdateRequest(indexName, id);
                    updateRequest.doc(JSONObject.toJSONString(entity), XContentType.JSON);
                    restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
                    return true;
                } else {
                    if (saveEntity(entity, indexName)) return true;
                }
            } else {
                if (saveEntity(entity, indexName)) return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private <T> Boolean saveEntity(T entity, String indexName) throws IOException {
        String jsonString = JSONObject.toJSONString(entity);
        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.source(jsonString, XContentType.JSON);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return true;
    }

}
