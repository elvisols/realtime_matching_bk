package com.aet.module.matching.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.aet.module.matching.entities.Channel;
import com.aet.module.matching.entities.PseudoReference;

@Repository
public interface PseudoReferenceRepository extends ElasticsearchRepository<PseudoReference, String>{
	
	Optional<PseudoReference> findByChannel(Channel refName);
	
	Page<PseudoReference> findAll(Pageable pageable);

}
