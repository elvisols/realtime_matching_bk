package com.aet.module.matching.entities;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;
import lombok.NoArgsConstructor;


@Document(indexName = "psuedo-reference-table", type = "psuedoref")
@Data
@NoArgsConstructor
public class PseudoReference {
	
	@Id
	private String id;
	
	private Channel channel;
	
	private List<Integer> position = new ArrayList<>();

}
