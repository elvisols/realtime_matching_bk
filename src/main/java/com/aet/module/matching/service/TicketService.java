package com.aet.module.matching.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.aet.module.matching.entities.core.CoreTicket;
import com.aet.module.matching.entities.fep.FepTicket;

public interface TicketService {
	
    Page<CoreTicket> findByCoreTicketId(String tid, PageRequest pageRequest);

    Page<FepTicket> findByFepTicketId(String tid, PageRequest pageRequest);
    
    Page<CoreTicket> findByCoreLogtime(Date startDate, Date endDate, PageRequest pageRequest);

    Page<FepTicket> findByFepLogtime(Date startDate, Date endDate, PageRequest pageRequest);

    Page<CoreTicket> findAllCoreTicket(PageRequest pageRequest);
    
    Page<FepTicket> findAllFepTicket(PageRequest pageRequest);

}
