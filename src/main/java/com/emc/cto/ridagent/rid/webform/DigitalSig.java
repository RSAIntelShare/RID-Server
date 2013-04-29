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

public class DigitalSig {
	public String hash_value=null;
	public String hash_type=null;
	public String signature_value=null;
	public String signature_method=null;
	public String validity=null;
	public String can_method=null;
	public String type=null;
	public String issuer=null;
	public String issuernumber=null;
	public DigitalSig(){
		
	}
	public void setHash_type(String hash_type){
		this.hash_type=hash_type;
		
	}
	public void setHash_value(String hash_value){
		this.hash_value=hash_value;
	}
	public String getHash_type(){
		return this.hash_type;
	}
	public String getHash_value(){
		return this.hash_value;
	}
	public void setType(String type){
		this.type=type;
		
	}
	public String getType(){
		return this.type;
	}
	public void setSignature_value(String signature_value){
		this.signature_value=signature_value;
	}
	public String getSignature_value(){
		return this.signature_value;
	}
	public void setSignature_method(String signature_method){
		this.signature_method=signature_method;
	}
	public String getSignature_method(){
		return this.signature_method;
	}
	
	public void setCan_method(String can_method){
		this.can_method=can_method;
	}
	public String getCan_method(){
		return this.can_method;
	}	
	public void setValidity(String validity){
		this.validity=validity;
	}
	public String getValidity(){
		return this.validity;
	}
	public void setIssuer(String issuer){
		this.issuer=issuer;
	}
	public String getIssuer(){
		return this.issuer;
	}
	public void setIssuernumber(String issuernumber){
		this.issuernumber=issuernumber;
	}
	public String getIssuernumber(){
		return this.issuernumber;
	}
	

}
