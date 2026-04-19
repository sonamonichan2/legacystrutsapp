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
 * Web-Adapter Module - Manages the HTTP layer boundary for the OpenMRS modular-monolith.
 *
 * <p>This module encapsulates all web-layer concerns: filters, servlets, controllers,
 * and REST adapters. It depends on all api modules through the Context.getService() pattern.</p>
 *
 * <p>Current web layer (39 files) includes:</p>
 * <ul>
 *   <li>Filters: InitializationFilter, UpdateFilter, StartupErrorFilter, GZIPFilter, OpenmrsFilter</li>
 *   <li>Servlets: DispatcherServlet, StaticDispatcherServlet, ModuleResourcesServlet</li>
 *   <li>Module web integration: WebModuleUtil, ModuleFilter, ModuleServlet</li>
 *   <li>Controllers: PseudoStaticContentController</li>
 *   <li>Support: Listener, WebConstants, WebUtil, OpenmrsBindingInitializer</li>
 * </ul>
 *
 * <p><strong>Key dependency pattern:</strong> The web layer accesses api services exclusively
 * through {@code Context.getService()} - this service locator pattern is the primary
 * coupling point to be addressed in future refactoring waves.</p>
 *
 * @since 2.1.0
 */
package org.openmrs.module.webadapter;
