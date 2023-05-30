/*[INCLUDE-IF SharedClasses]*/
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
package com.ibm.oti.shared;

/**
 * <p>SharedClassHelper API that stores and finds classes using String tokens.</p>
 * <h2>Description</h2>
 * <p>A SharedClassTokenHelper is obtained by calling getTokenHelper(ClassLoader) on a SharedClassHelperFactory.</p>
 * <p>The SharedClassTokenHelper, which is the most simple type of helper, uses generated String tokens to find and store classes.</p>
 * <h2>Usage</h2>
 * <p>The ClassLoader should call findSharedClass after looking in its local cache and asking its parent (if one exists).
 * If findSharedClass does not return null, the ClassLoader calls defineClass on the byte[] that is returned.</p>
 * <p>The ClassLoader calls storeSharedClass immediately after a class is defined, unless the class that is being defined was loaded from the shared cache.</p>
 * <p>The ClassLoader is responsible for coordinating the creation of token Strings.</p>
 * <h2>Dynamic cache updates.</h2>
 * <p>Because the shared cache persists beyond the lifetime of a JVM, classes in the shared cache can become out of date (stale).
 * Using this helper, it is entirely the responsibility of the ClassLoader to ensure that cache entries are kept up-to-date.
 * Tokens have no meaning to the cache, so effectively turn the it into a dictionary of classes.</p>
 * <p>For example, a token may be the location where the class was found, combined with some type of versioning data.</p>
 * <p>If a ClassLoader stores multiple versions of the same class by using the same token, only the most recent will be returned by findSharedClass. </p>
 * <h2>Security</h2>
 * <p>A SharedClassHelper will only allow classes to be stored in the cache which were defined by the ClassLoader that owns the SharedClassHelper.</p>
 * <p>If a SecurityManager is installed, SharedClassPermissions must be used to permit read/write access to the shared class cache.
 * Permissions are granted by ClassLoader classname in the java.policy file and are fixed when the SharedClassHelper is created.</p>
 * <p>Note also that if the createClassLoader RuntimePermission is not granted, ClassLoaders cannot be created,
 * which in turn means that SharedClassHelpers cannot be created.</p>
 * <h2>Compatibility with other SharedClassHelpers</h2>
 * <p>Classes stored using the SharedClassTokenHelper cannot be retrieved using any other type of helper, and vice versa.  </p>
 *
 * @see SharedClassHelper
 * @see SharedClassHelperFactory
 * @see SharedClassPermission
 */
public interface SharedClassTokenHelper extends SharedClassHelper {

	/**
	 * Finds a class in the shared cache by using a specific token and class name.<p>
	 * A class will be returned only for an exact String match of both the token and class name.
	 * Otherwise, null is returned.<br>
	 * To obtain an instance of the class, the byte[] returned must be passed to defineClass by the caller ClassLoader.
	 *
	 * @param token String a token generated by the ClassLoader
	 * @param className String the name of the class to be found
	 * @return byte[] a byte array describing the class found, or null
	 */
	public byte[] findSharedClass(String token, String className);

	/**
	 * Stores a class in the shared cache by using a specific token.<p>
	 * The class that is being stored must have been defined by the caller ClassLoader.<br>
	 * Returns <code>true</code> if the class is stored successfully or false otherwise.<br>
	 * Will return <code>false</code> if the class that is being stored was not defined by the caller ClassLoader.
	 *
	 * @param token String a token generated by the ClassLoader
	 * @param clazz Class the class to store in the shared cache
	 * @return boolean true if the class was stored successfully, false otherwise
	 */
	public boolean storeSharedClass(String token, Class<?> clazz);

}
