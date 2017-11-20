package com.pfchoice.springboot.service;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ApplicationMailer {
	private static final Logger LOGGER = Logger.getAnonymousLogger();

	private String emailId;

	private Properties emailProperties;

	private Session session;

	private String cc;

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * @param session
	 *            the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @return the cc
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc
	 *            the cc to set
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @param emailProperties
	 *            the emailProperties to set
	 */
	public void setEmailProperties(Properties emailProperties) {
		this.emailProperties = emailProperties;
	}

	/**
	 * @return the emailProperties
	 */
	public Properties getEmailProperties() {
		return emailProperties;
	}

	/**
	 * This method will send compose and send the message
	 */
	public void sendMail(String to, String subject, String body) {

		final Message message = new MimeMessage(session);
		try {
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setFrom(new InternetAddress(emailId));
			message.setSubject(subject);
			message.setText(body);
			message.setSentDate(new Date());
			Transport.send(message);
		} catch (AddressException e) {
			LOGGER.log(Level.INFO, "AddressException: " + e.getMessage(), e);
		} catch (MessagingException e) {
			LOGGER.log(Level.INFO, "MessagingException: " + e.getMessage(), e);
		}
	}

	/**
	 * This method will send compose and send the message
	 */
	public void sendMailWithAttachment(String to, String subject, String body, String filename) {

		final Message message = new MimeMessage(session);

		try {
			// Set From: header field of the header.
			message.setFrom(new InternetAddress("skumar@infocus.com"));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText(body);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}