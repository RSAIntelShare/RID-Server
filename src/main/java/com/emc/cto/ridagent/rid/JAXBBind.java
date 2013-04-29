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

package com.emc.cto.ridagent.rid;

import java.io.IOException;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import com.emc.cto.ridagent.rid.jaxb.ActionType;
import com.emc.cto.ridagent.rid.jaxb.Address;
import com.emc.cto.ridagent.rid.jaxb.Assessment;
import com.emc.cto.ridagent.rid.jaxb.AttType;
import com.emc.cto.ridagent.rid.jaxb.CanonicalizationMethodType;
import com.emc.cto.ridagent.rid.jaxb.Confidence;
import com.emc.cto.ridagent.rid.jaxb.Contact;
import com.emc.cto.ridagent.rid.jaxb.ContactMeansType;
import com.emc.cto.ridagent.rid.jaxb.DigestMethodType;
import com.emc.cto.ridagent.rid.jaxb.DomainData;
import com.emc.cto.ridagent.rid.jaxb.DtypeType;
import com.emc.cto.ridagent.rid.jaxb.EmailDetails;
import com.emc.cto.ridagent.rid.jaxb.EventData;
import com.emc.cto.ridagent.rid.jaxb.Expectation;
import com.emc.cto.ridagent.rid.jaxb.ExtensionType;
import com.emc.cto.ridagent.rid.jaxb.Flow;
import com.emc.cto.ridagent.rid.jaxb.HashSigDetails;
import com.emc.cto.ridagent.rid.jaxb.IODEFDocument;
import com.emc.cto.ridagent.rid.jaxb.Impact;
import com.emc.cto.ridagent.rid.jaxb.Incident;
import com.emc.cto.ridagent.rid.jaxb.IncidentIDType;
import com.emc.cto.ridagent.rid.jaxb.KeyInfoType;
import com.emc.cto.ridagent.rid.jaxb.MLStringType;
import com.emc.cto.ridagent.rid.jaxb.Method;
import com.emc.cto.ridagent.rid.jaxb.NodeRole;
import com.emc.cto.ridagent.rid.jaxb.ObjectFactory;
import com.emc.cto.ridagent.rid.jaxb.PolicyRegion;
import com.emc.cto.ridagent.rid.jaxb.RIDPolicyType;
import com.emc.cto.ridagent.rid.jaxb.RIDType;
import com.emc.cto.ridagent.rid.jaxb.Record;
import com.emc.cto.ridagent.rid.jaxb.RecordData;
import com.emc.cto.ridagent.rid.jaxb.Reference;
import com.emc.cto.ridagent.rid.jaxb.ReferenceType;
import com.emc.cto.ridagent.rid.jaxb.RegistryKeyModified;
import com.emc.cto.ridagent.rid.jaxb.RelatedDNSEntryType;
import com.emc.cto.ridagent.rid.jaxb.ReportID;
import com.emc.cto.ridagent.rid.jaxb.ReportSchemaType;
import com.emc.cto.ridagent.rid.jaxb.Service;
import com.emc.cto.ridagent.rid.jaxb.SeverityType;
import com.emc.cto.ridagent.rid.jaxb.SignatureMethodType;
import com.emc.cto.ridagent.rid.jaxb.SignatureType;
import com.emc.cto.ridagent.rid.jaxb.SignatureValueType;
import com.emc.cto.ridagent.rid.jaxb.SignedInfoType;
import com.emc.cto.ridagent.rid.jaxb.SoftwareType;
import com.emc.cto.ridagent.rid.jaxb.TrafficType;
import com.emc.cto.ridagent.rid.jaxb.X509DataType;
import com.emc.cto.ridagent.rid.jaxb.X509IssuerSerialType;
import com.emc.cto.ridagent.rid.jaxb.RegistryKeyModified.Key;
import com.emc.cto.ridagent.rid.webform.DNSRecord;
import com.emc.cto.ridagent.rid.webform.DigitalSig;
import com.emc.cto.ridagent.rid.webform.EmailInfo;
import com.emc.cto.ridagent.rid.webform.Event;
import com.emc.cto.ridagent.rid.webform.FileData;
import com.emc.cto.ridagent.rid.webform.Hash;
import com.emc.cto.ridagent.rid.webform.HashData;
import com.emc.cto.ridagent.rid.webform.IncidentData;
import com.emc.cto.ridagent.rid.webform.MyNamespacePrefixMapper;
import com.emc.cto.ridagent.rid.webform.NetworkInfo;
import com.emc.cto.ridagent.rid.webform.PhishingData;
import com.emc.cto.ridagent.rid.webform.RegistryValues;
import com.emc.cto.ridagent.rid.webform.SystemData;

public class JAXBBind {
	
	
	public static Document createDDOS(IncidentData watchList) throws DatatypeConfigurationException, IOException, ParserConfigurationException, JAXBException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		prop.load(classLoader.getResourceAsStream("/wrapper.properties"));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		ObjectFactory factory = new ObjectFactory();
		RIDType ridtype=factory.createRIDType();
		ridtype.setLang("en");
		RIDPolicyType ridpolicy=factory.createRIDPolicyType();
		ridpolicy.setMsgType("Report");
		ridpolicy.setMsgDestination("RIDSystem");
		PolicyRegion policyregion=factory.createPolicyRegion();
		policyregion.setRegion("IntraConsortium");
		ridpolicy.getPolicyRegion().add(policyregion);
		com.emc.cto.ridagent.rid.jaxb.Node node=factory.createNode();
		MLStringType nodeName=factory.createMLStringType();
		nodeName.setValue(prop.getProperty("nodename"));
		node.getNodeNameOrDomainDataOrAddress().add(nodeName);
		ridpolicy.setNode(node);
		ReportSchemaType rst=factory.createReportSchemaType();
		ridpolicy.setReportSchema(rst);
		TrafficType tt=factory.createTrafficType();
		tt.setType("Network");
		ridpolicy.getTrafficType().add(tt);
		ridtype.setRIDPolicy(ridpolicy);
		ExtensionType et=factory.createExtensionType();
		et.setDtype(DtypeType.XML);
		rst.setXMLDocument(et);

