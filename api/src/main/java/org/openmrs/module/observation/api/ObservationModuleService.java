/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.observation.api;

import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Observation module service contract extracted from {@link org.openmrs.api.ObsService}.
 *
 * <p>Dependencies: encounter, concepts, person modules</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.ObsService
 */
public interface ObservationModuleService extends ServiceContract {

	Obs saveObs(Obs obs, String changeMessage) throws APIException;

	Obs getObs(Integer obsId) throws APIException;

	Obs getObsByUuid(String uuid) throws APIException;

	List<Obs> getObservationsByPerson(Person who) throws APIException;

	Obs voidObs(Obs obs, String reason) throws APIException;

	Obs unvoidObs(Obs obs) throws APIException;

	void purgeObs(Obs obs) throws APIException;
}
