/*
 * Copyright (c) 2013, EMC Corporation ("EMC"). 
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 * 
 */

package com.emc.cto.ridagent.rid.webform;

import org.springframework.util.AutoPopulatingList;


public class PhishingData {
	public String role=null;
	public String category=null;
	public String systemcategory=null;
	public AutoPopulatingList system  = new AutoPopulatingList<SystemData>(SystemData.class);
	public AutoPopulatingList emailinfo  = new AutoPopulatingList<EmailInfo>(EmailInfo.class);


	
	public PhishingData(){
		
	}
	
	public String getCategory(){
		return this.category;		
	}
	public void setCategory(String category){
		this.category=category;
	}


	public void setRole(String role){
		this.role=role;		
	}
	public String getRole(){
		return this.role;
	}
	public void setSystemcategory(String systemcategory){
		this.systemcategory=systemcategory;		
	}
	public String getSystemcategory(){
		return this.systemcategory;
	}
	public AutoPopulatingList<SystemData> getSystem() {
		return this.system;
	}

	public void setSystem(AutoPopulatingList<SystemData> system) {
		this.system = system;
	}
	public AutoPopulatingList<EmailInfo> getEmailinfo() {
		return this.emailinfo;
	}

	public void setEmailinfo(AutoPopulatingList<EmailInfo> emailinfo) {
		this.emailinfo = emailinfo;
	}


	


}
