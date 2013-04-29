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


public class EmailInfo {
	public String emailid=null;
	public String subject=null;
	public String mailerid=null;
	public String domain=null;
	public String domaindate=null;
	public AutoPopulatingList dns  = new AutoPopulatingList<DNSRecord>(DNSRecord.class);
	
	
	public String getEmailid(){
		return this.emailid;
		
	}
	public void setEmailid(String emailid){
		this.emailid=emailid;
		
	}
	public String getSubject(){
		return this.subject;
	}
	public void setSubject(String subject){
		this.subject=subject;
	}
	public String getMailerid(){
		return this.mailerid;
	}
	public void setMailerid(String mailerid){
		this.mailerid=mailerid;
	}
	public String getDomain(){
		return this.domain;
	}
	public void setDomain(String domain){
		this.domain=domain;
	}
	public String getDomaindate(){
		return this.domaindate;
	}
	public void setDomaindate(String domaindate){
		this.domaindate=domaindate;
	}
	public AutoPopulatingList<DNSRecord> getDns() {
		return this.dns;
	}

	public void setDns(AutoPopulatingList<DNSRecord> dns) {
		this.dns = dns;
	}

}
