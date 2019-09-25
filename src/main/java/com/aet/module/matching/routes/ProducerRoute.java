package com.aet.module.matching.routes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.elasticsearch.ElasticsearchConstants;
import org.apache.camel.component.elasticsearch.ElasticsearchOperation;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.aet.module.matching.entities.CardType;
import com.aet.module.matching.entities.Channel;
import com.aet.module.matching.entities.GLCore;
import com.aet.module.matching.entities.Journal;
import com.aet.module.matching.entities.Postilion;
import com.aet.module.matching.entities.PseudoReference;
import com.aet.module.matching.entities.TestOne;
import com.aet.module.matching.entities.TestTwo;
import com.aet.module.matching.entities.TransactionType;
import com.aet.module.matching.repository.PseudoReferenceRepository;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ProducerRoute extends RouteBuilder {
	
	int count = 0;
	
	ObjectMapper mapper = new ObjectMapper();
	
	private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    private static List<Integer> coreRefPosition = null;
    private static List<Integer> fepRefPosition = null;
    private static List<Integer> journalRefPosition = null;
	
	@Autowired
    private ProducerTemplate template;
	
	@Autowired
	private PseudoReferenceRepository pref;
	
	@Value("${file.directory.path}")
	private String filePath;
	
	@Autowired
    Client client;
	
	private List<Integer> getRefPosition(Channel channel) {
		switch(channel) {
			case FEP:
				if(fepRefPosition == null) {
					fepRefPosition = pref.findByChannel(Channel.FEP).get().getPosition();
				} 
				return fepRefPosition;
			case CORE:
				if(coreRefPosition == null) {
					coreRefPosition = pref.findByChannel(Channel.CORE).get().getPosition();
				}
				return coreRefPosition;
			case JOURNAL:
				if(journalRefPosition == null) {
					journalRefPosition = pref.findByChannel(Channel.JOURNAL).get().getPosition();
				} 
				return journalRefPosition;
		}
		
		return new ArrayList<>();
		
	}

	@Override
	public void configure() throws Exception {
		
		onCompletion()
		.process(new Processor() {
            public void process(Exchange exchange) throws Exception {
            	coreRefPosition = null;
                fepRefPosition = null;
                journalRefPosition = null;
            }
        });
		
		// generic exception handler...
		onException(Exception.class)
		.process(new Processor() {
            public void process(Exchange exchange) throws Exception {
            	Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
                System.out.println("...Oops! exception caught!..." + cause.getMessage());
//                exchange.getIn().setBody(ex.getMessage());
            }
        })
		.log("Received body ");
//		.handled(true);
		
		interceptSendToEndpoint("kafka:{{kafka.topic.journal.csv}}?brokers={{kafka.server}}:{{kafka.port}}").process(new Processor() {
			public void process(Exchange exchange) throws JsonProcessingException {
				String body = exchange.getIn().getBody().toString();
				
				List<String> line = parseLine(body);
		
				Journal journal = new Journal();
				
				String id = "";
				
				for(int i: getRefPosition(Channel.JOURNAL)) {
					if(isStringInteger(line.get(i))) {
						id += Integer.valueOf(line.get(i)) + "_";
					} else if(isStringJournalDate(line.get(i))) {
						id += getDateFromJournal(line.get(i))  + "_";
					} else {
						id += line.get(i)  + "_";
					}
				}

//				journal.setId(Integer.valueOf(line.get(9)) + "-" + getDateFromJournal(line.get(3)));
				journal.setId(id);
				journal.setAccountNo(line.get(0));
				String amt = line.get(1).isEmpty() ? "0" : line.get(1).replaceAll(",", "");
				journal.setAmount(Float.valueOf(amt));
				journal.setBankTrnType(line.get(2));
				journal.setJournalTime(line.get(3));
				journal.setErrorMsg(line.get(4));
				journal.setJournalName(line.get(5));
				journal.setPan(line.get(7));
				journal.setRrn(line.get(8));
				journal.setStan(Integer.valueOf(line.get(9)));
				journal.setTerminalId(line.get(10));
				journal.setTransactionId(line.get(11));
				journal.setTransactionType(line.get(12));
				journal.setCreated(LocalDate.now().toEpochDay());
				
				exchange.getIn().setBody(mapper.writeValueAsString(journal));

			}
		});
		
		interceptSendToEndpoint("kafka:{{kafka.topic.fep.csv}}?brokers={{kafka.server}}:{{kafka.port}}").process(new Processor() {
			public void process(Exchange exchange) throws JsonProcessingException {
				String body = exchange.getIn().getBody().toString();
				
				List<String> line = parseLine(body);
				
				Postilion postilion = new Postilion();
				
				String id = "";
				
				for(int i: getRefPosition(Channel.FEP)) {
					if(isStringInteger(line.get(i))) {
						id += Integer.valueOf(line.get(i)) + "_";
					} else if(isStringFepDate(line.get(i))) {
						id += getDateFromPostilion(line.get(i))  + "_";
					} else {
						id += line.get(i) + "_";
					}
				}
				
//				postilion.setId(Integer.valueOf(line.get(12)) + "-" + getDateFromPostilion(line.get(8)));
				postilion.setId(id);
				String bin = line.get(0).isEmpty() ? "0" : line.get(0);
				postilion.setBin(Integer.valueOf(bin));
				postilion.setBank(line.get(1));
				postilion.setTransactionType(TransactionType.valueFor(line.get(2).toUpperCase()));
				postilion.setCardType(CardType.valueFor(line.get(3).toUpperCase()));
				postilion.setTerminalId(line.get(4));
				postilion.setMessageType(line.get(5));
				postilion.setRetrievalReferenceNR(line.get(6));
				postilion.setDatetimeTranGMT(line.get(7));
				postilion.setDatetimeTranLOCAL(line.get(8));
				postilion.setFromAccountID(line.get(9));
				String amtREQ = line.get(10).isEmpty() ? "0" : line.get(10);
				postilion.setTranAmountREQ(Float.valueOf(amtREQ));
				String amtRSP = line.get(11).isEmpty() ? "0" : line.get(11);
				postilion.setTranAmountRSP(Float.valueOf(amtRSP));
				postilion.setStan(Integer.valueOf(line.get(12)));
				postilion.setDatetimeREQ(line.get(13));
				postilion.setPan(line.get(14));
				postilion.setRspCodeRSP(line.get(15));
				postilion.setTranPostilionOriginated(Boolean.valueOf(line.get(16)));
				postilion.setTranCompleted(Boolean.valueOf(line.get(17)));
				postilion.setTranReversed(Boolean.valueOf(line.get(18)));
				postilion.setCrdr(line.get(19));
				postilion.setCreated(LocalDate.now().toEpochDay());
				
				exchange.getIn().setBody(mapper.writeValueAsString(postilion));
				
			}
		});
		
		interceptSendToEndpoint("kafka:{{kafka.topic.core.csv}}?brokers={{kafka.server}}:{{kafka.port}}").process(new Processor() {
			public void process(Exchange exchange) throws JsonProcessingException {
				String body = exchange.getIn().getBody().toString();

				List<String> line = parseLine(body);
				
				GLCore core = new GLCore();
				
				String id = "";
				
				for(int i: getRefPosition(Channel.CORE)) {
					if(isStringInteger(line.get(i))) {
						id += Integer.valueOf(line.get(i)) + "_";
					} else if(isStringCoreDate(line.get(i))) {
						id += getDateFromCore(line.get(i))  + "_";
					} else {
						id += line.get(i)  + "_";
					}
				}

//				core.setId(Integer.valueOf(line.get(5)) + "-" + getDateFromCore(line.get(9)));
				core.setId(id);
				core.setTrnRefNo(line.get(0));
				core.setTermId(line.get(1));
				core.setMaskedPan(line.get(2));
				String fii = line.get(3).isEmpty() ? "0" : line.get(3);
				core.setFwdInsId(Integer.valueOf(fii));
				core.setRrn(line.get(4));
				core.setStan(Integer.valueOf(line.get(5)));
				String acBranch = line.get(6).isEmpty() ? "0" : line.get(6);
				core.setAcBranch(Integer.valueOf(acBranch));
				core.setAcNo(line.get(7));
				core.setAcCCY(line.get(8));
				core.setTrnDT(line.get(9));
				core.setDrCrInd(line.get(10));
				String lcyAmount = line.get(11).isEmpty() ? "0" : line.get(11);
				core.setLcyAmount(Float.valueOf(lcyAmount.replaceAll(",|\"", "")));
				String fcyAmount = line.get(12).isEmpty() ? "0" : line.get(12);
				core.setFcyAmount(Float.valueOf(fcyAmount.replaceAll(",|\"", "")));
				core.setNarration(line.get(13));
				core.setTimestamp(line.get(14));
				core.setValueDT(line.get(15));
				core.setRelatedCustomer(line.get(16));
				core.setAcqInsID(line.get(17));
				core.setAuthID(line.get(18));
				core.setUserID(line.get(19));
				core.setCreated(LocalDate.now().toEpochDay());
				
				exchange.getIn().setBody(mapper.writeValueAsString(core));
				
			}
		});
		
		from("file:{{file.directory.path}}JOURNALS")
//			.process(new FileProcessor())
			.split()
			.tokenize("\n")
			.log("...receiving journals...")
			.to("kafka:{{kafka.topic.journal.csv}}?brokers={{kafka.server}}:{{kafka.port}}");
		
		from("file:{{file.directory.path}}GLCORE")
		.split()
		.tokenize("\n")
		.log("...receiving cbs...")
		.to("kafka:{{kafka.topic.core.csv}}?brokers={{kafka.server}}:{{kafka.port}}");
		
		from("file:{{file.directory.path}}FEP")
		.split()
		.tokenize("\n")
		.log("...receiving postilion...")
		.to("kafka:{{kafka.topic.fep.csv}}?brokers={{kafka.server}}:{{kafka.port}}");
		

		/////////////////////////////////////////////////  Sinks  /////////////////////////////////////////////////////////////////
		
		from("kafka:core-sink-results?brokers={{kafka.server}}:{{kafka.port}}&autoOffsetReset=earliest")
		.transform(body().append("\n"))
		.to("file:{{file.directory.path}}SINK/?fileName=core-sink-results-${date:now:yyyyMMdd}.log&charset=utf-8&allowNullBody=true&fileExist=Append")
//		.log("Body is: ${body}")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				try {
					String json = exchange.getIn().getBody(String.class);
					
					Map<String, Object> map = new HashMap<String, Object>();
					
					// convert JSON string to Map 
					map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
					
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_NAME, "core-sink-results-table");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, "coretype");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_OPERATION, ElasticsearchOperation.Index);
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_ID, map.get("id"));
					exchange.getIn().setBody(map);
					
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		})
		.to("elasticsearch-rest://{{elasticsearch.clustername}}");
		
		from("kafka:core-sink-results?brokers={{kafka.server}}:{{kafka.port}}&autoOffsetReset=earliest")
		.transform(body().append("\n"))
		.to("file:{{file.directory.path}}SINK/?fileName=core-sink-results-${date:now:yyyyMMdd}.log&charset=utf-8&allowNullBody=true&fileExist=Append")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				try {
					String json = exchange.getIn().getBody(String.class);
					
					Map<String, Object> map = new HashMap<String, Object>();
					
					// convert JSON string to Map 
					map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
					
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_NAME, "core-sink-results-stream");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, "coretype");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_OPERATION, ElasticsearchOperation.Index);
					exchange.getIn().setBody(map);
					
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		})
		.to("elasticsearch-rest://{{elasticsearch.clustername}}");
		
		from("kafka:fep-sink-results?brokers={{kafka.server}}:{{kafka.port}}&autoOffsetReset=earliest")
		.transform(body().append("\n"))
		.to("file:{{file.directory.path}}SINK/?fileName=fep-sink-results-${date:now:yyyyMMdd}.log&charset=utf-8&allowNullBody=true&fileExist=Append")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				try {
					String json = exchange.getIn().getBody(String.class);
					
					Map<String, Object> map = new HashMap<String, Object>();
					
					// convert JSON string to Map 
					map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
					
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_NAME, "fep-sink-results-table");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, "feptype");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_OPERATION, ElasticsearchOperation.Index);
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_ID, map.get("id"));
					exchange.getIn().setBody(map);
					
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		})
		.to("elasticsearch-rest://docker-cluster");
		
		from("kafka:fep-sink-results?brokers={{kafka.server}}:{{kafka.port}}&autoOffsetReset=earliest")
		.transform(body().append("\n"))
		.to("file:{{file.directory.path}}SINK/?fileName=fep-sink-results-${date:now:yyyyMMdd}.log&charset=utf-8&allowNullBody=true&fileExist=Append")
		.process(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				try {
					String json = exchange.getIn().getBody(String.class);
					
					Map<String, Object> map = new HashMap<String, Object>();
					
					// convert JSON string to Map 
					map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
					
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_NAME, "fep-sink-results-stream");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_INDEX_TYPE, "feptype");
					exchange.getIn().setHeader(ElasticsearchConstants.PARAM_OPERATION, ElasticsearchOperation.Index);
					exchange.getIn().setBody(map);
					
				} catch (JsonGenerationException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		})
		.to("elasticsearch-rest://{{elasticsearch.clustername}}");
		
	}
	
	private boolean isStringInteger(String number ){
	    try{
	        Integer.parseInt(number);
	    }catch(Exception e ){
	        return false;
	    }
	    return true;
	}
	
	private boolean isStringJournalDate(String date ){
		try {
			LocalDateTime.parse(date);
		} catch(java.time.format.DateTimeParseException e) {
			return false;
		}
	    return true;
	}

	private String getDateFromJournal(String rawdate) { // 2018-12-30T09:11
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime localDateTime = LocalDateTime.parse(rawdate);
		String foramttedString = localDateTime.format(dateFormatter);
		 
		return foramttedString;
	}
	
	//Format of the date defined in the input String
	DateFormat dfCore = new SimpleDateFormat("MM/dd/yy");
	//Desired format
	DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd");
	private String getDateFromCore(String rawdate) { // 10/12/17
		String foramttedString = null;
		try{
			//Changing the format of date and storing it in String
			foramttedString = outputformat.format(dfCore.parse(rawdate));
		}catch(ParseException pe){
			pe.printStackTrace();
		}
		
		return foramttedString;
	}

	private boolean isStringCoreDate(String date ){
		try{
			outputformat.format(dfCore.parse(date));
		}catch(ParseException pe){
			return false;
		}
		return true;
	}
	
	//Format of the date defined in the input String
	DateFormat dfPostilion = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
	private String getDateFromPostilion(String rawdate) { // 12/10/2017 06:39:17 AM
		String foramttedString = null;
		try{
			//Changing the format of date and storing it in String
			foramttedString = outputformat.format(dfPostilion.parse(rawdate));
		}catch(ParseException pe){
			pe.printStackTrace();
		}
		
		return foramttedString;
	}
	

	private boolean isStringFepDate(String date ){
		try{
			outputformat.format(dfPostilion.parse(date));
		}catch(ParseException pe){
			return false;
		}
		return true;
	}
	
	public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

	
}