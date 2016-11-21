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
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.core.server.resources.CoapExchange;
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



public class testTryFog extends CoapServer {

	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
    /*
     * Application entry point.
     */
	
	BigInteger[] AnswerValueArray;
	
    public static void main(String[] args) {
        
        try {

            // create server
            testTryFog server = new testTryFog();
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
     * Constructor for a new Hello-World server. Here, the resources
     * of the server are initialized.
     */
    public testTryFog() throws SocketException {
        
        // provide an instance of a Hello-World resource
        add(new TestResource());
        add(new TestPubSubResource());
    }

    /*
     * Definition of the Hello-World Resource
     */
    class TestResource extends CoapResource 
    {
    	
    	List<byte[]> list = new ArrayList<byte[]>();
    	int f;
    	
        public TestResource() {
            
            // set resource identifier
            super("test");
            
            // set display name
            getAttributes().setTitle("Hello-World Resource");
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            
            // respond to the request
            exchange.respond("Hello World!");
        }
        
        @Override
        public void handlePUT(CoapExchange exchange) {
            
            // respond to the request
        	System.out.println(exchange.getRequestText());
        	//InputStream is = new StringBufferInputStream(exchange.getRequestText());
        	//is = new ByteArrayInputStream(exchange.getRequestPayload());
        	
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
        	
        	//原先想說直接丟到thread處理，目前先改直接丟DB
        	//list.add(exchange.getRequestPayload());
        	//System.out.println(list.size());
        	/*
        	if(list.size()==10)
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
        	
        	
        	//丟DB
        	try
        	{
        		Connection con = FogDB.getConnection();
        		Statement st = con.createStatement();
        		
        		String[] payload = exchange.getRequestText().split(",");
        		String sql = "INSERT INTO StorageSourceRecode VALUES (\'"+payload[1]+"\',\'"+payload[0]+"\');";
        		
        		st.executeUpdate(sql);
        		
        	}
        	catch(SQLException | ClassNotFoundException ex)
        	{
        		ex.printStackTrace();
    			System.out.println(ex.getMessage());
    			System.out.println(ex.getLocalizedMessage());
        	}
        	
        	
        	
        	
        	
            exchange.respond("Good");
        }
        
        public void recieveData()
        {
        	
        }
        
        public void useFilterTest(List<byte[]> list) throws InterruptedException
        {
        	int threadNum = 10;
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
    			Scanner sc = new Scanner(new File("./XpathList"));
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
    		
    		long starttime = System.currentTimeMillis();
    		
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
    				reducerService.execute(reducer);
    				
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
