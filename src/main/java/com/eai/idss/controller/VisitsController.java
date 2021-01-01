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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eai.idss.dao.VisitsDao;
import com.eai.idss.model.LeaveSchedule;
import com.eai.idss.model.User;
import com.eai.idss.model.VisitProcessEfficiency;
import com.eai.idss.model.Visits;
import com.eai.idss.repository.LeaveScheduleRepository;
import com.eai.idss.repository.UserRepository;
import com.eai.idss.vo.TileVo;
import com.eai.idss.vo.VisitDetails;
import com.eai.idss.vo.VisitsByComplianceVo;
import com.eai.idss.vo.VisitsDetailsRequest;
import com.eai.idss.vo.VisitsFilter;
import com.eai.idss.vo.VisitsScheduleDetailsRequest;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class VisitsController {

	@Autowired
	private VisitsDao cd;
	
	@Autowired
	private LeaveScheduleRepository lsr;
	
    @Autowired
    private UserRepository userRepository;
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/visits-details", produces = "application/json")
	public ResponseEntity<List<Visits>> getVisitsDetailsData(@RequestBody VisitsDetailsRequest vdr, Pageable pageable) throws IOException {
    	List<Visits> cl = null;
	    try {
	    	cl =  cd.getVisitsPaginatedRecords(vdr,pageable);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /visits-details", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<List<Visits>>(cl,HttpStatus.OK);
	}
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/visits-dashboard/pending-visits",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,List<TileVo>>>> getVisitsDashboardPendingData(@RequestHeader String userName,@RequestBody VisitsFilter vf) throws IOException {
    	Map<String,Map<String,List<TileVo>>> ct = new LinkedHashMap<String, Map<String,List<TileVo>>>();
	    try {
	    	User u = userRepository.findByUserName(userName);
	    	ct.put("pendingRequest",cd.getPendingVisitsData(vf,u.getRegion(),u.getSubRegion()));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /visits-dashboard/pending-legal-actions", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,List<TileVo>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/visits-dashboard/visits-by-team",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getVisitsDataPendingByTeam(@RequestHeader String userName,@RequestBody VisitsFilter vf) throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new LinkedHashMap<String, Map<String,Map<String,List<TileVo>>>>();
	    try {
	    	User u = userRepository.findByUserName(userName);
	    	ct.put("visitsByTeam",cd.getByTeamVisitsData(vf,u));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /visits-dashboard/pending-by-team", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/visits-dashboard/by-region", produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getVisitsDataByRegion(@RequestBody VisitsFilter vf) throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new LinkedHashMap<String,Map<String, Map<String,List<TileVo>>>>();
	    try {
	    	ct.put("byRegion",cd.getByRegionVisitsData(vf));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /visits-dashboard/by-region", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.POST, value = "/visits-schedule-details", produces = "application/json")
   	public ResponseEntity<List<Visits>> getVisitsScheduleDetailsData(@RequestHeader String userName,@RequestBody VisitsScheduleDetailsRequest vdr, Pageable pageable) throws IOException {
       	List<Visits> cl = null;
   	    try {
   	    	cl =  cd.getVisitsSchedulePaginatedRecords(vdr,pageable,userName);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /visits-schedule-details", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<List<Visits>>(cl,HttpStatus.OK);
   	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.GET, value = "/visits-schedule-current-month", produces = "application/json")
   	public ResponseEntity<Map<String,Map<String,List<TileVo>>>> getVisitsScheduleByScaleCategory(@RequestHeader String userName) throws IOException {
    	Map<String,Map<String,List<TileVo>>> cl =  new LinkedHashMap<String, Map<String,List<TileVo>>>();
   	    try {
   	    	cl.put("visitsForCurrentMonth",cd.getVisitsScheduleByScaleCategory(userName));
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /visits-schedule-current-month", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<Map<String,Map<String,List<TileVo>>>>(cl,HttpStatus.OK);
   	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.POST, value = "/leave-calender", produces = "application/json")
   	public ResponseEntity<String> saveLeaveCalender(@RequestBody LeaveSchedule ls) throws IOException {
   	    try {
   	    	lsr.deleteByUserNameAndMonthYear(ls.getUserName(), ls.getMonthYear());
   	    	lsr.save(ls);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /leave-calender", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<>("Leave schedule updated.",HttpStatus.OK);
   	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.GET, value = "/visits-details/{industryId}/{visitId}", produces = "application/json")
   	public ResponseEntity<VisitDetails> getVisitsDetailsForOneVisit(@PathVariable("industryId") Long industryId,
   			@PathVariable("visitId") Long visitId) throws IOException {
    	VisitDetails vd = null;
   	    try {
   	    	vd = cd.getVisitDetailsForOneIndustryOneVisit(industryId,visitId);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /visits-details/{industryId}/{visitId}", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<VisitDetails>(vd,HttpStatus.OK);
   	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.GET, value = "/visits-details/{industryId}", produces = "application/json")
   	public ResponseEntity<List<Visits>> getVisitsDetailsForIndustry(@PathVariable("industryId") Long industryId,String fromDate,String toDate) throws IOException {
    	List<Visits> vd = null;
   	    try {
   	    	vd = cd.getVisitDetailsForOneIndustry(industryId,fromDate,toDate);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /visits-details/{industryId}", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<List<Visits>>(vd,HttpStatus.OK);
   	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.GET, value = "/visits-details/visits-by-compliance", produces = "application/json")
   	public ResponseEntity<Map<String,List<VisitsByComplianceVo>>> getVisitsByCompliance(@RequestHeader String userName) throws IOException {
    	Map<String,List<VisitsByComplianceVo>> vd = null;
   	    try {
   	    	User u = userRepository.findByUserName(userName);
   	    	vd = cd.getVisitsByCompliance(u.getRegion(), u.getSubRegion());
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /visits-details/visits-by-compliance", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<Map<String,List<VisitsByComplianceVo>>>(vd,HttpStatus.OK);
   	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.GET, value = "/visits-dashboard/visits-process-efficiency/{region}", produces = "application/json")
   	public ResponseEntity<VisitProcessEfficiency> getVisitsProcessEfficiency(@PathVariable("region") String region) throws IOException {
    	VisitProcessEfficiency vd = null;
   	    try {
   	    	vd = cd.getVisitProcessEfficiency(region);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /visits-details/visits-process-efficiency", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<VisitProcessEfficiency>(vd,HttpStatus.OK);
   	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/visits-dashboard/visits-by-sub-region",  produces = "application/json")
	public ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>> getVisitDataBySubRegionData(@RequestHeader String userName,@RequestBody VisitsFilter vf) throws IOException {
    	Map<String,Map<String,Map<String,List<TileVo>>>> ct = new LinkedHashMap<String, Map<String,Map<String,List<TileVo>>>>();
	    try {
	    	User u = userRepository.findByUserName(userName);
	    	ct.put("bySubRegionRequest",cd.getBySubRegionVisitsData(u.getRegion(), vf));
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /visits-dashboard/request-by-sub-region", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<Map<String,Map<String,Map<String,List<TileVo>>>>>(ct,HttpStatus.OK);
	}
}
