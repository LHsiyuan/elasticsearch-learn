package com._520xuzai.es7.dto;

import com._520xuzai.es7.annotation.ESMappingField;
import com._520xuzai.es7.constains.ESFieldTypeConstains;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 16:00
 * \* To change this template use File | Settings | File Templates.
 * \* Description: 商品搜索映射实体类
 * \
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchGoodsMappingDTO implements java.io.Serializable {


    /**
     * 商品ID
     */
    @ESMappingField(fieldName = "goodsId", dataType = ESFieldTypeConstains.DATATYPE_KEYWORD)
    private String goodsId;

    /**
     * 商品名称-不分词
     */
    @ESMappingField(fieldName = "goodsName", dataType = ESFieldTypeConstains.DATATYPE_TEXT, isAnalyze = true, setAnalyzer = ESFieldTypeConstains.SETANALYZER_KEYWORD)
    private String goodsName;

    /**
     * 商品名称-使用ik_max_word分词
     */
    @ESMappingField(fieldName = "goodsNameIk", dataType = ESFieldTypeConstains.DATATYPE_TEXT, isAnalyze = true, setAnalyzer = ESFieldTypeConstains.SETANALYZER_IK_MAX_WORD)
    private String goodsNameIk;

    /**
     * 商品名称-使用ik_smart分词
     */
    @ESMappingField(fieldName = "goodsNameSmart", dataType = ESFieldTypeConstains.DATATYPE_TEXT, isAnalyze = true, setAnalyzer = ESFieldTypeConstains.SETANALYZER_IK_smart)
    private String goodsNameSmart;

    /**
     * 商品名称-使用标准分词器
     */
    @ESMappingField(fieldName = "goodsNameStandard", dataType = ESFieldTypeConstains.DATATYPE_TEXT, isAnalyze = true, setAnalyzer = ESFieldTypeConstains.SETANALYZER_standard)
    private String goodsNameStandard;

    /**
     * 商品分类ID
     */
    @ESMappingField(fieldName = "goodsClassId", dataType = ESFieldTypeConstains.DATATYPE_KEYWORD)
    private String goodsClassId;

    /**
     * 商品分类名称
     */
    @ESMappingField(fieldName = "goodsClassName", dataType = ESFieldTypeConstains.DATATYPE_KEYWORD)
    private String goodsClassName;

    /**
     * 商品图片
     */
    @ESMappingField(fieldName = "goodsImg", dataType = ESFieldTypeConstains.DATATYPE_KEYWORD)
    private String goodsImg;

    /**
     * 商品价格
     */
    @ESMappingField(fieldName = "goodsPrice", dataType = ESFieldTypeConstains.DATATYPE_LONG)
    private Long goodsPrice;

    /**
     * 商品状态，1正常，0下架，-1已删除
     */
    @ESMappingField(fieldName = "goodsStatus", dataType = ESFieldTypeConstains.DATATYPE_KEYWORD)
    private String goodsStatus;

    /**
     * 商品销量
     */
    @ESMappingField(fieldName = "goodsSales", dataType = ESFieldTypeConstains.DATATYPE_LONG)
    private Long goodsSales;

    /**
     * 商品库存
     */
    @ESMappingField(fieldName = "goodsNum", dataType = ESFieldTypeConstains.DATATYPE_INTEGER)
    private Integer goodsNum;

    /**
     * 商品详情
     */
    @ESMappingField(fieldName = "goodsContent", dataType = ESFieldTypeConstains.DATATYPE_TEXT,isAnalyze = true, setAnalyzer = ESFieldTypeConstains.SETANALYZER_IK_MAX_WORD)
    private Integer goodsContent;


    /**
     * 搜索结果匹配度
     */
    private float score;

}