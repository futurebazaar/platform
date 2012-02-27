/**
 * 
 */
package com.fb.platform.caching;

/**
 * This interface abstracts the caching functionality. The abstraction will help us replace
 * the underlying caching provider in future by removing the dependencies on the providers
 * implementation from the code.
 * 
 * @author vinayak
 *
 */
public interface PlatformCachingManager {

	/**
	 * Adds an object into the cache, identified by key. If an object with same key 
	 * is already present in the cache, it will be overwritten.
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public void put(NamedCachesEnum cacheName, Object key, Object value);

	/**
	 * Gets the object corresponding to the key from the named cache.
	 * @param cacheName
	 * @param key
	 * @return the object or null if no object is found.
	 */
	public Object get(NamedCachesEnum cacheName, Object key);

	/**
	 * Removes the cached object corresponding to the key from the named cache.
	 * @param cacheName
	 * @param key
	 * @return true if object removed successfully, false otherwise.
	 */
	public boolean remove(NamedCachesEnum cacheName, Object key);

	/**
	 * Gets a write lock on the object corresponding to the key on the named cache.
	 * Always remember to release the lock by calling the unlock. Get the lock 
	 * to update an already existing object from the cache.
	 */
	public void lock(NamedCachesEnum cacheName, Object key);

	/**
	 * Releases the write lock obtained by the lock method.
	 * @param cacheName
	 * @param key
	 */
	public void unlock(NamedCachesEnum cacheName, Object key);
}
