package com.eai.idss.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eai.idss.dao.LegalDao;
import com.eai.idss.model.Legal;
import com.eai.idss.model.User;
import com.eai.idss.repository.UserRepository;
import com.eai.idss.vo.LegalDetailsRequest;
import com.eai.idss.vo.LegalFilter;
import com.eai.idss.vo.TileVo;


@RestController
@CrossOrigin
public class LegalController {

	@Autowired
	private LegalDao cd;
	
	
	
    @Autowired
    private UserRepository userRepository;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/legal-details", produces = "application/json")
	public ResponseEntity<List<Legal>> getLegalDetailsData(@RequestBody LegalDetailsRequest cdr, Pageable pageable) throws IOException {
    	List<Legal> cl = null;
	    try {
	    	cl =  cd.getLegalPaginatedRecords(cdr,pageable);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /Legal-details", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<List<Legal>>(cl,HttpStatus.OK);
	}
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/legal-dashboard/action-by-category-type", produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getLegalActionByCategoryTypeData() throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new LinkedHashMap<String,Map<String, Map<String,List<TileVo>>>>();
	    try {
	    	ct.put("actionByCategory",cd.getLegalActionsByIndustryScaleCategoryData());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /legal-dashboard/action-by-category-type", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/legal-dashboard/pending-legal-actions",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,List<TileVo>>>> getLegalDashboardPendingData() throws IOException {
    	Map<String,Map<String,List<TileVo>>> ct = new LinkedHashMap<String, Map<String,List<TileVo>>>();
	    try {
	    	ct.put("pendingRequest",cd.getPendingLegalActionsData());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /legal-dashboard/pending-legal-actions", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,List<TileVo>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/legal-dashboard/request-by-sub-region",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getLegalDataBySubRegionData(@RequestHeader String userName, @RequestBody LegalFilter cf) throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new LinkedHashMap<String, Map<String,Map<String,List<TileVo>>>>();
	    try {
	    	User u = userRepository.findByUserName(userName);
	    	ct.put("bySubRegionRequest",cd.getBySubRegionLegalData(u.getRegion(), cf));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /legal-dashboard/request-by-sub-region", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/legal-dashboard/pending-by-team",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,List<TileVo>>>> getLegalDataPendingByTeam(@RequestBody LegalFilter cf) throws IOException {
    	Map<String,Map<String,List<TileVo>>> ct = new LinkedHashMap<String, Map<String,List<TileVo>>>();
	    try {
	    	ct.put("bySubRegionRequest",cd.getByTeamLegalData(cf));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /legal-dashboard/pending-by-team", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,List<TileVo>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/legal-dashboard/by-region", produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getLegalDataByRegion(LegalFilter cf) throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new LinkedHashMap<String,Map<String, Map<String,List<TileVo>>>>();
	    try {
	    	ct.put("byRegion",cd.getByRegionLegalData(cf));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /legal-dashboard/by-region", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
    
}
