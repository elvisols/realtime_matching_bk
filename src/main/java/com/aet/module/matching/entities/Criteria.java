package com.aet.module.matching.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;


@Data
@Document(indexName = "criteria-table", type = "criteriatype")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Criteria {
	
	@Id
	private String id;
	
	private String name;
	
	private String query;
	

}
