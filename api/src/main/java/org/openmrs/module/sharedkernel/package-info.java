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
 * Shared Kernel Module - Foundation layer for the OpenMRS modular-monolith architecture.
 *
 * <p>This package defines the shared-kernel module boundary. The shared-kernel contains
 * minimal shared primitives, contracts, and utilities that all other modules depend on.
 * It has zero business logic and serves as the foundational layer.</p>
 *
 * <p>Sub-packages:</p>
 * <ul>
 *   <li>{@code domain} - Base entity interfaces and contracts (OpenmrsObjectContract, etc.)</li>
 *   <li>{@code api} - Base service interfaces and contracts (ServiceContract, etc.)</li>
 *   <li>{@code util} - Shared utility classes and constants</li>
 * </ul>
 *
 * @since 2.1.0
 */
package org.openmrs.module.sharedkernel;
