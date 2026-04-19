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

import org.openmrs.module.sharedkernel.domain.OpenmrsDataContract;

/**
 * Person module domain contract extracted from {@link org.openmrs.PersonAddress}.
 *
 * <p>Defines the public API contract for PersonAddress entities within the person module.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.PersonAddress
 */
public interface PersonAddressContract extends OpenmrsDataContract {

	/**
	 * @return the first line of the address
	 */
	String getAddress1();

	/**
	 * @param address1 the first line of the address to set
	 */
	void setAddress1(String address1);

	/**
	 * @return the second line of the address
	 */
	String getAddress2();

	/**
	 * @param address2 the second line of the address to set
	 */
	void setAddress2(String address2);

	/**
	 * @return the city or village
	 */
	String getCityVillage();

	/**
	 * @param cityVillage the city or village to set
	 */
	void setCityVillage(String cityVillage);

	/**
	 * @return the state or province
	 */
	String getStateProvince();

	/**
	 * @param stateProvince the state or province to set
	 */
	void setStateProvince(String stateProvince);

	/**
	 * @return the country
	 */
	String getCountry();

	/**
	 * @param country the country to set
	 */
	void setCountry(String country);

	/**
	 * @return the postal code
	 */
	String getPostalCode();

	/**
	 * @param postalCode the postal code to set
	 */
	void setPostalCode(String postalCode);

	/**
	 * @return whether this is the preferred address
	 */
	Boolean getPreferred();

	/**
	 * @param preferred whether this is the preferred address
	 */
	void setPreferred(Boolean preferred);
}
