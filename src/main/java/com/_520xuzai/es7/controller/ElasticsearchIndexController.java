package com._520xuzai.es7.controller;

import com._520xuzai.es7.service.IElasticsearchIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 10:43
 * \* To change this template use File | Settings | File Templates.
 * \* Description:elasticsearch索引相关接口
 * \
 */
@RestController
@RequestMapping("/es/index")
@Api(tags = "elasticsearch索引相关接口")
public class ElasticsearchIndexController {

    @Autowired
    private IElasticsearchIndexService elasticsearchIndexService;


    @GetMapping("/isIndexExists")
    @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query")
    Boolean isIndexExists(@RequestParam(value = "indexName")String indexName) throws IOException{
        return elasticsearchIndexService.isIndexExists(indexName);
    }


    @PostMapping("/createIndex")
    @ApiOperation(value = "创建索引")
    @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query")
    Boolean createIndex(@RequestParam(value = "indexName")String indexName) throws IOException {
        return elasticsearchIndexService.createIndex(indexName);
    }

    @PostMapping("/createIndexWithShards")
    @ApiOperation(value = "创建索引及自定义分片信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "分片数量", name = "numberOfShards", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(value = "副本数量", name = "numberOfReplicas", dataType = "int", required = true, paramType = "query")
    })
    Boolean createIndexWithShards(@RequestParam(value = "indexName")String indexName,
                                  @RequestParam(value = "numberOfShards")int numberOfShards,
                                  @RequestParam(value = "numberOfReplicas")int numberOfReplicas) throws IOException {
        return elasticsearchIndexService.createIndexWithShards(indexName,numberOfShards,numberOfReplicas);
    }

    @PostMapping("/deleteIndex")
    @ApiOperation(value = "删除索引")
    @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query")
    Boolean deleteIndex(@RequestParam(value = "indexName")String indexName) throws IOException {
        return elasticsearchIndexService.deleteIndex(indexName);
    }

    /**
     * 判断索引别名是否存在
     * @param aliasName
     * @return
     */
    @GetMapping("/isAliasExists")
    @ApiOperation(value = "判断索引别名是否存在")
    @ApiImplicitParam(value = "别名", name = "aliasName", dataType = "String", required = true, paramType = "query")
    Boolean isAliasExists(@RequestParam(value = "aliasName")String aliasName) throws IOException{
        return elasticsearchIndexService.isAliasExists(aliasName);
    }

    @PostMapping("/createIndexWithAlias")
    @ApiOperation(value = "创建索引同时给索引创建别名")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "别名", name = "aliasName", dataType = "String", required = true, paramType = "query")
    })
    Boolean createIndexWithAlias(@RequestParam(value = "indexName")String indexName,
                                 @RequestParam(value = "aliasName")String aliasName) throws IOException{
        return elasticsearchIndexService.createIndexWithAlias(indexName,aliasName);
    }

    @PostMapping("/addAlias")
    @ApiOperation(value = "给索引添加别名")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "别名", name = "aliasName", dataType = "String", required = true, paramType = "query")
    })
    Boolean addAlias(@RequestParam(value = "indexName")String indexName,
                                 @RequestParam(value = "aliasName")String aliasName) throws IOException{
        return elasticsearchIndexService.addAlias(indexName,aliasName);
    }

    @PostMapping("/deleteAlias")
    @ApiOperation(value = "删除某个索引的别名")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "别名", name = "aliasName", dataType = "String", required = true, paramType = "query")
    })
    Boolean deleteAlias(@RequestParam(value = "indexName")String indexName,
                        @RequestParam(value = "aliasName")String aliasName) throws IOException{
        return elasticsearchIndexService.deleteAlias(indexName,aliasName);
    }

    @PostMapping("/reindex")
    @ApiOperation(value = "重建索引，拷贝数据")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "原索引名称", name = "oldIndexName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "新索引名称", name = "newIndexName", dataType = "String", required = true, paramType = "query")
    })
    void reindex(@RequestParam(value = "oldIndexName")String oldIndexName,
                 @RequestParam(value = "newIndexName")String newIndexName) throws IOException{
        elasticsearchIndexService.reindex(oldIndexName,newIndexName);

    }

    @PostMapping("/changeAliasAfterReindex")
    @ApiOperation(value = "重建索引后修改别名")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "原索引名称别名", name = "aliasName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "原索引名称", name = "oldIndexName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "新索引名称", name = "newIndexName", dataType = "String", required = true, paramType = "query")
    })
    Boolean changeAliasAfterReindex(@RequestParam(value = "aliasName")String aliasName,
                                    @RequestParam(value = "oldIndexName")String oldIndexName,
                                    @RequestParam(value = "newIndexName")String newIndexName) throws IOException{
        return elasticsearchIndexService.changeAliasAfterReindex(aliasName,oldIndexName,newIndexName);
    }


    /**
     * 添加mapping
     * @param indexName
     * @param className
     * @return
     */
    @PostMapping("/addMapping")
    @ApiOperation(value = "添加mapping")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "索引名称", name = "indexName", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(value = "添加的映射实体权限定名", name = "className", dataType = "String", required = true, paramType = "query")
    })
    Boolean addMapping(@RequestParam(value = "indexName")String indexName,
                       @RequestParam(value = "className")String className) throws IOException, ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return elasticsearchIndexService.addMapping(indexName,clazz);
    }


}
