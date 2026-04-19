/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.location.domain;

import org.openmrs.module.sharedkernel.domain.OpenmrsMetadataContract;

/**
 * Location module domain contract extracted from {@link org.openmrs.LocationTag}.
 *
 * <p>Defines the public API contract for LocationTag entities within the location module
 * boundary. LocationTags are used to categorize locations (e.g., "Login Location",
 * "Visit Location", "Transfer Location").</p>
 *
 * @since 2.1.0
 * @see org.openmrs.LocationTag
 */
public interface LocationTagContract extends OpenmrsMetadataContract {

	/**
	 * @return the locationTagId
	 */
	Integer getLocationTagId();

	/**
	 * @param locationTagId the locationTagId to set
	 */
	void setLocationTagId(Integer locationTagId);
}
