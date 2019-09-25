package com.aet.module.matching.entities.fep;

import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;

import com.aet.module.matching.entities.ExceptionStat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
abstract class Payload {

	@Id
	@JsonProperty("id")
	private String id;
	@JsonProperty("status")
	private ExceptionStat status;
	@JsonProperty("comment")
	private String comment;
	@JsonProperty("logtime")
	private String logtime;
	@JsonProperty("payload")
	private JSONObject payload;
	
	private String J_ID;
	private String J_ACCOUNTNO;
	private Float J_AMOUNT;
	private String J_BANKTRNTYPE;
	private String J_JOURNALTIME;
	private String J_ERRORMSG;
	private String J_JOURNALNAME;
	private String J_PAN;
	private String J_RRN;
	private Integer J_STAN;
	private String J_TERMINALID;
	private String J_TRANSACTIONID;
	private String J_TRANSACTIONTYPE;
	private Long J_CREATED;
	
	private String F_ID;
	private Integer F_BIN;
	private String F_BANK;
	private String F_TRANSACTIONTYPE;
	private String F_CARDTYPE;
	private String F_TERMINALID;
	private String F_MESSAGETYPE;
	private String F_RETRIEVALREFERENCENR;
	private String F_DATETIMETRANGMT; 
	private String F_DATETIMETRANLOCAL;
	private String F_FROMACCOUNTID;
	private Float F_TRANAMOUNTREQ;
	private Float F_TRANAMOUNTRSP;
	private Integer F_STAN;
	private String F_DATETIMEREQ;
	private String F_PAN;
	private String F_RSPCODERSP;
	private Boolean F_TRANPOSTILIONORIGINATED;
	private Boolean F_TRANCOMPLETED;
	private Boolean F_TRANREVERSED;
	private String F_CRDR;
	private Long F_CREATED;

}
