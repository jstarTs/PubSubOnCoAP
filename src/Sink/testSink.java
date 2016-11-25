package Sink;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jaxen.saxpath.SAXPathException;

import jlibs.xml.DefaultNamespaceContext;
import jlibs.xml.Namespaces;
import jlibs.xml.sax.dog.XMLDog;
import XMLfilter.callSaxonFilter;
import XMLfilter.callXMLDogFilter;
import XMLfilter.callYFilter;

public class testSink 
{
	public static List<XMLDog> xmlDogList = new ArrayList<XMLDog>();
	
	public static void main(String[] args) throws MalformedURLException, InterruptedException
	{
		// [0]Thread number  [1]Meter number  [2]Total documents to Run   
		
//		int threadNum = Integer.parseInt(args[0]);
//		int totalDocNum = Integer.parseInt(args[2]);

		int meterNum = 10;
//		if(Integer.parseInt(args[1]) > 0) {
//			meterNum = Integer.parseInt(args[1]);
//		}
		
		int threadNum = 5;
		int totalDocNum = 10;
		
		System.gc();
		
		meterStream[] ms = new meterStream[meterNum]; 			
		
		for(int i = 0 ; i < meterNum ; i++)
		{
			ms[i] = new meterStream();
			//ms[i].setUrl("http://service2.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
			ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");									
			
		}
		
		/*
		String[] xpathArray = new String[] {"/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/@aB80BB7740288FDA1F201890375A60C8F",
				"/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/@a2063C1608D6E0BAF80249C42E2BE5804"};
		*/
		
		String[] xpathArray = new String[] {"/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/",
				"/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/"};//YFilter專用
		
		List<String> xpathList = new ArrayList<String>();
		Collections.addAll(xpathList, xpathArray);
		
		//List<XMLDogTask> taskPool = null;
		//List<SaxonTask> taskPool = null;
		List<YFilterTask> taskPool = null;
		
		if(threadNum > 0) {
			//taskPool = TaskPool.CreateXMLDogTasks(threadNum, xpathList);
			//taskPool = TaskPool.CreateSaxonTasks(threadNum, xpathList);
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
		List<Future<List<String>>> resultList = null;
		
		long starttime = System.currentTimeMillis();
		
		for(int i = 0 ; i < totalDocNum ; i++ )
		{
			//callXMLDogFilter myFilter = new callXMLDogFilter(ms[i%meterNum].strURI);
			//callSaxonFilter myFilter = new callSaxonFilter(ms[i%meterNum].strURI);
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
				
				//XMLDogTaskReducer reducer = (XMLDogTaskReducer) TaskReducerFactory.Create("XMLDogTaskReducer");
				//SaxonTaskReducer reducer = (SaxonTaskReducer) TaskReducerFactory.Create("SaxonTaskReducer");
				YFilterTaskReducer reducer = (YFilterTaskReducer) TaskReducerFactory.Create("YFilterTaskReducer");
				
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
		System.out.println(("duration:" + (endTime - starttime)));
	}
}