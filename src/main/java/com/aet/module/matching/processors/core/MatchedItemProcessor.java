package com.aet.module.matching.processors.core;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.json.simple.JSONObject;

import com.aet.module.matching.entities.ExceptionStat;
import com.aet.module.matching.entities.Tuple;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class MatchedItemProcessor extends AbstractProcessor<String, com.aet.module.matching.entities.core.Matched> {

	Gson gson = new Gson();
	
	JSONObject mJSONObject;
	
	Map<String, Object> map;
	
	ObjectMapper mapper = new ObjectMapper();
	
	com.aet.module.matching.entities.core.Matched matchedObj = new com.aet.module.matching.entities.core.Matched();
	
    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        super.init(context);

    }


    @Override
    public void process(String key, com.aet.module.matching.entities.core.Matched matchedItem) {
        if (key != null && matchedItem != null) {
        	String jsonInString = gson.toJson(matchedItem);
			try {
				map = mapper.readValue(jsonInString, new TypeReference<Map<String, Object>>() {});
			} catch (IOException e) {e.printStackTrace();}
        	matchedObj.setPayload(new JSONObject(map));
        	matchedObj.setId(matchedItem.getC_ID());
        	matchedObj.setComment("Matched! Item found in both journal and core.");
        	matchedObj.setStatus(ExceptionStat.MATCHED);
        	matchedObj.setLogtime(LocalDateTime.now().toString());
            Tuple<com.aet.module.matching.entities.core.Unimpacted, com.aet.module.matching.entities.core.Matched, com.aet.module.matching.entities.core.Dispense> tuple = Tuple.of(null, matchedObj, null);
            context().forward(key, tuple);
        }
    }
    
}
