package com.ironplayer.restapi.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class SendEmailTLS {

	public SendEmailTLS() {

	}

	public void send(String login,String password) {

		Properties props = System.getProperties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		// //Get session
		Session session = Session.getInstance(props, null);
		ShMimeMessage message = new ShMimeMessage(session);
		message.setEnvelopeFrom("ironplayermusic@gmail.com");
		InternetAddress address = new InternetAddress();
		address.setAddress("ironplayermusic@gmail.com");

		try {
			message.setSender(address);
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			System.out.println("DEBUG00068 - Cabecera sender no se pudo establecer");
		}

		InternetAddress[] addressTo = new InternetAddress[1];

		try {
			addressTo[0] = new InternetAddress(login);
		} catch (AddressException e2) {
			e2.printStackTrace();
		}

		try {
			message.setRecipients(Message.RecipientType.TO, addressTo);
			message.setFrom(new InternetAddress("ironplayermusic@gmail.com", "ironplayermusic@gmail.com",
					"ISO-8859-1"));
		} catch (MessagingException e1) {

			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			message.setSentDate(new Date());
			(message).setSubject("IronPlayer Pass recover");
			(message).setText(password);
			Transport transport = session.getTransport("smtp");
			try {

				transport.connect("smtp.googlemail.com", 587, "ironplayermusic@gmail.com", "jonanjoseAupaBoas");

				transport.sendMessage(message, message.getAllRecipients());

			} catch (AuthenticationFailedException e) {
				System.out.println("ERROR00061 - Error de autentificacion conectando con smtp server");
				throw e;
			} catch (MessagingException e) {
				System.out.println("ERROR00063 - Error desconocido conectando con server SMTP" + e);
				throw e;
			} finally {
				if (transport != null) {
					System.out.println("Cerramos Transport");
					transport.close();
				}
			}

		} catch (Exception e) {
			System.out.println("General excepction sending message by SendVitriaDumpErrorEmail" + e);
		}

	}

}
