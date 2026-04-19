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

/**
 * Shared-kernel contract interface extracted from {@link org.openmrs.OpenmrsMetadata}.
 *
 * <p>In OpenMRS, metadata represent system and descriptive data such as data types — a
 * relationship type or encounter type. Metadata are generally referenced by clinical data
 * but don't represent patient-specific data themselves. This contract combines the base
 * object, auditable, and retireable contracts, plus name/description.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.OpenmrsMetadata
 */
public interface OpenmrsMetadataContract extends OpenmrsObjectContract, AuditableContract, RetireableContract {

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @param name the name to set
	 */
	void setName(String name);

	/**
	 * @return the description
	 */
	String getDescription();

	/**
	 * @param description the description to set
	 */
	void setDescription(String description);
}
