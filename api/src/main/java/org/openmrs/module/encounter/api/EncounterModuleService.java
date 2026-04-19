/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.encounter.api;

import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Encounter module service contract extracted from {@link org.openmrs.api.EncounterService}.
 *
 * <p>Dependencies: patient, concepts, location modules</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.EncounterService
 */
public interface EncounterModuleService extends ServiceContract {

	Encounter saveEncounter(Encounter encounter) throws APIException;

	Encounter getEncounter(Integer encounterId) throws APIException;

	Encounter getEncounterByUuid(String uuid) throws APIException;

	List<Encounter> getEncountersByPatient(Patient patient) throws APIException;

	Encounter voidEncounter(Encounter encounter, String reason) throws APIException;

	Encounter unvoidEncounter(Encounter encounter) throws APIException;

	void purgeEncounter(Encounter encounter) throws APIException;

	List<EncounterType> getAllEncounterTypes() throws APIException;

	EncounterType saveEncounterType(EncounterType encounterType) throws APIException;
}
