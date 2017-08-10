package PublishSubscribe;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;


public class testsubscriberRecieve extends CoapServer {

	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
	
    /*
     * Application entry point.
     */
	
    public testsubscriberRecieve() throws SocketException {
        
        // provide an instance of a Hello-World resource
        add(new ReceivedResult());
    }
    
    public testsubscriberRecieve(int numberResource) throws SocketException
    {
    	add(new ReceivedResult());
    	for(int i = 1 ; i <= numberResource ; i++)
    	{
    		add(new ReceivedResult("ReceivedResult"+i));
    	}
    }
	
	public static void main(String[] args) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        
		int listSize = 100;
		
        try {

            // create server
        	testsubscriberRecieve server = new testsubscriberRecieve(listSize);
            // add endpoints on all IP addresses
            server.addEndpoints();
            server.start();

        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }

    /**
     * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
     */
    private void addEndpoints() {
    	for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
    		// only binds to IPv4 addresses and localhost
			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
    } 

    /*
     * Definition of the Hello-World Resource
     */
    class ReceivedResult extends CoapResource {
        
    	public ReceivedResult() {
            
            // set resource identifier
            super("ReceivedResult");
            
            // set display name
            getAttributes().setTitle("Hello-World Resource");
            getAttributes().addResourceType("block");
    		getAttributes().setMaximumSizeEstimate(1280);
        }
        
        public ReceivedResult(String name) {
            
            // set resource identifier
            super(name);
            
            // set display name
            getAttributes().setTitle("Hello-World Resource" + " : "+name);
//            getAttributes().addResourceType("block");
//    		getAttributes().setMaximumSizeEstimate(1280);
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            
            // respond to the request
            exchange.respond("Hello World!");
        }
        
        @Override
        public void handlePUT(CoapExchange exchange)
        {
        	byte[] docByte = exchange.getRequestPayload();
        	
        	exchange.respond("Get");
        }
        
    }
   
}
