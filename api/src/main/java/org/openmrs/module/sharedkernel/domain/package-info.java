/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

/**
 * Shared Kernel Domain Contracts - Base entity interfaces for the modular-monolith.
 *
 * <p>Contains contract interfaces extracted from the core domain types:</p>
 * <ul>
 *   <li>{@code OpenmrsObjectContract} - Base persistent object contract</li>
 *   <li>{@code OpenmrsDataContract} - Voidable data entity contract</li>
 *   <li>{@code OpenmrsMetadataContract} - Retireable metadata entity contract</li>
 *   <li>{@code AuditableContract} - Audit trail contract</li>
 *   <li>{@code VoidableContract} - Void support contract</li>
 *   <li>{@code RetireableContract} - Retire support contract</li>
 * </ul>
 *
 * @since 2.1.0
 */
package org.openmrs.module.sharedkernel.domain;
