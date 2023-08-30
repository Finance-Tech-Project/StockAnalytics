package telran.fintech.accounting.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.fintech.accounting.model.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

}
