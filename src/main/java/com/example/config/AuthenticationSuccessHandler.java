package com.example.config;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
	
	//★ログイン成功時の処理を行うメソッド
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
		//authenticationオブジェクトからユーザーのロールを取得し、GrantedAuthorityオブジェクトの配列をSetに変換
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		
		//ユーザーのロールに応じて、ログイン後のリダイレクト先を決定するための条件分岐
		//管理者ロールの場合
        if (roles.contains("ADMIN")) {
        	//管理者専用ページへリダイレクト
        	response.sendRedirect(request.getContextPath() + "/admin/users");
    	//ユーザーロールの場合
        } else if(roles.contains("USER")){
        	//ユーザー専用ページへリダイレクト
        	response.sendRedirect(request.getContextPath() + "/work/input");
        }
	}

	
}