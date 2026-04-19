/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.integration.hl7;

import java.util.List;

import org.openmrs.api.APIException;
import org.openmrs.hl7.HL7InQueue;
import org.openmrs.hl7.HL7Source;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Integration module HL7 service contract extracted from {@link org.openmrs.hl7.HL7Service}.
 *
 * <p>Defines the public service API for HL7 message processing within the integration
 * module boundary.</p>
 *
 * <p><strong>Cross-module dependencies (to be addressed in future waves):</strong></p>
 * <ul>
 *   <li>ADTA28Handler → patient, person, location, users-admin modules</li>
 *   <li>ORUR01Handler → patient, person, concepts, encounter, observation, location,
 *       users-admin modules (touches 8 domain areas)</li>
 * </ul>
 *
 * @since 2.1.0
 * @see org.openmrs.hl7.HL7Service
 */
public interface HL7ModuleService extends ServiceContract {

	/**
	 * Saves an HL7 source.
	 *
	 * @param hl7Source the HL7 source to save
	 * @return the saved HL7 source
	 * @throws APIException if an error occurs
	 */
	HL7Source saveHL7Source(HL7Source hl7Source) throws APIException;

	/**
	 * Gets an HL7 source by its identifier.
	 *
	 * @param hl7SourceId the HL7 source identifier
	 * @return the HL7 source, or null if not found
	 * @throws APIException if an error occurs
	 */
	HL7Source getHL7Source(Integer hl7SourceId) throws APIException;

	/**
	 * Gets an HL7 source by its name.
	 *
	 * @param name the HL7 source name
	 * @return the HL7 source, or null if not found
	 * @throws APIException if an error occurs
	 */
	HL7Source getHL7SourceByName(String name) throws APIException;

	/**
	 * Gets all HL7 sources.
	 *
	 * @return list of all HL7 sources
	 * @throws APIException if an error occurs
	 */
	List<HL7Source> getAllHL7Sources() throws APIException;

	/**
	 * Saves an HL7 message to the incoming queue.
	 *
	 * @param hl7InQueue the HL7 message to queue
	 * @return the saved HL7 queue item
	 * @throws APIException if an error occurs
	 */
	HL7InQueue saveHL7InQueue(HL7InQueue hl7InQueue) throws APIException;

	/**
	 * Gets the next HL7 message from the queue for processing.
	 *
	 * @return the next HL7 message, or null if the queue is empty
	 * @throws APIException if an error occurs
	 */
	HL7InQueue getNextHL7InQueue() throws APIException;
}
