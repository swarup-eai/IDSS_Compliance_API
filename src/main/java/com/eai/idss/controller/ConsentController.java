package com.eai.idss.controller;

import java.io.IOException;
import java.util.*;

import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.*;
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

import com.eai.idss.dao.ConcentDao;
import com.eai.idss.dao.GenericDao;
import com.eai.idss.model.User;
import com.eai.idss.repository.UserRepository;


@RestController
@CrossOrigin(origins ={"http://localhost:4200", "http://10.10.10.32:8080"})
public class ConsentController {

	@Autowired
	private ConcentDao cd;
	
	
	@Autowired
	private GenericDao gd;
	
    @Autowired
    private UserRepository userRepository;

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/consent-details", produces = "application/json")
	public ResponseEntity<ConsentPaginationResponseVo> getIndustryMasterTileData(@RequestBody ConsentDetailsRequest cdr, Pageable pageable) throws IOException {
    	ConsentPaginationResponseVo cl = null;
	    try {
	    	cl =  cd.getConsentPaginatedRecords(cdr,pageable);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /consent-details", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<ConsentPaginationResponseVo>(cl,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/concent/tile", produces = "application/json")
	public ResponseEntity<Map<String,List<TileVo>>> getConcentTileData() throws IOException {
    	Map<String,List<TileVo>> ct = null;
	    try {
	    	ct = gd.getConcentTileData(null);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in ConcentTile", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,List<TileVo>>>(ct,HttpStatus.OK);
	}
    
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//	@RequestMapping(method = RequestMethod.GET, value = "/concent-dashboard", produces = "application/json")
//	public ResponseEntity<Map<String,Map<String,List<TileVo>>>> getConcentDashboardData(ConcentFilter cf) throws IOException {
//    	Map<String,Map<String,List<TileVo>>> ct = new HashMap<String, Map<String,List<TileVo>>>();
//	    try {
//	    	ct.put("pendingRequest",cd.getPendingRequestConcentData(cf));
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity("Exception in /concent-dashboard", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	    return new ResponseEntity<Map<String,Map<String,List<TileVo>>>>(ct,HttpStatus.OK);
//	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/concent-dashboard/upcoming-renewal", produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getConcentDashboardUpcomingRenewalData(@RequestHeader String userName) throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new HashMap<String,Map<String, Map<String,List<TileVo>>>>();
	    try {
	    	User u = userRepository.findByUserName(userName);
	    	ct.put("upcomingRenewal",cd.getUpcomingRenewalConcentData(u.getRegion(), u.getSubRegion()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /concent-dashboard/upcoming-renewal", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/concent-dashboard/upcoming-renewal-new", produces = "application/json")
	public ResponseEntity<Map<String,Map<String,List<TileVo>>>> getConcentDashboardUpcomingRenewalDataNew(@RequestHeader String userName,@RequestBody ConcentFilter cf) throws IOException {
    	Map<String,Map<String,List<TileVo>>> ct = new HashMap<String, Map<String,List<TileVo>>>();
	    try {
//	    	User u = userRepository.findByUserName(userName);
	    	ct.put("upcomingRenewalNew",cd.getUpcomingRenewalConcentDataNew(cf,cf.getRegion(), cf.getSubRegion()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /concent-dashboard/upcoming-renewal", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,List<TileVo>>>>(ct,HttpStatus.OK);
	    //return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/concent-dashboard/pending-request",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,List<TileVo>>>> getConcentDashboardPendingData(@RequestHeader String userName,@RequestBody ConcentFilter cf) throws IOException {
    	Map<String,Map<String,List<TileVo>>> ct = new HashMap<String, Map<String,List<TileVo>>>();
	    try {
//	    	User u = userRepository.findByUserName(userName);
	    	ct.put("pendingRequest",cd.getPendingRequestConcentData(cf,cf.getRegion(), cf.getSubRegion()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /concent-dashboard/pending-request", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,List<TileVo>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/concent-dashboard/request-by-sub-region",  produces = "application/json")
    public ResponseEntity<Map<String,Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>>>> getConcentDashboardBySubRegionData(@RequestHeader String userName, @RequestBody ConcentFilter cf) throws IOException {
		Map<String,Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>>> ct = new LinkedHashMap<String, Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>>>();
	    try {
	    	User u = userRepository.findByUserName(userName);
	    	List<String> subRegions =new ArrayList<String>();
	    	if(u.getDesignation().equals("RO")){
				subRegions = IDSSUtil.getSubRegion(u.getRegion());
			}else{
	    		subRegions.add(u.getSubRegion());
			}
	    	ct.put("bySubRegionRequest",cd.getBySubRegionConcentData(subRegions, cf));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /concent-dashboard/pending-request", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/concent-dashboard/pending-by-team",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getConcentDashboardPendingByTeamData(@RequestHeader String userName,@RequestBody ConcentFilter cf) throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new HashMap<String, Map<String,Map<String,List<TileVo>>>>();
	    try {
	    	User u = userRepository.findByUserName(userName);
	    	ct.put("pendingByTeamConcentData",cd.getByTeamConcentData(cf,u));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /concent-dashboard/pending-request", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/concent-dashboard/by-region", produces = "application/json")
	public ResponseEntity<Map<String,Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>>>> getConcentDashboardByRegionData(ConcentFilter cf) throws IOException {
    	Map<String,Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>>> ct = new HashMap<String,Map<String, List<ConsentDrillDownRegionAndSubRegionResponseVO>>>();
	    try {
	    	ct.put("byRegion",cd.getByRegionConcentData(cf));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /concent-dashboard/by-region", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,List<ConsentDrillDownRegionAndSubRegionResponseVO>>>>(ct,HttpStatus.OK);
	}
    
}
