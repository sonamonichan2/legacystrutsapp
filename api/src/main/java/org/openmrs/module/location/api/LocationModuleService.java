/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.location.api;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.api.APIException;
import org.openmrs.module.sharedkernel.api.ServiceContract;

/**
 * Location module service contract extracted from {@link org.openmrs.api.LocationService}.
 *
 * <p>Defines the public service API for the location module boundary. This interface
 * declares location-specific operations that the location module exposes to other modules.</p>
 *
 * <p>Note: This interface currently references concrete types (Location, LocationTag) from
 * {@code org.openmrs} package. In future waves, these will be replaced with contract interfaces
 * (LocationContract, LocationTagContract) as the migration progresses.</p>
 *
 * @since 2.1.0
 * @see org.openmrs.api.LocationService
 */
public interface LocationModuleService extends ServiceContract {

	/**
	 * Saves a location to the database.
	 *
	 * @param location the location to save
	 * @return the saved location
	 * @throws APIException if an error occurs
	 */
	Location saveLocation(Location location) throws APIException;

	/**
	 * Gets a location by its internal database identifier.
	 *
	 * @param locationId the location identifier
	 * @return the location with the given id, or null if not found
	 * @throws APIException if an error occurs
	 */
	Location getLocation(Integer locationId) throws APIException;

	/**
	 * Gets a location by its name.
	 *
	 * @param name the name of the location
	 * @return the location with the given name, or null if not found
	 * @throws APIException if an error occurs
	 */
	Location getLocationByName(String name) throws APIException;

	/**
	 * Gets a location by its UUID.
	 *
	 * @param uuid the UUID of the location
	 * @return the location with the given UUID, or null if not found
	 * @throws APIException if an error occurs
	 */
	Location getLocationByUuid(String uuid) throws APIException;

	/**
	 * Gets the default location for the implementation.
	 *
	 * @return the default location
	 * @throws APIException if an error occurs
	 */
	Location getDefaultLocation() throws APIException;

	/**
	 * Gets all locations, optionally including retired ones.
	 *
	 * @param includeRetired whether to include retired locations
	 * @return list of all locations
	 * @throws APIException if an error occurs
	 */
	List<Location> getAllLocations(boolean includeRetired) throws APIException;

	/**
	 * Gets locations matching a name fragment.
	 *
	 * @param nameFragment the partial name to search for
	 * @return list of matching locations
	 * @throws APIException if an error occurs
	 */
	List<Location> getLocations(String nameFragment) throws APIException;

	/**
	 * Retires a location with a given reason.
	 *
	 * @param location the location to retire
	 * @param reason the reason for retiring
	 * @return the retired location
	 * @throws APIException if an error occurs
	 */
	Location retireLocation(Location location, String reason) throws APIException;

	/**
	 * Unretires a location.
	 *
	 * @param location the location to unretire
	 * @return the unretired location
	 * @throws APIException if an error occurs
	 */
	Location unretireLocation(Location location) throws APIException;

	/**
	 * Permanently deletes a location from the database.
	 *
	 * @param location the location to purge
	 * @throws APIException if an error occurs
	 */
	void purgeLocation(Location location) throws APIException;

	/**
	 * Saves a location tag.
	 *
	 * @param tag the location tag to save
	 * @return the saved location tag
	 * @throws APIException if an error occurs
	 */
	LocationTag saveLocationTag(LocationTag tag) throws APIException;

	/**
	 * Gets a location tag by its identifier.
	 *
	 * @param locationTagId the tag identifier
	 * @return the location tag, or null if not found
	 * @throws APIException if an error occurs
	 */
	LocationTag getLocationTag(Integer locationTagId) throws APIException;

	/**
	 * Gets a location tag by name.
	 *
	 * @param tag the tag name
	 * @return the location tag, or null if not found
	 * @throws APIException if an error occurs
	 */
	LocationTag getLocationTagByName(String tag) throws APIException;

	/**
	 * Gets all location tags.
	 *
	 * @param includeRetired whether to include retired tags
	 * @return list of all location tags
	 * @throws APIException if an error occurs
	 */
	List<LocationTag> getAllLocationTags(boolean includeRetired) throws APIException;

	/**
	 * Gets locations that have a particular tag.
	 *
	 * @param tag the location tag to filter by
	 * @return list of locations with the given tag
	 * @throws APIException if an error occurs
	 */
	List<Location> getLocationsByTag(LocationTag tag) throws APIException;

	/**
	 * Gets root locations (locations without a parent).
	 *
	 * @param includeRetired whether to include retired locations
	 * @return list of root locations
	 */
	List<Location> getRootLocations(boolean includeRetired);
}
