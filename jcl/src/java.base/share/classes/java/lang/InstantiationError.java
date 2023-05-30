/*[INCLUDE-IF Sidecar16]*/

package java.lang;

/*******************************************************************************
 * Copyright IBM Corp. and others 1998
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
 
/**
 * This error is thrown when the VM notices that a
 * an attempt is being made to create a new instance
 * of a class which has no visible constructors from
 * the location where new is invoked.
 * <p>
 * Note that this can only occur when inconsistent
 * class files are being loaded.
 *
 * @author		OTI
 * @version		initial
 */
public class InstantiationError extends IncompatibleClassChangeError {
	private static final long serialVersionUID = -4885810657349421204L;
	
/**
 * Constructs a new instance of this class with its 
 * walkback filled in.
 *
 * @author		OTI
 * @version		initial
 */
public InstantiationError () {
	super();
}

/**
 * Constructs a new instance of this class with its 
 * walkback and message filled in.
 *
 * @author		OTI
 * @version		initial
 *
 * @param		detailMessage String
 *				The detail message for the exception.
 */
public InstantiationError (String detailMessage) {
	super(detailMessage);
}

/**
 * Constructs a new instance of this class with its 
 * walkback and message filled in.
 *
 * @author		OTI
 * @version		initial
 *
 * @param		clazz Class
 *				The class which cannot be instantiated.
 */
InstantiationError (Class clazz) {
	super(clazz.getName());
}

}
