package com.aet.module.matching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.aet.module.matching.entities.Criteria;
import com.aet.module.matching.repository.CriteriaRepository;


@Service
public class CriteriaServiceImpl  implements CriteriaService {

	@Autowired
	private CriteriaRepository criteriaRepository;
	
	@Override
	public Criteria save(Criteria c) {
		return criteriaRepository.save(c);
	}

	@Override
	public void delete(String cid) {
		criteriaRepository.deleteById(cid);
	}

	@Override
	public Criteria getCriterion(String cid) {
		return criteriaRepository.findById(cid).get();
	}

	@Override
	public Page<Criteria> getAllCriteria(PageRequest pageRequest) {
		return criteriaRepository.findAll(pageRequest);
	}
	

}
