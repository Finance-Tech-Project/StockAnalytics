package com.stockanalytics.accounting.dao;

//import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stockanalytics.accounting.model.UserAccount;

@Repository
public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

}
