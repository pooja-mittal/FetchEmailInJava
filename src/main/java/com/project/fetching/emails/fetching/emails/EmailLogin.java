package com.project.fetching.emails.fetching.emails;

import com.project.fetching.emails.fetching.emails.api.EmailLoginApi;

public class EmailLogin {
	
	public static void main(String[] args) {
		EmailLoginApi emailLogin=new EmailLoginApi();
		String host = "smtp.gmail.com";
		String type = "imaps";
		String user = "kakumittal78@gmail.com";
		String pwd = "pooja2016";
		emailLogin.recieveMail(host, type, user, pwd);
		System.out.println("process success !");
	}

}






