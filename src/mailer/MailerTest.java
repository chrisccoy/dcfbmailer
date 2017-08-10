package mailer;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.Test;


public class MailerTest extends Mailer {

	@Test
	public void test() throws IOException {
	}
	
	@Test
	public void testTokenizeFile() throws Exception{
		List<List<String>> myList= Mailer.tokenizeFile("test.csv");
		for (List<String> sl: myList)
		{
			for (String s: sl)
			{
				System.out.println("Value = "+s);
			}
		}
	}
	
	@Test
	public void testSendMail() throws Exception{
		//Mailer.sendEmail(Mailer.prepareEmail(null));
	}
	

}
