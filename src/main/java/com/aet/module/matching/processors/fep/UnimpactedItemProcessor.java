package com.aet.module.matching.processors.fep;

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

public class UnimpactedItemProcessor extends AbstractProcessor<String, com.aet.module.matching.entities.fep.Unimpacted> {

	Gson gson = new Gson();
	
	JSONObject mJSONObject;
	
	Map<String, Object> map;
	
	ObjectMapper mapper = new ObjectMapper();
	
	com.aet.module.matching.entities.fep.Unimpacted unimpactedObj = new com.aet.module.matching.entities.fep.Unimpacted();
	
    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        super.init(context);

    }


    @Override
    public void process(String key, com.aet.module.matching.entities.fep.Unimpacted unimpactedItem) {
        if (key != null && unimpactedItem != null) {
        	String jsonInString = gson.toJson(unimpactedItem);
			try {
				map = mapper.readValue(jsonInString, new TypeReference<Map<String, Object>>() {});
			} catch (IOException e) {e.printStackTrace();}

        	unimpactedObj.setPayload(new JSONObject(map));
        	unimpactedObj.setId(unimpactedItem.getJ_ID());
        	unimpactedObj.setComment("Mismatched! Item found in journal but not in fep");
        	unimpactedObj.setStatus(ExceptionStat.UNIMPACTED);
        	unimpactedObj.setLogtime(LocalDateTime.now().toString());
            Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense> tuple = Tuple.of(unimpactedObj, null, null);
            context().forward(key, tuple);
        }
    }
    
}
