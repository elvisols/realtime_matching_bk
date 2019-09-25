package com.aet.module.matching.config;

import org.apache.camel.CamelContext;
import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamelConfig {

    @Bean
    CamelContextConfiguration contextConfiguration() {

        return new CamelContextConfiguration() {

            @Override
            public void beforeApplicationStart(CamelContext context) {
            	System.out.println("before application start camel configuring custom config...");
                // your custom configuration goes here
//                ThreadPoolProfile threadPoolProfile = new ThreadPoolProfile();
//                threadPoolProfile.setId("MyDefault");
//                threadPoolProfile.setPoolSize(10);
//                threadPoolProfile.setMaxPoolSize(15);
//                threadPoolProfile.setMaxQueueSize(250);
//                threadPoolProfile.setKeepAliveTime(25L);
//                threadPoolProfile.setRejectedPolicy(ThreadPoolRejectedPolicy.Abort);
//                context.getExecutorServiceManager().registerThreadPoolProfile(threadPoolProfile);

            }

			@Override
			public void afterApplicationStart(CamelContext camelContext) {
				// TODO Auto-generated method stub
				System.out.println("after application start camel config...");
			}
        };
    }
    
    @Bean
    @ConditionalOnClass(CamelContext.class)
    @ConditionalOnMissingBean(ElasticsearchComponent.class)
    public ElasticsearchComponent configureElasticsearchComponent(CamelContext camelContext) throws Exception {
        ElasticsearchComponent elasticsearchComponent = new ElasticsearchComponent();
        elasticsearchComponent.setHostAddresses("localhost:9200");
        camelContext.addComponent("elasticsearch-rest", elasticsearchComponent);
//        component.setCamelContext(camelContext);
        return elasticsearchComponent;
    }
    
     
}
