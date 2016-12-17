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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;


public class testCient {

	/*
	 * Application entry point.
	 * 
	 */	
	public static void main(String args[]) throws FileNotFoundException {
		
		URI uri = null; // URI parameter of the request
		
		if (args.length > 0) {
			
			// input URI from command line arguments
			try 
			{
				//uri = new URI(args[0]);
				
				uri = new URI("coap://140.120.15.159:5683/TestConcurrentResource");
				//uri = new URI("coap://140.120.15.136:5683/TestConcurrentResource");
			} 
			catch (URISyntaxException e) {
				System.err.println("Invalid URI: " + e.getMessage());
				System.exit(-1);
			}
			
			String[] xmlfile = {"testPubSub1.xml" , "testPubSub2.xml" , "testPubSub3.xml" ,"testPubSubAll.xml"};
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
			
			
			CoapClient client = new CoapClient(uri);
			CoapResponse response ;
			for(int i = 0 ; i < 5000 ; i++)
			{
				
				//response = client.put((timeArray[0]+","+fileList.get(3)), 0);
				response = client.put(fileList.get(3), 0);
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
				
				
				/*
				new Thread (()->{
					
					CoapClient client;
					try 
					{
						client = new CoapClient(new URI(args[0]));
						
						//CoapResponse response = client.put(fileList.get(3), 0);
						CoapResponse response = client.put("QQ", 0);
						
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
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}).start();
				*/
			}
			
			
			
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
			System.out.println("Usage: " + testCient.class.getSimpleName() + " URI");
			System.out.println("  URI: The CoAP URI of the remote resource to GET");
		}
	}

}
