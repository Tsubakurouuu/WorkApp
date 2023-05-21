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
	
	//ログイン成功時に呼び出される処理
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
		//GrantedAuthority オブジェクトの配列をセットに変換する
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		
		//権限ごとでログイン後の遷移先を変えるif文
        if (roles.contains("ADMIN")) {
        	response.sendRedirect(request.getContextPath() + "/admin/users");
        }else if(roles.contains("USER")){
        	response.sendRedirect(request.getContextPath() + "/work/input");
        }
	}

	
}