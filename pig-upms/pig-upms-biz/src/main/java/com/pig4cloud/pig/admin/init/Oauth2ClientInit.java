package com.pig4cloud.pig.admin.init;

import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Oauth2ClientInit implements CommandLineRunner {


	private SysOauthClientDetailsService sysOauthClientDetailsService;

	@Override
	public void run(String... args) throws Exception {
		sysOauthClientDetailsService.initClientCache();
	}

	@Autowired
	public void setSysOauthClientDetailsService(SysOauthClientDetailsService sysOauthClientDetailsService) {
		this.sysOauthClientDetailsService = sysOauthClientDetailsService;
	}
}
