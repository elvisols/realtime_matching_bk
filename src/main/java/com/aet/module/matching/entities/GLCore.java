package com.aet.module.matching.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class GLCore {
	
	private String id; // stan+date
	private String trnRefNo;
	private String termId;
	private String maskedPan;
	private Integer fwdInsId;
	private String rrn;
	private Integer stan;
	private Integer acBranch;
	private String acNo;
	private String acCCY;
	private String trnDT; // 10/12/17
	private String drCrInd;
	private Float lcyAmount;
	private Float fcyAmount;
	private String narration;
	private String timestamp;
	private String valueDT;
	private String relatedCustomer;
	private String acqInsID;
	private String authID;
	private String userID;
	private Long created;
	
	/*
	 * TRN REF NO,TERM ID,MASKED PAN,FWD INS ID,RRN,STAN,AC BRANCH,AC NO,AC CCY,TRN DT,DRCR IND,LCY AMOUNT,FCY AMOUNT,NARRATION,TIME STAMP,VALUE DT,RELATED CUSTOMER,ACQ INS ID,AUTH ID,USER ID
	 */

}
