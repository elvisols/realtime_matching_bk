package com.aet.module.matching.entities.core;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(indexName = "core-sink-results-table", type = "coretype")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoreTicket extends Payload {

}
