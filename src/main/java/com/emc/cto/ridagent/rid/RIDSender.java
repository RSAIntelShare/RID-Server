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




import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import com.emc.cto.ridagent.rid.jaxb.*;
import com.emc.cto.ridagent.rid.jaxb.RegistryKeyModified.Key;
import com.emc.cto.ridagent.rid.util.HTTPSender;
import com.emc.cto.ridagent.rid.webform.DNSRecord;
import com.emc.cto.ridagent.rid.webform.DigitalSig;
import com.emc.cto.ridagent.rid.webform.EmailInfo;
import com.emc.cto.ridagent.rid.webform.Event;
import com.emc.cto.ridagent.rid.webform.FileData;
import com.emc.cto.ridagent.rid.webform.Hash;
import com.emc.cto.ridagent.rid.webform.HashData;
import com.emc.cto.ridagent.rid.webform.IncidentData;
import com.emc.cto.ridagent.rid.webform.NetworkInfo;
import com.emc.cto.ridagent.rid.webform.PhishingData;
import com.emc.cto.ridagent.rid.webform.RegistryValues;
import com.emc.cto.ridagent.rid.webform.SystemData;
import com.emc.cto.xproc.PipelineInputCache;
import com.emc.cto.xproc.XProcXMLProcessingContext;
import com.emc.documentum.xml.xproc.XProcException;
import com.emc.documentum.xml.xproc.pipeline.model.PipelineOutput;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;


@Controller
@RequestMapping("/RIDSender")
public class RIDSender {

	private static XProcXMLProcessingContext m_createReport = null;     // POST
	private static XProcXMLProcessingContext m_getReport = null;     // POST	
	private static XProcXMLProcessingContext m_createQuery = null;     // POST
	private static XProcXMLProcessingContext m_getQuery = null;     // POST
	private static XProcXMLProcessingContext m_createWatchList = null;
	private static XProcXMLProcessingContext m_getWatchList = null;

	//get log4j handler
	private static final Logger logger = Logger.getLogger(RIDSender.class);


	public RIDSender() {
		super();
		// TODO Auto-generated constructor stub
	}


	public void setCreateReport (XProcXMLProcessingContext val) {
		m_createReport = val;
	}

	public void setGetReport (XProcXMLProcessingContext val) {
		m_getReport = val;
	}


	public void setCreateQuery (XProcXMLProcessingContext val) {
		m_createQuery = val;
	}

	public void setGetQuery (XProcXMLProcessingContext val) {
		m_getQuery = val;
	}

	public void setCreateWatchList (XProcXMLProcessingContext val) {
		m_createWatchList = val;
	}
	public void setGetWatchList (XProcXMLProcessingContext val) {
		m_getWatchList = val;
	}



	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView displayMainPage(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displayMainPage");
			}


