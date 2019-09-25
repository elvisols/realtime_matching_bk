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
public class KsqlApiServiceImplOld {

	@Autowired
	private RestTemplate restTemplate;
	
	HttpHeaders headers = new HttpHeaders();
	
	MessageDigest md = null;
	
	public KsqlResponse[] createUnimpartedErrorStream(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
//		String whereCondition = "journal.fullname <> core.name";
		
		// in journal not in core
		String statement = "CREATE TABLE unimparted-"+today+extract(whereCondition)+" AS SELECT j.* from journal j LEFT JOIN glcore core ON " +
		"core.id = j.id WHERE " + whereCondition + " AND (j.created = " + today + " AND core.created = " + today + ") OR (core.id IS NULL AND j.created = "+today+");";
				
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);

//				ResponseEntity<KsqlResponse[]> result1 = restTemplate.postForEntity(Param.KSQL_DEFAULT_URI, ksqlRequest, KsqlResponse[].class);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}
	
	public KsqlResponse[] createUnimpartedErrorStreamFEP(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
//		String whereCondition = "journal.fullname <> core.name";
		
		// in journal not in core
		String statement = "CREATE TABLE unimparted-"+today+extract(whereCondition)+" AS SELECT journal.* from journal journal LEFT JOIN postilion fep ON " +
				"fep.id = journal.id WHERE " + whereCondition + " AND (journal.created = " + today + " AND fep.created = " + today + ") OR (fep.id IS NULL AND journal.created = "+today+");";
		
		// set request body
		KsqlRequest ksqlRequest = new KsqlRequest();
		ksqlRequest.setKsql(statement);
		Map<String, String> streamsProperties = new HashMap<>();
		streamsProperties.put("ksql.streams.auto.offset.reset", "earliest");
		ksqlRequest.setStreamsProperties(streamsProperties);
		
		HttpEntity<KsqlRequest> request = new HttpEntity<KsqlRequest>(ksqlRequest, headers);
		
//				ResponseEntity<KsqlResponse[]> result1 = restTemplate.postForEntity(Param.KSQL_DEFAULT_URI, ksqlRequest, KsqlResponse[].class);
		
		KsqlResponse[] result = restTemplate.postForObject(Param.KSQL_DEFAULT_URI, request, KsqlResponse[].class);
		
		return result;
	}

