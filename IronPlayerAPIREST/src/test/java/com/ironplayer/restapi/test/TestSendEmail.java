package com.ironplayer.restapi.test;

import org.junit.Test;

import com.ironplayer.restapi.util.SendEmailTLS;

public class TestSendEmail {

	@Test
	public void test() {
		try {
			SendEmailTLS sendEmailTLS = new SendEmailTLS();
			sendEmailTLS.send("jyanezp@mundo-r.com","hello");	
		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
		}
		
	}

}
