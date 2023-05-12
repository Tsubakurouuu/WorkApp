package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//セキュリティの対象外を設定する処理
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			//セキュリティを適用しない
			.ignoring()
				//"/webjars/**"にはセキュリティを適用しない
				.antMatchers("/webjars/**")
				//"/css/**"にはセキュリティを適用しない
				.antMatchers("/css/**")
				//"/js/**"にはセキュリティを適用しない
				.antMatchers("/js/**")
				//"/h2-console/**"にはセキュリティを適用しない
				.antMatchers("/h2-console/**");
	}
	
	//セキュリティの各種設定をする処理
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//ログイン不要ページの設定
		http
			.authorizeRequests()
				//トップページにはログイン不要でアクセスできる
				.antMatchers("/").permitAll()
				//ログインページにはログイン不要でアクセスできる
				.antMatchers("/login").permitAll()
				//新規登録画面にはログイン不要でアクセスできる
				.antMatchers("/signup").permitAll()
				//それ以外はログインしないとアクセスできない
				.anyRequest().authenticated();
		//ログイン処理
		http
			.formLogin()
				//ログイン処理のパス
				.loginProcessingUrl("/login")
				//ログインページの指定
				.loginPage("/login")
				//ログイン失敗時の遷移先
				.failureUrl("/login?error")
				//ログインページのユーザーID
				.usernameParameter("userId")
				//ログインページのパスワード
				.passwordParameter("password")
				//ログイン成功時の遷移先
				.defaultSuccessUrl("/my_page", true);
		//ログアウト処理
		http
			.logout()
				//ログアウトのリクエスト先パス
				.logoutUrl("/logout")
				//ログアウト成功時の遷移先
				.logoutSuccessUrl("/");
//		//CSRF対策を無効に設定
//		http.csrf().disable();
	}
	
	//認証設定の処理
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = passwordEncoder();
		//インメモリ認証
		auth
			.inMemoryAuthentication()
				//userを追加
				.withUser("user")
				.password(encoder.encode("user"))
				.roles("GENERAL")
			.and()
				//adminを追加
				.withUser("admin")
				.password(encoder.encode("admin"))
				.roles("ADMIN");
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder);
	}
}
