package PublishSubscribe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.StaticPskStore;


public class testFogPublishDTLS 
{
	
	private static final String TRUST_STORE_PASSWORD = "rootPass";
	private static final String KEY_STORE_PASSWORD = "endPass";
	private static final String KEY_STORE_LOCATION = "PublishSubscribe/keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "PublishSubscribe/trustStore.jks";
	
	DTLSConnector dtlsConnector;
	KeyStore trustStore,keyStore;
	
	public testFogPublishDTLS()
	{
		try 
    	{
        	// load the trust store
        	trustStore = KeyStore.getInstance("JKS");
        	InputStream inTrust = testFogPublishDTLS.class.getClassLoader().getResourceAsStream(TRUST_STORE_LOCATION);
    		trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());
    		// You can load multiple certificates if needed
        	Certificate[] trustedCertificates = new Certificate[1];
        	trustedCertificates[0] = trustStore.getCertificate("root");
        	
        	// load the key store
        	keyStore = KeyStore.getInstance("JKS");
        	InputStream in = testFogPublishDTLS.class.getClassLoader().getResourceAsStream(KEY_STORE_LOCATION);
        	keyStore.load(in, KEY_STORE_PASSWORD.toCharArray());

        	DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder(new InetSocketAddress(0));
        	builder.setPskStore(new StaticPskStore("Client_identity", "secretPSK".getBytes()));
        	builder.setIdentity((PrivateKey)keyStore.getKey("client", KEY_STORE_PASSWORD.toCharArray()),
        						keyStore.getCertificateChain("client"), true);
        	builder.setTrustStore(trustedCertificates);
        	//builder.setMaxConnections(20000);
        	//builder.setClientAuthenticationRequired(true);
        	//builder.setRetransmissionTimeout(10000);
        	dtlsConnector = new DTLSConnector(builder.build());
        	
        	
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws URISyntaxException, IOException, InterruptedException {
		
		testFogPublishDTLS test = new testFogPublishDTLS();
		
		URI uri = null; // URI parameter of the request
		
		if (args.length > 0) {
			
			// input URI from command line arguments
			try 
			{
				
				uri = new URI("coap://127.0.0.1:5683/test1");
				
			} 
			catch (URISyntaxException e) {
				System.err.println("Invalid URI: " + e.getMessage());
				System.exit(-1);
			}
			
			//String[] xmlfile = {"testPubSub1.xml" , "testPubSub2.xml" , "testPubSub3.xml" ,"testPubSubAll.xml"};
			String[] xmlfile = {"testPubSub_2K.xml" , "testPubSub_4K.xml" , "testPubSub_6K.xml" ,"testPubSub_8K.xml","testPubSub_10K.xml","testPubSub_12K.xml"};
			String temp = "";
			Scanner sc ;
			List<String> fileList = new ArrayList<String>();
			
			String[] timeArray = {"hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bg","hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Ba","hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bf","hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Be"};		
			
			String timeA = "hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bh";
			
			for(String filename : xmlfile)
			{
				sc = new Scanner(new File("testData/"+filename));
				
				while(sc.hasNextLine())
				{
					temp += sc.nextLine();
				}
				fileList.add(temp);
				
				temp = "";
			}
			
			NetworkConfig config = new NetworkConfig();
			config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE,1024);
			CoapEndpoint endpoint = new CoapEndpoint(config);
			
			CoapClient client = new CoapClient(uri);
			
			client.setEndpoint(endpoint);
			
			ExecutorService executorService = Executors.newFixedThreadPool(5);
			
			
			long startTime = System.currentTimeMillis();
			long EndTime = 0;
  			for(int runtime=1 ; runtime <= 1 ;runtime++)
  			{
  				for(int i = 1 ; i <= 100 ; i++)
  	  			{
  	  				executorService.submit(test.new Request(fileList.get(0), i));
  	  			}
  				EndTime = System.currentTimeMillis();
  	  			Thread.sleep(30000);
  			}
			
  			System.out.println("Time : " + (EndTime-startTime));
  			
			executorService.shutdown();
			
			
		} else {
			// display help
			System.out.println("Californium (Cf) GET Client");
			System.out.println("(c) 2014, Institute for Pervasive Computing, ETH Zurich");
			System.out.println();
			System.out.println("Usage: " + testCient.class.getSimpleName() + " URI");
			System.out.println("  URI: The CoAP URI of the remote resource to GET");
		}
	}
	
	public DTLSConnector connector()
	{
		Certificate[] trustedCertificates = new Certificate[1];
		DTLSConnector dtlsConnector;
		
		try {
			trustedCertificates[0] = trustStore.getCertificate("root");
			
			DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder(new InetSocketAddress(0));
			builder.setRetransmissionTimeout(20000);
	    	builder.setPskStore(new StaticPskStore("Client_identity", "secretPSK".getBytes()));
	    	builder.setIdentity((PrivateKey)keyStore.getKey("client", KEY_STORE_PASSWORD.toCharArray()),
	    						keyStore.getCertificateChain("client"), true);
	    	builder.setTrustStore(trustedCertificates);
	    	dtlsConnector = new DTLSConnector(builder.build());
	    	
	    	return dtlsConnector;
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return null;
	}
	
	public class Request implements Runnable
	{
		String document;
		int index;
		
		public Request() {
			// TODO Auto-generated constructor stub
		}
		
		public Request(String doc , int index)
		{
			document = doc;
			this.index = index;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			URI uris;
			
			try 
			{
				uris = new URI("coaps://140.120.15.155/ReceivedResult"+index);
			
			DTLSConnector dtlsConnectorR = connector();
			
			
			NetworkConfig config = new NetworkConfig();
			config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE,1024);
			
			CoapClient client;
			
			client = new CoapClient(uris);
			//client.setEndpoint(new CoapEndpoint(dtlsConnectorR, NetworkConfig.getStandard()));
			client.setEndpoint(new CoapEndpoint(dtlsConnectorR, config));
			
			client.setTimeout(3000);
			
			//CoapResponse response = client.put(fileList.get(3), index);
			CoapResponse response = client.put(document, MediaTypeRegistry.TEXT_PLAIN);
			//CoapResponse response = client.put("QQ", 0);
			
			if (response!=null) {
				
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
				System.out.println(response.getResponseText());
				
				System.out.println("\nADVANCED\n");
				// access advanced API with access to more details through .advanced()
				System.out.println(Utils.prettyPrint(response));
				
				
				dtlsConnectorR.stop();
				//dtlsConnectorR.destroy();
				
			} 
			else 
			{
				//dtlsConnectorR.forceResumeSessionFor(peer);
				System.out.println("No response received.");
				dtlsConnectorR.stop();
			}
			
			//dtlsConnectorR.stop();
			
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("Invalid URI: " + e.getMessage());
				System.exit(-1);
			}
			
			
		}
		
	}
	
}
