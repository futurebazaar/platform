/**
 * 
 */
package com.fb.platform.mom.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.fb.platform.mom.manager.PlatformMessageReceiver;

/**
 * @author vinayak
 *
 */
public abstract class AbstractPlatformListener {

	private List<PlatformMessageReceiver> receivers = new ArrayList<PlatformMessageReceiver>();

	public void addReceiver(PlatformMessageReceiver receiver) {
		this.receivers.add(receiver);
	}

	public void notify(Object message) {
		for (PlatformMessageReceiver receiver : receivers) {
			receiver.handleMessage(message);
		}
	}
}
