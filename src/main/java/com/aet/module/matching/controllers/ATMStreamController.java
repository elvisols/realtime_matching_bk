package com.aet.module.matching.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aet.module.matching.service.CriteriaService;
import com.aet.module.matching.service.KsqlApiService;
import com.aet.module.matching.wrapper.KsqlResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("ksql")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "ATMStreams", description="This controller exposes endpoints for starting all matching stream.")
public class ATMStreamController {

	@Autowired
	private KsqlApiService ksqlService;
	
	@Autowired
	private CriteriaService criteriaService;

	@PostMapping(value="unimpacted/core/start/{cid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Start Unimparted stream against core")
	public KsqlResponse[] startUnimpartedStream(@PathVariable String cid) {
		
		KsqlResponse[] kr;
		
		kr = ksqlService.createUnimpactedErrorStream(criteriaService.getCriterion(cid).getQuery());
//		kr = ksqlService.createUnimpactedErrorStream("j.amount <> c.lcyAmount");
		
		return kr;
	}
	
	@PostMapping(value="dispense/core/start/{cid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Start Dispense stream against core")
	public KsqlResponse[] startDisperseStream(@PathVariable String cid) {
		
		KsqlResponse[] kr;
		
		kr = ksqlService.createDispenseErrorStream(criteriaService.getCriterion(cid).getQuery());
//		kr = ksqlService.createDispenseErrorStream("j.amount <> c.lcyAmount");
		
		return kr;
	}
	
	@PostMapping(value="match/core/start/{cid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Start Match stream against core")
	public KsqlResponse[] startMatchStream(@PathVariable String cid) {
		
		KsqlResponse[] kr;
		
		kr = ksqlService.createMatchStream(criteriaService.getCriterion(cid).getQuery());
//		kr = ksqlService.createMatchStream("j.amount = c.lcyAmount");
		
		return kr;
	}
	
	@PostMapping(value="unimpacted/fep/start/{cid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Start Unimparted stream against FEP")
	public KsqlResponse[] startUnimpartedStreamFEP(@PathVariable String cid) {
		
		KsqlResponse[] kr = ksqlService.createUnimpactedErrorStreamFEP(criteriaService.getCriterion(cid).getQuery());
//		KsqlResponse[] kr = ksqlService.createUnimpactedErrorStreamFEP("f.tranAmountRSP <> j.amount");
		
		return kr;
	}
	
	@PostMapping(value="dispense/fep/start/{cid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Start Dispense stream against FEP")
	public KsqlResponse[] startDispenseStreamFEP(@PathVariable String cid) {
		
		KsqlResponse[] kr = ksqlService.createDispenseErrorStreamFEP(criteriaService.getCriterion(cid).getQuery());
//		KsqlResponse[] kr = ksqlService.createDispenseErrorStreamFEP("f.tranAmountRSP <> j.amount");
		
		return kr;
	}
	
	@PostMapping(value="match/fep/start/{cid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Start Match stream against FEP")
	public KsqlResponse[] startMatchStreamFEP(@PathVariable String cid) {
		
		KsqlResponse[] kr = ksqlService.createMatchStreamFEP(criteriaService.getCriterion(cid).getQuery());
//		KsqlResponse[] kr = ksqlService.createMatchStreamFEP("f.tranAmountRSP = j.amount");
		
		return kr;
	}
	
	@GetMapping(value="live-streams", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "View running live streams")
	public KsqlResponse[] liveStreams() {
		
		KsqlResponse[] kr = ksqlService.getLiveStreams();
		
		return kr;
	}
	
	@DeleteMapping(value="live-streams/{qid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Delete a running live stream")
	public KsqlResponse[] deleteLiveStream(@PathVariable String qid) {
		
		KsqlResponse[] kr = ksqlService.deleteLiveStream(qid);
		
		return kr;
	}
	
	/*
	@RequestMapping("/unimparted/test/all")
	public KsqlResponse currentAllUnimpartedStream() {
		log.info("Request unimparted/all !");
		
		Long today = LocalDate.now().toEpochDay();
		
		KsqlResponse kr = ksqlService.getUnimpartedRecordsTest("emp.name <> emp2.fullname", today);
		
		return kr;
	}
	
	@RequestMapping("/dispense/test/all")
	public KsqlResponse currentAllDisperseStream() {
		log.info("Request disperse/all !");
		
		Long today = LocalDate.now().toEpochDay();
		
		KsqlResponse kr = ksqlService.getDisperseRecordsTest("emp.name <> emp2.fullname", today);
		
		return kr;
	}
	
	@RequestMapping("/match/test/all")
	public KsqlResponse currentAllMatchStream() {
		log.info("Request matched/all !");
		
		Long today = LocalDate.now().toEpochDay();
		
		KsqlResponse kr = ksqlService.getMatchedRecordsTest("emp.name = emp2.fullname", today);
		
		return kr;
	}
	
	@RequestMapping("/unimparted/fep/all")
	public KsqlResponse currentAllUnimpartedStreamFEP() {
		log.info("Request unimparted/all !");
		
		Long today = LocalDate.now().toEpochDay();
		
		KsqlResponse kr = ksqlService.getUnimpartedRecords("fep.pan <> j.pan", today);
		
		return kr;
	}
	
	@RequestMapping("/dispense/fep/all")
	public KsqlResponse currentAllDisperseStreamFEP() {
		log.info("Request disperse/all !");
		
		Long today = LocalDate.now().toEpochDay();
		
		KsqlResponse kr = ksqlService.getDisperseRecordsTest("fep.pan <> j.pan", today);
		
		return kr;
	}
	
	@RequestMapping("/match/fep/all")
	public KsqlResponse currentAllMatchStreamFEP() {
		log.info("Request matched/all !");
		
		Long today = LocalDate.now().toEpochDay();
		
		KsqlResponse kr = ksqlService.getMatchedRecordsTest("fep.pan = j.pan", today);
		
		return kr;
	}
	*/
	
}
