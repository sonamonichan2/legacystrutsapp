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
 * Shared-kernel contract interface extracted from {@link org.openmrs.OpenmrsObject}.
 *
 * <p>This is the base contract for all OpenMRS-defined persistent objects within the
 * modular-monolith architecture. Modules should depend on this contract interface
 * rather than the concrete {@code org.openmrs.OpenmrsObject} interface.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.OpenmrsObject
 */
public interface OpenmrsObjectContract {

	/**
	 * @return id - The unique Identifier for the object
	 */
	Integer getId();

	/**
	 * @param id - The unique Identifier for the object
	 */
	void setId(Integer id);

	/**
	 * @return the universally unique id for this object
	 */
	String getUuid();

	/**
	 * @param uuid a universally unique id for this object
	 */
	void setUuid(String uuid);
}
