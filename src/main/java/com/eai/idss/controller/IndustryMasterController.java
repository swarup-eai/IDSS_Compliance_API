package com.eai.idss.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.eai.idss.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eai.idss.dao.IndustryMasterDao;
import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.User_Filters;
import com.eai.idss.repository.UserFiltersRepository;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class IndustryMasterController {

	@Autowired
	private IndustryMasterDao imd;
	
    @Autowired
    private UserFiltersRepository ufr;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/industry-master", produces = "application/json")
	public ResponseEntity<List<IndustryMaster>> getIndustryMasterTileData(@RequestBody IndustryMasterRequest imr, Pageable pageable) throws IOException {
    	List<IndustryMaster> iml = null;
	    try {
	    	iml =  imd.getIndustryMasterPaginatedRecords(imr,pageable);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /industry-master", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<List<IndustryMaster>>(iml,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/industry-master-filter", produces = "application/json")
	public ResponseEntity<String> saveIndustryMasterFilter(@RequestHeader(value="userName") String userName,@RequestBody IndustryMasterRequest imr, String filterName) throws IOException {
	    try {
	    	User_Filters existingFilter = ufr.findByUserNameAndFilterName(userName, filterName);
	    	if(null!=existingFilter)
	    		return new ResponseEntity("Filter name already exist for user.", HttpStatus.INTERNAL_SERVER_ERROR);
	    	User_Filters uf = new User_Filters();
	    	uf.setFilterName(filterName);
	    	uf.setUserName(userName);
	    	uf.setImr(imr);
	    	ufr.save(uf);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in deleting industry-master-filter.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<String>("Saved filter.",HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.DELETE, value = "/industry-master-filter", produces = "application/json")
	public ResponseEntity<String> deleteIndustryMasterFilter(@RequestHeader(value="userName") String userName, String filterName) throws IOException {
	    try {
	    	User_Filters existingFilter = ufr.findByUserNameAndFilterName(userName, filterName);
	    	if(null==existingFilter)
	    		return new ResponseEntity("Filter Not exist for user.", HttpStatus.INTERNAL_SERVER_ERROR);
	    	ufr.delete(existingFilter);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in saving industry-master-filter.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<String>("Removed filter.",HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/industry-master-filter", produces = "application/json")
	public ResponseEntity<List<User_Filters>> getIndustryMasterFilter(@RequestHeader(value="userName") String userName) throws IOException {
    	List<User_Filters> existingFilters = null;
    	try {
	    	existingFilters = ufr.findByUserName(userName);
	    	
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in saving industry-master-filter.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<List<User_Filters>>(existingFilters,HttpStatus.OK);
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/visit/tile", produces = "application/json")
	public ResponseEntity<List<IndustryMaster>> getVisitTileData(Pageable pageable) throws IOException {
    	List<IndustryMaster> iml = null;
	    try {
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /visit/tile", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<List<IndustryMaster>>(iml,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.GET, value = "/myvisits", produces = "application/json")
	public ResponseEntity<List<IndustryMaster>> getMyVisitsData(Pageable pageable) throws IOException {
    	List<IndustryMaster> iml = null;
	    try {
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /myvisits", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<List<IndustryMaster>>(iml,HttpStatus.OK);
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.POST, value = "/industry-master-score-card/compliance-score",  produces = "application/json")
   	public ResponseEntity <List<ComplianceScoreResponseVo>> getComplianceScoreDatabyDate(@RequestBody ComlianceScoreFilter imr) throws IOException {
    	List<ComplianceScoreResponseVo> iml = null;
       	try {
   	    	iml=imd.getByIndustryIdComplianceScoreData(imr);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /industry-master-score-card/compliance-score", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
        return new ResponseEntity<List<ComplianceScoreResponseVo>>(iml,HttpStatus.OK);
        
    }
   
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(method = RequestMethod.POST, value = "/industry-master-score-card/pollution-score",  produces = "application/json")
   	public ResponseEntity  <List<Map<String,String>>> getPollutionScoreCardDatabyDate(@RequestBody PollutionScoreFilter imr) throws IOException {
    	List<Map<String,String>> iml = null;
       	try {
   	    	iml=imd.getPollutionScoreData(imr);
   		} catch (Exception e) {
   			e.printStackTrace();
   			
   			return new ResponseEntity("Exception in /industry-master-score-card/pollution-score", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
        return new ResponseEntity<List<Map<String,String>>>(iml,HttpStatus.OK);
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(method = RequestMethod.GET, value = "/industry-master-score-card/pollution-score/param-list/{industryId}",  produces = "application/json")
   	public ResponseEntity<List<Map<String,String>>> getPollutionGraphParam(@PathVariable("industryId") long industryId,String form) throws IOException {
    	List<Map<String,String>> iml = null;
       	try {
   	    	iml=imd.getPollutionGraphParam(industryId,form);
   		} catch (Exception e) {
   			e.printStackTrace();
   			
   			return new ResponseEntity("Exception in /industry-master-score-card/pollution-score/param-list/{industryId}", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
        return new ResponseEntity<List<Map<String,String>>>(iml,HttpStatus.OK);
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(method = RequestMethod.GET, value = "/industry-master-score-card/comparison/{industryId}",  produces = "application/json")
   	public ResponseEntity<ComparisonTableResponseVo> getComparisonData(@PathVariable("industryId") long industryId,int consentYear, int esrYear,int form4Year) throws IOException {
    	ComparisonTableResponseVo iml = null;
       	try {
   	    	iml=imd.getComparisonData(industryId,consentYear,esrYear,form4Year);
   		} catch (Exception e) {
   			e.printStackTrace();
   			
   			return new ResponseEntity("Exception in /industry-master-score-card/comparison", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
        return new ResponseEntity<ComparisonTableResponseVo>(iml,HttpStatus.OK);
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(method = RequestMethod.GET, value = "/industry-master-score-card/mandatory-reports/{industryId}",  produces = "application/json")
   	public ResponseEntity<MandatoryReportsResponseVo> getMandatoryReportsData(@PathVariable("industryId") long industryId,int year) throws IOException {
    	MandatoryReportsResponseVo iml = null;
       	try {
   	    	iml=imd.getMandatoryReportsData(industryId,year);
   		} catch (Exception e) {
   			e.printStackTrace();
   			
   			return new ResponseEntity("Exception in /industry-master-score-card/comparison", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
        return new ResponseEntity<MandatoryReportsResponseVo>(iml,HttpStatus.OK);
        
    }
	@RequestMapping(method = RequestMethod.GET, value = "/industry-master-detail", produces = "application/json")
	public ResponseEntity<IndustryMasterDetailResponseVo> getIndustryMasterDetailByIndustryId(@RequestParam(required = true) long indutryId) throws IOException {
		IndustryMasterDetailResponseVo industryMasterDetailResponseVo = null;
		try {
			industryMasterDetailResponseVo = imd.getIndustryMasterDetailByIndustryId(indutryId);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in industry master.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<IndustryMasterDetailResponseVo>(industryMasterDetailResponseVo,HttpStatus.OK);
	}
}
