package com.aet.module.matching.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Postilion {
	
	private String id; // stan+date
	private Integer bin;
	private String bank;
	private Enum TransactionType;
	private Enum CardType;
	private String terminalId;
	private String messageType;
	private String retrievalReferenceNR;
	private String datetimeTranGMT; // see https://www.javacodegeeks.com/2017/08/formatparse-dates-localdatetime-java-8-example-tutorial.html
	private String datetimeTranLOCAL; // 12/10/2017 06:39:17 AM // https://beginnersbook.com/2014/01/how-to-convert-12-hour-time-to-24-hour-date-in-java/
	private String fromAccountID;
	private Float tranAmountREQ;
	private Float tranAmountRSP;
	private Integer stan;
	private String datetimeREQ;
	private String pan;
	private String rspCodeRSP;
	private Boolean tranPostilionOriginated;
	private Boolean tranCompleted;
	private Boolean tranReversed;
	private String crdr;
	private Long created;
	
	/*
	 * BIN,BANK,TRANSACTION TYPE,CARD TYPE,TERMINAL ID,MESSAGE TYPE,RETRIEVAL REFERENCE NR,DATETIME TRAN GMT,DATETIME TRAN LOCAL,FROM ACCOUNT ID,TRAN AMOUNT REQ,TRAN AMOUNT RSP,STAN,DATETIME REQ,PAN,RSP CODE RSP,TRAN POSTILION ORIGINATED,TRAN COMPLETED,TRAN REVERSED,CRDR
	 */

}
