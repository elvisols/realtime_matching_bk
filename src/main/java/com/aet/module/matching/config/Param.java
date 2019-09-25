package com.aet.module.matching.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Param {
	
	
	public static String JOURNAL_CSV_TOPIC;
	public static String JOURNAL_TOPIC;
	
	public static String GLCORE_CSV_TOPIC;
	public static String GLCORE_TOPIC;
	
	public static String FEP_CSV_TOPIC;
	public static String FEP_TOPIC;
	
	public static String TEST1_CSV_TOPIC;
	public static String TEST1_TOPIC;
	
	public static String TEST2_CSV_TOPIC;
	public static String TEST2_TOPIC;
	
	public static String KSQL_DEFAULT_URI;
	public static String KSQL_QUERY_URI;
	
	public static int EMPTY_BYTE_RESPONSE_COUNT;
	
	public static String FILE_DIRECTORY_PATH;
	
	
	@Value("${kafka.topic.journal.csv}")
    public void setJOURNAL_CSV_TOPIC(String journal_topic) {
		JOURNAL_CSV_TOPIC = journal_topic;
    }
	
	@Value("${kafka.topic.journal.transformed}")
	public void setJOURNAL_TOPIC(String journal_topic) {
		JOURNAL_TOPIC = journal_topic;
	}
	
	@Value("${kafka.topic.core.csv}")
	public void setGL_CORE_CSV_TOPIC(String core_topic) {
		GLCORE_CSV_TOPIC = core_topic;
	}
	
	@Value("${kafka.topic.core.transformed}")
	public void setGL_CORE_TOPIC(String core_topic) {
		GLCORE_TOPIC = core_topic;
	}
	
	@Value("${kafka.topic.fep.csv}")
	public void setFEP_CSV_TOPIC(String fep_topic) {
		FEP_CSV_TOPIC = fep_topic;
	}
	
	@Value("${kafka.topic.fep.transformed}")
	public void setFEP_TOPIC(String fep_topic) {
		FEP_TOPIC = fep_topic;
	}
	
	@Value("${kafka.topic.test1.csv}")
	public void setTEST1_CSV_TOPIC(String test_topic) {
		TEST1_CSV_TOPIC = test_topic;
	}
	
	@Value("${kafka.topic.test1.transformed}")
	public void setTEST1_TOPIC(String test_topic) {
		TEST1_TOPIC = test_topic;
	}
	
	@Value("${kafka.topic.test2.csv}")
	public void setTEST2_CSV_TOPIC(String test_topic) {
		TEST2_CSV_TOPIC = test_topic;
	}
	
	@Value("${kafka.topic.test2.transformed}")
	public void setTEST2_TOPIC(String test_topic) {
		TEST2_TOPIC = test_topic;
	}
	@Value("${ksql.rest.api.endpoint.ksql}")
	public void setDEFAULT_URI(String default_uri) {
		KSQL_DEFAULT_URI = default_uri;
	}
	
	@Value("${ksql.rest.api.endpoint.query}")
	public void setQUERY_URI(String query_uri) {
		KSQL_QUERY_URI = query_uri;
	}
	
	@Value("${empty.byte.connection.count}")
	public void setEmptyByteConnectionCount(int count) {
		EMPTY_BYTE_RESPONSE_COUNT = count;
	}
	
	@Value("${file.directory.path}")
	public void setFileDirectoryPath(String path) {
		FILE_DIRECTORY_PATH = path;
	}
	
	@Bean
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(new ObjectMapper());
		restTemplate.getMessageConverters().add(converter);
		restTemplate.setRequestFactory(new HttpComponentsAsyncClientHttpRequestFactory());
		return restTemplate;
	}
	
}