		IODEFDocument d=factory.createIODEFDocument();
		d.setLang("en");
		et.getContent().add(d);
		Incident inc = factory.createIncident();
		inc.setPurpose("reporting");
		IncidentIDType value=factory.createIncidentIDType();
		value.setName(prop.getProperty("nodename"));
		value.setValue("189234");  //hard coded
		ReportID rid=factory.createReportID();
		rid.getIncidentID().add(value);
		inc.setReportID(rid);
		if(watchList.getReporttime()!=null&&!watchList.getReporttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getReporttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar reporttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setReportTime(reporttime);

		}
		else{
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar now = 
				datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			inc.setReportTime(now);
		}
		if(watchList.getStarttime()!=null&&!watchList.getStarttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getStarttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar starttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setStartTime(starttime);

		}
		if(watchList.getDetecttime()!=null&&!watchList.getDetecttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getDetecttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar detecttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setDetectTime(detecttime);

		}
		Assessment assess=factory.createAssessment();
		assess.setOccurrence("potential");
		Impact im=factory.createImpact();
		im.setSeverity(SeverityType.MEDIUM);
		im.setType("dos");
		im.setValue("DDos Traffic");
		assess.getImpactOrTimeImpactOrMonetaryImpact().add(im);
		inc.getAssessment().add(assess);
		Contact con=factory.createContact();
		con.setRole(prop.getProperty("contactrole"));
		con.setType(prop.getProperty("contacttype"));
		MLStringType conname=factory.createMLStringType();
		conname.setValue(prop.getProperty("name"));
		con.setContactName(conname);
		ContactMeansType cmt=factory.createContactMeansType();
		cmt.setValue(prop.getProperty("email"));
		con.getEmail().add(cmt);
		inc.getContact().add(con);
		if(watchList.getDescription()!=null &&!watchList.getDescription().equals("")){
			MLStringType des=factory.createMLStringType();
			des.setValue(watchList.getDescription());
			inc.getDescription().add(des);
		}			
		Event nobj=watchList.getNode().get(0);

		if(nobj.getConfidence()!=null&&!nobj.getConfidence().equals("")){
			Confidence confidence=factory.createConfidence();
			confidence.setRating("numeric");
			confidence.setContent(nobj.getConfidence());
			assess.setConfidence(confidence);
		}

