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
 * Web-Adapter filter package - will contain refactored web filters.
 *
 * <p>Current filters to be migrated in future waves:</p>
 * <ul>
 *   <li>InitializationFilter - First-run database setup wizard</li>
 *   <li>UpdateFilter - Database update handling</li>
 *   <li>StartupErrorFilter - Startup error display</li>
 *   <li>GZIPFilter - Response compression</li>
 *   <li>OpenmrsFilter - Authentication filter</li>
 *   <li>JspClassLoaderFilter - Classloader management</li>
 * </ul>
 *
 * @since 2.1.0
 */
package org.openmrs.module.webadapter.filter;
