/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.sharedkernel.domain;

import java.util.Date;

/**
 * Shared-kernel contract interface extracted from {@link org.openmrs.Voidable}.
 *
 * <p>When data can be removed (effectively deleted from the user's perspective), then they
 * are voidable. Voided data are no longer valid and references from other non-voided data
 * are not valid.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.Voidable
 */
public interface VoidableContract extends OpenmrsObjectContract {

	/**
	 * @return Boolean - whether or not this object is voided
	 */
	Boolean getVoided();

	/**
	 * @param voided - whether or not this object is voided
	 */
	void setVoided(Boolean voided);

	/**
	 * @return Date - the date the object was voided
	 */
	Date getDateVoided();

	/**
	 * @param dateVoided - the date the object was voided
	 */
	void setDateVoided(Date dateVoided);

	/**
	 * @return String - the reason the object was voided
	 */
	String getVoidReason();

	/**
	 * @param voidReason - the reason the object was voided
	 */
	void setVoidReason(String voidReason);
}
