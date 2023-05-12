package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.MUser;
import com.example.service.MUserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private MUser loginUser;
	
	@Autowired
	private MUserService service;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		//ユーザー情報取得
		loginUser = service.getLoginUser(userId);
		//ユーザーが存在しない場合
		if(loginUser == null) {
			throw new UsernameNotFoundException("user not found");
		}
		//権限List作成
		GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(authority);
		//UserDetails生成
		UserDetails userDetails = (UserDetails) new User(loginUser.getUserId(), loginUser.getPassword(), authorities);
		return userDetails;
	}
	
	//ログインユーザー情報取得
	public MUser getLoginUser() {
		return loginUser;
	}
	
}
