/*******************************************************************************
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasado@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurcia@elpuig.xeill.net 
 * 
 *  Clase que permite enviar un correo 
 *  
 *******************************************************************************/
package com.example.SendMail;

import java.io.File;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;



public class sendMail {

	private MimeBodyPart messageBodyPart;
	private MimeMultipart multipart;
	private DataSource source;
	private String receiverEmailID = null;
	private String emailSubject = null;
	private String filename = null;
	private ResourceBundle rb = ResourceBundle.getBundle("GA");
	private String senderEmailID = rb.getString("sender_mail");
	private String senderPassword = rb.getString("sender_pass");
	private String emailSMTPserver = rb.getString("sender_smtp");
	private String emailServerPort = rb.getString("sender_port");

	/**
	 * Este método es el encargado de enviar un mail a partir de los parametros solicitados
	 * 
	 * @param receiverEmailID
	 *     		Parámetro que contiene el correo de la persona que va a recibirlo.
	 *            
	 * @param emailSubject 
	 * 			Parámetro que contiene el asunto del correo
	 * 
	 * @param filename 
	 * 			Parámetro que contiene el archivo adjunto que se va a enviar.
	 * 
	 */
	public sendMail(String receiverEmailID, String emailSubject, String filename) {
		this.receiverEmailID = receiverEmailID;
		this.emailSubject = emailSubject;
		this.filename = filename;

		messageBodyPart = new MimeBodyPart();
		multipart = new MimeMultipart();

		try {
			source = new FileDataSource(filename);

			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);

			multipart.addBodyPart(messageBodyPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Properties props = new Properties();
		props.put("mail.smtp.user", senderEmailID);
		props.put("mail.smtp.host", emailSMTPserver);
		props.put("mail.smtp.port", emailServerPort);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable","true");
		// props.put("mail.smtp.debug", "true");
		props.put("mail.smtp.socketFactory.port", emailServerPort);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//		props.put("mail.smtp.socketFactory.fallback", "false");

		SecurityManager security = System.getSecurityManager();

		try {
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props, auth);
		    //session.setDebug(true); // Verbose!
			MimeMessage msg = new MimeMessage(session);
			msg.setSubject(emailSubject);
			msg.setFrom(new InternetAddress(senderEmailID));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmailID));
			msg.setContent(multipart);
			Transport.send(msg);
		} catch (Exception mex) {
			mex.printStackTrace();
		}

	}

	/**
	 * Este método se encarga de autentificar la conexión SMTP
	 * 
	 * @param grup
	 *            Éste es el objeto Group que se quiere eliminar de la base de
	 *            datos
	 */
	public class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(senderEmailID, senderPassword);
		}
	}

}