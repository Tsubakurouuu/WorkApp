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
	
	@Autowired
	private AuthenticationSuccessHandler AuthenticationSuccessHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//★セキュリティの対象外を設定するメソッド
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
				.antMatchers("/js/**");
	}
	
	//★セキュリティの各種設定をするメソッド
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			//ログイン不要ページの設定
			.authorizeRequests()
				//ログインページにはログイン不要でアクセスできる
				.antMatchers("/login").permitAll()
				//"/admin/**には"ADMIN"ロールを持つユーザーしか遷移できない
				.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
				//それ以外はログインしないとアクセスできない
				.anyRequest().authenticated();
		http
			//ログイン処理の設定
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
				.successHandler(AuthenticationSuccessHandler);
		http
			//ログアウト処理の設定
			.logout()
				//ログアウトのリクエスト先パス
				.logoutUrl("/logout")
				//ログアウト成功時の遷移先
				.logoutSuccessUrl("/login");
	}
	
	//★認証マネージャーを構成するメソッド
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//パスワードエンコーダーの生成
		PasswordEncoder encoder = passwordEncoder();
		//ユーザーデータで認証
		auth
			//認証マネージャーに対してユーザーの詳細を提供するUserDetailsServiceオブジェクトを設定する（認証時にユーザーの情報を取得する）
			.userDetailsService(userDetailsService)
			//認証マネージャーに対してパスワードエンコーダーを設定する
			.passwordEncoder(encoder);
	}
	
}
