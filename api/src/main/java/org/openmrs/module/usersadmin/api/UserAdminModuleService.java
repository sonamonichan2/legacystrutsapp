/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.usersadmin.api;

import java.util.List;

import org.openmrs.Privilege;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Users-admin module service contract extracted from {@link org.openmrs.api.UserService}.
 *
 * <p>Dependencies: person module</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.UserService
 */
public interface UserAdminModuleService extends ServiceContract {

	User saveUser(User user) throws APIException;

	User getUser(Integer userId) throws APIException;

	User getUserByUuid(String uuid) throws APIException;

	User getUserByUsername(String username) throws APIException;

	List<User> getUsers(String name, List<Role> roles, boolean includeRetired) throws APIException;

	User retireUser(User user, String reason) throws APIException;

	User unretireUser(User user) throws APIException;

	void purgeUser(User user) throws APIException;

	List<Role> getAllRoles() throws APIException;

	Role saveRole(Role role) throws APIException;

	List<Privilege> getAllPrivileges() throws APIException;

	Privilege savePrivilege(Privilege privilege) throws APIException;
}
