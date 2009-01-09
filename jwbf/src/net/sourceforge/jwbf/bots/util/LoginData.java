/*
 * Copyright 2007 Thomas Stock.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors:
 * 
 */
package net.sourceforge.jwbf.bots.util;

/**
 * TODO API related, use only if posting data works.
 * http://www.mediawiki.org/wiki/API#Posting_Data_.2F_needs_major_editPage.php_rewrite
 * 
 * @author Thomas Stock
 *
 */
public class LoginData {
	
	private int userid;
	private String botName;
	private String loginToken;
	
	private boolean isLoggedIn;
	
	public LoginData() {
		this.userid = -1;
		this.botName = "";
		this.loginToken = "";
		this.isLoggedIn = false;
	}
	
	public void setup(int userid, String botName, String loginToken, boolean isLoggedIn) {
		this.userid = userid;
		this.botName = botName;
		this.loginToken = loginToken;
		this.isLoggedIn = isLoggedIn;
	}
	
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	
	public String get() {
		return "& lgtoken=123ABC & lgusername="+ botName + " & lguserid=23456";
	}

}
