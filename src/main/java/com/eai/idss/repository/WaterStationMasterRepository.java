package com.eai.idss.repository;



import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eai.idss.model.WaterStationMaster;

import java.util.List;

@Repository
public interface WaterStationMasterRepository extends MongoRepository<WaterStationMaster, String> {



}
