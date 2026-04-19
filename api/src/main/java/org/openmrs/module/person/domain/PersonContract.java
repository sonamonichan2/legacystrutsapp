/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.person.domain;

import java.util.Date;
import java.util.Set;

import org.openmrs.module.sharedkernel.domain.OpenmrsDataContract;

/**
 * Person module domain contract extracted from {@link org.openmrs.Person}.
 *
 * <p>Defines the public API contract for Person entities. Person is the foundational
 * entity type - Patient extends Person, User references Person.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.Person
 */
public interface PersonContract extends OpenmrsDataContract {

	/**
	 * @return the personId
	 */
	Integer getPersonId();

	/**
	 * @param personId the personId to set
	 */
	void setPersonId(Integer personId);

	/**
	 * @return the gender
	 */
	String getGender();

	/**
	 * @param gender the gender to set
	 */
	void setGender(String gender);

	/**
	 * @return the birthdate
	 */
	Date getBirthdate();

	/**
	 * @param birthdate the birthdate to set
	 */
	void setBirthdate(Date birthdate);

	/**
	 * @return whether the birthdate is estimated
	 */
	Boolean getBirthdateEstimated();

	/**
	 * @param birthdateEstimated whether the birthdate is estimated
	 */
	void setBirthdateEstimated(Boolean birthdateEstimated);

	/**
	 * @return whether the person is dead
	 */
	Boolean getDead();

	/**
	 * @param dead whether the person is dead
	 */
	void setDead(Boolean dead);

	/**
	 * @return the death date
	 */
	Date getDeathDate();

	/**
	 * @param deathDate the death date to set
	 */
	void setDeathDate(Date deathDate);

	/**
	 * @return the person's family name (from the preferred PersonName)
	 */
	String getFamilyName();

	/**
	 * @return the person's given name (from the preferred PersonName)
	 */
	String getGivenName();

	/**
	 * @return the person's middle name (from the preferred PersonName)
	 */
	String getMiddleName();

	/**
	 * @return true if the person is female
	 */
	Boolean isFemale();

	/**
	 * @return true if the person is male
	 */
	Boolean isMale();

	/**
	 * @return the person's age in years based on birthdate
	 */
	Integer getAge();
}
