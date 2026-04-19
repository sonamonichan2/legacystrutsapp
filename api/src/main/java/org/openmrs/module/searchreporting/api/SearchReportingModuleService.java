/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.searchreporting.api;

import java.util.List;

import org.openmrs.Cohort;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Search-reporting module service contract extracted from {@link org.openmrs.api.CohortService}.
 *
 * <p>Dependencies: patient module</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.CohortService
 */
public interface SearchReportingModuleService extends ServiceContract {

	Cohort saveCohort(Cohort cohort) throws APIException;

	Cohort getCohort(Integer cohortId) throws APIException;

	Cohort getCohortByUuid(String uuid) throws APIException;

	List<Cohort> getAllCohorts() throws APIException;

	List<Cohort> getCohorts(String nameFragment) throws APIException;

	Cohort voidCohort(Cohort cohort, String reason) throws APIException;

	void purgeCohort(Cohort cohort) throws APIException;
}
