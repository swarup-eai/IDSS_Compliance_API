package com.eai.idss.repository;

import com.eai.idss.model.CScoreMaster;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CscoreMasterRepository extends MongoRepository<CScoreMaster,String> {
    CScoreMaster findTop1ByIndustryIdOrderByCalculatedDateDesc(long industryId);
}
