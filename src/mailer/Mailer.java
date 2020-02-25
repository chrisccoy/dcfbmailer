package mailer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class Mailer {

	private static class MailerOptions {

		@Option(name = "-u", aliases = "--user", usage = "SMTP UserName", required = true)
		private String userName;
		@Option(name = "-p", aliases = "--passwd", usage = "SMTP User's Password", required = true)
		private String passWord;
		@Option(name = "-b", aliases = "--body", usage = "File to be used as the email body", required = true)
		private String body;
		@Option(name = "-i", aliases = "--input", usage = "CSV Input file for email distribution", required = true)
		private String inputFile;
		@Option(name = "-e", aliases = "--encoding", usage = "HTML Encoding to use", required = false)
		private String encoding="ISO-8859-1";
		@Option(name = "-a", aliases = "--attachment", usage = "Attachment to Include", required = false)
		private String attachmentFile="2020_MM_Flyer_promo.pdf";
		@Option(name = "-d", aliases = "--description", usage = "Description for Attached", required = false)
		private String attachmentDesc="2020 March Madness Promo";
		@Option(name = "-s", aliases = "--subject", usage = "Subject of email", required = false)
		private String subject="MTA 2020 March Madness Basketball Raffle";
        @Option(name = "-t", aliases = "--test", usage = "Override email with Test Email", required = false)
        private String testEmail;

	}

	private static MailerOptions opts;

	public static void main(String []args)throws Exception
	{

		opts=new MailerOptions();
		final CmdLineParser parser = new CmdLineParser(opts);
		try
		{
			parser.parseArgument(args);
		}
		catch (CmdLineException clEx)
		{
			System.out.println("ERROR: Unable to parse command-line options: " + clEx);
		}
		List<List<String>> theList = Mailer.tokenizeFile(opts.inputFile);
		for (List<String> s: theList) {
			String email = prepareEmail(s);
			sendEmail(email, s.get(1));
		}
	}


	
	private static String theFile = null;
	

	public static String prepareEmail(List<String> s) throws IOException {
		
		theFile = FileUtils.readFileToString(new File(opts.body),opts.encoding);
		
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
		  attachment.setPath("./"+opts.attachmentFile);
		  attachment.setDisposition(EmailAttachment.ATTACHMENT);
		  attachment.setDescription(opts.attachmentDesc);
		  attachment.setName(opts.attachmentFile);

		  // Create the email message
		  HtmlEmail email = new HtmlEmail();
		  //MultiPartEmail email = new MultiPartEmail();
		  //email.set
		  email.setHostName("smtp.gmail.com");
		  email.setSmtpPort(587);
		  email.setAuthenticator(new DefaultAuthenticator(opts.userName, opts.passWord));
		  email.setStartTLSEnabled(true);
		  //Use the Test email if present
		  recpt=opts.testEmail==null?recpt:opts.testEmail;
		  email.addTo(recpt);
		  email.addBcc("chrisccoy@gmail.com", "Chris Coy");
		  email.addBcc("pattess@gmail.com", "Patrick");
		 
		  email.setFrom("mtadadsclubraffle@gmail.com", "Dads Raffle");
		  email.setSubject(opts.subject);
		  email.setHtmlMsg(theMail);
		  save(theMail);

		//  add the attachment
		  email.attach(attachment);

		  // send the email
		  email.send();		
	}

	private static void save(String theMail) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("wtf.html"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.write(theMail);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}