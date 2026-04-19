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

import java.util.Collection;
import java.util.Locale;

import org.openmrs.module.sharedkernel.domain.OpenmrsMetadataContract;

/**
 * Concepts module domain contract extracted from {@link org.openmrs.Concept}.
 *
 * <p>Defines the public API contract for Concept entities. Concept is the central
 * type in the OpenMRS data model for clinical terminology.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.Concept
 */
public interface ConceptContract extends OpenmrsMetadataContract {

	/**
	 * @return the conceptId
	 */
	Integer getConceptId();

	/**
	 * @param conceptId the conceptId to set
	 */
	void setConceptId(Integer conceptId);

	/**
	 * @return the concept's display string (preferred name in the current locale)
	 */
	String getDisplayString();

	/**
	 * @return whether this concept is set (a grouping concept)
	 */
	Boolean getSet();

	/**
	 * @param set whether this concept is a set
	 */
	void setSet(Boolean set);

	/**
	 * @return the version of this concept
	 */
	String getVersion();

	/**
	 * @param version the version to set
	 */
	void setVersion(String version);
}
