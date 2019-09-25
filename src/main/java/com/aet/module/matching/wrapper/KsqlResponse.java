package com.aet.module.matching.wrapper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
// note this class can be returned as a list
public class KsqlResponse {
	
	// for /query
	private Row row;
	private String finalMessage;
	private String errorMessage;
	
	// for CREATE, DROP, TERMINATE statements.
	private String statementText;
	private String commandId; // used with the status endpoint to pull result of the operation
	private CommandStatus commandStatus;
	private Long commandSequenceNumber; // the sequence number of the requested operation in the command queue, or -1 if the operation was unsuccessful

	// for LIST STREAMS, SHOW STREAMS
	private List<Body> streams;
	// for LIST TABLES, SHOW TABLES
	private List<Body> tables;
	// for LIST QUERIES, SHOW QUERIES
	private List<Body> queries;
	// for LIST PROPERTIES, SHOW PROPERTIES
	private Map<String, String> properties;
	private List<String> overwrittenProperties;
    private List<String> defaultProperties;
	
    // for Errors
    private String message;
    private String error_code;
    private List<String> entities;
    
    // for /status
    private Status status;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private class Row {
		private List columns;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private class CommandStatus {
		private Status status;
		private String message;
	}
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class Body {
		private String id;
		private List<String> sinks;
		private String queryString;
		private String type;
		private String name;
		private String topic;
		private String format;
		private String isWindowed;
	}
	

}
