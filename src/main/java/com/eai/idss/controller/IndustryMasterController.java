package com.eai.idss.controller;

import java.io.IOException;
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

import com.eai.idss.dao.IndustryMasterDao;
import com.eai.idss.model.IndustryMaster;
import com.eai.idss.model.LegalDataMaster;
import com.eai.idss.model.User_Filters;
import com.eai.idss.repository.UserFiltersRepository;
import com.eai.idss.repository.UserRepository;
import com.eai.idss.vo.ComlianceScoreFilter;
import com.eai.idss.vo.IndustryMasterRequest;


@RestController
@CrossOrigin
public class IndustryMasterController {

	@Autowired
	private IndustryMasterDao imd;
	
    @Autowired
    private UserRepository userRepository;
    
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
			return new ResponseEntity("Exception in saving industry-master-filter.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<String>("Saved filter.",HttpStatus.OK);
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
   	@RequestMapping(method = RequestMethod.POST, value = "/industry-master-filter/compliance-score",  produces = "application/json")
   	public ResponseEntity <Map<String,List<LegalDataMaster>>> getComplianceScoreDatabyDate(@RequestBody ComlianceScoreFilter imr,Pageable pageable) throws IOException {
       	Map<String,List<LegalDataMaster>> iml = null;
       	try {
   	    	iml=imd.getByIndustryNameComplianceScoreData(imr,pageable);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /industry-master-filter/compliance-score", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
        return new ResponseEntity<Map<String,List<LegalDataMaster>>>(iml,HttpStatus.OK);
        
    }
   
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.POST, value = "/industry-master-filter/customFilter/compliance-score",  produces = "application/json")
   	public ResponseEntity <List<LegalDataMaster>>getCustomFilterData(@RequestBody ComlianceScoreFilter imr,Pageable pageable) throws IOException {
       	List<LegalDataMaster> iml = null;
       	try {
   	    	iml=imd.getDataBetweenDuration(imr,pageable);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /industry-master-filter/compliance-score", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
        return new ResponseEntity<List<LegalDataMaster>>(iml,HttpStatus.OK);
        
    }
    
    
}
