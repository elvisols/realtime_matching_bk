package com.aet.module.matching.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jasypt.util.text.StrongTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.aet.module.matching.config.Param;
import com.aet.module.matching.wrapper.KsqlRequest;
import com.aet.module.matching.wrapper.KsqlResponse;
import com.aet.module.matching.wrapper.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.CharStreams;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
// https://docs.confluent.io/current/ksql/docs/developer-guide/api.html
public class KsqlApiServiceImpl implements KsqlApiService {

	@Autowired
	private RestTemplate restTemplate;
	
	HttpHeaders headers = new HttpHeaders();
	
	MessageDigest md = null;
	
	@Override
	public KsqlResponse[] createUnimpactedErrorStream(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		// in journal not in core
		String statement = "CREATE TABLE core_unimpacted AS SELECT j.*, c.* from journal j LEFT JOIN glcore c ON " +
				"c.id = j.id WHERE " + whereCondition + " OR c.id IS NULL;";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);
		
//		ResponseEntity<KsqlResponse[]> result1 = restTemplate.postForEntity(Param.KSQL_DEFAULT_URI, ksqlRequest, KsqlResponse[].class);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;

	}
	
	@Override
	public KsqlResponse[] createUnimpactedErrorStreamFEP(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		// in journal not in core
		String statement = "CREATE TABLE fep_unimpacted AS SELECT j.*, f.* from journal j LEFT JOIN postilion f ON " +
				"f.id = j.id WHERE " + whereCondition + " OR f.id IS NULL;";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}

	@Override
	public KsqlResponse[] createDispenseErrorStream(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		String statement = "CREATE TABLE core_dispense AS SELECT c.*, j.* from glcore c LEFT JOIN journal j ON " +
		"j.id = c.id WHERE " + whereCondition + " OR j.id IS NULL;";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);

		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}
	
	@Override
	public KsqlResponse[] createDispenseErrorStreamFEP(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		String statement = "CREATE TABLE fep_dispense AS SELECT f.*, j.* from postilion f LEFT JOIN journal j ON " +
				"j.id = f.id WHERE " + whereCondition + " OR j.id IS NULL;";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}

	@Override
	public KsqlResponse[] createMatchStream(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		String statement = "CREATE TABLE core_matched AS SELECT j.*, c.* from journal j INNER JOIN glcore c ON " +
		"j.id = c.id WHERE " + whereCondition + ";";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);

		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}

	public KsqlResponse[] createMatchStreamFEP(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		// employee and in employee2 statement
		String statement = "CREATE TABLE fep_matched AS SELECT j.*, f.* from journal j INNER JOIN postilion f ON " +
				"j.id = f.id WHERE " + whereCondition + ";";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}
	
	public KsqlResponse[] getLiveStreams() {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		// employee and in employee2 statement
		String statement = "SHOW QUERIES;";

		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}
	
	public KsqlResponse[] deleteLiveStream(String queryId) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		// "terminate CTAS_CORE_MATCHED_4; drop table CORE_MATCHED;"
		String statement = "TERMINATE " + queryId +"; DROP TABLE " + queryId.substring(queryId.indexOf("_") + 1, queryId.lastIndexOf("_")) + ";"; 
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		/*
		 * [
    {
        "@type": "currentStatus",
        "statementText": "terminate CTAS_FEP_UNIMPACTED_0;",
        "commandId": "terminate/CTAS_FEP_UNIMPACTED_0/execute",
        "commandStatus": {
            "status": "SUCCESS",
            "message": "Query terminated."
        },
        "commandSequenceNumber": 15
    },
    {
        "@type": "currentStatus",
        "statementText": "drop table FEP_UNIMPACTED;",
        "commandId": "table/FEP_UNIMPACTED/drop",
        "commandStatus": {
            "status": "SUCCESS",
            "message": "Source FEP_UNIMPACTED was dropped. "
        },
        "commandSequenceNumber": 16
    }
]
		 */
		
		return result;
	}
	

	/*
	 * Ensure you have your KSQL tables for each sources created before running this method!
	 *
	@Override
	public KsqlResponse[] createUnimpactedErrorStreamTest(String whereCondition) throws HttpClientErrorException, HttpStatusCodeException {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee not in employee2 statement
		String statement = "CREATE TABLE unimpacted_testing AS SELECT emp.* from employee emp LEFT JOIN employee2 emp2 ON " +
		"emp2.code = emp.id WHERE " + whereCondition + " OR emp2.code IS NULL;";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);

		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}
	
	/*
	@Override
	public KsqlResponse[] createDispenseErrorStreamTest(String whereCondition) throws HttpClientErrorException, HttpStatusCodeException {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee not in employee2 statement
		String statement = "CREATE TABLE dispense_testing AS SELECT emp2.* from employee2 emp2 LEFT JOIN employee emp ON " +
		"emp.id = emp2.code WHERE " + whereCondition + " OR emp.id IS NULL;";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);

		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}

	@Override
	public KsqlResponse[] createMatchStreamTest(String whereCondition) throws HttpClientErrorException, HttpStatusCodeException {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee and in employee2 statement
		String statement = "CREATE TABLE matched_testing AS SELECT emp.* from employee emp INNER JOIN employee2 emp2 ON " +
		"emp.id = emp2.code WHERE " + whereCondition + ";";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);

		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}

	@Override
	public void getSinkRecords() throws HttpClientErrorException, HttpStatusCodeException {
		URLConnection connection;
		OutputStream outputStream = null;
		ObjectMapper mapper = new ObjectMapper();
		String statement = null;
		String lineSeparator = System.getProperty("line.separator");

		try {
			connection = new URL(Param.KSQL_QUERY_URI).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");
			
			// unimparted statement
			statement = "SELECT * FROM sink_result_stream;";
			
			// request body
			KsqlRequest ksqlRequest = new KsqlRequest();
			ksqlRequest.setKsql(statement);
			Map<String, String> streamsProperties = new HashMap<>();
			streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
			ksqlRequest.setStreamsProperties(streamsProperties);

			String data = mapper.writeValueAsString(ksqlRequest);
			
			try (OutputStream output = connection.getOutputStream()) {
				output.write(data.getBytes(StandardCharsets.UTF_8.name()));
			}  
			
			try (InputStream response = connection.getInputStream()) {
				try {
					// write the inputStream to a FileOutputStream
					File targetFile = new File(Param.FILE_DIRECTORY_PATH + "SINK/tickets-stream.log");
				    
				    targetFile.createNewFile(); //if file already exists will do nothing 
				    
				    outputStream = new FileOutputStream(targetFile, false);

					int read = 0, empty = 0;
					byte[] bytes = new byte[8 * 1024];

					while ((read = response.read(bytes)) != -1) {
						if(read == 1) continue;
//						PrintWriter writer = new PrintWriter(new OutputStreamWriter(encfileout, charset));
						outputStream.write(bytes, 0, read);
//						outputStream.write("\n".getBytes());
//						outputStream.flush();
					}

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (response != null) {
						try {
							response.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (outputStream != null) {
						try {
							// outputStream.flush();
							outputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	*/
	private String md5(String condition) {
		
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        byte[] hashInBytes = md.digest(condition.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
	
	}
	
}
