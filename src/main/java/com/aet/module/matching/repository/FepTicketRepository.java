package com.aet.module.matching.repository;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.aet.module.matching.entities.fep.FepTicket;

@Repository
public interface FepTicketRepository extends ElasticsearchRepository<FepTicket, String>{
	
	Page<FepTicket> findById(String fepId, Pageable pageable);

	@Query("{"
			+ "\"range\": {" 
			+	"\"logtime\": {"
			+		"\"gte\": \"?0\","
			+		"\"lte\": \"?1\""
//					"format": "dd/MM/yyyy||yyyy"
//					"time_zone": "+01:00"
			+	"}"
			+ "}"
		+ "}")
	Page<FepTicket> findByLogtime(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
	
	Page<FepTicket> findAll(Pageable pageable);

}
