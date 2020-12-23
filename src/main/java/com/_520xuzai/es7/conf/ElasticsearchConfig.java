package com._520xuzai.es7.conf;

import com._520xuzai.es7.conf.esbase.ElasticsearchRuntimeEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: 煦仔
 * \* Date: 2020-12-22
 * \* Time: 9:40
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Configuration
public class ElasticsearchConfig {


    @Autowired(required = false)
    private ElasticsearchRuntimeEnvironment esRuntimeEnvironment;


    @Bean
    //当前es相关的配置存在则实例化RestHighLevelClient,如果不存在则不实例化RestHighLevelClient
    @ConditionalOnBean(value = ElasticsearchRuntimeEnvironment.class)
    public RestHighLevelClient restHighLevelClient(){

        //es地址，以逗号分隔
        String nodes = esRuntimeEnvironment.getAddress();
        nodes = nodes.contains("http://") ? nodes.replace("http://","") : nodes;
        //es密码
        String password = esRuntimeEnvironment.getPassword();
        String scheme = esRuntimeEnvironment.getScheme();
        List<HttpHost> httpHostList = new ArrayList();
        //拆分es地址
        for(String address : nodes.split(",")){
            int index = address.lastIndexOf(":");
            httpHostList.add(new HttpHost(address.substring(0, index),Integer.parseInt(address.substring(index + 1)),scheme));
        }
        //转换成 HttpHost 数组
        HttpHost[] httpHosts = httpHostList.toArray(new HttpHost[httpHostList.size()]);

        //构建连接对象
        RestClientBuilder builder = RestClient.builder(httpHosts);

        //使用账号密码连接
        if ( StringUtils.isNotEmpty(password)){
            String username = esRuntimeEnvironment.getUsername();
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider .setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(username,password));
            builder.setHttpClientConfigCallback(f->f.setDefaultCredentialsProvider(credentialsProvider));
        }

        // 异步连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(esRuntimeEnvironment.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(esRuntimeEnvironment.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(esRuntimeEnvironment.getConnectionRequestTimeout());
            return requestConfigBuilder;
        });

        // 异步连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(esRuntimeEnvironment.getMaxConnectNum());
            httpClientBuilder.setMaxConnPerRoute(esRuntimeEnvironment.getMaxConnectPerRoute());
            return httpClientBuilder;
        });

        return new RestHighLevelClient(builder);
    }


}
