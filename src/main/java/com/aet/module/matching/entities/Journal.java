package com.aet.module.matching.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Journal {
	
	private String id; // stan+date
	private String accountNo;
	private Float amount;
	private String bankTrnType;
	private String journalTime; // 2018-12-30T09:11
	private String errorMsg;
	private String journalName;
	private String pan;
	private String rrn;
	private Integer stan;
	private String terminalId;
	private String transactionId;
	private String transactionType;
	private Long created;
	
	/*
	 *  CREATE TABLE employee (id INT, name VARCHAR, edge INT, wages DOUBLE, created INT) WITH (KAFKA_TOPIC = 'test1-topic', VALUE_FORMAT = 'JSON', KEY = 'id');

		CREATE TABLE employee2 (code INT, fullname VARCHAR, age INT, salary DOUBLE, created INT) WITH (KAFKA_TOPIC = 'test2-topic', VALUE_FORMAT = 'JSON', KEY = 'code');
		
		CREATE TABLE glcore (id VARCHAR, trnRefNo VARCHAR, termId VARCHAR, maskedPan VARCHAR, fwdInsId INT, rrn VARCHAR, stan INT, acBranch INT, acNo VARCHAR, acCCY VARCHAR, trnDT VARCHAR, drCrInd VARCHAR, lcyAmount DOUBLE, fcyAmount DOUBLE, narration VARCHAR, timestamp VARCHAR, valueDT VARCHAR, relatedCustomer VARCHAR, acqInsID VARCHAR, authID VARCHAR, userID VARCHAR, created INT) WITH (KAFKA_TOPIC = 'glcore-topic', VALUE_FORMAT = 'JSON', KEY = 'id');
		
		
		CREATE TABLE postilion (id VARCHAR, bin INT, bank VARCHAR, transactionType VARCHAR, cardType VARCHAR, terminalId VARCHAR, messageType VARCHAR, retrievalReferenceNR VARCHAR, datetimeTranGMT VARCHAR, datetimeTranLOCAL VARCHAR, fromAccountID VARCHAR, tranAmountREQ DOUBLE, tranAmountRSP DOUBLE, stan INT, datetimeREQ VARCHAR, pan VARCHAR, rspCodeRSP VARCHAR, tranPostilionOriginated BOOLEAN, tranCompleted BOOLEAN, crdr VARCHAR, created INT) WITH (KAFKA_TOPIC = 'fep-topic', VALUE_FORMAT = 'JSON', KEY = 'id');
		
		
		CREATE TABLE journal (id VARCHAR, accountNo VARCHAR, amount DOUBLE, bankTrnType VARCHAR, journalTime VARCHAR, errorMsg VARCHAR, journalName VARCHAR, pan VARCHAR, rrn VARCHAR, stan INT, terminalId VARCHAR, transactionId VARCHAR, transactionType VARCHAR, created INT) WITH (KAFKA_TOPIC = 'journal-topic', VALUE_FORMAT = 'JSON', KEY = 'id');
		
		// {"ROWTIME":1544042630406,"ROWKEY":"1","type":"key1","data":{"timestamp":"2018-12-21 23:58:42.1","field-a":1,"field-b":"first-value-for-key1"}}
		// {"ROWTIME":1544042630406,"ROWKEY":"2","type":"key2","data":{"timestamp":"2018-12-21 23:58:42.2","field-a":1,"field-c":11,"field-d":"first-value-for-key2"}}

		CREATE STREAM sink_result_test (
//				TYPE VARCHAR,
                _1 STRUCT< 
                	EMP_ROWTIME BIGINT,
                	EMP_ROWKEY VARCHAR,
                	EMP_ID INT,
                 	EMP_NAME VARCHAR, 
                 	EMP_EDGE INT,
                 	EMP_WAGES DOUBLE,
                 	EMP_CREATED INT>,
                _2 STRUCT< 
                	EMP_ROWTIME BIGINT
                	EMP_ROWKEY VARCHAR,
                	EMP_ID INT,
                 	EMP_NAME VARCHAR, 
                 	EMP_EDGE INT,
                 	EMP_WAGES DOUBLE,
                 	EMP_CREATED INT>,
                _3 STRUCT< 
                	EMP2_ROWTIME BIGINT,
                	EMP2_ROWKEY VARCHAR,
                	EMP2_CODE INT,
                 	EMP2_FULNAME VARCHAR, 
                 	EMP2_AGE INT,
                 	EMP2_SALARY DOUBLE,
                 	EMP_CREATED INT>,
                 	)
        WITH (KAFKA_TOPIC='sink-results',
              VALUE_FORMAT='JSON');
		
		SELECT _1->EMP_ROWTIME, DATA->"field-b" FROM T WHERE TYPE='key1' LIMIT 2;
		
		-SHOW QUERIES;
		-TERMINATE CSAS_PAGEVIEWS_INTRO_0;
		-DROP STREAM pageviews_intro; // ensure to terminate the stream before dropping.
	 
	 */

}
