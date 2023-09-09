package com.stockanalytics.accounting.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stockanalytics.accounting.model.UserAccount;
@Primary
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

}
