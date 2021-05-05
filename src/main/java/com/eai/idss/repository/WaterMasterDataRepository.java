package com.eai.idss.repository;

import com.eai.idss.model.WaterMasterData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eai.idss.model.WaterMasterData;


public interface WaterMasterDataRepository extends MongoRepository<WaterMasterData, String> {
}
