package com.eai.idss.repository;

import com.eai.idss.model.BenchmarkingSourceData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenchmarkingSourceDataRepository extends MongoRepository<BenchmarkingSourceData, String> {
    List<BenchmarkingSourceData> findByCategoryCode(Double categoryCode);
}

