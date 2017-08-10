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


public class testsubscriberRecieveDTLS extends CoapServer {

	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
	
    /*
     * Application entry point.
     */
	public static final int DTLS_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_SECURE_PORT);
	
	private static final String TRUST_STORE_PASSWORD = "rootPass";
	private static final String KEY_STORE_PASSWORD = "endPass";
	private static final String KEY_STORE_LOCATION = "PublishSubscribe/keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "PublishSubscribe/trustStore.jks";
    
	/*
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public testsubscriberRecieveDTLS() throws SocketException {
        
        // provide an instance of a Hello-World resource
        add(new ReceivedResultDTSL());
    }
    
    public testsubscriberRecieveDTLS(int numberResource) throws SocketException
    {
    	add(new ReceivedResultDTSL());
    	for(int i = 1 ; i <= numberResource ; i++)
    	{
    		add(new ReceivedResultDTSL("test"+i));
    	}
    }
	
	public static void main(String[] args) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        
		int listSize = Integer.parseInt(args[0]);
        try {

            // create server
        	testsubscriberRecieveDTLS server = new testsubscriberRecieveDTLS(listSize);
            // add endpoints on all IP addresses
            server.addEndpoints();
            server.start();

        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }

//    /**
//     * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
//     */
//    private void addEndpoints() {
//    	for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
//    		// only binds to IPv4 addresses and localhost
//			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
//				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
//				addEndpoint(new CoapEndpoint(bindToAddress));
//			}
//		}
//    }

    
    
    /**
     * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
     * @throws KeyStoreException 
     * @throws IOException 
     * @throws CertificateException 
     * @throws NoSuchAlgorithmException 
     * @throws UnrecoverableKeyException 
     */
    private void addEndpoints() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException 
    {
    	// Pre-shared secrets
    	InMemoryPskStore pskStore = new InMemoryPskStore();
    	pskStore.setKey("password", "sesame".getBytes()); // from ETSI Plugtest test spec

    	// load the trust store
    	KeyStore trustStore = KeyStore.getInstance("JKS");
    	InputStream inTrust = testsubscriberRecieveDTLS.class.getClassLoader().getResourceAsStream(TRUST_STORE_LOCATION);
    	trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());

    	// You can load multiple certificates if needed
    	Certificate[] trustedCertificates = new Certificate[1];
    	trustedCertificates[0] = trustStore.getCertificate("root");
    	
    	// load the key store
    	KeyStore keyStore = KeyStore.getInstance("JKS");
    	InputStream in = testsubscriberRecieveDTLS.class.getClassLoader().getResourceAsStream(KEY_STORE_LOCATION);
    	keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());
		
    	for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
    		// only binds to IPv4 addresses and localhost
			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				DtlsConnectorConfig.Builder config = new DtlsConnectorConfig.Builder(new InetSocketAddress(addr,DTLS_PORT));
				config.setSupportedCipherSuites(new CipherSuite[]{CipherSuite.TLS_PSK_WITH_AES_128_CCM_8,
						CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8});
				config.setPskStore(pskStore);
				config.setIdentity((PrivateKey)keyStore.getKey("server", KEY_STORE_PASSWORD.toCharArray()),
						keyStore.getCertificateChain("server"), true);
				config.setTrustStore(trustedCertificates);
				//config.
				DTLSConnector connector = new DTLSConnector(config.build());
				
				addEndpoint(new CoapEndpoint(connector,NetworkConfig.getStandard()));
				

			}
		}
    }
    

    /*
     * Definition of the Hello-World Resource
     */
    class ReceivedResultDTSL extends CoapResource {
        
    	public ReceivedResultDTSL() {
            
            // set resource identifier
            super("ReceivedResultDTSL");
            
            // set display name
            getAttributes().setTitle("Hello-World Resource");
            getAttributes().addResourceType("block");
    		getAttributes().setMaximumSizeEstimate(1280);
        }
        
        public ReceivedResultDTSL(String name) {
            
            // set resource identifier
            super(name);
            
            // set display name
            getAttributes().setTitle("Hello-World Resource" + " : "+name);
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
