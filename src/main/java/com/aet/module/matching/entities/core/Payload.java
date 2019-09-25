package com.aet.module.matching.entities.core;

import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import com.aet.module.matching.entities.ExceptionStat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;


//@Data
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class Payload {

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
	
	private String C_ID;
	private String C_TRNREFNO;
	private String C_TERMID;
	private String C_MASKEDPAN;
	private Integer C_FWDINSID;
	private String C_RRN;
	private Integer C_STAN;
	private Integer C_ACBRANCH;
	private String C_ACNO;
	private String C_ACCCY;
	private String C_TRNDT;
	private String C_DRCRIND;
	private Float C_LCYAMOUNT;
	private Float C_FCYAMOUNT;
	private String C_NARRATION;
	private String C_TIMESTAMP;
	private String C_VALUEDT;
	private String C_RELATEDCUSTOMER;
	private String C_ACQINSID;
	private String C_AUTHID;
	private String C_USERID;
	private Long C_CREATED;

}
