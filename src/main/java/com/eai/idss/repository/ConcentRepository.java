package com.eai.idss.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.eai.idss.model.Consent;

@Repository
public interface ConcentRepository extends MongoRepository<Consent, String> {


}
