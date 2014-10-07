package org.demo.chapa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@ComponentScan
@EnableWebMvcSecurity
@EnableAutoConfiguration
public class Application extends WebSecurityConfigurerAdapter {

	/**
	 * WEBアプリ起動
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/*
	 * ページに対しての認証周りの設定
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/admin", "/metrics", "/health", "/dump", "/configprops", "/env").hasRole("ADMIN")
					.and().authorizeRequests()
				.antMatchers("/user").hasAnyRole("ADMIN", "USER")
					.and().authorizeRequests()
				.anyRequest().authenticated();

		http.formLogin().loginProcessingUrl("/login").loginPage("/login")
				.failureUrl("/login?error")
				.defaultSuccessUrl("/hello")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll();

		http.logout().logoutUrl("/logout").permitAll();
	}

	/*
	 * 認証無視する設定
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/api/**");
	}

	@Configuration
	protected static class AuthenticationConfiguration extends
			GlobalAuthenticationConfigurerAdapter {
		/*
		 * ログインユーザーの権限設定
		 */
		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.inMemoryAuthentication()
				.withUser("user").password("user")
				.roles("USER");

			auth.inMemoryAuthentication()
				.withUser("guest").password("guest")
				.roles("GUEST");

			auth.inMemoryAuthentication()
				.withUser("admin").password("admin")
				.roles("ADMIN");
		}
	}
}
