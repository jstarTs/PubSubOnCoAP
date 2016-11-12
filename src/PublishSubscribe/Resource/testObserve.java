package PublishSubscribe.Resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.server.resources.CoapExchange;

import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;
import static org.eclipse.californium.core.coap.MediaTypeRegistry.*;

public class testObserve extends CoapResource
{

	// Members ////////////////////////////////////////////////////////////////

	private byte[] data = null;
	private int dataCf = TEXT_PLAIN;
	private boolean wasUpdated = false;

	// The current time represented as string
	private String time;

	/*
	 * Constructor for a new TimeResource
	 */
	public testObserve() {
		super("obs");
		setObservable(true);
		getAttributes().setTitle("Observable resource which changes every 5 seconds");
		getAttributes().addResourceType("observe");
		getAttributes().setObservable();
		setObserveType(Type.CON);

		// Set timer task scheduling
		Timer timer = new Timer();
		timer.schedule(new TimeTask(), 0, 5000);
	}

	/*
	 * Defines a new timer task to return the current time
	 */
	private class TimeTask extends TimerTask {

		@Override
		public void run() {
			time = getTime();
			dataCf = TEXT_PLAIN;

			// Call changed to notify subscribers
			changed();
		}
	}

	/*
	 * Returns the current time
	 * 
	 * @return The current time
	 */
	private String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date time = new Date();
		return dateFormat.format(time);
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		
		exchange.setMaxAge(5);
		
		if (wasUpdated) {
			exchange.respond(CONTENT, data, dataCf);
			wasUpdated = false;
		} else {
			exchange.respond(CONTENT, time, dataCf);
		}
	}
	
	@Override
	public void handlePUT(CoapExchange exchange) {

		if (!exchange.getRequestOptions().hasContentFormat()) {
			exchange.respond(BAD_REQUEST, "Content-Format not set");
			return;
		}
		
		// store payload
		storeData(exchange.getRequestPayload(), exchange.getRequestOptions().getContentFormat());

		// complete the request
		exchange.respond(CHANGED);
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		wasUpdated = false;
		
		clearAndNotifyObserveRelations(NOT_FOUND);
		
		exchange.respond(DELETED);
	}
	

	// Internal ////////////////////////////////////////////////////////////////
	
	/*
	 * Convenience function to store data contained in a 
	 * PUT/POST-Request. Notifies observing endpoints about
	 * the change of its contents.
	 */
	private synchronized void storeData(byte[] payload, int format) {

		wasUpdated = true;
		
		if (format != dataCf) {
			clearAndNotifyObserveRelations(NOT_ACCEPTABLE);
		}
		
		// set payload and content type
		data = payload;
		dataCf = format;

		getAttributes().clearContentType();
		getAttributes().addContentType(dataCf);
		
		// signal that resource state changed
		changed();
	}
}
