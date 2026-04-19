/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.concepts.api;

import java.util.List;
import java.util.Locale;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.ConceptSource;
import org.openmrs.Drug;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Concepts module service contract extracted from {@link org.openmrs.api.ConceptService}.
 *
 * <p>Defines the public service API for the concepts module. ConceptService is the most
 * self-contained service, referencing only Concept* and Drug* types.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.ConceptService
 */
public interface ConceptModuleService extends ServiceContract {

	/**
	 * Saves a concept.
	 *
	 * @param concept the concept to save
	 * @return the saved concept
	 * @throws APIException if an error occurs
	 */
	Concept saveConcept(Concept concept) throws APIException;

	/**
	 * Gets a concept by its internal database identifier.
	 *
	 * @param conceptId the concept identifier
	 * @return the concept, or null if not found
	 * @throws APIException if an error occurs
	 */
	Concept getConcept(Integer conceptId) throws APIException;

	/**
	 * Gets a concept by its UUID.
	 *
	 * @param uuid the UUID
	 * @return the concept, or null if not found
	 * @throws APIException if an error occurs
	 */
	Concept getConceptByUuid(String uuid) throws APIException;

	/**
	 * Gets concepts matching a search query.
	 *
	 * @param query the search query
	 * @return list of matching concepts
	 * @throws APIException if an error occurs
	 */
	List<Concept> getConceptsByName(String query) throws APIException;

	/**
	 * Gets all concept classes.
	 *
	 * @return list of all concept classes
	 * @throws APIException if an error occurs
	 */
	List<ConceptClass> getAllConceptClasses() throws APIException;

	/**
	 * Gets all concept datatypes.
	 *
	 * @return list of all concept datatypes
	 * @throws APIException if an error occurs
	 */
	List<ConceptDatatype> getAllConceptDatatypes() throws APIException;

	/**
	 * Gets all concept sources.
	 *
	 * @return list of all concept sources
	 * @throws APIException if an error occurs
	 */
	List<ConceptSource> getAllConceptSources() throws APIException;

	/**
	 * Saves a drug.
	 *
	 * @param drug the drug to save
	 * @return the saved drug
	 * @throws APIException if an error occurs
	 */
	Drug saveDrug(Drug drug) throws APIException;

	/**
	 * Gets a drug by its identifier.
	 *
	 * @param drugId the drug identifier
	 * @return the drug, or null if not found
	 * @throws APIException if an error occurs
	 */
	Drug getDrug(Integer drugId) throws APIException;

	/**
	 * Gets all drugs.
	 *
	 * @param includeRetired whether to include retired drugs
	 * @return list of all drugs
	 * @throws APIException if an error occurs
	 */
	List<Drug> getAllDrugs(boolean includeRetired) throws APIException;

	/**
	 * Retires a concept.
	 *
	 * @param concept the concept to retire
	 * @param reason the reason for retiring
	 * @return the retired concept
	 * @throws APIException if an error occurs
	 */
	Concept retireConcept(Concept concept, String reason) throws APIException;

	/**
	 * Permanently deletes a concept.
	 *
	 * @param concept the concept to purge
	 * @throws APIException if an error occurs
	 */
	void purgeConcept(Concept concept) throws APIException;
}
