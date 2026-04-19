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
 * Concepts module domain contract extracted from {@link org.openmrs.Drug}.
 *
 * <p>Defines the public API contract for Drug entities within the concepts module.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.Drug
 */
public interface DrugContract extends OpenmrsMetadataContract {

	/**
	 * @return the drugId
	 */
	Integer getDrugId();

	/**
	 * @param drugId the drugId to set
	 */
	void setDrugId(Integer drugId);

	/**
	 * @return whether this drug is a combination drug
	 */
	Boolean getCombination();

	/**
	 * @param combination whether this is a combination drug
	 */
	void setCombination(Boolean combination);

	/**
	 * @return the strength of the drug
	 */
	String getStrength();

	/**
	 * @param strength the strength to set
	 */
	void setStrength(String strength);
}
