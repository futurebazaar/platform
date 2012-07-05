/**
 * 
 */
package com.fb.platform.mom.manager;

import java.io.Serializable;

/**
 * @author vinayak
 *
 */
public interface MomManager {

	public void send(PlatformDestinationEnum destination, Serializable message);

	public void registerReceiver(PlatformDestinationEnum destination, PlatformMessageReceiver receiver);
}
