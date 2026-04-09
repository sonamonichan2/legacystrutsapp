/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.tools.doclet;

/**
 * Utility class for @should annotations.
 * 
 * Previously implemented com.sun.tools.doclets.Taglet which was removed in Java 9+.
 * This class is retained for backward compatibility but no longer functions as a
 * Javadoc taglet. The @should tag processing was used to generate "Expected Behavior"
 * documentation in Javadoc output.
 */
public class ShouldTaglet {
	
	private static final String NAME = "should";
	
	private static final String HEADER = "Expected Behavior:";
	
	/**
	 * Return the name of this custom tag.
	 */
	public String getName() {
		return NAME;
	}
	
	/**
	 * Return the header text for this taglet.
	 */
	public String getHeader() {
		return HEADER;
	}
	
	/**
	 * Format a single should tag text into HTML.
	 *
	 * @param text the text of the should tag.
	 * @return HTML formatted string.
	 */
	public String formatTag(String text) {
		return "\n<DT><B>" + HEADER + "</B></DT>\n  <DD>Should " + text + "</DD>";
	}
	
	/**
	 * Format an array of should tag texts into HTML.
	 *
	 * @param texts the array of should tag texts.
	 * @return HTML formatted string or null if empty.
	 */
	public String formatTags(String[] texts) {
		if (texts == null || texts.length == 0) {
			return null;
		}
		StringBuilder result = new StringBuilder("\n<DT><B>").append(HEADER).append("</B></DT>");
		for (String text : texts) {
			result.append("\n  <DD>Should ").append(text).append("</DD>");
		}
		return result.toString();
	}
}
