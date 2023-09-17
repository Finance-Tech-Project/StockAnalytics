package com.stockanalytics.accounting.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.stockanalytics.accounting.dao.UserAccountRepository;
import com.stockanalytics.accounting.model.UserAccount;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	final UserAccountRepository userAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserAccount userAccount = userAccountRepository.findById(userName)
				.orElseThrow(() -> new UsernameNotFoundException(userName));

		// Retrieve the role of the userAccount
		String role = userAccount.getRole();

		// Create a UserDetails object with the username, password, and role

		UserDetails userDetails = org.springframework.security.core.userdetails.User
				.withUsername(userName)
				.password(userAccount.getPassword()) // You should use a proper password encoder here
				.roles(role)
				.build();

		return userDetails;
	}

}
