/*[INCLUDE-IF Sidecar16]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2003
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
package com.ibm.oti.vm;

import java.util.*;
import java.io.IOException;
import java.io.InputStream;

/*[IF Sidecar19-SE]
import jdk.internal.reflect.CallerSensitive;
/*[ELSE]*/
import sun.reflect.CallerSensitive;
/*[ENDIF]*/

/**
 * This class contains helper methods for loading resource
 * bundles and formatting external message strings.
 * 
 * @author		OTI
 * @version		initial
 */
public final class MsgHelp {

/**
 * Generates a formatted text string given a source string
 * containing "argument markers" of the form "{argNum}" 
 * where each argNum must be in the range 0..9. The result
 * is generated by inserting the toString of each argument
 * into the position indicated in the string.
 * <p>
 * To insert the "{" character into the output, use a single
 * backslash character to escape it (i.e. "\{"). The "}"
 * character does not need to be escaped.
 *
 * @param       format String   
 *                  the format to use when printing.
 * @param       args Object[]
 *                  the arguments to use.
 * @return      String
 *                  the formatted message.
 */
public static String format (String format, Object[] args) {
    return format(format, null, args);
}

/**
 * Similar to format(String, Object[]) but taking just a 
 * single argument.
 *
 * @param       format String   
 *                  the format to use when printing.
 * @param       arg Object
 *                  the argument to use.
 * @return      String
 *                  the formatted message.
 */
public static String format (String format, Object arg) {
    if(arg == null) {
        arg = "<null>"; //$NON-NLS-1$
    }
    return format(format, arg, null);
}

/**
 * Generates the formatted text string for 
 * format(String, Object[]) or format(String, Object)
 * 
 * @param format 
 *                  the format to use when printing.
 * @param singleArg 
 *                  the single arg, or null if multipleArgs is non-null.
 * @param multipleArgs 
 *                  If singleArg is null, then multipleArgs specifies multiple args. 
 *                  Otherwise multipleArgs is ignored.
 * @return the formatted message
 */
private static String format (String format, Object singleArg, Object[] multipleArgs) {
    boolean hasSingleArg = singleArg != null;
    int argLength = 0;
    String argString = null;
    String[] argStrings = null;
    if(hasSingleArg) {
        argLength = 1;
        argString = singleArg.toString();
    } else if(multipleArgs != null) {
        argLength = multipleArgs.length;
        argStrings = new String[argLength];
        for (int i = 0; i < argLength; ++i) {
            if (multipleArgs[i] == null)
                argStrings[i] = "<null>"; //$NON-NLS-1$
            else
                argStrings[i] = multipleArgs[i].toString();
        }
    }
	/*[PR 110011] StringBuffer not created with initial size */
	StringBuilder answer = new StringBuilder(format.length() + (argLength * 20));
   
	int lastI = 0;
	for (int i = format.indexOf('{', 0); i >= 0; i = format.indexOf('{', lastI)) {
		if (i != 0 && format.charAt(i-1) == '\\') {
			// It's escaped, just print and loop.
			if (i != 1)
				answer.append(format.substring(lastI,i-1));
			answer.append('{');
			lastI = i+1;
		} else {
			// It's a format character.
			if (i > format.length()-3) {
				// Bad format, just print and loop.
				answer.append(format.substring(lastI, format.length()));
				lastI = format.length();
			} else {
				int argnum = (byte) Character.digit(format.charAt(i+1), 10);
				if (argnum < 0 || format.charAt(i+2) != '}') {
					// Bad format, just print and loop.
					answer.append(format.substring(lastI, i+1));
					lastI = i+1;
				} else {
                    // Got a good one!
                    String sub = format.substring(lastI, i);
                    answer.append(sub);
                    if (argnum >= argLength) {
						answer.append("<missing argument>"); //$NON-NLS-1$
                    } else if(hasSingleArg) {
                        answer.append(argString);
                    } else {
						answer.append(argStrings[argnum]);
                    }
					lastI = i + 3;
                }
			}
		}
	}
	if (lastI < format.length())
		answer.append(format.substring(lastI, format.length()));
	return answer.toString();
}

// Loads properties from the specified resource. The properties are of
// the form <code>key=value</code>, one property per line.
/*[IF]*/
// This is taken from java.util.Properties#load(InputStream).
/*[ENDIF]*/
@CallerSensitive
public static Hashtable loadMessages(String resourceName) throws IOException {
	InputStream resourceStream;
	String language, region, variant;
	String resName;
	Properties props = new Properties();

	Locale defLocale = Locale.getDefault();
	language = defLocale.getLanguage();
	if (language.length() == 0) language = "en"; //$NON-NLS-1$
	region = defLocale.getCountry();
	if (region.length() == 0) region = "US"; //$NON-NLS-1$
	variant = defLocale.getVariant();
/*[IF !Sidecar19-SE]
	final ClassLoader loader = com.ibm.oti.vm.VM.getStackClassLoader(1);
	if (null == loader) {
		return null;
	}
/*[ENDIF]*/
/*[IF Sidecar19-SE]*/
	Module module = MsgHelp.class.getModule();
	if (module == null) {
		/* Too early in bootstrap - modules not initialized yet */
		return null;
	}
/*[ENDIF]*/

	if (variant.length() > 0) {
		resName = resourceName + "_" + language + "_" + region + "_" + variant + ".properties"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
/*[IF Sidecar19-SE]
		resourceStream = module.getResourceAsStream(resName);
/*[ELSE]*/
		resourceStream = loader.getResourceAsStream(resName);
/*[ENDIF]*/
		if (resourceStream != null) {
			props.load(resourceStream);
			/*[PR CMVC 182098] Perf: zWAS ftprint regressed 6% Java7 vs 626FP1 (MsgHelp) */
			resourceStream.close();
			return props;
		}
	}
	
	resName = resourceName + "_" + language + "_" + region + ".properties"; //$NON-NLS-1$  //$NON-NLS-2$ //$NON-NLS-3$
/*[IF Sidecar19-SE]
	resourceStream = module.getResourceAsStream(resName);
/*[ELSE]*/
	resourceStream = loader.getResourceAsStream(resName);
/*[ENDIF]*/
	if (resourceStream != null) {
		props.load(resourceStream);
		resourceStream.close();
		return props;
	}

	resName = resourceName + "_" + language + ".properties"; //$NON-NLS-1$  //$NON-NLS-2$
/*[IF Sidecar19-SE] 
	resourceStream = module.getResourceAsStream(resName);
/*[ELSE]*/
	resourceStream = loader.getResourceAsStream(resName);
/*[ENDIF]*/
	if (resourceStream != null) {
		props.load(resourceStream);
		resourceStream.close();
		return props;
	}

/*[IF Sidecar19-SE] 
	resourceStream = module.getResourceAsStream(resourceName + ".properties"); //$NON-NLS-1$	
/*[ELSE]*/
	resourceStream = loader.getResourceAsStream(resourceName + ".properties"); //$NON-NLS-1$
/*[ENDIF]*/
	if (resourceStream != null) {
		props.load(resourceStream);
		resourceStream.close();
		return props;
	} else {
		return null;		
	}
}

}
