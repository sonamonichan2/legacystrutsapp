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

import org.openmrs.module.sharedkernel.domain.OpenmrsObjectContract;

/**
 * Integration module HL7 message contract - common contract for HL7 message types.
 *
 * <p>Defines a common interface for HL7 message entities (HL7InQueue, HL7InArchive, HL7InError)
 * within the integration module boundary.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.hl7.HL7InQueue
 * @see org.openmrs.hl7.HL7InArchive
 * @see org.openmrs.hl7.HL7InError
 */
public interface HL7MessageContract extends OpenmrsObjectContract {

	/**
	 * @return the raw HL7 message data
	 */
	String getHL7Data();

	/**
	 * @param hl7Data the raw HL7 message data to set
	 */
	void setHL7Data(String hl7Data);

	/**
	 * @return the message state (e.g., pending, processing, processed, error)
	 */
	Integer getMessageState();

	/**
	 * @param messageState the message state to set
	 */
	void setMessageState(Integer messageState);
}
