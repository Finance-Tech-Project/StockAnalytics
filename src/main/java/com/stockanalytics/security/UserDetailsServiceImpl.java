package com.stockanalytics.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
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
		System.out.println("in userdetails 401 not found "+userName);
		UserAccount userAccount = userAccountRepository.findById(userName)
				.orElseThrow(() -> new UsernameNotFoundException(userName));
		System.out.println("userAccount from repo...  "+userAccount.getRoles());		

	String[] roles = userAccount.getRoles()	
			.stream()
			.map(r -> "ROLE_" + r)	
			.toArray(String[]::new);
		
	return new User(userName, userAccount.getPassword(), AuthorityUtils.createAuthorityList(roles));
	}

}
