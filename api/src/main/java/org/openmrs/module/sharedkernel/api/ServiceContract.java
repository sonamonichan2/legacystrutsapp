/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.sharedkernel.api;

/**
 * Shared-kernel service contract interface extracted from {@link org.openmrs.api.OpenmrsService}.
 *
 * <p>Base service contract for all module services in the modular-monolith architecture.
 * All module service interfaces should extend this contract.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.OpenmrsService
 */
public interface ServiceContract {

	/**
	 * Called when the OpenMRS service layer is initializing. This occurs when a new module is
	 * loaded or during the initial server/api start.
	 */
	void onStartup();

	/**
	 * Called when the OpenMRS service layer is shutting down.
	 */
	void onShutdown();
}
