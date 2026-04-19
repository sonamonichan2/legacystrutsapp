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
 * Shared-kernel contract interface extracted from {@link org.openmrs.Auditable}.
 *
 * <p>In OpenMRS, the convention is to track basic audit information for each object related
 * to who initially created the object and when, and who last changed the object and when.
 * This contract defines the audit trail methods without referencing the concrete User type.</p>
 *
 * <p>Note: Unlike the original {@code Auditable} interface which uses the concrete {@code User}
 * type, this contract uses {@code Integer} for creator/changedBy IDs to avoid cross-module
 * coupling. Adapter implementations can resolve these to actual User objects.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.Auditable
 */
public interface AuditableContract extends OpenmrsObjectContract {

	/**
	 * @return the date the object was created
	 */
	Date getDateCreated();

	/**
	 * @param dateCreated - the date the object was created
	 */
	void setDateCreated(Date dateCreated);

	/**
	 * @return the date the object was last changed
	 */
	Date getDateChanged();

	/**
	 * @param dateChanged - the date the object was last changed
	 */
	void setDateChanged(Date dateChanged);
}
