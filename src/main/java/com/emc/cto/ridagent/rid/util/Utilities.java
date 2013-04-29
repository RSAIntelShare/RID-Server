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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

public class Utilities {


public Utilities() {
		super();
		// TODO Auto-generated constructor stub
	}

public static String nodeToString(Node node) {
	StringWriter sw = new StringWriter();
	try {
	Transformer t = TransformerFactory.newInstance().newTransformer();
	t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	t.transform(new DOMSource(node), new StreamResult(sw));
	} catch (TransformerException te) {
	System.out.println("nodeToString Transformer Exception");
	}
	return sw.toString();
	}


public static InputStream stringToInputStream(String str){
	
	System.out.println(str);
	
	InputStream is = new ByteArrayInputStream(str.getBytes());
		
	return is;
}

}
