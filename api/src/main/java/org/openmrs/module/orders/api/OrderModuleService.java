/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.orders.api;

import java.util.List;

import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Orders module service contract extracted from {@link org.openmrs.api.OrderService}.
 *
 * <p>Dependencies: patient, encounter, concepts modules</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.OrderService
 */
public interface OrderModuleService extends ServiceContract {

	Order saveOrder(Order order, org.openmrs.api.OrderContext orderContext) throws APIException;

	Order getOrder(Integer orderId) throws APIException;

	Order getOrderByUuid(String uuid) throws APIException;

	List<Order> getOrdersByPatient(Patient patient) throws APIException;

	Order voidOrder(Order order, String voidReason) throws APIException;

	Order unvoidOrder(Order order) throws APIException;

	void purgeOrder(Order order) throws APIException;

	List<OrderType> getOrderTypes(boolean includeRetired) throws APIException;

	OrderType saveOrderType(OrderType orderType) throws APIException;
}
