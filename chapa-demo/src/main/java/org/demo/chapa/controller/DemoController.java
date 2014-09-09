package org.demo.chapa.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

	@RequestMapping(value = "login")
	public String doGetLogin() {
		return "login";
	}

	@RequestMapping(value = "hello")
	public String doGetHello(Principal principal, Model m) {
		m.addAttribute("loginUser", principal.getName());
		return "hello";
	}

	@RequestMapping(value = "admin")
	public String doGetAdmin(Principal principal, Model m) {
		m.addAttribute("loginUser", principal.getName());
		return "admin";
	}

	@RequestMapping(value = "user")
	public String doGetUser(Principal principal, Model m) {
		m.addAttribute("loginUser", principal.getName());
		return "user";
	}

	@RequestMapping(value = "guest")
	public String doGetGuest(Principal principal, Model m) {
		m.addAttribute("loginUser", principal.getName());
		return "guest";
	}

	@RequestMapping(value = "logout")
	public String doGetLogout(HttpSession ses) {
		ses.invalidate();
		return "redirect:login?logout";
	}

	@RequestMapping("/403")
	public String forbidden() {
		return "error/403";
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return new MyCustomizer();
	}

	private static class MyCustomizer implements EmbeddedServletContainerCustomizer {
		@Override
		public void customize(ConfigurableEmbeddedServletContainer factory) {
			factory.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403"));
		}

	}
}
