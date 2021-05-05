package com.eai.idss.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eai.idss.vo.HeatmapResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eai.idss.dao.GenericDao;
import com.eai.idss.model.User;
import com.eai.idss.repository.UserRepository;
import com.eai.idss.util.IDSSUtil;
import com.eai.idss.vo.DashboardRequest;
import com.eai.idss.vo.DashboardResponse;
import com.eai.idss.vo.TileVo;


@RestController
@CrossOrigin(origins ={"http://localhost:4200", "http://10.10.10.32:8080"})
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
	    	User u = userRepository.findByUserName(userName);
	    	dr.setTopPerformers(gd.getTopPerformer(u.getRegion()));
	    	dr.setMyVisits(gd.getMyVisitsData(userName));
	    	dr.setDashboardMap(dashboardMap);
	    	dr.setIndustryCscoreDetails(gd.getIndustryScore(dbr));
//			dr.setIndustryScore(gd.getIndustryScore(dbr));
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
    	if(u!=null & (u.getDesignation().equalsIgnoreCase("MS")
    			|| u.getDesignation().equalsIgnoreCase("HOD"))
    			|| u.getDesignation().equalsIgnoreCase("ADMIN")) {
    		dd.put("regionList", IDSSUtil.getRegionList());
    		dd.put("subRegionList", IDSSUtil.getSubRegion(u.getRegion().toUpperCase()));
    	}
    	else {
    		dd.put("regionList", Arrays.asList(u.getRegion()));
    		dd.put("subRegionList", IDSSUtil.getSubRegion(u.getRegion()));
    	}
    	dd.put("categoryList", IDSSUtil.getCategoryList());
    	dd.put("scaleList", IDSSUtil.getScaleList());
    	dd.put("typeList", gd.getIndustryTypes(null));
    	dd.put("complianceScoreList", IDSSUtil.getComplianceScoreList());
    	dd.put("legalActionsList", IDSSUtil.getLegalActionsDropdownList());
    	dd.put("pendingCasesList", IDSSUtil.getPendingCasesList());
	    return new ResponseEntity<Map<String,List<String>>>(dd,HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/sub-regions/{region}", produces = "application/json")
	public ResponseEntity<List<String>> getSubRegions(@PathVariable("region") String region) throws IOException {
	    return new ResponseEntity<List<String>>(IDSSUtil.getSubRegion(region),HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/industry-types/{category}", produces = "application/json")
	public ResponseEntity<List<String>> getIndustryType(@PathVariable("category") String category) throws IOException {
	    return new ResponseEntity<List<String>>(gd.getIndustryTypes(category),HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/heat-map-dashboard", produces = "application/json")
	public ResponseEntity<List<HeatmapResponseVo>> getHeadMapDetailByIndustryIds(@RequestParam List<Integer> industryIds) throws IOException {
		List<HeatmapResponseVo> heatmapResponseVo = null;
		try {
			heatmapResponseVo = gd.getHeatmapDataByIndustryIds(industryIds);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /heat-dashboard", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<HeatmapResponseVo>>(heatmapResponseVo,HttpStatus.OK);
	}


}
