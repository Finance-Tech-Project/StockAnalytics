package com.stockanalytics.accounting.dao;

import com.stockanalytics.accounting.model.TemporaryPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TemporaryPasswordRepository extends JpaRepository<TemporaryPassword,String> {
    Optional<TemporaryPassword> findByLogin(String login);
    void deleteByLogin(String login);
}
