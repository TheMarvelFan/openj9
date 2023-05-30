/*[INCLUDE-IF CRIU_SUPPORT]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2021
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
package org.eclipse.openj9.criu;

/**
 * An exception representing a JVM failure after restore.
 */
public final class JVMRestoreException extends JVMCRIUException {
	private static final long serialVersionUID = 1539393473417716292L;

	/**
	 * Creates a JVMRestoreException with the specified message and a default error code.
	 *
	 * @param message   the message
	 */
	public JVMRestoreException(String message) {
		super(message, 0);
	}

	/**
	 * Creates a JVMRestoreException with the specified message and error code.
	 *
	 * @param message   the message
	 * @param errorCode the error code
	 */
	public JVMRestoreException(String message, int errorCode) {
		super(message, errorCode);
	}

	/**
	 * Creates a JVMRestoreException with the specified message and error code.
	 *
	 * @param message   the message
	 * @param errorCode the error code
	 * @param causedBy  throwable that caused the exception
	 */
	public JVMRestoreException(String message, int errorCode, Throwable causedBy) {
		super(message, errorCode, causedBy);
	}
}
