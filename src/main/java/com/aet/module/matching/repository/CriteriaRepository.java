package com.aet.module.matching.repository;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.aet.module.matching.entities.Criteria;
import com.aet.module.matching.entities.core.CoreTicket;

@Repository
public interface CriteriaRepository extends ElasticsearchRepository<Criteria, String>{
	
//	Page<CoreTicket> findById(String coreId, Pageable pageable);
//	Page<CoreTicket> findByLogtime(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
//	
//	Page<CoreTicket> findAll(Pageable pageable);
	
}
