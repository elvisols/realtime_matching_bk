package com.aet.module.matching.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.transaction.NoTransactionException;

import com.aet.module.matching.exceptions.MyException;

public class FileProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		String a = exchange.getIn().getBody(String.class);
        System.out.println("hello " + a);
//        if (a.equalsIgnoreCase("learn"))
//        	throw new MyException("MyException thrown...");
        
	}

}
