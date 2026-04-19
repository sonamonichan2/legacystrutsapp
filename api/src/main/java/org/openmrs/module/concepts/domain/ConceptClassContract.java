/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.concepts.domain;

import org.openmrs.module.sharedkernel.domain.OpenmrsMetadataContract;

/**
 * Concepts module domain contract extracted from {@link org.openmrs.ConceptClass}.
 *
 * <p>Defines the public API contract for ConceptClass entities (e.g., Test, Drug, Diagnosis).</p>
 *
 * @since 2.1.0
 * @see org.openmrs.ConceptClass
 */
public interface ConceptClassContract extends OpenmrsMetadataContract {

	/**
	 * @return the conceptClassId
	 */
	Integer getConceptClassId();

	/**
	 * @param conceptClassId the conceptClassId to set
	 */
	void setConceptClassId(Integer conceptClassId);
}
