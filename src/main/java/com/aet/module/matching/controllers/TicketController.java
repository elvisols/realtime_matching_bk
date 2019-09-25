package com.aet.module.matching.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aet.module.matching.entities.core.CoreTicket;
import com.aet.module.matching.entities.fep.FepTicket;
import com.aet.module.matching.service.TicketService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("tickets")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Tickets", description="This controller exposes endpoints for getting tickets generated")
public class TicketController {
	
	@Autowired
	TicketService ticketService;
	 
	private Calendar cal = new GregorianCalendar();
	
	/**
	* This method fetches all journal against core tickets
	*
	* @param void
	* @method GET
	* @return result as list of paginated object
	*/
	@GetMapping(value="core", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all Journal against Core tickets")
	public Page<CoreTicket> findCoreTickets(@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		
		Page<CoreTicket> coreTickets = ticketService.findAllCoreTicket(PageRequest.of(page, size));

		return coreTickets;
	}
	
	/**
	 * This method fetches all journal against fep tickets
	 *
	 * @param void
	 * @method GET
	 * @return result as list of paginated object
	 */
	@GetMapping(value="fep", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all Journal against Fep tickets")
	public Page<FepTicket> findFepTickets(@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		
		Page<FepTicket> fepTickets = ticketService.findAllFepTicket(PageRequest.of(page, size));
		
		return fepTickets;
	}
	
	/**
	 * This method fetches all core tickets by id
	 *
	 * @param void
	 * @method GET
	 * @return result as list of page object
	 */
	@GetMapping(value="/core/{tid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all Core tickets by id")
	public Page<CoreTicket> findCoreTicketsById(@PathVariable String tid, @RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		
		Page<CoreTicket> coreTickets = ticketService.findByCoreTicketId(tid, PageRequest.of(page, size));
		
		return coreTickets;
	}
	
	/**
	 * This method fetches all fep tickets by id
	 *
	 * @param void
	 * @method GET
	 * @return result as list of page object
	 */
	@GetMapping(value="/fep/tickets/{tid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all Fep tickets by id")
	public Page<FepTicket> findFepTicketsById(@PathVariable String tid, @RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		
		Page<FepTicket> fepTickets = ticketService.findByFepTicketId(tid, PageRequest.of(page, size));
		
		return fepTickets;
	}
	
	/**
	 * This method fetches all core tickets by logtime
	 *
	 * @param void
	 * @method GET
	 * @return result as list of page object
	 */
	@GetMapping(value="/core/tickets/logtime", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all Core Tickets by Logtime")
	public Page<CoreTicket> findCoreTicketByLogtime(
			@ApiParam(value = "Format: 'yyyy-MM-dd_hh:mm:ss' E.g: 2019-04-30_13:45:44", required = false) @RequestParam(value = "start-date", required = true, defaultValue="beginning-of-day") Date startDate, 
			@ApiParam(value = "Format: 'yyyy-MM-dd_hh:mm:ss' E.g: 2019-04-30_13:45:44", required = false) @RequestParam(value = "end-date", required = true, defaultValue="now") Date endDate,
			@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		
		Page<CoreTicket> coreTickets = ticketService.findByCoreLogtime(startDate, endDate, PageRequest.of(page, size));
		
		return coreTickets;
	}
	
	/**
	 * This method fetches all fep tickets by logtime
	 *
	 * @param void
	 * @method GET
	 * @return result as list of page object
	 */
	@GetMapping(value="/fep/tickets/logtime", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all Fep Tickets by Logtime")
	public Page<FepTicket> findFepTicketByLogtime(
			@ApiParam(value = "Format: 'yyyy-MM-dd_hh:mm:ss' E.g: 2019-04-30_13:45:44", required = false) @RequestParam(value = "start-date", required = true, defaultValue="beginning-of-day") Date startDate, 
			@ApiParam(value = "Format: 'yyyy-MM-dd_hh:mm:ss' E.g: 2019-04-30_13:45:44", required = false) @RequestParam(value = "end-date", required = true, defaultValue="now") Date endDate,
			@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		
		Page<FepTicket> fepTickets = ticketService.findByFepLogtime(startDate, endDate, PageRequest.of(page, size));
		
		return fepTickets;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) throws Exception {
	    final DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
	    final CustomDateEditor dateEditor = new CustomDateEditor(df, true) {
	        @Override
	        public void setAsText(String text) throws IllegalArgumentException {
	            if ("now".equals(text)) {
	                setValue(new Date());
	            } else if("beginning-of-day".equals(text)) {
	            	cal.set(Calendar.HOUR_OF_DAY, 0);
	            	cal.set(Calendar.MINUTE, 0);
	            	cal.set(Calendar.SECOND, 0);
	            	cal.set(Calendar.MILLISECOND, 0);
	            	setValue(cal.getTime());
	            } else {
	            	super.setAsText(text);
	            }
	        }
	    };
	    binder.registerCustomEditor(Date.class, dateEditor);
	}

}
