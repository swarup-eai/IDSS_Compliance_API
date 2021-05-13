package com.eai.idss.repository;

import com.eai.idss.model.WaterParamStdMasterData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WaterParamStdMasterRepository extends MongoRepository<WaterParamStdMasterData, String> {
}
