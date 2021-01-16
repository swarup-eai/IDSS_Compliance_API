package com.eai.idss.repository;

import com.eai.idss.model.BenchmarkingDestinationData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenchmarkingDestinationDataRepository extends MongoRepository<BenchmarkingDestinationData, String> {
    List<BenchmarkingDestinationData> findByCategoryCodeAndIndustryType(Double categoryCode,String industryType);
}
