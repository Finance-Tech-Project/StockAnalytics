package com.stockanalytics.accounting.dao;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stockanalytics.accounting.model.UserAccount;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    @Query("SELECT u FROM UserAccount u WHERE u.login = :login")
    Optional<UserAccount> findByLogin(@Param("login") String login);

    boolean existsById(@NonNull String userLogin);
}
