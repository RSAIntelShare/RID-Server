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


public class IncidentData {
	public String description=null;
	public String starttime=null;
	public String stoptime=null;
	public String detecttime=null;
	public String reporttime=null;
	public AutoPopulatingList node = new AutoPopulatingList<Event>(Event.class);
    

    
	public IncidentData() {
		// TODO Auto-generated constructor stub
	}
	
	public void setNode(AutoPopulatingList<Event> node) {
		this.node = node;
	}
	public AutoPopulatingList<Event> getNode() {
		return this.node;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getStarttime(){
		return this.starttime;
		
	}
	public void setStarttime(String starttime){
		this.starttime=starttime;
	}
	public String getStoptime(){
		return this.stoptime;
		
	}
	public void setStoptime(String stoptime){
		this.stoptime=stoptime;
	}
	public String getDetecttime(){
		return this.detecttime;
		
	}
	public void setDetecttime(String detecttime){
		this.detecttime=detecttime;
	}
	public String getReporttime(){
		return this.reporttime;
		
	}
	public void setReporttime(String reporttime){
		this.reporttime=reporttime;
	}

}