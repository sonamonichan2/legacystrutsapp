/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.person.api;

import java.util.List;
import java.util.Set;

import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Person module service contract extracted from {@link org.openmrs.api.PersonService}.
 *
 * <p>Defines the public service API for the person module boundary. This interface
 * declares person-specific operations.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.PersonService
 */
public interface PersonModuleService extends ServiceContract {

	/**
	 * Saves a person to the database.
	 *
	 * @param person the person to save
	 * @return the saved person
	 * @throws APIException if an error occurs
	 */
	Person savePerson(Person person) throws APIException;

	/**
	 * Gets a person by their internal database identifier.
	 *
	 * @param personId the person identifier
	 * @return the person, or null if not found
	 * @throws APIException if an error occurs
	 */
	Person getPerson(Integer personId) throws APIException;

	/**
	 * Gets a person by their UUID.
	 *
	 * @param uuid the UUID
	 * @return the person, or null if not found
	 * @throws APIException if an error occurs
	 */
	Person getPersonByUuid(String uuid) throws APIException;

	/**
	 * Gets people matching a search string.
	 *
	 * @param searchPhrase the search string
	 * @param dead whether to include dead people
	 * @return list of matching people
	 * @throws APIException if an error occurs
	 */
	List<Person> getPeople(String searchPhrase, Boolean dead) throws APIException;

	/**
	 * Voids a person with a given reason.
	 *
	 * @param person the person to void
	 * @param reason the reason for voiding
	 * @return the voided person
	 * @throws APIException if an error occurs
	 */
	Person voidPerson(Person person, String reason) throws APIException;

	/**
	 * Unvoids a person.
	 *
	 * @param person the person to unvoid
	 * @return the unvoided person
	 * @throws APIException if an error occurs
	 */
	Person unvoidPerson(Person person) throws APIException;

	/**
	 * Saves a relationship.
	 *
	 * @param relationship the relationship to save
	 * @return the saved relationship
	 * @throws APIException if an error occurs
	 */
	Relationship saveRelationship(Relationship relationship) throws APIException;

	/**
	 * Gets all relationship types.
	 *
	 * @return list of all relationship types
	 * @throws APIException if an error occurs
	 */
	List<RelationshipType> getAllRelationshipTypes() throws APIException;

	/**
	 * Gets all person attribute types.
	 *
	 * @return list of all person attribute types
	 * @throws APIException if an error occurs
	 */
	List<PersonAttributeType> getAllPersonAttributeTypes() throws APIException;

	/**
	 * Saves a person attribute type.
	 *
	 * @param type the person attribute type to save
	 * @return the saved person attribute type
	 * @throws APIException if an error occurs
	 */
	PersonAttributeType savePersonAttributeType(PersonAttributeType type) throws APIException;
}
