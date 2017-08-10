package PublishSubscribe.FogNode;
import org.eclipse.californium.core.*;
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
 *    Kai Hudalla (Bosch Software Innovations GmbH) - add endpoints for all IP addresses
 ******************************************************************************/

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathException;

import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.californium.core.server.resources.ConcurrentCoapResource;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.ScandiumLogger;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import org.eclipse.californium.scandium.dtls.pskstore.InMemoryPskStore;
import org.jaxen.saxpath.SAXPathException;
import org.xml.sax.InputSource;

import Sink.TaskPool;
import Sink.TaskReducerFactory;
import Sink.XMLDogTask;
import Sink.XMLDogTaskReducer;
import Sink.meterStream;
import XMLfilter.callXMLDogFilter;
import jlibs.xml.DefaultNamespaceContext;
import jlibs.xml.Namespaces;
import jlibs.xml.sax.dog.NodeItem;
import jlibs.xml.sax.dog.XMLDog;
import jlibs.xml.sax.dog.XPathResults;
import jlibs.xml.sax.dog.expr.Expression;
import trial.testFilter.useXmldog;



public class testTryFog_DTLS extends CoapServer {

	
//	static {
//		CaliforniumLogger.initialize();
//		CaliforniumLogger.setLevel(Level.CONFIG);
//		ScandiumLogger.initialize();
//		ScandiumLogger.setLevel(Level.FINER);
//	}
	
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
    /*
     * Application entry point.
     */
	public static final int DTLS_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_SECURE_PORT);
	
	private static final String TRUST_STORE_PASSWORD = "rootPass";
	private static final String KEY_STORE_PASSWORD = "endPass";
	private static final String KEY_STORE_LOCATION = "PublishSubscribe/keyStore.jks";
	private static final String TRUST_STORE_LOCATION = "PublishSubscribe/trustStore.jks";
	
	BigInteger[] AnswerValueArray;
	FilterManagement fm = new FilterManagement();
	List<byte[]> list = new ArrayList<byte[]>();
	long starttime ;
	
	static int listSize , threads;
	
    public static void main(String[] args) {
        
    	//args[0] -> data number ; args[1] -> thread number
    	
    	listSize = Integer.parseInt(args[0]);
    	threads = Integer.parseInt(args[1]);
    	
    	try {

        	
        	// create server
            //testTryFog server = new testTryFog();
            
            testTryFog_DTLS server = new testTryFog_DTLS(listSize);
            
            //server.fm.setSensorNum(4);
            //server.fm.setSensorNum(Integer.parseInt(args[0]) );
            
            
            
            // add endpoints on all IP addresses
            try 
            {
				server.addEndpoints();
				server.start();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            
            //server.fm.run();//用DB時用
            
            /*
            try {
				//server.fm.selectData();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            */

        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
    }

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
    	InputStream inTrust = testTryFog_DTLS.class.getClassLoader().getResourceAsStream(TRUST_STORE_LOCATION);
    	trustStore.load(inTrust, TRUST_STORE_PASSWORD.toCharArray());

    	// You can load multiple certificates if needed
    	Certificate[] trustedCertificates = new Certificate[1];
    	trustedCertificates[0] = trustStore.getCertificate("root");
    	
    	// load the key store
    	KeyStore keyStore = KeyStore.getInstance("JKS");
    	InputStream in = testTryFog_DTLS.class.getClassLoader().getResourceAsStream(KEY_STORE_LOCATION);
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
				
//				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
//				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
    	
    	// server.addEndpoint(new CoAPEndpoint(new DTLSConnector(new InetSocketAddress("::1", DTLS_PORT)), NetworkConfig.getStandard()));
		// server.addEndpoint(new CoAPEndpoint(new DTLSConnector(new InetSocketAddress("127.0.0.1", DTLS_PORT)), NetworkConfig.getStandard()));
		// server.addEndpoint(new CoAPEndpoint(new DTLSConnector(new InetSocketAddress("2a01:c911:0:2010::10", DTLS_PORT)), NetworkConfig.getStandard()));
		// server.addEndpoint(new CoAPEndpoint(new DTLSConnector(new InetSocketAddress("10.200.1.2", DTLS_PORT)), NetworkConfig.getStandard()));

    }

    /*
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public testTryFog_DTLS() throws SocketException {
        
        // provide an instance of a Hello-World resource
        add(new TestResource());
        add(new TestResource("test1"));
        add(new TestPubSubResource());
        add(new TestConcurrentResource("ten-threaded", 20));
    }
    
    public testTryFog_DTLS(int numberResource) throws SocketException
    {
    	add(new TestResource());
    	for(int i = 1 ; i <= numberResource ; i++)
    	{
    		add(new TestResource("test"+i));
    	}
    }

    /*
     * Definition of the Hello-World Resource
     */
    
    class TestResource extends CoapResource 
    {
    	
    	//List<byte[]> list = new ArrayList<byte[]>();
    	int f;
    	//long starttime ;
    	
        public TestResource() {
            
            // set resource identifier
            super("test");
            
            // set display name
            getAttributes().setTitle("Hello-World Resource");
            getAttributes().addResourceType("block");
    		getAttributes().setMaximumSizeEstimate(1280);
        }
        
        public TestResource(String name) {
            
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
        public void handlePUT(CoapExchange exchange) {
        	long AccessTime = System.currentTimeMillis();
            
        	// respond to the request
        	//System.out.println(exchange.getRequestText());
        	//InputStream is = new StringBufferInputStream(exchange.getRequestText());
        	//is = new ByteArrayInputStream(exchange.getRequestPayload());
        	
        	/*
        	try 
        	{
				Connection conn = FogDB.getConnection();
				Statement st = conn.createStatement();
				
				st.executeUpdate("INSERT INTO StorageSourceRecords VALUES (\'"+exchange.getRequestText()+"\',\'"+exchange.getSourceAddress()+"\');");
			} 
        	catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
        	catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	*/
        	
        	//丟DB
        	/*
        	try
        	{
        		Connection con = FogDB.getConnection();
        		Statement st = con.createStatement();
        		
        		String[] payload = exchange.getRequestText().split(",");
        		
        		//payload[1] 內容  ， payload[0] time , exchange.getSourceAddress()
        		//System.out.println(System.currentTimeMillis());
        		//payload[0] = "hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bs";
        		
        		String sql;
        		if(fm.timeIsNotNull == false)
        		{
        			long AccessTime = System.currentTimeMillis();
        			fm.setInitialTime(payload[0],AccessTime);
            		sql = "INSERT INTO StorageSourceRecords VALUES (\'"+payload[1]+"\',\'"+payload[0]+"\',\'"+exchange.getSourceAddress()+"\',"+AccessTime+");";
        		}
        		else
        			sql = "INSERT INTO StorageSourceRecords VALUES (\'"+payload[1]+"\',\'"+payload[0]+"\',\'"+exchange.getSourceAddress()+"\',"+System.currentTimeMillis()+");";

        		st.executeUpdate(sql);
        		
        	}
        	catch(SQLException | ClassNotFoundException ex)
        	{
        		ex.printStackTrace();
    			System.out.println(ex.getMessage());
    			System.out.println(ex.getLocalizedMessage());
        	}
        	*/
        	
        	/*
        	//原先想說直接丟到thread處理，目前先改直接丟DB
        	list.add(exchange.getRequestPayload());
        	//System.out.println(list.size());
        	if(list.size() == 1)
        		System.out.println(AccessTime);
        	
        	if(list.size() == listSize)
        	{
        		try 
        		{
        			useFilterTest(list);
        			list.clear();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	*/
        	synchronized (list) 
        	{
        		list.add(exchange.getRequestPayload());
            	//System.out.println(list.size());
            	if(list.size() == 1)
            	{
            		//System.out.println(AccessTime);
            		starttime = AccessTime;
            	}
            		
            	if(list.size() == listSize)
            	{
            		
            		try 
            		{
            			useFilterTest(list);
            			list = new ArrayList<byte[]>();
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    				
            	}
            	
			}
        	
        	
            exchange.respond("Good");
        }
        
        public void recieveData()
        {
        	
        }
        
        public void useFilterTest(List<byte[]> list) throws InterruptedException
        {
        	//int threadNum = 5;
        	int threadNum = threads;
        	int meterNum = list.size();
    		//int runtime = Integer.parseInt(args[2]);
    		//int totalDocNum = meterNum*runtime;
    		
    		int typeNum = 1;//指的是幾個term
    		
    		meterStream[] ms = new meterStream[meterNum]; 			
    		
    		for(int i = 0 ; i < meterNum ; i++)
    		{
    			ms[i] = new meterStream();
    			//ms[i].setUrl("http://service2.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");									
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testE1.xml");
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/test20T1.xml");
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/test20TE2.xml");
    			ms[i].setByteArray(list.get(i));
    		}
    		
    		List<String> xpathList = new ArrayList<String>();
    		//Collections.addAll(xpathList, xpathArray);
    		try 
    		{
    			//Scanner sc = new Scanner(new File("./XpathList"));
    			Scanner sc = new Scanner(new File("testData/testXpathList"));
    			while(sc.hasNextLine())
    			{
    				xpathList.add(sc.nextLine().trim());
    			}
    		}
    		catch (FileNotFoundException e) 
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		
    		List<XMLDogTask> taskPool = null;

    		if(threadNum > 0) {
    			taskPool = TaskPool.CreateXMLDogTasks(threadNum, xpathList);

    		}
    		
    		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
    		
    		ExecutorService reducerService = Executors.newCachedThreadPool(); 
    		//ExecutorService reducerService = Executors.newFixedThreadPool(reducerNum);
    		
    		List<Future<List<String>>> resultList = null;
    		
    		//long starttime = System.currentTimeMillis();
    		
    		for(int i = 0 ; i < list.size() ; i++ )
    		{
    			
    			//callXMLDogFilter myFilter = new callXMLDogFilter(ms[i%meterNum].strURI);
    			
    			//server client new , 20161011
    			callXMLDogFilter myFilter = new callXMLDogFilter(ms[i%meterNum].bytef);
    			
    			myFilter.SetTaskList(taskPool);
    			// myFilter.setStream(ms[i%meterNum].getStream());
    			/* myThreads[t] = new Thread(testFilter[t]);
    			myThreads[t].start(); */
    			Future<List<String>> result = executorService.submit(myFilter);
    			if(i % meterNum == 0) {
    				resultList = new ArrayList<Future<List<String>>>();
    			} 
    			resultList.add(result);
    			if(resultList.size() == meterNum) {
    				//ExecuteService myReducer = Executors.newFixedThreadPool(threadNum)
    				String taskID = (i/meterNum)+"";
    				
    				XMLDogTaskReducer reducer = (XMLDogTaskReducer) TaskReducerFactory.Create("XMLDogTaskReducer");
    				
    				reducer.typeNum = typeNum ;
    				reducer.queryNumPerType = xpathList.size()/typeNum;
    				reducer.SetID(taskID);
    				reducer.resultList = resultList;					
    				//reducerService.execute(reducer);
    				
    				Future<BigInteger[]> reducerResult = reducerService.submit(reducer);
    				
    				AnswerValueArray = reducer.getAnswerValueArray();
    			}
    			// resultList.add(result);
    			
    		}
    		executorService.shutdown(); 
    		executorService.awaitTermination(30, TimeUnit.MINUTES);
    		reducerService.shutdown();
    		reducerService.awaitTermination(30, TimeUnit.MINUTES);
    				
    		long endTime = System.currentTimeMillis();
    		System.out.println(("MeterNum: "+meterNum+" , ThreadNum: "+threadNum+" , "+"duration:" + (endTime - starttime)));
    		//System.out.println(("MeterNum: "+meterNum+" , ThreadNum: "+threadNum+" , "+"duration:" + endTime));
    		
    		System.gc();
        }
        
    }
    
    class TestConcurrentResource extends ConcurrentCoapResource 
    {
    	
    	//List<byte[]> list = new ArrayList<byte[]>();
    	int f;
    	//long starttime ;
    	
        public TestConcurrentResource() {
            
            // set resource identifier
            super("TestConcurrentResource");
            
            // set display name
            getAttributes().setTitle("Hello-World ConcurrentResource");
            getAttributes().addResourceType("block");
    		getAttributes().setMaximumSizeEstimate(1280);
        }
        
        public TestConcurrentResource(String name , int threadNum)
        {
        	super("TestConcurrentResource",threadNum);
        	
        	getAttributes().setTitle("Hello-World ConcurrentResource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            
            // respond to the request
            exchange.respond("Hello World!");
        }
        
        @Override
        public void handlePUT(CoapExchange exchange) {
        	long AccessTime = System.currentTimeMillis();
            
        	// respond to the request
        	//System.out.println(exchange.getRequestText());
        	//InputStream is = new StringBufferInputStream(exchange.getRequestText());
        	//is = new ByteArrayInputStream(exchange.getRequestPayload());
        	
        	/*
        	try 
        	{
				Connection conn = FogDB.getConnection();
				Statement st = conn.createStatement();
				
				st.executeUpdate("INSERT INTO StorageSourceRecords VALUES (\'"+exchange.getRequestText()+"\',\'"+exchange.getSourceAddress()+"\');");
			} 
        	catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
        	catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	*/
        	
        	//丟DB
        	/*
        	try
        	{
        		Connection con = FogDB.getConnection();
        		Statement st = con.createStatement();
        		
        		String[] payload = exchange.getRequestText().split(",");
        		
        		//payload[1] 內容  ， payload[0] time , exchange.getSourceAddress()
        		//System.out.println(System.currentTimeMillis());
        		//payload[0] = "hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bs";
        		
        		String sql;
        		if(fm.timeIsNotNull == false)
        		{
        			long AccessTime = System.currentTimeMillis();
        			fm.setInitialTime(payload[0],AccessTime);
            		sql = "INSERT INTO StorageSourceRecords VALUES (\'"+payload[1]+"\',\'"+payload[0]+"\',\'"+exchange.getSourceAddress()+"\',"+AccessTime+");";
        		}
        		else
        			sql = "INSERT INTO StorageSourceRecords VALUES (\'"+payload[1]+"\',\'"+payload[0]+"\',\'"+exchange.getSourceAddress()+"\',"+System.currentTimeMillis()+");";

        		st.executeUpdate(sql);
        		
        	}
        	catch(SQLException | ClassNotFoundException ex)
        	{
        		ex.printStackTrace();
    			System.out.println(ex.getMessage());
    			System.out.println(ex.getLocalizedMessage());
        	}
        	*/
        	
        	//原先想說直接丟到thread處理，目前先改直接丟DB
        	exchange.accept();
        	
        	synchronized (list) 
        	{
        		list.add(exchange.getRequestPayload());
            	//System.out.println(list.size());
            	if(list.size() == 1)
            	{
            		//System.out.println(AccessTime);
            		starttime = AccessTime;
            	}
            		
            	if(list.size() == listSize)
            	{
            		/*
            		try 
            		{
            			useFilterTest(list);
            			list.clear();
    				} catch (InterruptedException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    				*/
            	}
            	
			}
        	
        	exchange.respond("Good");
        }
        
        public void recieveData()
        {
        	
        }
        
        public void useFilterTest(List<byte[]> list) throws InterruptedException
        {
        	int threadNum = 15;
    		int meterNum = list.size();
    		//int runtime = Integer.parseInt(args[2]);
    		//int totalDocNum = meterNum*runtime;
    		
    		int typeNum = 1;//指的是幾個term
    		
    		meterStream[] ms = new meterStream[meterNum]; 			
    		
    		for(int i = 0 ; i < meterNum ; i++)
    		{
    			ms[i] = new meterStream();
    			//ms[i].setUrl("http://service2.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");									
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testE1.xml");
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/test20T1.xml");
    			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/test20TE2.xml");
    			ms[i].setByteArray(list.get(i));
    		}
    		
    		List<String> xpathList = new ArrayList<String>();
    		//Collections.addAll(xpathList, xpathArray);
    		try 
    		{
    			//Scanner sc = new Scanner(new File("./XpathList"));
    			Scanner sc = new Scanner(new File("testData/testXpathList"));
    			while(sc.hasNextLine())
    			{
    				xpathList.add(sc.nextLine().trim());
    			}
    		}
    		catch (FileNotFoundException e) 
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		
    		List<XMLDogTask> taskPool = null;

    		if(threadNum > 0) {
    			taskPool = TaskPool.CreateXMLDogTasks(threadNum, xpathList);

    		}
    		
    		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
    		
    		ExecutorService reducerService = Executors.newCachedThreadPool(); 
    		//ExecutorService reducerService = Executors.newFixedThreadPool(reducerNum);
    		
    		List<Future<List<String>>> resultList = null;
    		
    		//long starttime = System.currentTimeMillis();
    		
    		for(int i = 0 ; i < list.size() ; i++ )
    		{
    			
    			//callXMLDogFilter myFilter = new callXMLDogFilter(ms[i%meterNum].strURI);
    			
    			//server client new , 20161011
    			callXMLDogFilter myFilter = new callXMLDogFilter(ms[i%meterNum].bytef);
    			
    			myFilter.SetTaskList(taskPool);
    			// myFilter.setStream(ms[i%meterNum].getStream());
    			/* myThreads[t] = new Thread(testFilter[t]);
    			myThreads[t].start(); */
    			Future<List<String>> result = executorService.submit(myFilter);
    			if(i % meterNum == 0) {
    				resultList = new ArrayList<Future<List<String>>>();
    			} 
    			resultList.add(result);
    			if(resultList.size() == meterNum) {
    				//ExecuteService myReducer = Executors.newFixedThreadPool(threadNum)
    				String taskID = (i/meterNum)+"";
    				
    				XMLDogTaskReducer reducer = (XMLDogTaskReducer) TaskReducerFactory.Create("XMLDogTaskReducer");
    				
    				reducer.typeNum = typeNum ;
    				reducer.queryNumPerType = xpathList.size()/typeNum;
    				reducer.SetID(taskID);
    				reducer.resultList = resultList;					
    				//reducerService.execute(reducer);
    				
    				Future<BigInteger[]> reducerResult = reducerService.submit(reducer);
    				
    				AnswerValueArray = reducer.getAnswerValueArray();
    			}
    			// resultList.add(result);
    			
    		}
    		executorService.shutdown(); 
    		executorService.awaitTermination(30, TimeUnit.MINUTES);
    		reducerService.shutdown();
    		reducerService.awaitTermination(30, TimeUnit.MINUTES);
    				
    		long endTime = System.currentTimeMillis();
    		System.out.println(("MeterNum: "+meterNum+" , ThreadNum: "+threadNum+" , "+"duration:" + (endTime - starttime)));
    		//System.out.println(("MeterNum: "+meterNum+" , ThreadNum: "+threadNum+" , "+"duration:" + endTime));
    		
    		System.gc();
        }
        
    }
    
    class TestPubSubResource extends CoapResource 
    {

		public TestPubSubResource() 
		{
			super("PubSub");
			// TODO Auto-generated constructor stub
			getAttributes().setTitle("Hello-World Resource");
		}

		@Override
        public void handleGET(CoapExchange exchange) {
            
            // respond to the request
            exchange.respond("TestPubSub");
            
            
        }
    	
    }
    
}
