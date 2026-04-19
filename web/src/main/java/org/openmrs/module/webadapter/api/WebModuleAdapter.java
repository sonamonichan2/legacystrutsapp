/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.webadapter.api;

import jakarta.servlet.ServletContext;

import org.openmrs.module.Module;

/**
 * Web-Adapter module facade interface for web module lifecycle operations.
 *
 * <p>This interface defines the public API for the web-adapter module, providing
 * a clean boundary between web infrastructure and domain logic.</p>
 *
 * <p>In future waves, the existing {@link org.openmrs.module.web.WebModuleUtil}
 * functionality will be delegated through this facade.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.module.web.WebModuleUtil
 */
public interface WebModuleAdapter {

	/**
	 * Starts a web module within the given servlet context.
	 *
	 * @param module the module to start
	 * @param servletContext the servlet context
	 */
	void startModule(Module module, ServletContext servletContext);

	/**
	 * Stops a web module within the given servlet context.
	 *
	 * @param module the module to stop
	 * @param servletContext the servlet context
	 */
	void stopModule(Module module, ServletContext servletContext);

	/**
	 * Checks if the web infrastructure is initialized and ready to serve requests.
	 *
	 * @return true if the web layer is initialized
	 */
	boolean isWebInitialized();

	/**
	 * Performs startup initialization of the web layer.
	 *
	 * @param servletContext the servlet context
	 */
	void performStartup(ServletContext servletContext);

	/**
	 * Performs graceful shutdown of the web layer.
	 *
	 * @param servletContext the servlet context
	 */
	void performShutdown(ServletContext servletContext);
}
