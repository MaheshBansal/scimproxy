package info.simplecloud.scimproxy.storage;

import info.simplecloud.core.ScimUser;

public interface IStorage {

	/**
	 * Returns a ScimUser from the storage.
	 * @param id Unique identifier for the User as defined by the Service Provider.
	 * @return If id is found in storage then the fully populated SCIM user object is returned. If it's not found or if id is empty then null is returned.
	 */
	public ScimUser getUserForId(String id);
	
	/**
	 * Adds a user in the user storage.
	 * @param user A fully populated SCIM user.
	 */
	public void addUser(ScimUser user);
	
}

