package com.stockanalytics.accounting.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import com.stockanalytics.accounting.dao.UserAccountRepository;
import com.stockanalytics.accounting.model.UserAccount;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	final UserAccountRepository userAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		System.out.println("in UserDetails+++");
		UserAccount userAccount = userAccountRepository.findById(userName)
				.orElseThrow(() -> new UsernameNotFoundException(userName));

		// Retrieve the role of the userAccount
		String role = userAccount.getRole();
		// Create a collection of GrantedAuthority based on the role
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role)); // Prefix the role with "ROLE_"
UserDetails user= new org.springframework.security.core.userdetails.User(
		userName,
		userAccount.getPassword(), // You should use a proper password encoder here
		authorities
);
		System.out.println(" parameter user in UserDetails=principal  is-> "+user.getUsername()+"role ->"+user.getAuthorities()+"Pass ->"+user.getPassword());
		// Create a UserDetails object with the username, password, and role
        return user;
	}
}
