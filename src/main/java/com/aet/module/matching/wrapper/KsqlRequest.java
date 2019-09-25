package com.aet.module.matching.wrapper;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
// If a CREATE, DROP, or TERMINATE statement returns a command status with state QUEUED, PARSING, or EXECUTING from the /ksql endpoint, you can use the /status endpoint to poll the status of the command.
public class KsqlRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String ksql; // A semicolon delimited sequence of KSQL statements to run
	private Map<String, String> streamsProperties;
	/*
	 * Optional. If specified, the statements will not be run until all existing commands up to 
	 * and including the specified sequence number have completed. 
	 * If unspecified, the statements are run immediately. (Note: When a command is processed, the result object contains its sequence number.)
	 */
	private Long commandSequenceNumber;
	
	// for GET /status/{commandId}
	private String commandId;
	
	// Note that TERMINATE CLUSTER request can only be sent via the /ksql/terminate endpoint and you cannot send it via the CLI
	// /ksql/terminate
}
