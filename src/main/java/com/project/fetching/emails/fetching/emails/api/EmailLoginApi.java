package com.project.fetching.emails.fetching.emails.api;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.project.fetching.emails.fetching.emails.dao.EmailLoginDao;

public class EmailLoginApi {
	

	public void recieveMail(String POP3Host, String type, String userName, String pwd) {
		try {
			 EmailLoginDao userLogin = new EmailLoginDao();
			 String result = "";
			 String strDate = "";
			 String sub = "";
			 String sender = "";
			 String to = "";
			 String cc = "";
			 String message = "";
			 String signature = "";
			 
			Properties p = new Properties();
			p.put("mail.smtp.host", "smtp.gmail.com");
			p.put("mail.smtp.socketFactory.port", "465");
			p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			p.put("mail.smtp.auth", "true");
			p.put("mail.smtp.port", "465");

			Session emailSession = Session.getDefaultInstance(p);
			Properties p1 = emailSession.getProperties();
			Store s = emailSession.getStore(type);
			s.connect(POP3Host, userName, pwd);

			Folder f = s.getFolder("inbox");
			f.open(Folder.READ_ONLY);

			Message[] m = f.getMessages();

			for (int i = 0; i < m.length; i++) {
				Message m1 = m[i];

				sub = m1.getSubject();
				sender = m1.getFrom()[0].toString();
				to = InternetAddress.toString(m1.getRecipients(Message.RecipientType.TO));
				cc = InternetAddress.toString(m1.getRecipients(Message.RecipientType.CC));

				Date d = m1.getSentDate();
				DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				strDate = dateFormat.format(d);

				String x = m1.getContentType();

				if (m1.isMimeType("multipart/*")) {
					MimeMultipart mime = (MimeMultipart) m1.getContent();
					result = getTextFromMultiplier(mime);
					Document doc = Jsoup.parse(result);
					// Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
					signature = doc.getElementsByClass("gmail_signature").remove().text();
					message = doc.getElementsByTag("body").text();
				} else {
					Object cont = m1.getContent();
					result = cont.toString();// System.out.println("MESSAGE: " + result);
				}
				userLogin.insert(sub, sender, to, strDate, message, signature, cc);
			}
			f.close(false);
			s.close();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getTextFromMultiplier(MimeMultipart m) throws Exception {
		String result = "";
		int count = m.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart b = m.getBodyPart(i);
			if (b.isMimeType("text/plain")) {
				result += b.getContent();
			} else if (b.isMimeType("text/html")) {
				String html = (String) b.getContent();
				result = html;
			} else if (b.getContent() instanceof MimeMultipart) {
				result += getTextFromMultiplier((MimeMultipart) b.getContent());
			}

		}
		return result;

	}

}
