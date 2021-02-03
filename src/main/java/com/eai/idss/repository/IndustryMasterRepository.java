package com.eai.idss.repository;

import com.eai.idss.model.IndustryMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryMasterRepository extends MongoRepository<IndustryMaster, String> {
    IndustryMaster findByIndustryId(long industryId);
}
