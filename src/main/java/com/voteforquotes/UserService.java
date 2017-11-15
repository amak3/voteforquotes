package com.voteforquotes;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.voteforquotes.repository.FormRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder; 
	@Autowired
	private FormRepository form;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		String hash = form.obtainPassword(username);
		System.err.println("loadUserByUsername " + hash);
		if (hash != null) {
			String userRole= form.obtainUserRole(username);
			Collection<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(userRole));
			System.err.println("new user: " + username + hash + authorities); 
			return new User(username, hash, authorities);
		} else {
			throw new UsernameNotFoundException("no user " + username);
		}
	}
	
	public void createUser(String username, String password, String userrole) {
		String hash = encoder.encode(password);
		System.err.println("creating user " + username + " " + password + " " + hash );
		form.createUser(username, hash, userrole);
	}

}
