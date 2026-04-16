/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.util;

import org.junit.Test;

import org.openmrs.api.APIException;

public class JavaVersionTest {
	
	/**
	 * @see org.openmrs.util.OpenmrsUtil#validateJavaVersion()
	 */
	@Test
	public void validateJavaVersion_shouldPassIfTheCurrentJVMVersionIsLaterThanJava5() {
		// With Java 21, this should always pass since we're well above Java 6
		OpenmrsUtil.validateJavaVersion();
	}
	
	/**
	 * @see org.openmrs.util.OpenmrsUtil#validateJavaVersion()
	 */
	@Test(expected = APIException.class)
	public void validateJavaVersion_shouldFailIfTheCurrentJVMVersionIsEarlierThanJava6() {
		// Simulate old Java version by temporarily setting system property
		String originalVersion = System.getProperty("java.version");
		try {
			System.setProperty("java.version", "1.5.0_20");
			OpenmrsUtil.validateJavaVersion();
		} finally {
			System.setProperty("java.version", originalVersion);
		}
	}
}
