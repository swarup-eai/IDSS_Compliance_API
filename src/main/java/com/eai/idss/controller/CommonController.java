package com.eai.idss.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eai.idss.dao.GenericDao;
import com.eai.idss.model.User;
import com.eai.idss.repository.UserRepository;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.DashboardRequest;
import com.eai.idss.vo.DashboardResponse;
import com.eai.idss.vo.TileVo;


@RestController
@CrossOrigin
public class CommonController {

	
	@Autowired
	private GenericDao gd;
	
    @Autowired
    private UserRepository userRepository;
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/compliance-dashboard", produces = "application/json")
	public ResponseEntity<DashboardResponse> getDashboardData(@RequestHeader String userName, @RequestBody DashboardRequest dbr) throws IOException {
    	DashboardResponse dr = new DashboardResponse();
	    try {
	    	Map<String,Map<String,List<TileVo>>> dashboardMap = new LinkedHashMap<String, Map<String,List<TileVo>>>();
	    	dashboardMap.put("concentTile", gd.getConcentTileData(dbr));
	    	dashboardMap.put("legalTile", gd.getLegalTileData(dbr));
	    	dashboardMap.put("visitTile", gd.getVisitsTileData(dbr));
	    	dashboardMap.put("myVisits", gd.getMyVisitsData(userName));
	    	dr.setDashboardMap(dashboardMap);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /compliance-dashboard", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<DashboardResponse>(dr,HttpStatus.OK);
	}
    
	@RequestMapping(method = RequestMethod.GET, value = "/drop-down-values", produces = "application/json")
	public ResponseEntity<Map<String,List<String>>> getDropDownData(@RequestHeader(value="userName") String userName) throws IOException {
    	User u = userRepository.findByUserName(userName);
    	Map<String,List<String>> dd = new HashMap<String, List<String>>();
    	if(u!=null & (u.getDesignation().equalsIgnoreCase("MS") || u.getDesignation().equalsIgnoreCase("HOD")))
    		dd.put("regionList", IDSSUtil.getRegionList());
    	else
    		dd.put("regionList", new ArrayList<String>(Arrays.asList(u.getRegion())));
    	dd.put("categoryList", IDSSUtil.getCategoryList());
    	dd.put("scaleList", IDSSUtil.getScaleList());
    	dd.put("typeList", IDSSUtil.getTypeList());
    	dd.put("complianceScoreList", IDSSUtil.getComplianceScoreList());
    	dd.put("legalActionsList", IDSSUtil.getLegalActionsDropdownList());
    	dd.put("pendingCasesList", IDSSUtil.getPendingCasesList());
	    return new ResponseEntity<Map<String,List<String>>>(dd,HttpStatus.OK);
	}
}
