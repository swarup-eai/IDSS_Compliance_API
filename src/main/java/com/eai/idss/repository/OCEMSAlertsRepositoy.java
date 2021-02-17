package com.eai.idss.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eai.idss.model.OCEMS_Alerts;

@Repository
public interface OCEMSAlertsRepositoy extends MongoRepository<OCEMS_Alerts, String> {

	public List<OCEMS_Alerts> findBySroUserAndIsDisabled(String sroUserId,boolean isDisabled);

}
