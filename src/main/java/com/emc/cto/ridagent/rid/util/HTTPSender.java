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

package com.emc.cto.ridagent.rid.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.emc.documentum.xml.xproc.pipeline.model.PipelineOutput;

public class HTTPSender {

	//get log4j handler
	private static final Logger logger = Logger.getLogger(HTTPSender.class);
	
	private static String m_protocol = null;
	private static int m_port;
	private static String m_keystorePath = null;
	private static String m_keystorePassword = null;
	private static String m_truststorePath = null;
	private static String m_truststorePassword = null;
	
	
	public void setProtocol (String val) {
		m_protocol = val;
	}
	
	public void setPort (int val) {
		m_port = val;
	}
	
	public void setKeystorePath (String val) {
		m_keystorePath = val;
	}
	
	public void setKeystorePassword (String val) {
		m_keystorePassword = val;
	}
	
	public void setTruststorePath (String val) {
		m_truststorePath = val;
	}
	
	public void setTruststorePassword (String val) {
		m_truststorePassword = val;
	}
	
	public static Map<String,Object> httpSend (PipelineOutput output, String destURL){
		
		/* Set up TLS mutual authentication */
		
	    KeyStore keystore =null;
	    try {
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    InputStream keystoreInput =null;
	    try {
			keystoreInput = new FileInputStream(m_keystorePath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
		try {
			keystore.load(keystoreInput, m_keystorePassword.toCharArray());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    try {
	    	if(logger.isDebugEnabled()){
				logger.debug("Keystore has " + keystore.size() + " keys");
			}
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	    KeyStore truststore =null;
		try {
			truststore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
		InputStream truststoreInput =null;
		try {
			truststoreInput = new FileInputStream(m_truststorePath);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	    
		try {
			truststore.load(truststoreInput, m_truststorePassword.toCharArray());
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CertificateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
		
	    try {
	    	if(logger.isDebugEnabled()){
				logger.debug("Truststore has " + truststore.size() + " keys");
			}
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	    
	    SchemeRegistry schemeRegistry = new SchemeRegistry();
	    SSLSocketFactory schemeSocketFactory =null;
	    
		try {			
			schemeSocketFactory = new SSLSocketFactory(keystore, m_keystorePassword, truststore);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		schemeRegistry.register(new Scheme(m_protocol, m_port, schemeSocketFactory));
	    final HttpParams httpParams = new BasicHttpParams();
	    DefaultHttpClient httpClient = new DefaultHttpClient(new BasicClientConnectionManager(schemeRegistry),httpParams);
	    
	    
	    
	    /* Prepare the request to send */ 
	    
	    String body = null;
		Map<String,Object> responseMap = new HashMap<String,Object>();
		List<com.emc.documentum.xml.xproc.io.Source> sources = output.getSources(output.getPrimaryOutputPort());
		
		if (sources != null && !sources.isEmpty()) {
			// pipeline should only return a single value - we return the first as the output
			Node node = sources.get(0).getNode();
			InputStream is = sources.get(0).getInputStream();
			Reader rdr = sources.get(0).getReader();
			
			//For now we implement node only since we assume content is in the node
			if (node != null) {
				if(logger.isDebugEnabled()){
					logger.debug("Node has content");
				}
			    body = Utilities.nodeToString(node);
			    
			} else if (is != null) {
				if(logger.isDebugEnabled()){
					logger.debug("Input stream has content");
				}
			
				
			} else if (rdr != null) {
				if(logger.isDebugEnabled()){
					logger.debug("Reader has content");
				}	
			}
		}

	
	    HttpEntity request = new StringEntity(body, ContentType.TEXT_XML);
	    
	    //Create POST method
	    HttpPost postMethod = new HttpPost(destURL);
	    postMethod.setHeader("User-Agent","EMC RID System");
	    postMethod.setHeader("Content-Type","text/xml");
	    postMethod.setEntity(request);
	    
	    
	    /* POST the request and process the response */
	    HttpResponse httpResponse=null;
	    int code;
	    String responseBody =null;
	    
	    try {
	    	httpResponse = httpClient.execute(postMethod);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	
		if(httpResponse.getEntity()!=null){
			
			  code = httpResponse.getStatusLine().getStatusCode();
			  
			    try {
					responseBody = EntityUtils.toString(httpResponse.getEntity());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			
				if(logger.isDebugEnabled()){
					logger.debug("Response status code: " + code);
					logger.debug("Reponse body ="+ responseBody);
				}
				
				responseMap.put("success",true);
				responseMap.put("statusCode",code);
				responseMap.put("responseBody",responseBody);
		
		}
		else
		{
				responseMap.put("success", false);
				responseMap.put("errorMessage","Send failed (fill in exception)");
			}
	    
		
	   
	   return responseMap;
	}
	
}
