

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Sink.XMLDogTaskReducer;
import Sink.meterStream;
import XMLfilter.callXMLDogFilter;

public class testSinkUsingXMLdog 
{
	
	public static void main(String[] args) throws MalformedURLException
	{
		// [0]Thread number  [1]Meter Number  [2]Total document Run
		
		/*
		int threadNum = Integer.parseInt(args[0]);
		int meterNum  = Integer.parseInt(args[1]);
		int totalDocNum = Integer.parseInt(args[2]);
		*/
		System.out.println("[0]Thread number  [1]Meter Number  [2]Total document Run");
		int threadNum = 1;
		int totalDocNum = 10000;
		int meterNum = 100;
		
		System.gc();
		
		meterStream[] ms = new meterStream[meterNum]; 
		Thread myThreads[] = new Thread[threadNum];
		float value = 0f,answerValue = 0f;
		int finishedJobs = 0 ,allFinishedJobs = 0;
		
		
		for(int i = 0 ; i < meterNum ; i++)
		{
			ms[i] = new meterStream();
			ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
		}
		long starttime = System.currentTimeMillis();

		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		 ExecutorService reducerService = Executors.newCachedThreadPool(); 
		List<Future<List<String>>> resultList = null;
		
		for(int i = 0 ; i < totalDocNum ; i++ )
		{
			try 
			{

				long streamlatency = System.currentTimeMillis();
				callXMLDogFilter myFilter = new callXMLDogFilter();
				myFilter.setStream(ms[i%meterNum].getStream());
				
				Future<List<String>> result = executorService.submit(myFilter);
				if(i % meterNum == 0) 
				{
					resultList = new ArrayList<Future<List<String>>>();
				} 
				resultList.add(result);
				if(resultList.size() == meterNum) 
				{
					String taskId = (i/meterNum)+"";
					XMLDogTaskReducer reducer = new XMLDogTaskReducer(taskId);
					reducer.resultList = resultList;					
					//reducerService.execute(reducer); //因為我目前改用Call 所以影響到Runnable 的run
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		executorService.shutdown();  
		reducerService.shutdown();
		System.out.println(("start 1time:" + starttime));
	}
}
