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


public class DispenseItemProcessor extends AbstractProcessor<String, com.aet.module.matching.entities.core.Dispense> {

	Gson gson = new Gson();
	
	JSONObject mJSONObject;
	
	Map<String, Object> map;
	
	ObjectMapper mapper = new ObjectMapper();
	
	com.aet.module.matching.entities.core.Dispense dispenseObj = new com.aet.module.matching.entities.core.Dispense();

	@Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        super.init(context);

    }

    
    @Override
    public void process(String key, com.aet.module.matching.entities.core.Dispense dispenseItem) {
        if (key != null && dispenseItem != null) { 
        	String jsonInString = gson.toJson(dispenseItem);
			try {
				map = mapper.readValue(jsonInString, new TypeReference<Map<String, Object>>() {});
			} catch (IOException e) {e.printStackTrace();}
        	dispenseObj.setPayload(new JSONObject(map));
        	dispenseObj.setId(dispenseItem.getC_ID());
        	dispenseObj.setComment("-Mismatched! Item found in core but not in journal-");
        	dispenseObj.setStatus(ExceptionStat.DISPENSE);
        	dispenseObj.setLogtime(LocalDateTime.now().toString());
            Tuple<com.aet.module.matching.entities.core.Unimpacted, com.aet.module.matching.entities.core.Matched, com.aet.module.matching.entities.core.Dispense> tuple = Tuple.of(null, null, dispenseObj);
            context().forward(key, tuple);
        }
    }

}
