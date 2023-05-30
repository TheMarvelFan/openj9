/*******************************************************************************
 * Copyright IBM Corp. and others 2001
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] https://openjdk.org/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0-only WITH Classpath-exception-2.0 OR GPL-2.0-only WITH OpenJDK-assembly-exception-1.0
 *******************************************************************************/
package com.ibm.uma.om.parser;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ibm.uma.UMAException;
import com.ibm.uma.om.Object;
import com.ibm.uma.om.Objects;

public class ObjectsParser {
	static public Objects parse(Node node, String containingFile) throws UMAException {
		Objects objects = new Objects(containingFile);

		NamedNodeMap attributes = node.getAttributes();
		objects.setGroup(attributes.getNamedItem("group").getNodeValue());

		NodeList nodeList = node.getChildNodes();
		for ( int j=0; j<nodeList.getLength(); j++ ) {
			Node item = nodeList.item(j);
			if ( item.getNodeName().equalsIgnoreCase("object") ) {
				Object object = ObjectParser.parse(item, containingFile);
				objects.addObject(object);
			} else if (item.getNodeName().equalsIgnoreCase("group")) {
				NamedNodeMap attr = item.getAttributes();
				String objectGroupName = attr.getNamedItem("name").getNodeValue();
				Object object = new Object(objectGroupName, containingFile);
				objects.addObject(object);
			}
		}		
		Parser.populatePredicateList(nodeList, objects);
		return objects;
	}
}
