package com.emc.cto.ridagent.rid.test;
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



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

//import com.emc.documentum.xml.xproc.pipeline.model.PipelineOutput;

public class TestScript {

	//get log4j handler
	private static final Logger logger = Logger.getLogger(TestScript.class);

	private static String m_protocol = "https";
	private static int m_port=8443;
	private static String m_keystorePath = "C:\\Program Files\\Apache Software Foundation\\Tomcat 7.0\\keystore\\keystore";
	private static String m_keystorePassword = "changeit";
	private static String m_truststorePath = "C:\\Program Files\\Java\\jdk1.6.0_35\\jre\\lib\\security\\cacerts";
	private static String m_truststorePassword = "changeit";

	public static String httpSend (String  output, String destURL) throws ParserConfigurationException, SAXException{

		/* Set up TLS mutual authentication */

		KeyStore keystore =null;
		String docid = null;
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

		Map<String,Object> responseMap = new HashMap<String,Object>();

		HttpEntity request = new StringEntity(output, ContentType.TEXT_XML);

		//Create POST method
		HttpPost postMethod = new HttpPost(destURL);
		postMethod.setHeader("User-Agent","EMC RID System");
		postMethod.setHeader("Content-Type","text/xml");
		postMethod.setEntity(request);


		/* POST the request and process the response */
		HttpResponse httpResponse=null;
		int code;

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
				InputStream xml = httpResponse.getEntity().getContent();

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(xml);
				docid=doc.getElementsByTagName("iodef:IncidentID").item(0).getTextContent();
				System.out.println("ID of the newly created document   "+docid);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			responseMap.put("success",true);
			responseMap.put("statusCode",code);

		}
		else
		{
			responseMap.put("success", false);
			responseMap.put("errorMessage","Send failed (fill in exception)");
		}



		return docid;
	}
	
	public static void main(String args[]) throws SAXException, ParserConfigurationException, URISyntaxException, ClientProtocolException, IOException{
		String xmlData="  <iodef-rid:RID lang=\"en\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:iodef-rid=\"urn:ietf:params:xml:ns:iodef-rid-2.0\" xmlns:iodef=\"urn:ietf:params:xml:ns:iodef-1.42\" xsi:schemaLocation=\"urn:ietf:params:xml:ns:iodef-rid-2.0 iodef-rid-2.0.xsd\">";
		xmlData=xmlData+"<iodef-rid:RIDPolicy MsgType=\"Report\" MsgDestination=\"RIDSystem\">";
		xmlData=xmlData+"<iodef-rid:PolicyRegion region=\"IntraConsortium\"/>";
		xmlData=xmlData+" <iodef:Node>";
		xmlData=xmlData+"   <iodef:NodeName>192.168.1.1</iodef:NodeName>";
		xmlData=xmlData+" </iodef:Node>";
		xmlData=xmlData+ "<iodef-rid:TrafficType type=\"Network\"/>";
		xmlData=xmlData+"</iodef-rid:RIDPolicy>";
		xmlData=xmlData+"</iodef-rid:RID>";
		String id=TestScript.httpSend(xmlData, "https://ridtest.emc.com:4590/");
		HttpGet httpget = new HttpGet("http://localhost:1280/federation/RID/"+id+"/report.xml");

		DefaultHttpClient httpclient = new DefaultHttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials("Administrator", "dangerous");
		httpclient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), defaultcreds);
		httpget.setHeader("User-Agent","EMC RID System");

		HttpResponse response = httpclient.execute(httpget);
		if(response.getEntity()!=null){

			int code = response.getStatusLine().getStatusCode();
			if(code==404){
				System.out.println("Error has occured! Document not found in the xDB");
				
			}
			else if(code==200){
				System.out.println("Document Successfully saved in the database");
				
			}
			else{
				System.out.println("Error could not be determined");
			}
		}
	}

}

