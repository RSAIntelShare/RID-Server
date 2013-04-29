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

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;


public class MyNamespacePrefixMapper extends NamespacePrefixMapper {


	/*private static final String FOO_PREFIX = ""; // DEFAULT NAMESPACE
	private static final String FOO_URI = "http://www.example.com/FOO";*/
	private static final String DS_PREFIX = "ds";
	private static final String DS_URI = "http://www.w3.org/2000/09/xmldsig#";
	@Override
	public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
		if(DS_URI.equals(namespaceUri)) {
			return DS_PREFIX;
		}
		return suggestion;
	}
	@Override
	public String[] getPreDeclaredNamespaceUris() {
		return new String[] { DS_URI };
	}
}
