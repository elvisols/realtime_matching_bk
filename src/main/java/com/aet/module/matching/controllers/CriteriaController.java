package com.aet.module.matching.controllers;

import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aet.module.matching.entities.Criteria;
import com.aet.module.matching.entities.PseudoReference;
import com.aet.module.matching.repository.PseudoReferenceRepository;
import com.aet.module.matching.service.CriteriaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("criteria")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value="Criteria", description="This controller exposes endpoints for defining and managing your criterias")
public class CriteriaController {
	
	@Autowired
	private CriteriaService criteriaService;
	

    @Value("#{'${criteria.journal.fields}'.split(',')}") 
	private List<String> journalFields;
    
    @Value("#{'${criteria.fep.fields}'.split(',')}") 
    private List<String> fepFields;
    
    @Value("#{'${criteria.core.fields}'.split(',')}") 
    private List<String> coreFields;
    
    @Autowired
	private PseudoReferenceRepository pref;
	
	/** 
	* This method fetches all Criteria
	*
	* @param void
	* @method GET
	* @return result as list of paginated object
	*/
	@GetMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all Criteria...")
	public Page<Criteria> getCriteria(@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		log.info("Getting all criteria");
		
		Page<Criteria> criteria = criteriaService.getAllCriteria(PageRequest.of(page, size));

		return criteria;
		
	}
	
	/**
	 * This method fetches a single Criterium
	 *
	 * @param void
	 * @method GET
	 * @return result as list of paginated object
	 */
	@GetMapping(value="{cid}", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get a Criterion")
	public Criteria getCriterium(@PathVariable String cid) {
		log.info("Getting a criterion...");
		
		Criteria criteria = criteriaService.getCriterion(cid);
		
		return criteria;
		
	}
	
	/**
	 * This method creates a new Criterium
	 *
	 * @param void
	 * @method GET
	 * @return result Criteria object created
	 */
	@PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Save a Criterium")
	public Criteria saveCriteria(@RequestBody Criteria criteria) {
		log.info("Saving a criterion...");
		
		if(criteria.getName() == null) {
            log.error("Error in creating a criterion");
            throw new ValidationException("Item criterion name missing...");
		}

		criteria.setId(null);
		Criteria criterium = criteriaService.save(criteria);
		
		return criterium;
		
	}
	
	/**
	 * This method updates a Criterium
	 * 
	 * @param void
	 * @method GET
	 * @return result Criteria object created
	 */
	@PutMapping(produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Save a Criterium")
	public Criteria updateCriteria(@RequestBody Criteria criteria) {
		log.info("Updating a criterium...");
		
		if (criteria.getId() == null) {
            log.error("Error in updating a criterion");
            throw new ValidationException("Item to update missing...");
        }

        Criteria c = criteriaService.save(criteria);
        
        return c;
		
	}
	
	/**
     * This method deletes a Criterium
     *
     * @param id the id of the criteria to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("{id}")
    @ApiOperation(value = "Delete a Criterium")
    public ResponseEntity<Void> deleteCriteria(@PathVariable  final String id) {
        
    	log.debug("Updating a criterium...");
        
        criteriaService.delete(id);
        
        return ResponseEntity.ok().build();
    
    }
    
	/** 
	* This method fetches all PseudoReference
	*
	* @param void
	* @method GET
	* @return result as list of paginated object
	*/
	@GetMapping(value="pseudoreference", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all PseudoReference...")
	public Iterable<PseudoReference> getPseudoReference(@RequestParam(value = "page", defaultValue="0") int page, @RequestParam(value = "size", defaultValue="10") int size) {
		log.info("Getting all pseudoReference");
		
		Iterable<PseudoReference> prs = pref.findAll();

		return prs;
		
	}

	/**
	 * This method saves or updates a PseudoReference
	 * 
	 * @param void
	 * @method GET
	 * @return result PseudoReference object created
	 */
	@PutMapping(value="pseudoreference", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update a PseudoReference")
	public PseudoReference savePsedoReference(@RequestBody PseudoReference prefObj) {
		log.info("Saving/Updating pseudoReference...");
		
		if (prefObj.getId() == null || !pref.existsById(prefObj.getId())) {
            log.error("Error in updating a pseudo reference");
            throw new ValidationException("Pseudo item to update missing...");
        }
		
		PseudoReference pr = pref.save(prefObj);
		
		return pr;
		
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @GetMapping(value="/journal/fields", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getJournalFields(){

    	return journalFields;
    
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @GetMapping(value="/fep/fields", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getFepFields(){
    	
    	return fepFields;
    	
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @GetMapping(value="/core/fields", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<String> getCoreFields(){
    	
    	return coreFields;
    	
    }
	
}