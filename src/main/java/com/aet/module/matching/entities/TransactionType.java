package com.aet.module.matching.entities;

import java.util.Arrays;

public enum TransactionType {
	ON_US("ON-US"), 
	NOT_ON_US("NOT-ON-US"), 
	REMOTE_ON_US("REMOTE-ON-US");
	
	private String type;

	TransactionType(String type) {
        this.type = type;
    }
	
	public static TransactionType valueFor(String text) {
        return Arrays.stream(values())
          .filter(bl -> bl.type.equalsIgnoreCase(text))
          .findFirst()
          .orElse(null);
    }
	
	public String toString() {
        return type;
    }
    
}
