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

/*	
 *  @author Vijayanand Bharadwaj
 *	@author John P. Field
*/	

package com.emc.cto.ridagent.rid;


import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import com.emc.cto.xproc.PipelineInputCache;
import com.emc.cto.xproc.XProcXMLProcessingContext;
import com.emc.documentum.xml.xproc.XProcException;
import com.emc.documentum.xml.xproc.pipeline.model.PipelineOutput;

import org.apache.log4j.Logger;
import java.util.Set;
import org.springframework.security.core.context.*;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

@Controller
@RequestMapping("/")
public class RIDRequestHandler {

	// Defining processing context with design time bindings for each resource and each operation
	private static XProcXMLProcessingContext m_requestHandler = null;     // POST
	//get log4j handler
	private static final Logger logger = Logger.getLogger(RIDRequestHandler.class);

	private static Set <java.lang.String> whiteList;
	
	public RIDRequestHandler() {		
	}
	
	public void setWhiteList(Set <java.lang.String> whiteList) {
		RIDRequestHandler.whiteList = whiteList;
	}
	
	public void setRequestHandler (XProcXMLProcessingContext val) {
		m_requestHandler = val;
	}
	
	/* This method handles all incoming RID messages
     * */
	@RequestMapping(method = RequestMethod.POST)
	public String handleRequest(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException, NoSuchRequestHandlingMethodException {
		try {

			// check the white list to see if we want to talk to the counter-party
			logger.info("If we got this far, the Spring Security whitelist checking has already passed OK...");

			SecurityContext ssc = SecurityContextHolder.getContext();
			String ridPeerName = ssc.getAuthentication().getName().toString();

			if (ridPeerName != null) {
				logger.info("RID Peer Name is: " + ridPeerName);
				if (!whiteList.contains(ridPeerName)) {
					logger.info("RID Peer name not found in whitelist.");
					logger.info("Edit RIDSystem-servlet.xml if and as appropriate, in order to authorize this peer.");
					throw new NoSuchRequestHandlingMethodException(request); // results in a 404 Not Found.
				}
				logger.info("Local whitelist Checking completed.");
				logger.info("Incoming WatchList.   "+request.getInputStream().toString());
			}
			
			PipelineInputCache pi = new PipelineInputCache();

			// supply HTTP body as the source for the resource Create pipeline
			pi.setInputPort("source", request.getInputStream());			
		
			PipelineOutput output = m_requestHandler.executeOn(pi);

			model.addAttribute("pipelineOutput", output);
			return "pipelineOutput";
		} finally {
			; //TODO add finally handler
		}
	}

	
}
