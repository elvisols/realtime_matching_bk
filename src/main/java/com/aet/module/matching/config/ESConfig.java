package com.aet.module.matching.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.aet.module.matching.repository")
public class ESConfig{
    
	@Value("${elasticsearch.host:localhost}") 
    public String host;
	
	@Value("${elasticsearch.clustername:elasticsearch}") 
	public String clusterName;
    
    @Value("${elasticsearch.port:9300}") 
    public int port;
    
    public String getHost() {
    	return host;
    }
    
	public int getPort() {
		return port;
    }
	
	@Bean
	@SuppressWarnings("resource")
    public Client client(){
        TransportClient client = null;
        try{
        	Settings elasticsearchSettings = Settings.builder()
//        	          .put("client.transport.sniff", true)
//        	          .put("path.home", EsHome)
        	          .put("cluster.name", clusterName).build();
        	
            System.out.println("host:"+ host+" port:"+port+" cluster:"+clusterName);
            client = new PreBuiltTransportClient(elasticsearchSettings)
            .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
	
	@Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }

}