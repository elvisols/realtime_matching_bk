package com.aet.module.matching.entities;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aet.module.matching.entities.core.Payload;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class PayloadDeserializer extends JsonDeserializer<Payload> {
	
	private final Pattern NAME_PATTERN = Pattern.compile(".*?(\"?)name(\"?)[\\s]*:[\\s]*(\".*\").*?");
    private final Pattern DATE_PATTERN = Pattern.compile(".*?logtime:\"?([A-Za-z0-9\\-:]{20}).*?");

    @Override
    public Payload deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        JsonLocation currentLocation = jp.getCurrentLocation();
        String jsonString = (String) currentLocation.getSourceRef();
//        Matcher nameMatcher = NAME_PATTERN.matcher(jsonString);
//        String name = "";
//        if ( nameMatcher.matches() && nameMatcher.groupCount() == 3 ){
//            name = nameMatcher.group(3);
//        }

        Matcher dateMatcher = DATE_PATTERN.matcher(jsonString);
        String date = "";
        if ( dateMatcher.matches() && dateMatcher.groupCount() == 1 ){
            date = dateMatcher.group(1);
        }
        return null;
    }

}
