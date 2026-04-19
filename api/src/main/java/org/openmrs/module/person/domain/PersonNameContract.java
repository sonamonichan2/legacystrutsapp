/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.person.domain;

import org.openmrs.module.sharedkernel.domain.OpenmrsDataContract;

/**
 * Person module domain contract extracted from {@link org.openmrs.PersonName}.
 *
 * <p>Defines the public API contract for PersonName entities within the person module.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.PersonName
 */
public interface PersonNameContract extends OpenmrsDataContract {

	/**
	 * @return the given name (first name)
	 */
	String getGivenName();

	/**
	 * @param givenName the given name to set
	 */
	void setGivenName(String givenName);

	/**
	 * @return the middle name
	 */
	String getMiddleName();

	/**
	 * @param middleName the middle name to set
	 */
	void setMiddleName(String middleName);

	/**
	 * @return the family name (last name)
	 */
	String getFamilyName();

	/**
	 * @param familyName the family name to set
	 */
	void setFamilyName(String familyName);

	/**
	 * @return the prefix (e.g., Mr., Mrs.)
	 */
	String getPrefix();

	/**
	 * @param prefix the prefix to set
	 */
	void setPrefix(String prefix);

	/**
	 * @return whether this is the preferred name
	 */
	Boolean getPreferred();

	/**
	 * @param preferred whether this is the preferred name
	 */
	void setPreferred(Boolean preferred);
}
