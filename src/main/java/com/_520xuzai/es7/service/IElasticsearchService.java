package com._520xuzai.es7.service;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;

import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 10:44
 * \* To change this template use File | Settings | File Templates.
 * \* Description: 搜索引擎基础元素服务类
 * \
 */
public interface IElasticsearchService {


    /**
     * 判断记录是否存在
     *
     * @param indexName
     * @param id
     * @return
     */
    Boolean isExist(String indexName, String id) throws IOException;

    /**
     * 删除数据
     * @param indexName
     * @param id
     * @return
     * @throws IOException
     */
    Boolean deleteData(String indexName, String id);


    /**
     * 根据查询删除数据
     *
     * @param indexName
     * @param query
     * @return
     */
    Boolean deleteByQuery(String indexName, QueryBuilder query) throws IOException;


    /**
     * 根据查询更新数据
     *
     * @param indexName
     * @param query
     * @param script
     * @return
     */
    Boolean updateByQuery(String indexName, QueryBuilder query, Script script) throws IOException;

    /**
     * 获取单个记录
     *
     * @param indexName
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getEntity(String indexName, String id, Class<T> clazz);


    /**
     * 保存或者更新数据
     * @param entity
     * @param indexName
     * @param id
     * @param <T>
     * @return
     */
    <T> Boolean saveOrUpdateEntity(T entity, String indexName, String id);



}
