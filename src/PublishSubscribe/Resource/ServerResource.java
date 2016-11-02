package PublishSubscribe.Resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

/*  On Server
 * 
 */

public class ServerResource extends CoapResource
{

	public ServerResource() {
		super("testResource");
		getAttributes().setTitle("Test Resource)");
		setVisible(false);
	}
	
	/**
	 * Use Create Topic
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handlePOST(org.eclipse.californium.core.server.resources.CoapExchange)
	 */
	@Override
	public void handlePOST(CoapExchange exchange)
	{
		getAttributes().setTitle(exchange.getRequestPayload().toString());
		
		
		//創造主題
		add(new SubscribedTopic(new String(exchange.getRequestPayload())));
		
		exchange.respond("Thx Creating!");
		
		System.out.println("OK "+ new String(exchange.getRequestPayload()));
	}
	
	
	
}
