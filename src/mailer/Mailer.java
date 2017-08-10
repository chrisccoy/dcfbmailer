package mailer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;


public class Mailer {
	
	public static void main(String []args)throws Exception
	{
		//TODO Create args list for files, and env var support
		List<List<String>> theList = Mailer.tokenizeFile("test.csv");
		for (List<String> s: theList) {
			String email = prepareEmail(s);
			sendEmail(email, s.get(1));
		}
				
	}

	public static String prepareEmail(List<String> s) throws IOException {
		
		String theFile = FileUtils.readFileToString(new File("test.html"),"ISO-8859-1");
		System.out.println(s.get(0));
		//Lame, but pretty sure its unique
		theFile = StringUtils.replace(theFile, "ReplaceMeSalesmen", s.get(0) );
		theFile = StringUtils.replace(theFile, "ReplaceMeUser", s.get(3) );
		theFile = StringUtils.replace(theFile, "ReplaceMePwd", s.get(4) );
		theFile = StringUtils.replace(theFile, "ReplaceMeTickets", s.get(2) );
		return theFile;
	}

	public static void sendEmail(String theMail, String recpt) throws EmailException
	{
		  // Create the attachment
		  EmailAttachment attachment = new EmailAttachment();
		  attachment.setPath("./Raffle_Sample_Letter_2017.docx");
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  attachment.setDescription("MTA Pro Football Sample Letter");
		  attachment.setName("Raffle_Sample_Letter_2017.docx");

		  // Create the email message
		  HtmlEmail email = new HtmlEmail();
		  //MultiPartEmail email = new MultiPartEmail();
		  //email.set
		  email.setHostName("smtp.gmail.com");
		  email.setSmtpPort(587);
		  email.setAuthenticator(new DefaultAuthenticator("mtadadsclubraffle@gmail.com", "Madison15"));
		  email.setStartTLSEnabled(true);
		  // TODO add test mode with env var -- email.addTo("christophercoy@cox.net");
		  email.addTo(recpt);
		  email.addBcc("chrisccoy@gmail.com", "Chris Coy");
		  email.addBcc("pattess@gmail.com", "Patrick");
		 
		  email.setFrom("mtadadsclubraffle@gmail.com", "Dads Raffle");
		  email.setSubject("MTA 2017 Pro Football Raffle");
		  email.setMsg(theMail);

		//  add the attachment
		  email.attach(attachment);

		  // send the email
		  email.send();		
	}
	public static String prepareEmail(String template, List<String> toks)
	{
		String name = toks.get(0);
		String email = toks.get(1);
		String tickets = toks.get(2);
		String user = toks.get(3);
		String pass = toks.get(4);
		return null;
		
	}
	public static List<List<String>> tokenizeFile(String f) throws IOException {
		List<List<String>> listOfToks = new ArrayList <List<String>>();
		BufferedReader b = new BufferedReader(new FileReader(f));
		String line="";
        while ((line = b.readLine()) != null) {
        	StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
        	   List<String> sl   = new ArrayList<String>();
 		   while (stringTokenizer.hasMoreElements()) {
 			   sl.add(stringTokenizer.nextToken());
 		   }
 		   listOfToks.add(sl);
        }
		return listOfToks;
	}
	public static void test2() throws Exception {
		final String username = "mtadadsclubraffle@gmail.com";
		final String password = "Madison15";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("christophercoy@cox.net"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("chrisccoy@gmail.com"));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
}