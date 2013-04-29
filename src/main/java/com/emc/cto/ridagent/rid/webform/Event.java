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


public class Event {

	public String refName=null;
	public String refURL=null;
	public String httpcomments=null;
	public String confidence=null;
	public String expectation=null;
	public AutoPopulatingList address  = new AutoPopulatingList<NetworkInfo>(NetworkInfo.class);
	public AutoPopulatingList phishing  = new AutoPopulatingList<PhishingData>(PhishingData.class);
	public AutoPopulatingList registry  = new AutoPopulatingList<RegistryValues>(RegistryValues.class);
	public AutoPopulatingList hash  = new AutoPopulatingList<Hash>(Hash.class);
	public AutoPopulatingList dsig  = new AutoPopulatingList<DigitalSig>(DigitalSig.class);

    
	public Event() {
		// TODO Auto-generated constructor stub
	}
	
	public String getHttpcomments(){
		return this.httpcomments;
		
	}
	public void setHttpcomments(String httpcomments){
		this.httpcomments=httpcomments;
	}
	public String getExpectation(){
		return this.expectation;
		
	}
	public void setExpectation(String expectation){
		this.expectation=expectation;
	}
	public String getConfidence(){
		return this.confidence;
		
	}
	public void setConfidence(String confidence){
		this.confidence=confidence;
	}

	public void setRefName(String refName){
		this.refName=refName;
		
	}
	public String getRefName(){
		return this.refName;
	}
	public void setRefURL(String refURL){
		this.refURL=refURL;
	}
	public String getRefURL(){
		return this.refURL;
	}
	public void setRegistry(AutoPopulatingList<RegistryValues> registry){
		this.registry=registry;
		
	}
	public AutoPopulatingList<RegistryValues> getRegistry(){
		return this.registry;
	}

	
	public AutoPopulatingList<NetworkInfo> getAddress() {
		return this.address;
	}

	public void setAddress(AutoPopulatingList<NetworkInfo> address) {
		this.address = address;
	}
	public AutoPopulatingList<Hash> getHash() {
		return hash;
	}

	public void setHash(AutoPopulatingList<Hash> hash) {
		this.hash = hash;
	}
	public AutoPopulatingList<PhishingData> getPhishing() {
		return this.phishing;
	}

	public void setPhishing(AutoPopulatingList<PhishingData> phishing) {
		this.phishing = phishing;
	}
	public AutoPopulatingList<DigitalSig> getDsig() {
		return this.dsig;
	}

	public void setDsig(AutoPopulatingList<DigitalSig> dsig) {
		this.dsig = dsig;
	}

}
