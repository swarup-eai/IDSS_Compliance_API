package com.eai.idss.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eai.idss.model.OCEMS_Alerts;

@Repository
public interface OCEMSAlertsRepositoy extends MongoRepository<OCEMS_Alerts, String> {


}
