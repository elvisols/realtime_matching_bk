package com.aet.module.matching.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.aet.module.matching.entities.core.CoreTicket;
import com.aet.module.matching.entities.fep.FepTicket;
import com.aet.module.matching.repository.CoreTicketRepository;
import com.aet.module.matching.repository.FepTicketRepository;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private CoreTicketRepository coreTicketRepository;
	
	@Autowired
	private FepTicketRepository fepTicketRepository;
	
	@Override
	public Page<CoreTicket> findByCoreTicketId(String tid, PageRequest pageRequest) {
		return coreTicketRepository.findById(tid, pageRequest);
	}

	@Override
	public Page<FepTicket> findByFepTicketId(String tid, PageRequest pageRequest) {
		return fepTicketRepository.findById(tid, pageRequest);
	}

	@Override
	public Page<CoreTicket> findByCoreLogtime(Date startDate, Date endDate, PageRequest pageRequest) {
		LocalDateTime sd = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDateTime ed = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

		return coreTicketRepository.findByLogtime(sd, ed, pageRequest);
	}

	@Override
	public Page<FepTicket> findByFepLogtime(Date startDate, Date endDate, PageRequest pageRequest) {
		LocalDateTime sd = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
		LocalDateTime ed = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
		
		return fepTicketRepository.findByLogtime(sd, ed, pageRequest);
	}

	@Override
	public Page<CoreTicket> findAllCoreTicket(PageRequest pageRequest) {
		return coreTicketRepository.findAll(pageRequest);
	}

	@Override
	public Page<FepTicket> findAllFepTicket(PageRequest pageRequest) {
		return fepTicketRepository.findAll(pageRequest);
	}

}
