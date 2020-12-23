package com._520xuzai.es7.annotation;

import java.lang.annotation.*;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 15:23
 * \* To change this template use File | Settings | File Templates.
 * \* Description: 该注解类用于注解映射到ES中的数据字段，类似Hibernate中实体字段映射数据库中的表字段
 * \
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface ESMappingField {

    /**
     * 字段名称
     * @return
     */
    String fieldName() default "";

    /**
     * 数据类型
     * @return
     */
    String dataType() default "";

    /**
     * 使用哪种分词器
     * @return
     */
    String setAnalyzer() default "";

    /**
     * 是否使用分词功能
     * @return
     */
    boolean isAnalyze() default false;
}