			return new ModelAndView("index");
		} finally {
			; //TODO add finally handler
		}
	}



	/*************************************************************************
	 * 
	 * RID REPORTS
	 * 
	 *************************************************************************/

	@RequestMapping(method = RequestMethod.GET, value="/createReportForm")
	public ModelAndView displayCreateReportForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displayCreateReportForm");
			}

			return new ModelAndView("createReportForm", "report", new RIDReport());	
		} finally {
			; //TODO add finally handler
		}
	}



	@RequestMapping(method = RequestMethod.POST, value="/create/report")
	public String createReport(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("report")RIDReport report, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException, ServletException {
		try {

			if(logger.isDebugEnabled()){
				logger.debug("In createReport");
				logger.debug("The Report text=  " + report.getReportText());
			}			
			//request.getPart("reportText");
			PipelineInputCache pi = new PipelineInputCache();

			// supply the source for the resource Create pipeline as an InputStream  
			pi.setInputPort("source", new ByteArrayInputStream(report.getReportText().getBytes()));

			PipelineOutput output = m_createReport.executeOn(pi);

			return "createReportSuccess";

		} finally {
			; //TODO add finally handler
		}
	}



	@RequestMapping(method = RequestMethod.GET, value="/sendReportForm")
	public ModelAndView displaySendReportForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displaySendReportForm");
			}

			return new ModelAndView("sendReportForm", "params", new SendReportParams());	
		} finally {
			; //TODO add finally handler
		}
	}



	@RequestMapping(method = RequestMethod.POST, value="/send/report")
	public ModelAndView sendReport(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("params")SendReportParams params, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {

			Map<String,Object> postResponse = null;

			if(logger.isDebugEnabled()){
				logger.debug("In sendReport");	
				logger.debug("Report id = "+params.getId());
				logger.debug("Report destination = "+params.getDestination());
			}

			/* Get a report based on the id */

			PipelineInputCache pi = new PipelineInputCache();

			// pass the report ID into the pipeline for use in the xQuery (to look up the right report)
			pi.addParameter("xqueryParameters", new QName("id"), params.getId());


			// supply current resource URL as the base URL to craft hyperlinks
			//pi.addParameter("stylesheetParameters", new QName("baseURL"), request.getRequestURL().toString());


			//	SessionedPipelineOutput output = new SessionedPipelineOutput(m_getReport.executeOn(pi)); 
			PipelineOutput output = m_getReport.executeOn(pi);

			/* POST the report */
			postResponse = HTTPSender.httpSend(output,params.getDestination());

			return new ModelAndView("sendReportStatus","postResponse",postResponse);

		} finally {
			;  //TODO add finally handler
		}
	}



	/*************************************************************************
	 * 
	 * RID QUERIES
	 * 
	 *************************************************************************/


	@RequestMapping(method = RequestMethod.GET, value="/createQueryForm")
	public ModelAndView displayCreateQueryForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displayCreateQueryForm");
			}

			return new ModelAndView("createQueryForm", "query", new RIDQuery());	
		} finally {
			; //TODO add finally handler
		}
	}



	@RequestMapping(method = RequestMethod.POST, value="/create/query")
	public String createQuery(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("query")RIDQuery query, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {

			if(logger.isDebugEnabled()){
				logger.debug("In createQuery");
				logger.debug("Query text = " + query.getQueryText());
			}

			PipelineInputCache pi = new PipelineInputCache();

			// supply the source for the resource Create pipeline as an InputStream  
			pi.setInputPort("source", new ByteArrayInputStream(query.getQueryText().getBytes()));


			PipelineOutput output = m_createQuery.executeOn(pi);

			return "createQuerySuccess";

		} finally {
			;  //TODO add finally handler 
		}
	}



	@RequestMapping(method = RequestMethod.GET, value="/sendQueryForm")
	public ModelAndView displaySendQueryForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displaySendQueryForm");
			}

			return new ModelAndView("sendQueryForm", "params", new SendQueryParams());	
		} finally {
			; //TODO add finally handler
		}
	}



	@RequestMapping(method = RequestMethod.POST, value="/send/query")
	public ModelAndView sendQuery(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("params")SendQueryParams params, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {

			Map<String,Object> postResponse = null;

			if(logger.isDebugEnabled()){
				logger.debug("In sendQuery");	
				logger.debug("Id = "+params.getId());
				logger.debug("Destination = "+params.getDestination());
			}

			/* Get a report based on the id */
			PipelineInputCache pi = new PipelineInputCache();

			// pass the report ID into the pipeline for use in the xQuery (to look up the right report)
			pi.addParameter("xqueryParameters", new QName("id"), params.getId());


			// supply current resource URL as the base URL to craft hyperlinks
			//pi.addParameter("stylesheetParameters", new QName("baseURL"), request.getRequestURL().toString());

			PipelineOutput output = m_getQuery.executeOn(pi);

			/* POST the query */
			postResponse = HTTPSender.httpSend(output,params.getDestination());

			return new ModelAndView("sendQueryStatus","postResponse",postResponse);

		} finally {
			; //TODO add finally handler
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/createWatchListForm")
	public ModelAndView displaycreateWatchListForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displayCreateWatchListForm");
			}
			IncidentData ed=new IncidentData();			
			return new ModelAndView("createMalwareWatchListForm", "classCommand", ed);	
		} finally {
			; //TODO add finally handler
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="/create/WatchList")
	public String createWatchList(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("classCommand")IncidentData watchList, BindingResult result) throws IOException, DatatypeConfigurationException, ParserConfigurationException, JAXBException {
		try {


			if(logger.isDebugEnabled()){
				//ipv4address a=(ipv4address)watchList.getIpv4address().get(0);
				logger.debug("In createWatchList   ");
				logger.debug("Size of the Nodes   "+ watchList.getNode().size());
			}

			Document doc=JAXBBind.createWatchList(watchList);

			PipelineInputCache pi = new PipelineInputCache();
			pi.setInputPort("source",doc);
			PipelineOutput output = m_createWatchList.executeOn(pi);

		} finally {
			;  //TODO add finally handler 
		}
		return "createWatchListSuccess";
	}
	@RequestMapping(method = RequestMethod.GET, value="/createIndicatorsForm")
	public ModelAndView displaycreateIndicatorsForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displayCreateIndicatorsForm");
			}
			IncidentData ed=new IncidentData();			
			return new ModelAndView("createWatchListForm", "classCommand", ed);	
		} finally {
			; //TODO add finally handler
		}
	}
	@RequestMapping(method = RequestMethod.POST, value="/create/Indicators")
	public String createIndicators(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("classCommand")IncidentData watchList, BindingResult result) throws IOException, DatatypeConfigurationException, ParserConfigurationException, JAXBException {
		try {


			if(logger.isDebugEnabled()){
				logger.debug("In createIndicators");
				logger.debug("Size of the Nodes   "+ watchList.getNode().size());
			}
			Document doc=JAXBBind.createIndicators(watchList);
			PipelineInputCache pi = new PipelineInputCache();
			pi.setInputPort("source",doc);						
			PipelineOutput output = m_createWatchList.executeOn(pi);			

		} finally {
			;  //TODO add finally handler 
		}
		return "createWatchListSuccess";
	}

	@RequestMapping(method = RequestMethod.GET, value="/sendWatchListForm")
	public ModelAndView displaySendWatchListForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displaySendWatchListForm");
			}

			return new ModelAndView("sendWatchList", "params", new sendWatchList());	
		} finally {
			; //TODO add finally handler
		}
	}

	@RequestMapping(method = RequestMethod.POST, value="/send/WatchList")
	public ModelAndView sendWatchList(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("params")SendQueryParams params, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {

			Map<String,Object> postResponse = null;

			if(logger.isDebugEnabled()){
				logger.debug("In sendWatchList");	
				logger.debug("Id = "+params.getId());
				logger.debug("Destination = "+params.getDestination());
			}

			/* Get a report based on the id */
			PipelineInputCache pi = new PipelineInputCache();

			// pass the report ID into the pipeline for use in the xQuery (to look up the right report)
			pi.addParameter("xqueryParameters", new QName("id"), params.getId());


			// supply current resource URL as the base URL to craft hyperlinks
			//pi.addParameter("stylesheetParameters", new QName("baseURL"), request.getRequestURL().toString());

			PipelineOutput output = m_getWatchList.executeOn(pi);

			/* POST the query */
			postResponse = HTTPSender.httpSend(output,params.getDestination());

			return new ModelAndView("sendWatchListStatus","postResponse",postResponse);

		} finally {
			; //TODO add finally handler
		}
	}

	@RequestMapping(method = RequestMethod.GET, value="/createPhishingForm")
	public ModelAndView displaycreatePhishingForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displayCreateIndicatorsForm");
			}
			IncidentData ed=new IncidentData();			
			return new ModelAndView("createPhishingForm", "classCommand", ed);	
		} finally {
			; //TODO add finally handler
		}
	}
	@RequestMapping(method = RequestMethod.POST, value="/create/Phishing")
	public String createPhishing(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("classCommand")IncidentData watchList, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException, DatatypeConfigurationException, ParserConfigurationException, JAXBException {
		try {

			if(logger.isDebugEnabled()){
				logger.debug("In createPhishing");
				logger.debug("Size of the nodes   "+ watchList.getNode().size());
			}
			Document doc=JAXBBind.createPhishing(watchList);
			PipelineInputCache pi = new PipelineInputCache();
			pi.setInputPort("source",doc);						
			PipelineOutput output = m_createWatchList.executeOn(pi);			

		} finally {
			;  //TODO add finally handler 
		}
		return "createPhishingSuccess";
	}
	@RequestMapping(method = RequestMethod.GET, value="/createDDOSForm")
	public ModelAndView displaycreateDDOSForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displayCreateIndicatorsForm");
			}
			IncidentData ed=new IncidentData();			
			return new ModelAndView("createDDOSForm", "classCommand", ed);	
		} finally {
			; //TODO add finally handler
		}
	}
	@RequestMapping(method = RequestMethod.POST, value="/create/DDOS")
	public String createDDOS(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("classCommand")IncidentData watchList, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException, DatatypeConfigurationException, ParserConfigurationException, JAXBException {
		try {

			if(logger.isDebugEnabled()){
				logger.debug("In createDDOS");
				logger.debug("Size of the nodes   "+ watchList.getNode().size());
			}
			Document doc=JAXBBind.createDDOS(watchList);
			PipelineInputCache pi = new PipelineInputCache();
			pi.setInputPort("source",doc);
			PipelineOutput output = m_createWatchList.executeOn(pi);

		} finally {
			;  //TODO add finally handler 
		}

		return "createDDOSSuccess";
	}
	@RequestMapping(method = RequestMethod.GET, value="/viewWatchListForm")
	public ModelAndView displayViewWatchListForm(HttpServletRequest request, HttpServletResponse response, Model model) throws XProcException, IOException, URISyntaxException, TransformerException {
		//not implemented yet
		try {
			if(logger.isDebugEnabled()){
				logger.debug("In displaySendWatchListForm");
			}

			return new ModelAndView("viewReport", "params", new sendWatchList());	
		} finally {
			; //TODO add finally handler
		}
	}
	@RequestMapping(method = RequestMethod.POST, value="/listWatchList")
	public ModelAndView viewWatchList(HttpServletRequest request, HttpServletResponse response, Model model, @ModelAttribute("params")SendQueryParams params, BindingResult result) throws XProcException, IOException, URISyntaxException, TransformerException, JAXBException {
		try {

			if(logger.isDebugEnabled()){
				logger.debug("In viewWatchList");	
				logger.debug("Id = "+params.getId());
			}

			/* Get a report based on the id */
			PipelineInputCache pi = new PipelineInputCache();
			pi.addParameter("xqueryParameters", new QName("id"), params.getId());

			PipelineOutput output = m_getWatchList.executeOn(pi);
			IncidentData eventdata=new IncidentData();
			List<com.emc.documentum.xml.xproc.io.Source> sources = output.getSources(output.getPrimaryOutputPort());

			if (sources != null && !sources.isEmpty()) {
				// pipeline should only return a single value - we return the first as the output
				org.w3c.dom.Node node = sources.get(0).getNode();
				JAXBContext jc = JAXBContext.newInstance( "com.emc.cto.ridagent.rid.jaxb" );
				Unmarshaller u = jc.createUnmarshaller();
				JAXBElement element = (JAXBElement) u.unmarshal(node);
				RIDType d= (RIDType)element.getValue();
				RIDPolicyType policy=d.getRIDPolicy();
				if(policy.getReportSchema()!=null){
					ReportSchemaType rst=policy.getReportSchema();
					if(rst.getXMLDocument()!=null){
						ExtensionType et=rst.getXMLDocument();
						IODEFDocument document=(IODEFDocument)et.getContent().get(0);
						Incident incident=document.getIncident().get(0);
						if(incident.getDescription().size()!=0){
							MLStringType des=incident.getDescription().get(0);
							eventdata.setDescription(des.getValue());
						}
						if(incident.getReportTime()!=null)
							eventdata.setReporttime(incident.getReportTime().toString());
						if(incident.getStartTime()!=null)
							eventdata.setStarttime(incident.getStartTime().toString());
						if(incident.getEndTime()!=null)
							eventdata.setStoptime(incident.getEndTime().toString());
						if(incident.getDetectTime()!=null)
							eventdata.setDetecttime(incident.getDetectTime().toString());
						for (int eventCount=0;eventCount<incident.getEventData().size();eventCount++){
							EventData one=incident.getEventData().get(eventCount);
							Event nodeobj= new Event();		    	        
							eventdata.getNode().add(nodeobj);
							if(one.getMethod().size()!=0){
								Method methodobj=one.getMethod().get(0);
								Reference ref=(Reference)methodobj.getReferenceOrDescription().get(0);
								nodeobj.setRefName(ref.getReferenceName().getValue());
								nodeobj.setRefURL(ref.getURL().get(0));


							}
							boolean domainPresent=false;
							for(int flowindex=0;flowindex<one.getFlow().size();flowindex++){
								Flow flowobject=one.getFlow().get(flowindex);
								if(flowobject.getSystem().size()>0){
									com.emc.cto.ridagent.rid.jaxb.System sysobject=flowobject.getSystem().get(0);
									com.emc.cto.ridagent.rid.jaxb.Node nodes=sysobject.getNode().get(0);
									for(int addressindex=0;addressindex<nodes.getNodeNameOrDomainDataOrAddress().size();addressindex++){
										Object obj=nodes.getNodeNameOrDomainDataOrAddress().get(addressindex);
										if(obj instanceof DomainData ){
											domainPresent=true;
											break;
										}
									}

								}

								if(domainPresent){  //domaindata in flow element
									Flow flowobj=one.getFlow().get(flowindex);
									for(int systemindex=0;systemindex<flowobj.getSystem().size();systemindex++){
										com.emc.cto.ridagent.rid.jaxb.System sysobj=flowobj.getSystem().get(systemindex);
										PhishingData ip=new PhishingData();
										nodeobj.getPhishing().add(ip);
										ip.setSystemcategory(sysobj.getCategory());
										com.emc.cto.ridagent.rid.jaxb.Node nodes=sysobj.getNode().get(0);  //get the first node
										for(int addressindex=0;addressindex<nodes.getNodeNameOrDomainDataOrAddress().size();addressindex++){
											Object obj=nodes.getNodeNameOrDomainDataOrAddress().get(addressindex);
											if(obj instanceof Address){
												SystemData address=new SystemData();
												ip.getSystem().add(address);
												Address addressobj=(Address)obj;
												address.setType(addressobj.getCategory());
												address.setValue(addressobj.getValue());		    	        				
											}
											else if (obj instanceof MLStringType){
												SystemData address=new SystemData();
												ip.getSystem().add(address);
												MLStringType nodename=(MLStringType)obj;
												address.setType("Name");
												address.setValue(nodename.getValue());

											}
											else if(obj instanceof DomainData ){
												EmailInfo emailobj=new EmailInfo();
												ip.getEmailinfo().add(emailobj);
												DomainData domainobj=(DomainData)obj;
												emailobj.setDomain(domainobj.getName().getValue());
												emailobj.setDomaindate(domainobj.getDateDomainWasChecked().toString());
												for(int dnsindex=0;dnsindex<domainobj.getRelatedDNS().size();dnsindex++){
													RelatedDNSEntryType dnsobj=domainobj.getRelatedDNS().get(dnsindex);
													DNSRecord dnsrecordobj=new DNSRecord();
													emailobj.getDns().add(dnsrecordobj);
													dnsrecordobj.setType(dnsobj.getRecordType());
													dnsrecordobj.setValue(dnsobj.getValue());	

												}									
											}

										}
										for(int serviceindex=0;serviceindex<sysobj.getService().size();serviceindex++){
											Service serviceobj=sysobj.getService().get(serviceindex);
											if(serviceobj.getEmailInfo()!=null){
												EmailInfo emailinfoobj=ip.getEmailinfo().get(serviceindex);
												emailinfoobj.setEmailid(serviceobj.getEmailInfo().getEmail().getValue());
												emailinfoobj.setSubject(serviceobj.getEmailInfo().getEmailSubject().getValue());
												emailinfoobj.setMailerid(serviceobj.getEmailInfo().getXMailer().getValue());

											}
											else{
												SystemData address=ip.getSystem().get(serviceindex);
												address.setProtocolno(address.getProtocolno());
												address.setPortno(address.getPortno());
												if(serviceobj.getApplication()!=null){
													SoftwareType useragent=serviceobj.getApplication();
													address.setUseragent(useragent.getUserAgent());
												}	
											}

										}
										NodeRole noderole=nodes.getNodeRole().get(0); //get the only node role
										ip.setCategory(noderole.getCategory());
										if(noderole.getAttacktype()!=null)
											ip.setRole(noderole.getAttacktype().name());

									}

								}
								else{
									Flow flowobj=one.getFlow().get(flowindex);
									for(int systemindex=0;systemindex<flowobj.getSystem().size();systemindex++){
										com.emc.cto.ridagent.rid.jaxb.System sysobj=flowobj.getSystem().get(systemindex);
										NetworkInfo ip=new NetworkInfo();
										nodeobj.getAddress().add(ip);
										ip.setSystemcategory(sysobj.getCategory());
										for (int nodeindex=0;nodeindex<sysobj.getNode().size();nodeindex++){
											com.emc.cto.ridagent.rid.jaxb.Node nodes=sysobj.getNode().get(nodeindex);
											if(nodes.getNodeRole().size()!=0){
												NodeRole noderole=nodes.getNodeRole().get(0); //get the only node role
												ip.setCategory(noderole.getCategory());
												if(noderole.getAttacktype()!=null)
													ip.setRole(noderole.getAttacktype().name());
											}
											for(int addressindex=0;addressindex<nodes.getNodeNameOrDomainDataOrAddress().size();addressindex++){
												SystemData address=new SystemData();
												ip.getSystem().add(address);
												Object obj=nodes.getNodeNameOrDomainDataOrAddress().get(addressindex);
												if(obj instanceof Address){
													Address addressobj=(Address)obj;
													address.setType(addressobj.getCategory());
													address.setValue(addressobj.getValue());		    	        				
												}
												else if (obj instanceof MLStringType){
													MLStringType nodename=(MLStringType)obj;
													address.setType("Name");
													address.setValue(nodename.getValue());

												}

											}
										}
										for(int serviceindex=0;serviceindex<sysobj.getService().size();serviceindex++){
											Service serviceobj=sysobj.getService().get(serviceindex);
											SystemData address=ip.getSystem().get(serviceindex);
											address.setProtocolno(serviceobj.getIpProtocol());
											address.setPortno(serviceobj.getPort());
											if(serviceobj.getApplication()!=null){
												SoftwareType useragent=serviceobj.getApplication();
												address.setUseragent(useragent.getUserAgent());
											}							

										}

									}
								}

							}
							Record recordobj=one.getRecord();
							if(recordobj!=null){
								for(int recorddataindex=0;recorddataindex<recordobj.getRecordData().size();recorddataindex++){
									RecordData recorddataobj=recordobj.getRecordData().get(recorddataindex);
									for(int hashindex=0;hashindex<recorddataobj.getHashInformation().size();hashindex++){
										HashSigDetails hashobj=recorddataobj.getHashInformation().get(hashindex);
										if(hashobj.getSignature().size()!=0){
											DigitalSig signatureobj=new DigitalSig();
											nodeobj.getDsig().add(signatureobj);
											signatureobj.setType(hashobj.getType());
											signatureobj.setValidity(hashobj.isValid().toString());
											SignatureType signature=hashobj.getSignature().get(0);  //get the signature
											SignedInfoType signedinfo= signature.getSignedInfo();
											signatureobj.setCan_method(signedinfo.getCanonicalizationMethod().getAlgorithm());
											signatureobj.setSignature_method(signedinfo.getSignatureMethod().getAlgorithm());
											ReferenceType reference=signedinfo.getReference().get(0);
											signatureobj.setHash_type(reference.getDigestMethod().getAlgorithm());
											signatureobj.setHash_value(reference.getDigestValue());
											signatureobj.setSignature_value(signature.getSignatureValue().getValue());									

										}
										else{
											Hash hash=new Hash();
											nodeobj.getHash().add(hash);
											hash.setType(hashobj.getType());
											for(int fileindex=0;fileindex<hashobj.getFileName().size();fileindex++){
												MLStringType fileinfo=hashobj.getFileName().get(fileindex);
												FileData filedataobj=new FileData();
												hash.getFile().add(filedataobj);
												filedataobj.setFilename(fileinfo.getValue());
											}

											for(int anotherhashindex=0;anotherhashindex<hashobj.getReference().size();anotherhashindex++){
												ReferenceType referenceobj=hashobj.getReference().get(anotherhashindex);
												HashData hashvalueobj=new HashData();
												hash.getValue().add(hashvalueobj);
												DigestMethodType digestobj=referenceobj.getDigestMethod();
												hashvalueobj.setHash_type(digestobj.getAlgorithm());
												hashvalueobj.setValue(referenceobj.getDigestValue());									
											}
										}


									}
									for(int registryindex=0;registryindex<recorddataobj.getWindowsRegistryKeysModified().size();registryindex++){
										RegistryKeyModified rkmobj=recorddataobj.getWindowsRegistryKeysModified().get(registryindex);
										for(int keyindex=0;keyindex<rkmobj.getKey().size();keyindex++){
											Key key=rkmobj.getKey().get(keyindex);
											RegistryValues registryvaluesobj=new RegistryValues();
											nodeobj.getRegistry().add(registryvaluesobj);
											registryvaluesobj.setAction(key.getRegistryaction());
											registryvaluesobj.setKey(key.getKeyName());
											registryvaluesobj.setValue(key.getValue());

										}							

									}


								}

							}
						}
					}
				}

			}

			return new ModelAndView("viewWebReport","eventdata",eventdata);

		} finally {
			; //TODO add finally handler
		}
	}

}


