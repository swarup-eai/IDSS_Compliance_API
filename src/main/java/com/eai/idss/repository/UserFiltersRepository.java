package com.eai.idss.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.eai.idss.model.User_Filters;

public interface UserFiltersRepository extends MongoRepository<User_Filters, String> {

	User_Filters findByUserNameAndFilterName(String userName, String filterName);
	
	List<User_Filters> findByUserName(String userName);
}