		EventData event=factory.createEventData();
		Method m=factory.createMethod();
		Reference re= factory.createReference();
		MLStringType mval=factory.createMLStringType();
		if(nobj.getRefName()!=null &&!nobj.getRefName().equals("")){
			mval.setValue(nobj.getRefName());
		}
		re.setReferenceName(mval);
		if(nobj.getRefURL()!=null && !nobj.getRefURL().equals("")){
			re.getURL().add(nobj.getRefURL());
		}
		m.getReferenceOrDescription().add(re);
		event.getMethod().add(m);
		if(nobj.getExpectation()!=null&&!nobj.getExpectation().equals("")){
			Expectation expectation= factory.createExpectation();
			expectation.setAction(ActionType.OTHER);
			MLStringType expectationdes=factory.createMLStringType();
			expectationdes.setValue(nobj.getExpectation());
			expectation.getDescription().add(expectationdes);
			event.getExpectation().add(expectation);
		}
		if(nobj.getAddress().size()>0){
			Flow f= factory.createFlow();
			event.getFlow().add(f);
			for(int ipindex=0;ipindex<nobj.getAddress().size();ipindex++){
				NetworkInfo ipobj= nobj.getAddress().get(ipindex);
				com.emc.cto.ridagent.rid.jaxb.System systemobj = factory.createSystem();
				systemobj.setCategory(ipobj.getSystemcategory());
				if(ipobj.getSpoofed()!=null&&!ipobj.getSpoofed().equals("")){
					systemobj.setSpoofed(ipobj.getSpoofed());

				}
				if(ipobj.getLogicaloperator().equals("and")){
					com.emc.cto.ridagent.rid.jaxb.Node n1=factory.createNode();
					NodeRole nr1=factory.createNodeRole();
					if(!ipobj.getRole().equals("")){
						nr1.setAttacktype(AttType.fromValue(ipobj.getRole()));
					}
					if(ipobj.getCategory().equals("ext-value")){
						nr1.setCategory("ext-value");
						nr1.setExtCategory("unknown");
					}
					nr1.setCategory(ipobj.getCategory());					
					n1.getNodeRole().add(nr1);
					systemobj.getNode().add(n1);
					f.getSystem().add(systemobj);
					for(int ipvalueindex=0;ipvalueindex<ipobj.getSystem().size();ipvalueindex++){
						SystemData sysobj= ipobj.getSystem().get(ipvalueindex);
						if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
							Service service=factory.createService();
							service.setPort(sysobj.getPortno());
							service.setIpProtocol(sysobj.getProtocolno());
							if(sysobj.getUseragent()!=null&&!sysobj.getUseragent().equals("")){
								SoftwareType agent=factory.createSoftwareType();
								agent.setUserAgent(sysobj.getUseragent());
								service.setApplication(agent);
							}
							systemobj.getService().add(service);
						}
						if(sysobj.getValue()!=null&&sysobj.getType()!=null&&!sysobj.getValue().equals("")&&!sysobj.getType().equals("")){
							if(sysobj.getType().equals("name")){
								MLStringType nameServer=factory.createMLStringType();
								nameServer.setValue(sysobj.getValue());
								n1.getNodeNameOrDomainDataOrAddress().add(nameServer);								
							}else{
								Address add=factory.createAddress();
								add.setCategory(sysobj.getType());
								add.setValue(sysobj.getValue());
								n1.getNodeNameOrDomainDataOrAddress().add(add);
							}

						}	
						else{
							com.emc.cto.ridagent.rid.jaxb.Node empty=factory.createNode();
							systemobj.getNode().add(empty);
						}
					}

				}
				else{
					f.getSystem().add(systemobj);
					for(int ipvalueindex=0;ipvalueindex<ipobj.getSystem().size();ipvalueindex++){
						SystemData sysobj= ipobj.getSystem().get(ipvalueindex);
						if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
							Service service=factory.createService();
							service.setPort(sysobj.getPortno());
							service.setIpProtocol(sysobj.getProtocolno());
							if(sysobj.getUseragent()!=null&&!sysobj.getUseragent().equals("")){
								SoftwareType agent=factory.createSoftwareType();
								agent.setUserAgent(sysobj.getUseragent());
								service.setApplication(agent);
							}
							systemobj.getService().add(service);
						}
						if(sysobj.getValue()!=null&&sysobj.getType()!=null&&!sysobj.getValue().equals("")&&!sysobj.getType().equals("")){
							if(sysobj.getType().equals("name")){
								com.emc.cto.ridagent.rid.jaxb.Node n1=factory.createNode();
								MLStringType nameServer=factory.createMLStringType();
								nameServer.setValue(sysobj.getValue());
								n1.getNodeNameOrDomainDataOrAddress().add(nameServer);
								NodeRole nr1=factory.createNodeRole();
								if(!ipobj.getRole().equals("")){
									nr1.setAttacktype(AttType.fromValue(ipobj.getRole()));
								}
								if(ipobj.getCategory().equals("ext-value")){
									nr1.setCategory("ext-value");
									nr1.setExtCategory("unknown");
								}
								nr1.setCategory(ipobj.getCategory());					
								n1.getNodeRole().add(nr1);
								systemobj.getNode().add(n1);
							}else{
								com.emc.cto.ridagent.rid.jaxb.Node n1=factory.createNode();
								Address add=factory.createAddress();
								add.setCategory(sysobj.getType());
								add.setValue(sysobj.getValue());
								n1.getNodeNameOrDomainDataOrAddress().add(add);
								NodeRole nr1=factory.createNodeRole();
								if(!ipobj.getRole().equals("")){
									nr1.setAttacktype(AttType.fromValue(ipobj.getRole()));
								}
								if(ipobj.getCategory().equals("ext-value")){
									nr1.setCategory("ext-value");
									nr1.setExtCategory("unknown");
								}
								nr1.setCategory(ipobj.getCategory());					
								n1.getNodeRole().add(nr1);
								systemobj.getNode().add(n1);
							}

						}
						else{
							com.emc.cto.ridagent.rid.jaxb.Node empty=factory.createNode();
							systemobj.getNode().add(empty);
						}
					}

				}



			}
			if(nobj.getHttpcomments()!=null &&!nobj.getHttpcomments().equals("")){
				com.emc.cto.ridagent.rid.jaxb.System syssensor = factory.createSystem();
				syssensor.setCategory("sensor");
				f.getSystem().add(syssensor);
				com.emc.cto.ridagent.rid.jaxb.Node empty=factory.createNode();
				syssensor.getNode().add(empty);
				MLStringType description=factory.createMLStringType();
				description.setValue(nobj.getHttpcomments());
				syssensor.getDescription().add(description);
			}

		}



		inc.getEventData().add(event);			
		d.getIncident().add(inc);
		JAXBContext context = JAXBContext.newInstance("com.emc.cto.ridagent.rid.jaxb");
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE); 
		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
		marshaller.marshal(ridtype,doc);
		return doc;
		
	}
	public static Document createPhishing(IncidentData watchList) throws IOException, ParserConfigurationException, DatatypeConfigurationException, JAXBException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		prop.load(classLoader.getResourceAsStream("/wrapper.properties"));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		ObjectFactory factory = new ObjectFactory();
		RIDType ridtype=factory.createRIDType();
		ridtype.setLang("en");
		RIDPolicyType ridpolicy=factory.createRIDPolicyType();
		ridpolicy.setMsgType("Report");
		ridpolicy.setMsgDestination("RIDSystem");
		PolicyRegion policyregion=factory.createPolicyRegion();
		policyregion.setRegion("IntraConsortium");
		ridpolicy.getPolicyRegion().add(policyregion);
		com.emc.cto.ridagent.rid.jaxb.Node node=factory.createNode();
		MLStringType nodeName=factory.createMLStringType();
		nodeName.setValue(prop.getProperty("nodename"));
		node.getNodeNameOrDomainDataOrAddress().add(nodeName);
		ridpolicy.setNode(node);
		ReportSchemaType rst=factory.createReportSchemaType();
		ridpolicy.setReportSchema(rst);
		TrafficType tt=factory.createTrafficType();
		tt.setType("Network");
		ridpolicy.getTrafficType().add(tt);
		ridtype.setRIDPolicy(ridpolicy);
		ExtensionType et=factory.createExtensionType();
		et.setDtype(DtypeType.XML);
		rst.setXMLDocument(et);
		IODEFDocument d=factory.createIODEFDocument();
		d.setLang("en");
		et.getContent().add(d);
		Incident inc = factory.createIncident();
		inc.setPurpose("reporting");
		IncidentIDType value=factory.createIncidentIDType();
		value.setName(prop.getProperty("nodename"));
		value.setValue("189234");
		ReportID rid=factory.createReportID();
		rid.getIncidentID().add(value);
		inc.setReportID(rid);
		if(watchList.getReporttime()!=null&&!watchList.getReporttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getReporttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar reporttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setReportTime(reporttime);

		}
		else{
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar now = 
				datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			inc.setReportTime(now);
		}
		if(watchList.getStarttime()!=null&&!watchList.getStarttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getStarttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar starttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setStartTime(starttime);

		}
		if(watchList.getStoptime()!=null&&!watchList.getStoptime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getStoptime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar stoptime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setEndTime(stoptime);

		}
		if(watchList.getDetecttime()!=null&&!watchList.getDetecttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getDetecttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar detecttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setDetectTime(detecttime);

		}
		Assessment assess=factory.createAssessment();
		assess.setOccurrence("potential");
		Impact im=factory.createImpact();
		im.setSeverity(SeverityType.MEDIUM);
		im.setType("info-leak");
		im.setValue("Malware with Command and Control Server and System Changes");
		assess.getImpactOrTimeImpactOrMonetaryImpact().add(im);
		inc.getAssessment().add(assess);
		Contact con=factory.createContact();
		con.setRole(prop.getProperty("contactrole"));
		con.setType(prop.getProperty("contacttype"));
		MLStringType conname=factory.createMLStringType();
		conname.setValue(prop.getProperty("name"));
		con.setContactName(conname);
		ContactMeansType cmt=factory.createContactMeansType();
		cmt.setValue(prop.getProperty("email"));
		con.getEmail().add(cmt);
		inc.getContact().add(con);
		if(watchList.getDescription()!=null && !watchList.getDescription().equals("")){
			MLStringType des=factory.createMLStringType();
			des.setValue(watchList.getDescription());
			inc.getDescription().add(des);
		}
		Event nobj=watchList.getNode().get(0); //get the first Node info
		EventData event=factory.createEventData();
		inc.getEventData().add(event);

		if(nobj.getAddress().size()>0){
			Flow f= factory.createFlow();
			event.getFlow().add(f);

			for(int ipindex=0;ipindex<nobj.getAddress().size();ipindex++){
				NetworkInfo ipobj= nobj.getAddress().get(ipindex);
				com.emc.cto.ridagent.rid.jaxb.System systemobj = factory.createSystem();
				systemobj.setCategory(ipobj.getSystemcategory());
				com.emc.cto.ridagent.rid.jaxb.Node n1=factory.createNode();
				NodeRole nr1=factory.createNodeRole();
				nr1.setCategory(ipobj.getCategory());
				if(!ipobj.getRole().equals("")){
					nr1.setAttacktype(AttType.fromValue(ipobj.getRole()));
				}
				n1.getNodeRole().add(nr1);
				f.getSystem().add(systemobj);
				systemobj.getNode().add(n1);
				for(int ipvalueindex=0;ipvalueindex<ipobj.getSystem().size();ipvalueindex++){
					SystemData sysobj= ipobj.getSystem().get(ipvalueindex);
					if(sysobj.getValue()!=null&&sysobj.getType()!=null&&!sysobj.getValue().equals("")&&!sysobj.getType().equals("")){
						if(sysobj.getType().equals("name")){

							MLStringType nameServer=factory.createMLStringType();
							nameServer.setValue(sysobj.getValue());
							n1.getNodeNameOrDomainDataOrAddress().add(nameServer);
							if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
								Service service=factory.createService();
								service.setPort(sysobj.getPortno());
								service.setIpProtocol(sysobj.getProtocolno());
								systemobj.getService().add(service);
							}													
						}
						else{
							Address add=factory.createAddress();
							add.setCategory(sysobj.getType());
							add.setValue(sysobj.getValue());
							n1.getNodeNameOrDomainDataOrAddress().add(add);
							if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
								Service service=factory.createService();
								service.setPort(sysobj.getPortno());
								service.setIpProtocol(sysobj.getProtocolno());
								systemobj.getService().add(service);
							}							
						}
					}
				}
			}
		}

		if(nobj.getPhishing().size()>0){
			Flow f= factory.createFlow();
			event.getFlow().add(f);

			for(int pindex=0;pindex<nobj.getPhishing().size();pindex++){
				PhishingData pobj= nobj.getPhishing().get(pindex);
				com.emc.cto.ridagent.rid.jaxb.System systemobj = factory.createSystem();
				systemobj.setCategory(pobj.getSystemcategory());
				com.emc.cto.ridagent.rid.jaxb.Node n1=factory.createNode();
				NodeRole nr1=factory.createNodeRole();
				nr1.setCategory(pobj.getCategory());
				if(!pobj.getRole().equals("")){
					nr1.setAttacktype(AttType.fromValue(pobj.getRole()));
				}
				n1.getNodeRole().add(nr1);
				f.getSystem().add(systemobj);
				systemobj.getNode().add(n1);
				for(int pvalueindex=0;pvalueindex<pobj.getSystem().size();pvalueindex++){
					SystemData sysobj= pobj.getSystem().get(pvalueindex);
					if(sysobj.getValue()!=null&&sysobj.getType()!=null&&!sysobj.getValue().equals("")&&!sysobj.getType().equals("")){
						if(sysobj.getType().equals("name")){

							MLStringType nameServer=factory.createMLStringType();
							nameServer.setValue(sysobj.getValue());
							n1.getNodeNameOrDomainDataOrAddress().add(nameServer);
							if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
								Service service=factory.createService();
								service.setPort(sysobj.getPortno());
								service.setIpProtocol(sysobj.getProtocolno());
								systemobj.getService().add(service);
							}													
						}
						else{
							Address add=factory.createAddress();
							add.setCategory(sysobj.getType());
							add.setValue(sysobj.getValue());
							n1.getNodeNameOrDomainDataOrAddress().add(add);
							if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
								Service service=factory.createService();
								service.setPort(sysobj.getPortno());
								service.setIpProtocol(sysobj.getProtocolno());
								systemobj.getService().add(service);
							}							
						}
					}
				}
				for(int pvalueindex=0;pvalueindex<pobj.getEmailinfo().size();pvalueindex++){
					EmailInfo emailobj= pobj.getEmailinfo().get(pvalueindex);
					if(emailobj.getEmailid()!=null||emailobj.getSubject()!=null||!emailobj.getEmailid().equals("")||!emailobj.getSubject().equals("")||emailobj.getMailerid()!=null||!emailobj.getMailerid().equals("")){
						Service service=factory.createService();
						service.setPort(new BigInteger("25"));
						service.setIpProtocol(new BigInteger("6"));
						systemobj.getService().add(service);
						EmailDetails edetails=factory.createEmailDetails();
						if(!emailobj.getEmailid().equals("")){
							ContactMeansType emailid=factory.createContactMeansType();
							emailid.setValue(emailobj.getEmailid());
							edetails.setEmail(emailid);
						}
						if(!emailobj.getSubject().equals("")){
							MLStringType subject=factory.createMLStringType();
							subject.setValue(emailobj.getSubject());
							edetails.setEmailSubject(subject);
						}
						if(!emailobj.getMailerid().equals("")){
							MLStringType mailer=factory.createMLStringType();
							mailer.setValue(emailobj.getMailerid());
							edetails.setXMailer(mailer);
						}
						service.setEmailInfo(edetails);
						if(!emailobj.getDomain().equals("")){
							DomainData dd=factory.createDomainData();
							MLStringType dname=factory.createMLStringType();
							dname.setValue(emailobj.getDomain());
							dd.setName(dname);
							if(emailobj.getDomaindate()!=null&&!emailobj.getDomaindate().equals("")){
								StringTokenizer token = new StringTokenizer(emailobj.getDomaindate(), ":");
								GregorianCalendar domain = new GregorianCalendar();
								int year=Integer.parseInt(token.nextElement().toString());
								int month=Integer.parseInt(token.nextElement().toString());
								int date=Integer.parseInt(token.nextElement().toString());
								int hourOfDay=Integer.parseInt(token.nextElement().toString());
								int minute=Integer.parseInt(token.nextElement().toString());
								int second=Integer.parseInt(token.nextElement().toString());
								domain.set(year, month-1, date, hourOfDay, minute, second);
								DatatypeFactory factoryobj = DatatypeFactory.newInstance();
								XMLGregorianCalendar domaindate = 
									factoryobj.newXMLGregorianCalendar(domain);
								dd.setDateDomainWasChecked(domaindate);

							}
							n1.getNodeNameOrDomainDataOrAddress().add(dd);
							for(int dnsindex=0;dnsindex<emailobj.getDns().size();dnsindex++){
								DNSRecord dnsrecord=emailobj.getDns().get(dnsindex);
								if(dnsrecord.getType()!=null&&!dnsrecord.getType().equals("")&&dnsrecord.getValue()!=null&&!dnsrecord.getValue().equals("")){
									RelatedDNSEntryType rdnstype=factory.createRelatedDNSEntryType();
									rdnstype.setRecordType(dnsrecord.getType());
									rdnstype.setValue(dnsrecord.getValue());
									dd.getRelatedDNS().add(rdnstype);

								}								
							}
						}
					}
				}
			}
		}			

		Record r=factory.createRecord();
		event.setRecord(r);
		if(nobj.getHash().size()>0){
			RecordData rd=factory.createRecordData();
			r.getRecordData().add(rd);
			for(int hashindex=0;hashindex<nobj.getHash().size();hashindex++){
				Hash hobj=nobj.getHash().get(hashindex);
				HashSigDetails ha=factory.createHashSigDetails();
				rd.getHashInformation().add(ha);
				if(!hobj.getType().equals("")){
					ha.setType(hobj.getType());
				}
				for(int fileindex=0;fileindex<hobj.getFile().size();fileindex++){
					FileData filedata=hobj.getFile().get(fileindex);
					if(filedata.getFilename()!=null&&filedata.getFilesize()!=null&&!filedata.getFilename().equals("")){
						MLStringType filename=factory.createMLStringType();
						filename.setValue(filedata.getFilename());
						ha.getFileName().add(filename);
						ha.getFileSize().add(filedata.getFilesize());


					}
				}
				if(hobj.getHash_value()!=null&&hobj.getHash_type()!=null&&!hobj.getHash_value().equals("")&&!hobj.getHash_type().equals("")){
					ReferenceType ref=factory.createReferenceType();		
					DigestMethodType dgt=factory.createDigestMethodType();
					dgt.setAlgorithm(hobj.getHash_type());
					ref.setDigestMethod(dgt);
					ref.setDigestValue(hobj.getHash_value());
					ha.getReference().add(ref);

				}
			}				

		}
		if(nobj.getDsig().size()>0){
			RecordData rd=factory.createRecordData();
			r.getRecordData().add(rd);
			for(int hashindex=0;hashindex<nobj.getDsig().size();hashindex++){
				DigitalSig dobj=nobj.getDsig().get(hashindex);
				HashSigDetails ha=factory.createHashSigDetails();
				rd.getHashInformation().add(ha);
				if(!dobj.getType().equals("")){
					ha.setType(dobj.getType());
				}
				if(dobj.getValidity().equals("true"))
					ha.setValid(true);
				else
					ha.setValid(false);
				SignatureType signature=factory.createSignatureType();
				ha.getSignature().add(signature);
				SignatureValueType sigvalue=factory.createSignatureValueType();
				sigvalue.setValue(dobj.getSignature_value());
				signature.setSignatureValue(sigvalue);
				SignedInfoType signedinfo=factory.createSignedInfoType();
				SignatureMethodType method=factory.createSignatureMethodType();
				method.setAlgorithm(dobj.getSignature_method());
				signedinfo.setSignatureMethod(method);
				CanonicalizationMethodType cmethod=factory.createCanonicalizationMethodType();
				cmethod.setAlgorithm("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
				signedinfo.setCanonicalizationMethod(cmethod);
				signature.setSignedInfo(signedinfo);
				if(dobj.getIssuer()!=null&&!dobj.getIssuer().equals("")){
					KeyInfoType keyinfo=factory.createKeyInfoType();
					X509DataType data= factory.createX509DataType();
					X509IssuerSerialType serial=factory.createX509IssuerSerialType();
					data.getX509IssuerSerialOrX509SKIOrX509SubjectName().add(serial);
					serial.setX509IssuerName(dobj.getIssuer());
					serial.setX509SerialNumber(new BigInteger(dobj.getIssuernumber()));
					keyinfo.getContent().add(data);
					signature.setKeyInfo(keyinfo);
				}
				if(dobj.getHash_value()!=null&&dobj.getHash_type()!=null&&!dobj.getHash_value().equals("")&&!dobj.getHash_type().equals("")){
					ReferenceType ref=factory.createReferenceType();		
					DigestMethodType dgt=factory.createDigestMethodType();
					dgt.setAlgorithm(dobj.getHash_type());
					ref.setDigestMethod(dgt);
					ref.setDigestValue(dobj.getHash_value());
					signedinfo.getReference().add(ref);

				}
			}	
		}		
		d.getIncident().add(inc);
		JAXBContext context = JAXBContext.newInstance("com.emc.cto.ridagent.rid.jaxb");
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE); 
		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper()); 
		marshaller.marshal(ridtype,doc);
		return doc;
	}
	public static Document createIndicators(IncidentData watchList) throws IOException, ParserConfigurationException, DatatypeConfigurationException, JAXBException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		prop.load(classLoader.getResourceAsStream("/wrapper.properties"));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		ObjectFactory factory = new ObjectFactory();
		RIDType ridtype=factory.createRIDType();
		ridtype.setLang("en");
		RIDPolicyType ridpolicy=factory.createRIDPolicyType();
		ridpolicy.setMsgType("Report");
		ridpolicy.setMsgDestination("RIDSystem");
		PolicyRegion policyregion=factory.createPolicyRegion();
		policyregion.setRegion("IntraConsortium");
		ridpolicy.getPolicyRegion().add(policyregion);
		com.emc.cto.ridagent.rid.jaxb.Node node=factory.createNode();
		MLStringType nodeName=factory.createMLStringType();
		nodeName.setValue(prop.getProperty("nodename"));
		node.getNodeNameOrDomainDataOrAddress().add(nodeName);
		ridpolicy.setNode(node);
		ReportSchemaType rst=factory.createReportSchemaType();
		ridpolicy.setReportSchema(rst);
		TrafficType tt=factory.createTrafficType();
		tt.setType("Network");
		ridpolicy.getTrafficType().add(tt);
		ridtype.setRIDPolicy(ridpolicy);
		ExtensionType et=factory.createExtensionType();
		et.setDtype(DtypeType.XML);
		rst.setXMLDocument(et);
		IODEFDocument d=factory.createIODEFDocument();
		d.setLang("en");
		et.getContent().add(d);
		Incident inc = factory.createIncident();
		inc.setPurpose("reporting");
		IncidentIDType value=factory.createIncidentIDType();
		value.setName(prop.getProperty("nodename"));
		value.setValue("189234");
		ReportID rid=factory.createReportID();
		rid.getIncidentID().add(value);
		inc.setReportID(rid);
		if(watchList.getReporttime()!=null&&!watchList.getReporttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getReporttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar reporttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setReportTime(reporttime);

		}
		else{
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar now = 
				datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			inc.setReportTime(now);
		}
		Assessment assess=factory.createAssessment();
		assess.setOccurrence("potential");
		Impact im=factory.createImpact();
		im.setSeverity(SeverityType.MEDIUM);
		im.setType("info-leak");
		im.setValue("Malware with Command and Control Server and System Changes");
		assess.getImpactOrTimeImpactOrMonetaryImpact().add(im);
		inc.getAssessment().add(assess);
		Contact con=factory.createContact();
		con.setRole(prop.getProperty("contactrole"));
		con.setType(prop.getProperty("contacttype"));
		MLStringType conname=factory.createMLStringType();
		conname.setValue(prop.getProperty("name"));
		con.setContactName(conname);
		ContactMeansType cmt=factory.createContactMeansType();
		cmt.setValue(prop.getProperty("email"));
		con.getEmail().add(cmt);
		inc.getContact().add(con);
		if(watchList.getDescription()!=null && !watchList.getDescription().equals("")){
			MLStringType des=factory.createMLStringType();
			des.setValue(watchList.getDescription());
			inc.getDescription().add(des);
		}
		Event nobj=watchList.getNode().get(0); //get the first Node info

		if(nobj.getAddress().size()>0){
			EventData event=factory.createEventData();
			Flow f= factory.createFlow();
			event.getFlow().add(f);
			inc.getEventData().add(event);

			for(int ipindex=0;ipindex<nobj.getAddress().size();ipindex++){
				NetworkInfo ipobj= nobj.getAddress().get(ipindex);
				com.emc.cto.ridagent.rid.jaxb.System systemobj = factory.createSystem();
				systemobj.setCategory(ipobj.getSystemcategory());
				com.emc.cto.ridagent.rid.jaxb.Node n1=factory.createNode();
				NodeRole nr1=factory.createNodeRole();
				nr1.setCategory(ipobj.getCategory());
				if(!ipobj.getRole().equals("")){
					nr1.setAttacktype(AttType.fromValue(ipobj.getRole()));
				}
				n1.getNodeRole().add(nr1);
				f.getSystem().add(systemobj);
				for(int ipvalueindex=0;ipvalueindex<ipobj.getSystem().size();ipvalueindex++){
					SystemData sysobj= ipobj.getSystem().get(ipvalueindex);
					if(sysobj.getValue()!=null&&sysobj.getType()!=null&&!sysobj.getValue().equals("")&&!sysobj.getType().equals("")){
						if(sysobj.getType().equals("name")){

							MLStringType nameServer=factory.createMLStringType();
							nameServer.setValue(sysobj.getValue());
							n1.getNodeNameOrDomainDataOrAddress().add(nameServer);
							systemobj.getNode().add(n1);
							if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
								Service service=factory.createService();
								service.setPort(sysobj.getPortno());
								service.setIpProtocol(sysobj.getProtocolno());
								systemobj.getService().add(service);
							}													
						}
						else{
							Address add=factory.createAddress();
							add.setCategory(sysobj.getType());
							add.setValue(sysobj.getValue());
							n1.getNodeNameOrDomainDataOrAddress().add(add);
							systemobj.getNode().add(n1);
							if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
								Service service=factory.createService();
								service.setPort(sysobj.getPortno());
								service.setIpProtocol(sysobj.getProtocolno());
								systemobj.getService().add(service);
							}							
						}
					}
				}
			}
		}
		if(nobj.getHash().size()>0){
			if((nobj.getHash().size()>0)||(nobj.getRegistry().size()>0)){
				EventData event=factory.createEventData();
				Record r=factory.createRecord();
				RecordData rd=factory.createRecordData();
				r.getRecordData().add(rd);
				event.setRecord(r);
				inc.getEventData().add(event);
				for(int hashindex=0;hashindex<nobj.getHash().size();hashindex++){
					Hash hobj=nobj.getHash().get(hashindex);
					HashSigDetails ha=factory.createHashSigDetails();
					rd.getHashInformation().add(ha);
					if(!hobj.getType().equals("")){
						ha.setType(hobj.getType());
					}
					for(int hashvalindex=0;hashvalindex<hobj.getValue().size();hashvalindex++){
						HashData hval= hobj.getValue().get(hashvalindex);							
						if(hval.getValue()!=null&&hval.getHash_type()!=null&&!hval.getValue().equals("")&&!hval.getHash_type().equals("")){
							ReferenceType ref=factory.createReferenceType();		
							DigestMethodType dgt=factory.createDigestMethodType();
							dgt.setAlgorithm(hval.getHash_type());
							ref.setDigestMethod(dgt);
							ref.setDigestValue(hval.getValue());
							ha.getReference().add(ref);

						}
					}
				}
			}

		}
		if(nobj.getRegistry().size()>0){
			EventData event=factory.createEventData();
			Record r=factory.createRecord();
			RecordData rd=factory.createRecordData();
			r.getRecordData().add(rd);
			event.setRecord(r);
			inc.getEventData().add(event);
			RegistryKeyModified rkm=factory.createRegistryKeyModified();
			rd.getWindowsRegistryKeysModified().add(rkm);
			for(int regindex=0;regindex<nobj.getRegistry().size();regindex++){
				RegistryValues robj=nobj.getRegistry().get(regindex);
				if(robj.getKey()!=null&&robj.getValue()!=null&&!robj.getKey().equals("")&&!robj.getValue().equals("")){
					Key key=factory.createRegistryKeyModifiedKey();
					key.setRegistryaction(robj.getAction());
					key.setKeyName(robj.getKey());
					key.setValue(robj.getValue());
					key.setType("watchlist");
					rkm.getKey().add(key);											
				}					
			}					
		}		
		d.getIncident().add(inc);
		JAXBContext context = JAXBContext.newInstance("com.emc.cto.ridagent.rid.jaxb");
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE); 
		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper()); 
		marshaller.marshal(ridtype,doc);
		return doc;
	}
	public static Document createWatchList(IncidentData watchList) throws IOException, ParserConfigurationException, DatatypeConfigurationException, JAXBException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties prop = new Properties();
		prop.load(classLoader.getResourceAsStream("/wrapper.properties"));
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		ObjectFactory factory = new ObjectFactory();
		RIDType ridtype=factory.createRIDType();
		ridtype.setLang("en");
		RIDPolicyType ridpolicy=factory.createRIDPolicyType();
		ridpolicy.setMsgType("Report");
		ridpolicy.setMsgDestination("RIDSystem");
		PolicyRegion policyregion=factory.createPolicyRegion();
		policyregion.setRegion("IntraConsortium");
		ridpolicy.getPolicyRegion().add(policyregion);
		com.emc.cto.ridagent.rid.jaxb.Node node=factory.createNode();
		MLStringType nodeName=factory.createMLStringType();
		nodeName.setValue(prop.getProperty("nodename"));
		node.getNodeNameOrDomainDataOrAddress().add(nodeName);
		ridpolicy.setNode(node);
		ReportSchemaType rst=factory.createReportSchemaType();
		ridpolicy.setReportSchema(rst);
		TrafficType tt=factory.createTrafficType();
		tt.setType("Network");
		ridpolicy.getTrafficType().add(tt);
		ridtype.setRIDPolicy(ridpolicy);
		ExtensionType et=factory.createExtensionType();
		et.setDtype(DtypeType.XML);
		rst.setXMLDocument(et);

		IODEFDocument d=factory.createIODEFDocument();
		d.setLang("en");
		et.getContent().add(d);
		Incident inc = factory.createIncident();
		inc.setPurpose("reporting");
		IncidentIDType value=factory.createIncidentIDType();
		value.setName(prop.getProperty("nodename"));
		value.setValue("189234");
		ReportID rid=factory.createReportID();
		rid.getIncidentID().add(value);
		inc.setReportID(rid);
		if(watchList.getReporttime()!=null&&!watchList.getReporttime().equals("")){
			StringTokenizer token = new StringTokenizer(watchList.getReporttime(), ":");
			GregorianCalendar domain = new GregorianCalendar();
			int year=Integer.parseInt(token.nextElement().toString());
			int month=Integer.parseInt(token.nextElement().toString());
			int date=Integer.parseInt(token.nextElement().toString());
			int hourOfDay=Integer.parseInt(token.nextElement().toString());
			int minute=Integer.parseInt(token.nextElement().toString());
			int second=Integer.parseInt(token.nextElement().toString());
			domain.set(year, month-1, date, hourOfDay, minute, second);
			DatatypeFactory factoryobj = DatatypeFactory.newInstance();
			XMLGregorianCalendar reporttime = 
				factoryobj.newXMLGregorianCalendar(domain);
			inc.setReportTime(reporttime);

		}
		else{
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
			XMLGregorianCalendar now = 
				datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
			inc.setReportTime(now);
		}
		Assessment assess=factory.createAssessment();
		assess.setOccurrence("potential");
		Impact im=factory.createImpact();
		im.setSeverity(SeverityType.MEDIUM);
		im.setType("info-leak");
		im.setValue("Malware with Command and Control Server and System Changes");
		assess.getImpactOrTimeImpactOrMonetaryImpact().add(im);
		inc.getAssessment().add(assess);
		Contact con=factory.createContact();
		con.setRole(prop.getProperty("contactrole"));
		con.setType(prop.getProperty("contacttype"));
		MLStringType conname=factory.createMLStringType();
		conname.setValue(prop.getProperty("name"));
		con.setContactName(conname);
		ContactMeansType cmt=factory.createContactMeansType();
		cmt.setValue(prop.getProperty("email"));
		con.getEmail().add(cmt);
		inc.getContact().add(con);
		if(watchList.getDescription()!=null &&!watchList.getDescription().equals("")){
			MLStringType des=factory.createMLStringType();
			des.setValue(watchList.getDescription());
			inc.getDescription().add(des);
		}			
		for(int index=0;index<watchList.getNode().size();index++){
			Event nobj=watchList.getNode().get(index);
			EventData event=factory.createEventData();
			Method m=factory.createMethod();
			Reference re= factory.createReference();
			MLStringType mval=factory.createMLStringType();
			if(nobj.getRefName()!=null){
				mval.setValue(nobj.getRefName());
			}
			re.setReferenceName(mval);
			if(nobj.getRefURL()!=null){
				re.getURL().add(nobj.getRefURL());
			}
			m.getReferenceOrDescription().add(re);
			event.getMethod().add(m);
			if(nobj.getAddress().size()>0){
				Flow f= factory.createFlow();
				event.getFlow().add(f);
				for(int ipindex=0;ipindex<nobj.getAddress().size();ipindex++){
					NetworkInfo ipobj= nobj.getAddress().get(ipindex);
					com.emc.cto.ridagent.rid.jaxb.System systemobj = factory.createSystem();
					systemobj.setCategory(ipobj.getSystemcategory());
					com.emc.cto.ridagent.rid.jaxb.Node n1=factory.createNode();
					NodeRole nr1=factory.createNodeRole();
					if(!ipobj.getRole().equals("")){
						nr1.setAttacktype(AttType.fromValue(ipobj.getRole()));
					}
					if(ipobj.getCategory().equals("ext-value")){
						nr1.setCategory("ext-value");
						nr1.setExtCategory("unknown");
					}
					nr1.setCategory(ipobj.getCategory());					
					n1.getNodeRole().add(nr1);
					systemobj.getNode().add(n1);
					f.getSystem().add(systemobj);
					for(int ipvalueindex=0;ipvalueindex<ipobj.getSystem().size();ipvalueindex++){
						SystemData sysobj= ipobj.getSystem().get(ipvalueindex);
						if(sysobj.getPortno()!=null && sysobj.getProtocolno()!=null){
							Service service=factory.createService();
							service.setPort(sysobj.getPortno());
							service.setIpProtocol(sysobj.getProtocolno());
							systemobj.getService().add(service);
						}
						if(sysobj.getValue()!=null&&sysobj.getType()!=null&&!sysobj.getValue().equals("")&&!sysobj.getType().equals("")){
							if(sysobj.getType().equals("name")){
								MLStringType nameServer=factory.createMLStringType();
								nameServer.setValue(sysobj.getValue());
								n1.getNodeNameOrDomainDataOrAddress().add(nameServer);								
							}else{
								Address add=factory.createAddress();
								add.setCategory(sysobj.getType());
								add.setValue(sysobj.getValue());
								n1.getNodeNameOrDomainDataOrAddress().add(add);
							}

						}	
					}
				}
			}

			if((nobj.getHash().size()>0)||(nobj.getRegistry().size()>0)){
				Record r=factory.createRecord();
				RecordData rd=factory.createRecordData();
				for(int hashindex=0;hashindex<nobj.getHash().size();hashindex++){
					Hash hobj=nobj.getHash().get(hashindex);
					HashSigDetails ha=factory.createHashSigDetails();
					rd.getHashInformation().add(ha);
					if(!hobj.getType().equals("")){
						ha.setType(hobj.getType());
					}
					for(int hashvalindex=0;hashvalindex<hobj.getValue().size();hashvalindex++){
						HashData hval= hobj.getValue().get(hashvalindex);							
						if(hval.getValue()!=null&&hval.getHash_type()!=null&&!hval.getValue().equals("")&&!hval.getHash_type().equals("")){

							ReferenceType ref=factory.createReferenceType();		
							DigestMethodType dgt=factory.createDigestMethodType();
							dgt.setAlgorithm(hval.getHash_type());
							ref.setDigestMethod(dgt);
							ref.setDigestValue(hval.getValue());
							ha.getReference().add(ref);

						}
					}
				}
				RegistryKeyModified rkm=factory.createRegistryKeyModified();

				for(int regindex=0;regindex<nobj.getRegistry().size();regindex++){
					RegistryValues robj=nobj.getRegistry().get(regindex);
					if(robj.getKey()!=null&&robj.getValue()!=null&&!robj.getKey().equals("")&&!robj.getValue().equals("")){
						Key key=factory.createRegistryKeyModifiedKey();
						key.setRegistryaction(robj.getAction());
						key.setKeyName(robj.getKey());
						key.setValue(robj.getValue());
						rkm.getKey().add(key);											
					}					
				}
				rd.getWindowsRegistryKeysModified().add(rkm);
				r.getRecordData().add(rd);
				event.setRecord(r);

			}
			inc.getEventData().add(event);
		}			
		d.getIncident().add(inc);
		JAXBContext context = JAXBContext.newInstance("com.emc.cto.ridagent.rid.jaxb");
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE); 
		marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespacePrefixMapper());
		marshaller.marshal(ridtype,doc);
		return doc;
		
	}

}
