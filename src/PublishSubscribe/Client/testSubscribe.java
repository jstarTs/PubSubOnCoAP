package PublishSubscribe.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;

import PublishSubscribe.testCient;

public class testSubscribe 
{
	
public static void main(String args[]) throws FileNotFoundException {
		
		URI uri = null; // URI parameter of the request
		
		if (args.length > 0) {
			
			// input URI from command line arguments
			try {
				uri = new URI(args[0]);
			} catch (URISyntaxException e) {
				System.err.println("Invalid URI: " + e.getMessage());
				System.exit(-1);
			}
			
		/*	
			switch(args[0].toLowerCase())
			{
				case "get":
				case "post":
				case "put":
				case "delete":
				case "discover":
				default:
			}
		*/
			
			
			//CoapClient client = new CoapClient(uri);
			//CoapResponse response = client.post("testTopic1", 0);
			
			/*
			CoapResponse response = client.post("test", 0);
			response = client.post("test1", 0);
			response = client.post("test2", 0);
			response = client.post("test3", 0);
			response = client.post("test4", 0);
			*/
			
			//CoapClient client = new CoapClient(uri+"/testTopic1");
			//CoapResponse response = client.get();
			//CoapResponse response = client.delete();
			
			/*
			CoapClient client = new CoapClient(uri+"/test");
			CoapResponse response = client.get();
			client = new CoapClient(uri+"/test1");
			response = client.get();
			client = new CoapClient(uri+"/test2");
			response = client.get();
			client = new CoapClient(uri+"/test3");
			response = client.get();
			client = new CoapClient(uri+"/test4");
			response = client.get();
			*/
			
			CoapClient client = new CoapClient(uri);
			
			Scanner sc = new Scanner(new File("./xmldata/test1.xml"));
			
			String xmlD = "";
			while(sc.hasNextLine())
			{
				xmlD += sc.nextLine();
			}
			
			CoapResponse response = client.put(xmlD, 0);
			
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
