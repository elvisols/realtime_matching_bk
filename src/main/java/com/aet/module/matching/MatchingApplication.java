package com.aet.module.matching;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.aet.module.matching.config.Param;
import com.aet.module.matching.entities.Channel;
import com.aet.module.matching.entities.GLCore;
import com.aet.module.matching.entities.Journal;
import com.aet.module.matching.entities.Postilion;
import com.aet.module.matching.entities.PseudoReference;
import com.aet.module.matching.entities.TestOne;
import com.aet.module.matching.entities.TestTwo;
import com.aet.module.matching.repository.PseudoReferenceRepository;
import com.aet.module.matching.serde.StreamsSerdes;
import com.aet.module.matching.service.KsqlApiService;
import com.aet.module.matching.service.PollResultService;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/*
 * Running the application:
 * - Create the KTable for all entities
 * - Call the corresponding endpoints to create stream based on defined criteria
 * - Monitor the output in your sinks.
 */
@Slf4j
@EnableSwagger2
@SpringBootApplication
public class MatchingApplication implements CommandLineRunner {

	@Autowired
	private PollResultService pollResultService;
	
	@Autowired 
	private KsqlApiService ksqlApiService;
	
	@Autowired
	private PseudoReferenceRepository pref;
	
	public static String KAFKA_SERVER;
	
	public static String KAFKA_PORT;
	
	@Value("${kafka.server}")
    public void setKAFKA_SERVER(String kafka_server) {
		KAFKA_SERVER = kafka_server;
    }
	
	@Value("${kafka.port}")
	public void setKAFKA_PORT(String kafka_port) {
		KAFKA_PORT = kafka_port;
	}
	
	public static void main(String[] args) {
		
		SpringApplication.run(MatchingApplication.class, args);
		
		StreamsConfig streamsConfig = new StreamsConfig(getProperties());
		
		StreamsBuilder builder = new StreamsBuilder();

//        Serde<TestTwo> testTwoSerde = StreamsSerdes.TestTwoSerde();
//        Serde<TestOne> testOneSerde = StreamsSerdes.TestOneSerde();        
        Serde<Journal> journalDatSerde = StreamsSerdes.JournalSerde();
        Serde<GLCore> glCoreSerde = StreamsSerdes.GLCoreSerde();
        Serde<Postilion> postilionSerde = StreamsSerdes.PostilionSerde();
        Serde<String> stringSerde = Serdes.String();
        
//        KStream<String, TestTwo> testTwoStream = builder.stream(Param.TEST2_CSV_TOPIC, Consumed.with(Serdes.String(), testTwoSerde))
//        		.filter((k, v) -> v != null)
//        		.selectKey((k,v) -> v.getCode().toString());
//        
//        KStream<String, TestOne> testOneStream = builder.stream(Param.TEST1_CSV_TOPIC, Consumed.with(Serdes.String(), testOneSerde))
//        		.filter((k, v) -> v != null)
//        		.selectKey((k,v) -> v.getId().toString());
        
        KStream<String, Journal> journalStream = builder.stream(Param.JOURNAL_CSV_TOPIC, Consumed.with(Serdes.String(), journalDatSerde))
        		.filter((k, v) -> v != null)
        		.selectKey((k,v) -> v.getId());
        
        KStream<String, GLCore> glCoreStream = builder.stream(Param.GLCORE_CSV_TOPIC, Consumed.with(Serdes.String(), glCoreSerde))
        		.filter((k, v) -> v != null)
        		.selectKey((k,v) -> v.getId());
        
        KStream<String, Postilion> postilionStream = builder.stream(Param.FEP_CSV_TOPIC, Consumed.with(Serdes.String(), postilionSerde))
        		.filter((k, v) -> v != null)
        		.selectKey((k,v) -> v.getId());

        
//        testTwoStream.to(Param.TEST2_TOPIC, Produced.with(stringSerde, testTwoSerde));
//        
//        testOneStream.to(Param.TEST1_TOPIC, Produced.with(stringSerde, testOneSerde));
        
        journalStream.to(Param.JOURNAL_TOPIC, Produced.with(stringSerde, journalDatSerde));
        
        glCoreStream.to(Param.GLCORE_TOPIC, Produced.with(stringSerde, glCoreSerde));
        
        postilionStream.to(Param.FEP_TOPIC, Produced.with(stringSerde, postilionSerde));
        
        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), streamsConfig);

        log.info("... Kafka Application Starting ...");
        kafkaStreams.start();
        
        // print the topology
        kafkaStreams.localThreadsMetadata().forEach(data -> System.out.println(">>>Topology:-- " + data));

        // shutdown hook to correctly close the streams application
        // Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            kafkaStreams.close();
//            kafkaStreams2.close();
        }));

	}

	private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "matching_application");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "matching_group");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "matching_driver_client");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER + ":" + KAFKA_PORT);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "1");
        props.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "10000");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
//        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, TransactionTimestampExtractor.class);
        return props;
    }

	@Override
	public void run(String... args) throws Exception {
		
		Thread thread = new Thread(new Runnable() {

		    @Override
		    public void run() {
		    	log.info("... about to call KsqlApiService");
//		    	ksqlApiService.getSinkRecords();
		    }
		            
		});
		        
		thread.start();
		
		log.info("... about to call pollResultService");
		this.pollResultService.process();
		
		// Populate sample pseudo references
		PseudoReference pr1 = new PseudoReference();
		if(!pref.existsById("arJNk2oBwEuQ4YqHSSH_")) {
			log.info("...setting core pseudo ref default");
			pr1.setId("arJNk2oBwEuQ4YqHSSH_");
			pr1.setChannel(Channel.CORE);
			pr1.setPosition(Arrays.asList(5, 9));
			pref.save(pr1);
		}
		
		PseudoReference pr2 = new PseudoReference();
		if(!pref.existsById("FbJNk2oBwEuQ4YqHWyOO")) {
			log.info("...setting fep pseudo ref default");
			pr2.setId("FbJNk2oBwEuQ4YqHWyOO");
			pr2.setChannel(Channel.FEP);
			pr2.setPosition(Arrays.asList(12, 8));
			pref.save(pr2);
		}

		PseudoReference pr3 = new PseudoReference();
		if(!pref.existsById("M7JNk2oBwEuQ4YqHRiGD")) {
			log.info("...setting journal pseudo ref default");
			pr3.setId("M7JNk2oBwEuQ4YqHRiGD");
			pr3.setChannel(Channel.JOURNAL);
			pr3.setPosition(Arrays.asList(9, 3));
			pref.save(pr3);
		}
		
	}
	
	
//	private static Properties getProperties2() {
//		Properties props = new Properties();
//		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "matching_application2");
//		props.put(ConsumerConfig.GROUP_ID_CONFIG, "matching_group");
//		props.put(ConsumerConfig.CLIENT_ID_CONFIG, "matching_driver_client2");
//		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");
//		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//		props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, "1");
//		props.put(ConsumerConfig.METADATA_MAX_AGE_CONFIG, "10000");
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
//		props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, 1);
////        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, TransactionTimestampExtractor.class);
//		return props;
//	}
	
	

}
