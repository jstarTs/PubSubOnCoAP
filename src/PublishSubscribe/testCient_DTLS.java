package PublishSubscribe;
/*******************************************************************************
 * Copyright (c) 2015 Institute for Pervasive Computing, ETH Zurich and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *    Matthias Kovatsch - creator and main architect
 ******************************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
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
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.eclipse.californium.scandium.dtls.pskstore.StaticPskStore;

import PublishSubscribe.FogNode.testTryFog;


public class testCient_DTLS {

	/*
	 * Application entry point.
	 * 
	 */	
	private static final String TRUST_STORE_PASSWORD = "rootPass";
	private static final String KEY_STORE_PASSWORD = "endPass";
	private static final String KEY_STORE_LOCATION = "PublishSubscribe/keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "PublishSubscribe/trustStore.jks";
	
	DTLSConnector dtlsConnector;
	KeyStore trustStore,keyStore;
	
	public testCient_DTLS()
	{
		
    	try 
    	{
        	// load the trust store
        	trustStore = KeyStore.getInstance("JKS");
        	InputStream inTrust = testTryFog.class.getClassLoader().getResourceAsStream(TRUST_STORE_LOCATION);
    		trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());
    		// You can load multiple certificates if needed
        	Certificate[] trustedCertificates = new Certificate[1];
        	trustedCertificates[0] = trustStore.getCertificate("root");
        	
        	// load the key store
        	keyStore = KeyStore.getInstance("JKS");
        	InputStream in = testTryFog.class.getClassLoader().getResourceAsStream(KEY_STORE_LOCATION);
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
		
		testCient_DTLS test = new testCient_DTLS();
		
		URI uri = null; // URI parameter of the request
		
		if (args.length > 0) {
			
			// input URI from command line arguments
			try 
			{
				//uri = new URI(args[0]);
				
				//uri = new URI("coap://140.120.15.159:5683/TestConcurrentResource");
				//uri = new URI("coap://140.120.15.136:5683/TestConcurrentResource");
				//uri = new URI("coap://127.0.0.1:5684/TestConcurrentResource");
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
			
			//CoapResponse response = client.get();
			
			//new URI("coaps://140.120.15.159/test1");
			
			NetworkConfig config = new NetworkConfig();
			config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE,1024);
			CoapEndpoint endpoint = new CoapEndpoint(config);
			
			CoapClient client = new CoapClient(uri);
			
			client.setEndpoint(endpoint);
			
//			CoapResponse response ;
//			response = client.();
			
			//client.setEndpoint(new CoapEndpoint(test.dtlsConnector, NetworkConfig.getStandard()));
			//int index;
			ExecutorService executorService = Executors.newFixedThreadPool(20);
			//executorService.submit(test.new Request("QQ", 100));
			//Thread.sleep(2000);
			
//			for(int docNum = 0 ; docNum < fileList.size() ; docNum++)
//            {
//            	for(int runtime=1 ; runtime<=11 ; runtime++)
//                {
//            		if(runtime == 1)
//            		{
//
//            			for(int i = 1 ; i <= 20 ; i++)
//            			{
//            				executorService.submit(test.new Request(fileList.get(0), i));
//            			}
//            			//Thread.currentThread().sleep(2000);
//            			Thread.sleep(20000);
//            			for(int i = 1 ; i <= 480 ; i++)
//            			{
//            				executorService.submit(test.new Request(fileList.get(0), i));
//            			}
//            			
//            			Thread.sleep(30000);
//            		}
//            		else
//            		{
//
//            			for(int i = 1 ; i <= 500 ; i++)
//            			{
//            				executorService.submit(test.new Request(fileList.get(0), i));
//            			}
//            			
//            			Thread.sleep(30000);
//            			
//            		}
//                }
//            }
			
//			for(int i = 1 ; i <= 100 ; i++)
//			{
//				/*
//				//test.dtlsConnector.start();
//				String t = "coaps://140.120.15.155/test1";
//				uri = new URI(t);
//				client.setURI(t);
//				//response = client.put((timeArray[0]+","+fileList.get(3)), 0);
//				response = client.put(fileList.get(3), 0);
//				//response = client.get();
//				if (response!=null) {
//					
//					System.out.println(response.getCode());
//					System.out.println(response.getOptions());
//					System.out.println(response.getResponseText());
//					
//					System.out.println("\nADVANCED\n");
//					// access advanced API with access to more details through .advanced()
//					System.out.println(Utils.prettyPrint(response));
//					
//				} else {
//					System.out.println("No response received.");
//				}
//				test.dtlsConnector.stop();
//				System.out.println(test.dtlsConnector.isRunning());
//				*/
//				/*
//				int index = i;
//				new Thread (()->{
//					URI uris;
//					
//					try 
//					{
//						
//						//uris = new URI("coap://127.0.0.1:5683/test"+index);
//						uris = new URI("coap://140.120.15.136:5683/test"+index);
//						//uris = new URI("coap://140.120.15.159:5683/test"+index);
//						
//					CoapClient client;
//					client = new CoapClient(uris);
//					System.out.println();
//					//client.
//					CoapResponse response = client.put(fileList.get(3), index);
//					//CoapResponse response = client.put("QQ", 0);
//					
//					if (response!=null) {
//						
//						System.out.println(response.getCode());
//						System.out.println(response.getOptions());
//						System.out.println(response.getResponseText());
//						
//						System.out.println("\nADVANCED\n");
//						// access advanced API with access to more details through .advanced()
//						System.out.println(Utils.prettyPrint(response));
//						
//					} else {
//						System.out.println("No response received.");
//					}
//					
//					} catch (URISyntaxException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//				}).start();
//				*/
//				
//				executorService.submit(test.new Request(fileList.get(2), i));
//				
//			}
			
			//正式
//			int size = 100;
//			int tempN;
//			for(int i = 1 ; i <= 100 ; i++)
//			{
//				if(size==100)
//					tempN = size;
//				else
//					tempN = i%size;
//				executorService.submit(test.new Request(fileList.get(0), tempN ));
//			}
//			
			
			
//			for(int i = 1 ; i <= 10 ; i++)
//  			{
//  				executorService.submit(test.new Request(fileList.get(0), i));
//  			}
//  			//Thread.currentThread().sleep(2000);
//  			Thread.sleep(20000);
//  			for(int i = 1 ; i <= 190 ; i++)
//  			{
//  				executorService.submit(test.new Request(fileList.get(0), i));
//  			}

  			for(int runtime=1 ; runtime <= 1 ;runtime++)
  			{
  				for(int i = 1 ; i <= 300 ; i++)
  	  			{
  	  				executorService.submit(test.new Request(fileList.get(1), i));
  	  			}
  	  			//Thread.sleep(30000);
  			}
			
			executorService.shutdown();
			
			
			/*
			for(String time :  timeArray)
			{
				for(int i = 0 ; i < 10 ; i++)
				{
					response = client.put((time+","+fileList.get(3)), 0);
					if (response!=null) {
						
						System.out.println(response.getCode());
						System.out.println(response.getOptions());
						System.out.println(response.getResponseText());
						
						System.out.println("\nADVANCED\n");
						// access advanced API with access to more details through .advanced()
						System.out.println(Utils.prettyPrint(response));
						
					} else {
						System.out.println("No response received.");
					}
				}
			}
			*/
			
			/*
			for(String scXML :  fileList)
			{
				response = client.put((scXML), 0);
				if (response!=null) {
					
					System.out.println(response.getCode());
					System.out.println(response.getOptions());
					System.out.println(response.getResponseText());
					
					System.out.println("\nADVANCED\n");
					// access advanced API with access to more details through .advanced()
					System.out.println(Utils.prettyPrint(response));
					
				} else {
					System.out.println("No response received.");
				}
				
			}
			*/
			
		} else {
			// display help
			System.out.println("Californium (Cf) GET Client");
			System.out.println("(c) 2014, Institute for Pervasive Computing, ETH Zurich");
			System.out.println();
			System.out.println("Usage: " + testCient_DTLS.class.getSimpleName() + " URI");
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
				/*
				if(index == 0)
					uris = new URI("coap://127.0.0.1:5683/test");
				else
					uris = new URI("coap://127.0.0.1:5683/test1");
					*/
				
				//uris = new URI("coap://140.120.15.155:5684/test"+index);
				//uris = new URI("coap://140.120.15.136:5683/test"+index);
				//uris = new URI("coap://140.120.15.159:5683/test"+index);
				
				//uris = new URI("coaps://140.120.15.155/test"+index);
				//uris = new URI("coaps://140.120.15.136:5684/test"+index);
				uris = new URI("coaps://140.120.15.159/test"+index);
				
				//uris = new URI("coaps://140.120.15.155:5683/test1");
				//uris = new URI("coaps://140.120.15.153:5684/test1");
			
			DTLSConnector dtlsConnectorR = connector();
			
			//start啟用看看
//			try 
//			{
//				dtlsConnectorR.start();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			NetworkConfig config = new NetworkConfig();
			config.setInt(NetworkConfig.Keys.PREFERRED_BLOCK_SIZE,1024);
			
			CoapClient client;
			
			client = new CoapClient(uris);
			//client.setEndpoint(new CoapEndpoint(dtlsConnectorR, NetworkConfig.getStandard()));
			client.setEndpoint(new CoapEndpoint(dtlsConnectorR, config));
			
			client.setTimeout(20000);
			
			//CoapResponse response = client.put(fileList.get(3), index);
			CoapResponse response = client.put(document, MediaTypeRegistry.APPLICATION_XML);
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
				//dtlsConnectorR.destroy();

				/*
				dtlsConnectorR.stop();
				
				dtlsConnectorR = connector();
				client = new CoapClient(uris);
				client.setEndpoint(new CoapEndpoint(dtlsConnectorR, NetworkConfig.getStandard()));
				dtlsConnectorR.start();
				response = client.put(document, index);
				
				//boolean isNotNull = false ;
				while(response!=null)
				{
					dtlsConnectorR.stop();
					
					dtlsConnectorR = connector();
					client = new CoapClient(uris);
					client.setEndpoint(new CoapEndpoint(dtlsConnectorR, NetworkConfig.getStandard()));
					dtlsConnectorR.start();
					response = client.put(document, index);
					
					//isNotNull = (response!=null);
				}
				
				System.out.println(response.getCode());
				System.out.println(response.getOptions());
				System.out.println(response.getResponseText());
				
				System.out.println("\nADVANCED\n");
				// access advanced API with access to more details through .advanced()
				System.out.println(Utils.prettyPrint(response));
				*/
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
