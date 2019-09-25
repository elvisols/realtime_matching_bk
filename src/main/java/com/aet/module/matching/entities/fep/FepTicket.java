package com.aet.module.matching.entities.fep;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(indexName = "fep-sink-results-table", type = "feptype")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FepTicket extends Payload {

}
