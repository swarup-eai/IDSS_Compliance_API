package com.eai.idss.repository;

import com.eai.idss.model.CetpDataMaster;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CetpMasterDataRepository extends MongoRepository<CetpDataMaster, String>{
}
