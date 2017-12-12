package com.pfchoice.springboot.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.pfchoice.springboot.security.CustomAuthenticationSuccessHandler;
import com.pfchoice.springboot.security.CustomLogoutHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment env;

	@Autowired
	UserDetailsService authenticationService;

	// @Autowired
	// private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Bean
	@Primary
	@ConfigurationProperties(prefix = "email")
	public MailProperties mailProperties() {
		return new MailProperties();
	}

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		// Using gmail.
		mailSender.setHost(mailProperties().getHost());
		mailSender.setPort(mailProperties().getPort());
		mailSender.setUsername(mailProperties().getUsername());
		mailSender.setPassword(mailProperties().getPassword());

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable",
				mailProperties().getProperties().get("mail.smtp.starttls.enable"));
		javaMailProperties.put("mail.smtp.auth", mailProperties().getProperties().get("mail.smtp.auth"));
		javaMailProperties.put("mail.transport.protocol",
				mailProperties().getProperties().get("mail.transport.protocol"));
		javaMailProperties.put("mail.debug", mailProperties().getProperties().get("mail.debug"));

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/resources/**", "/");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers()
				.hasAnyAuthority("ROLE_AGENT", "ROLE_ADMIN", "ROLE_CARE_COORDINATOR", "ROLE_EVENT_COORDINATOR",
						"ROLE_MANAGER")
				.anyRequest().authenticated().and().formLogin().loginPage("/").usernameParameter("username")
				.passwordParameter("password").loginProcessingUrl("/loginform.do").failureUrl("/login?error")
				.successHandler(customAuthenticationSuccessHandler()).and().logout()
				.addLogoutHandler(customLogoutHandler()).logoutRequestMatcher(new AntPathRequestMatcher("/login")).and()
				.exceptionHandling().accessDeniedPage("/403").and()

				.csrf().disable()

				.sessionManagement().maximumSessions(1);
	}

	/**
	 * use below two lines when encoder is enabled in password
	 * ShaPasswordEncoder encoder = new ShaPasswordEncoder();
	 * auth.userDetailsService(authenticationService).passwordEncoder(encoder);
	 * 
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		auth.userDetailsService(authenticationService).passwordEncoder(encoder);
		auth.userDetailsService(authenticationService);
	}

	@Bean
	public CustomLogoutHandler customLogoutHandler() {
		return new CustomLogoutHandler();
	}

	@Bean
	public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}
}
