package com.fb.sap.client;

import java.io.StringReader;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.fb.sap.idocs.DeliveryDeletedIDoc;

class DeliveryDeletedObservable extends Observable implements ISAPEvent {

	private static DeliveryDeletedObservable INSTANCE = new DeliveryDeletedObservable();
	static Logger logger = Logger.getLogger("auris");

	public static DeliveryDeletedObservable getInstance() {
		return INSTANCE;
	}

	private DeliveryDeletedObservable() {

	}

	public synchronized void dispatch(String xml) {
		// Not sure if we really need synchronized here.
		// We perhaps need some kind of synchronization at a order level
		// Have synchronized this method so that notifyObservers will be
		// process one IDoc at a time. If we overlap, this.hasChanged()
		// might return false by the time notifyObservers has started to
		// execute. We might not be exposed to this, if the caller of
		// dispatch synchronizes, but we do not if it does.
	
		try {
			JAXBContext context = JAXBContext.newInstance("com.fb.sap.idocs");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			DeliveryDeletedIDoc deliveryDeletedIDoc = (DeliveryDeletedIDoc) unmarshaller.unmarshal(new StreamSource(new StringReader(xml)));
			this.setChanged();
			this.notifyObservers(deliveryDeletedIDoc);
		} catch (JAXBException e){
			logger.error("Error dispatching delivery deleted event", e);
		}
	}
}
