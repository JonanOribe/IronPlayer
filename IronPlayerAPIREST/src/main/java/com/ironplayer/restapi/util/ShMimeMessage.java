package com.ironplayer.restapi.util;

import java.io.Serializable;

import javax.mail.MessagingException;
import javax.mail.Session;

public class ShMimeMessage extends com.sun.mail.smtp.SMTPMessage implements Serializable {

	private static final long serialVersionUID = -865122359129893036L;

	/* *************** */
	/* ** Variables ** */
	/* *************** */

	private String messageIdDomain;

	/* ******************* */
	/* ** Constructores ** */
	/* ******************* */

	public ShMimeMessage(Session session) {
		super(session);
		messageIdDomain = "mydomain.com";
	}

	/* ********************* */
	/* ** Setters/Getters ** */
	/* ********************* */

	/**
	 * @param messageIdDomain
	 */
	public void setMessageIdDomain(String messageIdDomain) {
		this.messageIdDomain = messageIdDomain;
	}

	/**
	 * @return Returns the messageIdDomain.
	 */
	public String getMessageIdDomain() {
		return messageIdDomain;
	}

	/* ***************************** */
	/* ** M�todos Sobrecargados ** */
	/* ***************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.mail.internet.MimeMessage#updateHeaders()
	 */
	@Override
	protected void updateHeaders() throws MessagingException {
		super.updateHeaders();

		// Generamos un Message-ID
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append(stringbuffer.hashCode()).append('.').append(System.currentTimeMillis());
		setHeader("Message-ID", "<" + stringbuffer.toString() + "@" + messageIdDomain + ">");
	}
}
