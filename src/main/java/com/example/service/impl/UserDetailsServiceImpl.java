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
	
	//★指定されたユーザーIDを使用して、ユーザーの詳細をロードするメソッド
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		//ユーザー情報取得
		loginUser = service.selectUserDetail(userId);
		//ユーザーが存在しない場合
		if(loginUser == null) {
			throw new UsernameNotFoundException("user not found");
		}
		//loginUserオブジェクトからユーザーのロールを取得し、GrantedAuthorityオブジェクトを作成
		GrantedAuthority authority = new SimpleGrantedAuthority(loginUser.getRole());
		//ロールを格納するための空のGrantedAuthorityリストを作成
		List<GrantedAuthority> authorities = new ArrayList<>();
		//ロールを追加
		authorities.add(authority);
		//Userクラスを使用して、ユーザーのユーザー名、パスワード、ロールからUserDetailsオブジェクトを生成
		UserDetails userDetails = (UserDetails) new User(loginUser.getUserId(), loginUser.getPassword(), authorities);
		//生成されたUserDetailsオブジェクトを返す
		return userDetails;
	}
	
	//ログインユーザー情報取得
	public MUser selectLoginUser() {
		return loginUser;
	}
	
}
