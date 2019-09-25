package com.aet.module.matching.service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import com.aet.module.matching.wrapper.KsqlResponse;

public interface KsqlApiService {
	
	public KsqlResponse[] createUnimpactedErrorStream(String whereCondition);
	
	public KsqlResponse[] createDispenseErrorStream(String whereCondition);
	
	public KsqlResponse[] createUnimpactedErrorStreamFEP(String whereCondition);
	
	public KsqlResponse[] createDispenseErrorStreamFEP(String whereCondition);
	
	public KsqlResponse[] createMatchStream(String whereCondition);
	
	public KsqlResponse[] createMatchStreamFEP(String whereCondition);
	
	public KsqlResponse[] getLiveStreams();
	
	public KsqlResponse[] deleteLiveStream(String queryId);
	
	
//	public KsqlResponse getUnimpartedRecords(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException;;
//	
//	public KsqlResponse getDisperseRecords(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException;;
//	
//	public KsqlResponse getMatchedRecords(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException;;

	/////////     TEST SERVICES      //////////
	/*
	public KsqlResponse[] createUnimpactedErrorStreamTest(String whereCondition) throws HttpClientErrorException, HttpStatusCodeException;
	
	public KsqlResponse[] createDispenseErrorStreamTest(String whereCondition);
	
	public KsqlResponse[] createMatchStreamTest(String whereCondition);
	
	public void getSinkRecords() throws HttpClientErrorException, HttpStatusCodeException;
	*/
	
//	public KsqlResponse getUnimpartedRecordsTest(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException;;
//	
//	public KsqlResponse getDisperseRecordsTest(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException;;
//	
//	public KsqlResponse getMatchedRecordsTest(String whereCondition, Long date) throws HttpClientErrorException, HttpStatusCodeException;;

}
