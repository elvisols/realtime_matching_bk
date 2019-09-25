package com.aet.module.matching.processors.fep;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;

import com.aet.module.matching.entities.Tuple;


public class SinkProcessor extends AbstractProcessor<String, Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense>> {


    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        super.init(context);
    }

    @Override
    public void process(String key, Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense> value) {

    	if (value != null && value.u1 != null) {
            context().forward(key, value.u1);
        } else if (value != null && value.m2 != null) {
    		context().forward(key, value.m2);
        } else if (value != null && value.d3 != null) {
			context().forward(key, value.d3);
		} else {
        	System.out.println("Value is null - " + value);
        }
    
    }
    
}