	public KsqlResponse[] createDisperseErrorStream(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee not in employee2 statement
		String statement = "CREATE TABLE dispense_"+today+extract(whereCondition)+" AS SELECT core.* from glcore core LEFT JOIN journal j ON " +
		"j.id = core.id WHERE " + whereCondition + " AND (j.created = " + today + " AND core.created = " + today + ") OR (j.id IS NULL AND core.created = "+today+");";
		
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
	
	public KsqlResponse[] createDisperseErrorStreamFEP(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee not in employee2 statement
		String statement = "CREATE TABLE dispense_"+today+extract(whereCondition)+" AS SELECT fep.* from postilion fep LEFT JOIN journal j ON " +
				"j.id = fep.id WHERE " + whereCondition + " AND (j.created = " + today + " AND fep.created = " + today + ") OR (j.id IS NULL AND fep.created = "+today+");";
		
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

	public KsqlResponse[] createMatchStream(String whereCondition) {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee and in employee2 statement
		String statement = "CREATE TABLE matched_"+today+extract(whereCondition)+" AS SELECT j.* from journal j INNER JOIN glcore core ON " +
		"j.id = core.id WHERE " + whereCondition + " AND (j.created = " + today + " AND core.created = " + today + ");";
		
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
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee and in employee2 statement
		String statement = "CREATE TABLE matched_"+today+extract(whereCondition)+" AS SELECT j.* from journal j INNER JOIN postilion fep ON " +
				"j.id = fep.id WHERE " + whereCondition + " AND (j.created = " + today + " AND fep.created = " + today + ");";
		
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
	 * Ensure you have your KSQL tables for each sources created before running this method!
	 */
	public KsqlResponse[] createUnimpartedErrorStreamTest(String whereCondition) throws HttpClientErrorException, HttpStatusCodeException {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee not in employee2 statement
		String statement = "CREATE TABLE unimparted_testing_"+today+extract(whereCondition)+" AS SELECT emp.* from employee emp LEFT JOIN employee2 emp2 ON " +
		"emp2.code = emp.id WHERE " + whereCondition + " AND (emp.created = " + today + " AND emp2.created = " + today + ") OR (emp2.code IS NULL AND emp.created = "+today+");";
		
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
	
	public KsqlResponse[] createDisperseErrorStreamTest(String whereCondition) throws HttpClientErrorException, HttpStatusCodeException {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee not in employee2 statement
		String statement = "CREATE TABLE dispense_testing_"+today+extract(whereCondition)+" AS SELECT emp2.* from employee2 emp2 LEFT JOIN employee emp ON " +
		"emp.id = emp2.code WHERE " + whereCondition + " AND (emp.created = " + today + " AND emp2.created = " + today + ") OR (emp.id IS NULL AND emp2.created = "+today+");";
		
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

	public KsqlResponse[] createMatchStreamTest(String whereCondition) throws HttpClientErrorException, HttpStatusCodeException {
		// set headers
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Content-type", "application/vnd.ksql.v1+json; charset=utf-8");
		
		
		Long today = LocalDate.now().toEpochDay();
		
		// employee and in employee2 statement
		String statement = "CREATE TABLE matched_testing_"+today+extract(whereCondition)+" AS SELECT emp.* from employee emp INNER JOIN employee2 emp2 ON " +
		"emp.id = emp2.code WHERE " + whereCondition + " AND (emp.created = " + today + " AND emp2.created = " + today + ");";
		
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

	public KsqlResponse getUnimpartedRecords(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException {
		URLConnection connection;
		OutputStream outputStream = null;
		ObjectMapper mapper = new ObjectMapper();
		String statement = null;

		try {
			connection = new URL(Param.KSQL_QUERY_URI).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");
			System.out.println(date+extract(whereCondition));
			// unimparted statement
			statement = "SELECT * FROM unimparted_" + date+extract(whereCondition) + ";";
			
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
					File targetFile = new File(Param.FILE_DIRECTORY_PATH + "SINK/unimparted-errors-"+date+extract(whereCondition) + ".txt");
				    
				    targetFile.createNewFile(); // if file already exists will do nothing 
				    
				    outputStream = new FileOutputStream(targetFile, false);

					int read = 0, empty = 0;
					byte[] bytes = new byte[8 * 1024];

					while ((read = response.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
						if(empty >= Param.EMPTY_BYTE_RESPONSE_COUNT) {
							//IOUtils.closeQuietly(response);
						    //IOUtils.closeQuietly(outputStream);
						    break;
						}
						empty = (read == 1) ? empty + 1 : 0;
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
		
		// generate response
		KsqlResponse kr = new KsqlResponse();
		kr.setStatementText(statement);
		kr.setMessage("Unimparted exception generated at " + "C/SINK/unimparted-errors-"+date+extract(whereCondition) + ".txt");
		kr.setStatus(Status.SUCCESS);
		
		return kr;
	}
 
	public KsqlResponse getDisperseRecords(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException {
		URLConnection connection;
		OutputStream outputStream = null;
		ObjectMapper mapper = new ObjectMapper();
		String statement = null;

		try {
			connection = new URL(Param.KSQL_QUERY_URI).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");
			
			// unimparted statement
			statement = "SELECT * FROM dispense_" + date+extract(whereCondition) + ";";
			
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
					File targetFile = new File(Param.FILE_DIRECTORY_PATH + "SINK/dispense-errors-"+date+extract(whereCondition) + ".txt");
				    
				    targetFile.createNewFile(); // if file already exists will do nothing 
				    
				    outputStream = new FileOutputStream(targetFile, false);

					int read = 0, empty = 0;
					byte[] bytes = new byte[8 * 1024];

					while ((read = response.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
						if(empty >= Param.EMPTY_BYTE_RESPONSE_COUNT) {
							//IOUtils.closeQuietly(response);
						    //IOUtils.closeQuietly(outputStream);
						    break;
						}
						empty = (read == 1) ? empty + 1 : 0;
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
		
		// generate response
		KsqlResponse kr = new KsqlResponse();
		kr.setStatementText(statement);
		kr.setMessage("Disperse exception generated at " + "C/SINK/disperse-errors-"+date+extract(whereCondition) + ".txt");
		kr.setStatus(Status.SUCCESS);
		
		return kr;

	}

	public KsqlResponse getMatchedRecords(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException {
		URLConnection connection;
		OutputStream outputStream = null;
		ObjectMapper mapper = new ObjectMapper();
		String statement = null;

		try {
			connection = new URL(Param.KSQL_QUERY_URI).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");
			
			// unimparted statement
			statement = "SELECT * FROM matched_" + date+extract(whereCondition) + ";";
			
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
					File targetFile = new File(Param.FILE_DIRECTORY_PATH + "/SINK/matched-items-"+date+extract(whereCondition) + ".txt");
				    
				    targetFile.createNewFile(); // if file already exists will do nothing 
				    
				    outputStream = new FileOutputStream(targetFile, false);

					int read = 0, empty = 0;
					byte[] bytes = new byte[8 * 1024];

					while ((read = response.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
						if(empty >= Param.EMPTY_BYTE_RESPONSE_COUNT) {
							//IOUtils.closeQuietly(response);
						    //IOUtils.closeQuietly(outputStream);
						    break;
						}
						empty = (read == 1) ? empty + 1 : 0;
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
		
		// generate response
		KsqlResponse kr = new KsqlResponse();
		kr.setStatementText(statement);
		kr.setMessage("Matched items generated at " + "C/SINK/matched-items-"+date+extract(whereCondition) + ".txt");
		kr.setStatus(Status.SUCCESS);
		
		return kr;
	}

	public KsqlResponse getUnimpartedRecordsTest(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException {
		URLConnection connection;
		OutputStream outputStream = null;
		ObjectMapper mapper = new ObjectMapper();
		String statement = null;

		try {
			connection = new URL(Param.KSQL_QUERY_URI).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");
			System.out.println(date+extract(whereCondition));
			// unimparted statement
			statement = "SELECT * FROM unimparted_testing_" + date+extract(whereCondition) + ";";
			
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
					File targetFile = new File(Param.FILE_DIRECTORY_PATH + "TEST-SINK/unimparted-errors-"+date+".txt");
				    
				    targetFile.createNewFile(); // if file already exists will do nothing 
				    
				    outputStream = new FileOutputStream(targetFile, false);

					int read = 0, empty = 0;
					byte[] bytes = new byte[8 * 1024];

					while ((read = response.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
						if(empty >= Param.EMPTY_BYTE_RESPONSE_COUNT) {
							//IOUtils.closeQuietly(response);
						    //IOUtils.closeQuietly(outputStream);
						    break;
						}
						empty = (read == 1) ? empty + 1 : 0;
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
		
		// generate response
		KsqlResponse kr = new KsqlResponse();
		kr.setStatementText(statement);
		kr.setMessage("Unimparted exception generated at " + "C/TEST-SINK/unimparted-errors-"+date+".txt");
		kr.setStatus(Status.SUCCESS);
		
		return kr;
		
	}

	public KsqlResponse getDisperseRecordsTest(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException {
		URLConnection connection;
		OutputStream outputStream = null;
		ObjectMapper mapper = new ObjectMapper();
		String statement = null;

		try {
			connection = new URL(Param.KSQL_QUERY_URI).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");
			
			// unimparted statement
			statement = "SELECT * FROM dispense_testing_" + date+extract(whereCondition) + ";";
			
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
					File targetFile = new File(Param.FILE_DIRECTORY_PATH + "TEST-SINK/dispense-errors-"+date+".txt");
				    
				    targetFile.createNewFile(); // if file already exists will do nothing 
				    
				    outputStream = new FileOutputStream(targetFile, false);

					int read = 0, empty = 0;
					byte[] bytes = new byte[8 * 1024];

					while ((read = response.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
						if(empty >= Param.EMPTY_BYTE_RESPONSE_COUNT) {
							//IOUtils.closeQuietly(response);
						    //IOUtils.closeQuietly(outputStream);
						    break;
						}
						empty = (read == 1) ? empty + 1 : 0;
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
		
		// generate response
		KsqlResponse kr = new KsqlResponse();
		kr.setStatementText(statement);
		kr.setMessage("Disperse exception generated at " + "C/TEST-SINK/disperse-errors-"+date+".txt");
		kr.setStatus(Status.SUCCESS);
		
		return kr;

	}

	public KsqlResponse getMatchedRecordsTest(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException {
		URLConnection connection;
		OutputStream outputStream = null;
		ObjectMapper mapper = new ObjectMapper();
		String statement = null;

		try {
			connection = new URL(Param.KSQL_QUERY_URI).openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Content-Type", "application/vnd.ksql.v1+json; charset=utf-8");
			
			// unimparted statement
			statement = "SELECT * FROM matched_testing_" + date+extract(whereCondition) + ";";
			
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
					File targetFile = new File(Param.FILE_DIRECTORY_PATH + "/TEST-SINK/matched-items-"+date+".txt");
				    
				    targetFile.createNewFile(); // if file already exists will do nothing 
				    
				    outputStream = new FileOutputStream(targetFile, false);

					int read = 0, empty = 0;
					byte[] bytes = new byte[8 * 1024];

					while ((read = response.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
						if(empty >= Param.EMPTY_BYTE_RESPONSE_COUNT) {
							//IOUtils.closeQuietly(response);
						    //IOUtils.closeQuietly(outputStream);
						    break;
						}
						empty = (read == 1) ? empty + 1 : 0;
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
		
		// generate response
		KsqlResponse kr = new KsqlResponse();
		kr.setStatementText(statement);
		kr.setMessage("Matched items generated at " + "C/TEST-SINK/matched-items-"+date+".txt");
		kr.setStatus(Status.SUCCESS);
		
		return kr;
	}
	
	private String extract(String condition) {
		
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
