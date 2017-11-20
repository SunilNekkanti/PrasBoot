package com.pfchoice.springboot.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

import com.pfchoice.springboot.model.Email;

public interface EmailService {

	void sendMail(Email eParams) throws MessagingException, InterruptedException;

	void sendMailWithAttachment(Email eParams) throws MessagingException, IOException, InterruptedException;

	String geContentFromTemplate(Map<String, Object> model, String emailTemplateFile);

	String geContentFromTemplate(Object model, String emailTemplateFile);

}