package com.aet.module.matching.service;


import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aet.module.matching.entities.Tuple;
import com.aet.module.matching.processors.KStreamPrinter;
import com.aet.module.matching.serde.StreamsSerdes;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class PollResultService {
	
	@Value("${kafka.server}")
	private String kafka_server;
	
	@Value("${kafka.port}")
	private String kafka_port;

	
	public void process() {
        StreamsConfig streamsConfig = new StreamsConfig(getProperties());
        
        Deserializer<String> stringDeserializer = Serdes.String().deserializer();
        Serializer<String> stringSerializer = Serdes.String().serializer();
        
        Serde<Tuple<com.aet.module.matching.entities.core.Unimpacted, com.aet.module.matching.entities.core.Matched, com.aet.module.matching.entities.core.Dispense>> coreExceptionLogTuple = StreamsSerdes.CoreExceptionLogTupleSerde();
        Serde<Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense>> fepExceptionLogTuple = StreamsSerdes.FepExceptionLogTupleSerde();
        
        Serializer<Tuple<com.aet.module.matching.entities.core.Unimpacted, com.aet.module.matching.entities.core.Matched, com.aet.module.matching.entities.core.Dispense>> coreTupleSerializer = coreExceptionLogTuple.serializer();
        Serializer<Tuple<com.aet.module.matching.entities.fep.Unimpacted, com.aet.module.matching.entities.fep.Matched, com.aet.module.matching.entities.fep.Dispense>> fepTupleSerializer = fepExceptionLogTuple.serializer();
        
        Serde<com.aet.module.matching.entities.core.Unimpacted> coreUnimpactedSerde = StreamsSerdes.CoreUnimpactedItemSerde();
        Deserializer<com.aet.module.matching.entities.core.Unimpacted> coreUnimpactedDeserializer = coreUnimpactedSerde.deserializer();
        Serde<com.aet.module.matching.entities.fep.Unimpacted> fepUnimpactedSerde = StreamsSerdes.FepUnimpactedItemSerde();
        Deserializer<com.aet.module.matching.entities.fep.Unimpacted> fepUnimpactedDeserializer = fepUnimpactedSerde.deserializer();

        Serde<com.aet.module.matching.entities.core.Matched> coreMatchedItemSerde = StreamsSerdes.CoreMatchedItemSerde();
        Deserializer<com.aet.module.matching.entities.core.Matched> coreMatchedItemDeserializer = coreMatchedItemSerde.deserializer();
        Serde<com.aet.module.matching.entities.fep.Matched> fepMatchedItemSerde = StreamsSerdes.FepMatchedItemSerde();
        Deserializer<com.aet.module.matching.entities.fep.Matched> fepMatchedItemDeserializer = fepMatchedItemSerde.deserializer();
        
        Serde<com.aet.module.matching.entities.core.Dispense> coreDispenseItemSerde = StreamsSerdes.CoreDispenseItemSerde();
        Deserializer<com.aet.module.matching.entities.core.Dispense> coreDispenseItemDeserializer = coreDispenseItemSerde.deserializer();
        Serde<com.aet.module.matching.entities.fep.Dispense> fepDispenseItemSerde = StreamsSerdes.FepDispenseItemSerde();
        Deserializer<com.aet.module.matching.entities.fep.Dispense> fepDispenseItemDeserializer = fepDispenseItemSerde.deserializer();


        Topology topology = new Topology();

        topology.addSource("Core-Unimpacted-Source", stringDeserializer, coreUnimpactedDeserializer, "CORE_UNIMPACTED")
                .addSource("Core-Matched-Source", stringDeserializer, coreMatchedItemDeserializer, "CORE_MATCHED")
                .addSource("Core-Dispense-Source", stringDeserializer, coreDispenseItemDeserializer, "CORE_DISPENSE")
                .addProcessor("Core-Unimpacted-Processor", com.aet.module.matching.processors.core.UnimpactedItemProcessor::new, "Core-Unimpacted-Source")
                .addProcessor("Core-Matched-Processor", com.aet.module.matching.processors.core.MatchedItemProcessor::new, "Core-Matched-Source")
                .addProcessor("Core-Dispense-Processor", com.aet.module.matching.processors.core.DispenseItemProcessor::new, "Core-Dispense-Source")
                .addProcessor("Core-Sink-Processor", com.aet.module.matching.processors.core.SinkProcessor::new, "Core-Unimpacted-Processor", "Core-Matched-Processor", "Core-Dispense-Processor")
                .addSink("Core-Tuple-Sink", "core-sink-results", stringSerializer, coreTupleSerializer, "Core-Sink-Processor");
        topology        
                .addSource("Fep-Unimpacted-Source", stringDeserializer, fepUnimpactedDeserializer, "FEP_UNIMPACTED")
                .addSource("Fep-Matched-Source", stringDeserializer, fepMatchedItemDeserializer, "FEP_MATCHED")
                .addSource("Fep-Dispense-Source", stringDeserializer, fepDispenseItemDeserializer, "FEP_DISPENSE")
                .addProcessor("Fep-Unimpacted-Processor", com.aet.module.matching.processors.fep.UnimpactedItemProcessor::new, "Fep-Unimpacted-Source")
                .addProcessor("Fep-Matched-Processor", com.aet.module.matching.processors.fep.MatchedItemProcessor::new, "Fep-Matched-Source")
                .addProcessor("Fep-Dispense-Processor", com.aet.module.matching.processors.fep.DispenseItemProcessor::new, "Fep-Dispense-Source")
                .addProcessor("Fep-Sink-Processor", com.aet.module.matching.processors.fep.SinkProcessor::new, "Fep-Unimpacted-Processor", "Fep-Matched-Processor", "Fep-Dispense-Processor")
                .addSink("Fep-Tuple-Sink", "fep-sink-results", stringSerializer, fepTupleSerializer, "Fep-Sink-Processor");

//        topology.addProcessor("PrintCore", new KStreamPrinter("Co-Grouping-1"), "Core-Sink-Processor");
//        topology.addProcessor("PrintFep", new KStreamPrinter("Co-Grouping-2"), "Fep-Sink-Processor");

        KafkaStreams kafkaStreams = new KafkaStreams(topology, streamsConfig);
        
        log.info("... PollResultService Application Starting ...");
        kafkaStreams.start();
        
        // print the topology 
        kafkaStreams.localThreadsMetadata().forEach(data -> System.out.println(" >>> PollResultService Topology:-- " + data));

        // shutdown hook to correctly close the streams  application
         Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
        
	}
	
	private Properties getProperties() {
		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "poll-result-application-service-a");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafka_server + ":" + kafka_port);
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
		
		config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
		
		return config;
	}
	

}
