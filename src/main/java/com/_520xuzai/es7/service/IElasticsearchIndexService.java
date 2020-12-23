package com._520xuzai.es7.service;


import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 10:44
 * \* To change this template use File | Settings | File Templates.
 * \* Description: es索引相关操作
 * \
 */
public interface IElasticsearchIndexService {

    /**
     * 判断索引是否存在
     * @param indexName
     * @return
     */
    Boolean isIndexExists(String indexName) throws IOException;

    /**
     * 创建索引
     *
     * @param indexName
     * @param numberOfShards
     * @param numberOfReplicas
     * @return
     */
    Boolean createIndexWithShards(String indexName, Integer numberOfShards, Integer numberOfReplicas) throws IOException;

    /**
     * 创建索引(默认1个分片，1个副本)
     *
     * @param indexName
     * @return
     */
    Boolean createIndex(String indexName) throws IOException;

    /**
     * 删除索引
     * @param indexName
     * @return
     * @throws IOException
     */
    Boolean deleteIndex(String indexName) throws IOException;

    /**
     * 判断索引别名是否存在
     * @param aliasName
     * @return
     */
    Boolean isAliasExists(String aliasName) throws IOException;

    /**
     * 创建索引同时给索引创建别名
     *
     * @param indexName
     * @param aliasName
     * @return
     */
    Boolean createIndexWithAlias(String indexName, String aliasName) throws IOException;

    /**
     * 给索引添加别名
     * @param indexName
     * @param aliasName
     * @return
     * @throws IOException
     */
    Boolean addAlias(String indexName, String aliasName) throws IOException;

    /**
     * 删除某个索引的别名
     * @param aliasName
     * @return
     * @throws IOException
     */
    Boolean deleteAlias(String indexName,String aliasName) throws IOException;

    /**
     * 重建索引，拷贝数据
     *
     * @param oldIndexname
     * @param newIndexname
     */
    void reindex(String oldIndexname, String newIndexname) throws IOException;

    /**
     * 重建索引后修改别名
     *
     * @param aliasname
     * @param oldIndexname
     * @param newIndexname
     * @return
     */
    Boolean changeAliasAfterReindex(String aliasname, String oldIndexname, String newIndexname) throws IOException ;

    /**
     * 添加mapping
     * @param indexName
     * @param clazz
     * @return
     */
    Boolean addMapping(String indexName, Class<?> clazz) throws IOException;

}
