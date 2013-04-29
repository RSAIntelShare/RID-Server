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


import java.math.BigInteger;

public class Hash {
	public String type=null;
	public AutoPopulatingList value  = new AutoPopulatingList<HashData>(HashData.class);
	public AutoPopulatingList file  = new AutoPopulatingList<FileData>(FileData.class);
	public String hash_value=null;
	public String hash_type=null;
	public Hash(){
		//to do
	}
	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type=type;
	}


	public AutoPopulatingList<HashData> getValue() {
		return this.value;
	}

	public void setValue(AutoPopulatingList<HashData> value) {
		this.value = value;
	}
	public AutoPopulatingList<FileData> getFile() {
		return this.file;
	}

	public void setFile(AutoPopulatingList<FileData> file) {
		this.file = file;
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

	
}
