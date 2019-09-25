package com.aet.module.matching.entities;

import java.util.Arrays;

public enum CardType {
	VERVE("VERVE"), VISA("VISA"), MASTERCARD("MASTERCARD"), OTHERS("OTHERS"), MASTERCARD_NAIRA_DR_CARD("MASTERCARD NAIRA DR CARD");
	
	private String type;

	CardType(String type) {
        this.type = type;
    }
	
	public static CardType valueFor(String text) {
        return Arrays.stream(values())
          .filter(bl -> bl.type.equalsIgnoreCase(text))
          .findFirst()
          .orElse(null);
    }
}
