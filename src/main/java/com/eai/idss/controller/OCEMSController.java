package com.eai.idss.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eai.idss.model.OCEMS_Alerts;
import com.eai.idss.repository.OCEMSAlertsRepositoy;
import com.eai.idss.service.OCEMSAlertsScheduler;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class OCEMSController {

	@Autowired
	private OCEMSAlertsScheduler os;
	
	@Autowired
	private OCEMSAlertsRepositoy or;
	

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method = RequestMethod.POST, value = "/ocems-3-hours-alerts", produces = "application/json")
	public ResponseEntity<String> generateOCEMS3HoursAlerts() throws IOException {
	    try {

	    	os.generateOCEMS3HoursAlerts();
	    } catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity("Exception in /ocems-3-hours-alerts", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return new ResponseEntity<String>("Alerts",HttpStatus.OK);
	}
    
   
    @SuppressWarnings({ "rawtypes", "unchecked" })
   	@RequestMapping(method = RequestMethod.GET, value = "/ocems-alerts", produces = "application/json")
    public ResponseEntity<List<OCEMS_Alerts>> getAlerts(@RequestHeader String userName) throws IOException {
    	List<OCEMS_Alerts> oal =  new ArrayList<OCEMS_Alerts>();
   	    try {
   	    	oal = or.findBySroUser(userName);
   		} catch (Exception e) {
   			e.printStackTrace();
   			return new ResponseEntity("Exception in /ocems-alerts", HttpStatus.INTERNAL_SERVER_ERROR);
   		}
   	    return new ResponseEntity<List<OCEMS_Alerts>>(oal,HttpStatus.OK);
   	}
}
