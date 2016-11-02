
package trial.runTransport;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import Sink.TaskPool;
import Sink.TaskReducerFactory;
import Sink.XMLDogTask;
import Sink.XMLDogTaskReducer;
import Sink.YFilterTask;
import Sink.YFilterTaskReducer;
import Sink.meterStream;
import XMLfilter.callXMLDogFilter;
import XMLfilter.callYFilter;

public class testSinkYFilter 
{
	public static void main(String[] args) throws MalformedURLException, InterruptedException
	{
		// [0]Thread number  [1]Meter number  [2]Runtimes  [3] Type Number
		
				/*
				int threadNum = Integer.parseInt(args[0]);
				int totalDocNum = Integer.parseInt(args[2]);
				int meterNum = 100;
				if(Integer.parseInt(args[1]) > 0) {
					meterNum = Integer.parseInt(args[1]);
				}
				*/
						
		int threadNum = Integer.parseInt(args[0]);
		int meterNum = Integer.parseInt(args[1]);
		int runtime = Integer.parseInt(args[2]);
		int totalDocNum = meterNum*runtime;
		
		int typeNum = Integer.parseInt(args[3]);
		
		//int reducerNum = Integer.parseInt(args[3]);
		
		//System.gc();
		
		/*
		String[] xpathArray = new String[] {"/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/",
				"/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/"};//YFilter專用
		*/
		/*
		String[] xpathArray = new String[] {"/a85EF20040AB4A17A1908D5575C2A1F0D/a5196F1040C636101B5797C3CBC926613//CipherValue",
				"/a85EF20040AB4A17A1908D5575C2A1F0D/aE0AC20ADCE6FFEE48C7151B070AA5737/a490AA6E856CCF208A054389E47CE0D06//CipherValue",
				"/a85EF20040AB4A17A1908D5575C2A1F0D/aA76D4EF5F3F6A672BBFAB2865563E530//CipherValue",
				"/a85EF20040AB4A17A1908D5575C2A1F0D/a6A0D9EAEE314C567FD72FB97EE707A36//CipherValue"};
		*/
		
		meterStream[] ms = new meterStream[meterNum]; 			
		
		for(int i = 0 ; i < meterNum ; i++)
		{
			ms[i] = new meterStream();
			//ms[i].setUrl("http://service2.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");									
			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testE1.xml");
			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/test20T1.xml");
			//ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/test20TE1.xml");
			ms[i].setUrl("http://140.120.15.134/xml/xmldata/test20TE1.xml");
		}
		
		List<String> xpathList = new ArrayList<String>();
		//Collections.addAll(xpathList, xpathArray);
		try 
		{
			Scanner sc = new Scanner(new File("./YFilterXpathList"));
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

		List<YFilterTask> taskPool = null;
		
		if(threadNum > 0) {
			taskPool = TaskPool.CreateYFilterTasks(threadNum, xpathList);
			
			/*
			for(int i = 0; i < threadNum; ++i) {
				DefaultNamespaceContext nsContext = new DefaultNamespaceContext(); // an implementation of javax.xml.namespace.NamespaceContext
				nsContext.declarePrefix("xsd", Namespaces.URI_XSD);
				dog = new XMLDog(nsContext);
				try 
				{
					Expression xpath1 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/@aB80BB7740288FDA1F201890375A60C8F");
					Expression xpath2 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/@a2063C1608D6E0BAF80249C42E2BE5804");	
				} 
				catch (SAXPathException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			} */
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		ExecutorService reducerService = Executors.newCachedThreadPool(); 
		//ExecutorService reducerService = Executors.newFixedThreadPool(reducerNum);
		List<Future<List<String>>> resultList = null;
		
		long starttime = System.currentTimeMillis();
		for(int i = 0 ; i < totalDocNum ; i++ )
		{
			/*
			if(i!=0 && i%meterNum==0)
			{
				Thread.sleep(1000);
			}
			*/
				
			callYFilter myFilter = new callYFilter(ms[i%meterNum].strURI);
			
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

				YFilterTaskReducer reducer = (YFilterTaskReducer) TaskReducerFactory.Create("YFilterTaskReducer");
				
				reducer.typeNum = typeNum ;
				reducer.queryNumPerType = xpathList.size()/typeNum;
				reducer.SetID(taskID);
				reducer.resultList = resultList;					
				reducerService.execute(reducer);
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
