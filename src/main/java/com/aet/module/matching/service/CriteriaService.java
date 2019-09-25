package com.aet.module.matching.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.aet.module.matching.entities.Criteria;


public interface CriteriaService {
	
	Criteria save(Criteria c);
    
	void delete(String cid);

	Criteria getCriterion(String cid);
    
    Page<Criteria> getAllCriteria(PageRequest pageRequest);
    

}
