/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.integration.notification;

import java.util.List;

import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;
import org.openmrs.notification.Alert;

/**
 * Integration module notification service contract extracted from
 * {@link org.openmrs.notification.AlertService} and {@link org.openmrs.notification.MessageService}.
 *
 * <p>Defines the public service API for notifications within the integration module boundary.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.notification.AlertService
 * @see org.openmrs.notification.MessageService
 */
public interface NotificationModuleService extends ServiceContract {

	/**
	 * Saves an alert.
	 *
	 * @param alert the alert to save
	 * @return the saved alert
	 * @throws APIException if an error occurs
	 */
	Alert saveAlert(Alert alert) throws APIException;

	/**
	 * Gets an alert by its identifier.
	 *
	 * @param alertId the alert identifier
	 * @return the alert, or null if not found
	 * @throws APIException if an error occurs
	 */
	Alert getAlert(Integer alertId) throws APIException;

	/**
	 * Gets all active alerts for a user.
	 *
	 * @param user the user to get alerts for
	 * @return list of active alerts
	 * @throws APIException if an error occurs
	 */
	List<Alert> getAllActiveAlerts(User user) throws APIException;

	/**
	 * Gets all alerts, optionally including expired ones.
	 *
	 * @param includeExpired whether to include expired alerts
	 * @return list of all alerts
	 * @throws APIException if an error occurs
	 */
	List<Alert> getAllAlerts(boolean includeExpired) throws APIException;

	/**
	 * Permanently deletes an alert.
	 *
	 * @param alert the alert to purge
	 * @throws APIException if an error occurs
	 */
	void purgeAlert(Alert alert) throws APIException;
}
