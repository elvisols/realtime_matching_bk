package com.aet.module.matching.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.aet.module.matching.config.Param;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CronService {
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private void sendLogToExceptionManagement() throws ClientProtocolException, IOException {
		Map<String, Object> jsonData = new HashMap<>();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("http://.../sink");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("reference", "abc", ContentType.TEXT_PLAIN);
		
		String directoryName = Param.FILE_DIRECTORY_PATH + "/SINK";
		File directory = new File(directoryName);
        if (directory.exists()){
		      // This attaches the file to the POST:
		      File coreFile = new File(directoryName + "/core-sink-results-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".log");
		      File fepFile = new File(directoryName + "/fep-sink-results-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".log");
		      builder.addBinaryBody("core", new FileInputStream(coreFile), ContentType.APPLICATION_OCTET_STREAM, coreFile.getName());
		      builder.addBinaryBody("fep", new FileInputStream(fepFile), ContentType.APPLICATION_OCTET_STREAM, fepFile.getName());
		      
		      HttpEntity multipart = builder.build();
		      uploadFile.setEntity(multipart);
		      uploadFile.setHeader("Authorization", "Bearer: ks424h24h2j3");
		      CloseableHttpResponse response = httpClient.execute(uploadFile);
		      
		      System.out.println("Response: " + response.toString());
		      
		}
	}

}
