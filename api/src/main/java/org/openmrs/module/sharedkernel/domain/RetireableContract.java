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
 * Shared-kernel contract interface extracted from {@link org.openmrs.Retireable}.
 *
 * <p>When existing data remain valid but should no longer be used for new entries, they are
 * retired. Typically this applies to metadata. For example, an encounter form type or a
 * patient attribute type may no longer be valid but cannot be removed because there are
 * data previously collected using these metadata.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.Retireable
 */
public interface RetireableContract extends OpenmrsObjectContract {

	/**
	 * @return Boolean - whether or not this object is retired
	 */
	Boolean getRetired();

	/**
	 * @param retired - whether or not this object is retired
	 */
	void setRetired(Boolean retired);

	/**
	 * @return Date - the date the object was retired
	 */
	Date getDateRetired();

	/**
	 * @param dateRetired - the date the object was retired
	 */
	void setDateRetired(Date dateRetired);

	/**
	 * @return String - the reason the object was retired
	 */
	String getRetireReason();

	/**
	 * @param retireReason - the reason the object was retired
	 */
	void setRetireReason(String retireReason);
}
