package PublishSubscribe.FogNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
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

import Sink.TaskPool;
import Sink.TaskReducerFactory;
import Sink.XMLDogTask;
import Sink.XMLDogTaskReducer;
import Sink.meterStream;
import XMLfilter.callXMLDogFilter;

public class FilterManagement 
{
	String time = null;
	boolean timeIsNotNull = false ;
	
	public void setTime(String initialTime)
	{
		if(time == null)
		{
//			try
//			{
//				Connection con = FogDB.getConnection();
//				Statement st = con.createStatement();
//				
//				String sql = "SELECT FROM StorageSourceRecode WHERE ";
//				
//			}
//			catch(SQLException ex)
//			{
//				ex.printStackTrace();
//    			System.out.println(ex.getMessage());
//    			System.out.println(ex.getLocalizedMessage());
//			}
//			catch(ClassNotFoundException ex)
//			{
//				ex.printStackTrace();
//    			System.out.println(ex.getMessage());
//    			System.out.println(ex.getLocalizedMessage());
//			}
			
			time = initialTime;
			timeIsNotNull = true;
		}
		
	}
	
	
	//Pallier
	BigInteger[] AnswerValueArray;
	
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
