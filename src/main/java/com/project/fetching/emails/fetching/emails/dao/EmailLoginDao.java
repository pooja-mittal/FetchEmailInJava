package com.project.fetching.emails.fetching.emails.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class EmailLoginDao {
	static Connection con = null;
	
	public void insert(String Subject, String sender, String to, String strDate, String message, String signature, String cc) {
	try {
	Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
	con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fetchingemails", "root", "root");
	String sql = "";
	 sql="insert into EmailDetail(Subject, Sender, Receiver, SentTime, Message, Signature, ReceiverCC ) values (?, ?, ?, ?, ?, ?, ?)";
     PreparedStatement ps = con.prepareStatement(sql);
     ps.setString(1,  Subject);
     ps.setString(2, sender );
     ps.setString(3, to );
     ps.setString(4,  strDate);
     ps.setString(5, message);
     ps.setString(6, signature);
     ps.setString(7,  cc);
     ps.executeUpdate();
	}  catch (ClassNotFoundException | SQLException e) {
		e.printStackTrace();
	} catch (InstantiationException e) {
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		e.printStackTrace();
	} finally {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

	
	

}
