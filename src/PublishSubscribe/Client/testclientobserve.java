package PublishSubscribe.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;

import PublishSubscribe.testCient;

public class testclientobserve 
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
			
			CoapClient client = new CoapClient(uri + "/obs");
			
			
			
			System.out.println("===============\nCO01+06");
			System.out.println("---------------\nGET /obs with Observe");
			CoapObserveRelation relation1 = client.observe(
					new CoapHandler() {
						@Override public void onLoad(CoapResponse response) {
							String content = response.getResponseText();
							System.out.println("-CO01----------");
							System.out.println(content);
						}
						
						@Override public void onError() {
							System.err.println("-Failed--------");
						}
					});
			
			try { Thread.sleep(6*1000); } catch (InterruptedException e) { }
			System.out.println("---------------\nCancel Observe");
			relation1.reactiveCancel();
			try { Thread.sleep(6*1000); } catch (InterruptedException e) { }
			
			
			/*
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
