package PublishSubscribe.FogNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
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

public class FilterManagement implements Runnable
{
	String time = null ;
	int PreviousAccessTime ;
	boolean timeIsNotNull = false ;
	int sensorTotal = 0 ;
	long AccessTime ;
	List<String> topicHashList = new ArrayList<String>();
	
	public FilterManagement() 
	{
		
	}
	
	public void setSensorNum(int number)
	{
		sensorTotal = number;
		
	}
	
	public String getTime()
	{
		return time;
	}
	
	public void setInitialTime(String initialTime , long AccessTime)
	{
		time = initialTime;
		timeIsNotNull = true;
	}
	
	public void setTime()
	{
		try
		{
			Connection con = FogDB.getConnection();
			Statement st = con.createStatement();
			
			String sql = "SELECT * FROM StorageSourceRecords WHERE AccessTime = " + " (Select min(AccessTime) from StorageSourceRecords where (AccessTime > "+AccessTime+") AND ( Time != \'"+time+"\' ) );";
			String count = "SELECT count(Time) as Total FROM StorageSourceRecords WHERE AccessTime = " + " (Select min(AccessTime) from StorageSourceRecords where (AccessTime > "+AccessTime+") AND ( Time != \'"+time+"\' ) );";
			
			ResultSet rs ;
			
			rs = st.executeQuery(count);
			rs.next();
			if(rs.getInt("Total")==0)
			{
				con.close();
				timeIsNotNull = false;
				time = null;
				//System.out.println("6666");
			}
			else
			{
				rs = st.executeQuery(sql);
				rs.next();
				time = rs.getString("Time");
				AccessTime = rs.getLong("AccessTime");
				
				con.close();
				//System.out.println("7777");
			}
			
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			System.out.println(ex.getLocalizedMessage());
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			System.out.println(ex.getLocalizedMessage());
		}
		
	}
	
	//test用 , 非正式
	public void setTopic() throws FileNotFoundException
	{
		topicHashList.clear();
		
		Scanner sc = new Scanner(new File("testData/testTopic"));
		
		while(sc.hasNextLine())
		{
			topicHashList.add(sc.nextLine());
		}
	}
	
	public void selectData() throws InterruptedException, ExecutionException, FileNotFoundException
	{
		while(time==null)
		{
			System.out.println("000");
			
		}
		
		try
		{
			Connection con = FogDB.getConnection();
			Statement st = con.createStatement();
			String count = "SELECT Count(DataRecord) AS total FROM StorageSourceRecords WHERE Time = \'"+time+"\';" ;
			String query = "SELECT DataRecord FROM StorageSourceRecords WHERE Time = \'"+time+"\';" ;
			ResultSet rs ; 
			
			rs = st.executeQuery(count);
			rs.next();
			while(rs.getInt("total")!=sensorTotal)
			{
				rs = st.executeQuery(count);
				rs.next();
				System.out.println(rs.getInt("total"));
			}
			
			List<byte[]> list = new ArrayList<byte[]>();
			String detection;
			String[] detectionArray;
			setTopic();
			boolean checkTopic = false;
			rs = st.executeQuery(query);
			while(rs.next())
			{
				detection = rs.getString("DataRecord");
				detectionArray = detection.split("<a");
				for(int i = 1 ; i < detectionArray.length ; i++)
				{
					for(String topic : topicHashList)
					{
						if(detectionArray[i].substring(0, 32).equalsIgnoreCase(topic.substring(1)))
						{
							checkTopic = true;//以String match 判斷是否含有受訂閱的主題
						}
						if(checkTopic == true)
							break;
					}
					if(checkTopic == true)
						break;	
				}
				if(checkTopic == true)
					list.add(detection.getBytes());
				checkTopic = false;
				
			}
			con.close();
			useFilterTest(list);
			do
			{
				setTime();
				//System.out.println("555");
			}while(timeIsNotNull == false);
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			System.out.println(ex.getLocalizedMessage());
		}
		catch(ClassNotFoundException ex)
		{
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			System.out.println(ex.getLocalizedMessage());
		}

	}
	
	//Pallier
	BigInteger[] AnswerValueArray;
	
	public void useFilterTest(List<byte[]> list) throws InterruptedException, ExecutionException
    {
    	int threadNum = 10;
		int meterNum = list.size();
		//int runtime = Integer.parseInt(args[2]);
		//int totalDocNum = meterNum*runtime;
		
		int typeNum = topicHashList.size();//指的是幾個term
		
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
				//reducerService.execute(reducer);
				Future<BigInteger[]> reducerResult = reducerService.submit(reducer);
				
				AnswerValueArray = reducerResult.get();
				
				//System.out.println(AnswerValueArray[1]);
			}
			// resultList.add(result);
			
//			for(BigInteger bi : AnswerValueArray)
//			{
//				System.out.println(bi.toString());
//			}
			
		}
		executorService.shutdown(); 
		executorService.awaitTermination(30, TimeUnit.MINUTES);
		reducerService.shutdown();
		reducerService.awaitTermination(30, TimeUnit.MINUTES);
				
		long endTime = System.currentTimeMillis();
		//System.out.println(("MeterNum: "+meterNum+" , ThreadNum: "+threadNum+" , "+"duration:" + (endTime - starttime)));
		System.out.println(("MeterNum: "+meterNum+" , ThreadNum: "+threadNum+" , "+"endTime:" + endTime ));
		
		System.gc();
		//selectData();
    }
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		/*
		while(true)
		{
			if(timeIsNotNull == false && time == null)
			{
				break;
			}
		}
		*/
		System.out.println("000");
		try {
			selectData();
			System.out.println("111");
		} catch (FileNotFoundException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		while(true)
		{
			if(timeIsNotNull == false && time == null)
			{
				//System.out.println("111");
				
			}
			else if(timeIsNotNull == false)
			{
				//System.out.println("222");
			}
			else
			{
				try 
				{
					//System.out.println("333");
					selectData();
					
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (ExecutionException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
	}
    
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException, ExecutionException
	{
		Scanner sc ;
		
		String[] xmlfile = {"testPubSub1.xml" , "testPubSub2.xml" , "testPubSub3.xml" ,"testPubSubAll.xml"};
		String temp = "";
		
		List<byte[]> fileList = new ArrayList<byte[]>();
		
		FilterManagement fm = new FilterManagement();
		
		fm.topicHashList.add("a85EF20040AB4A17A1908D5575C2A1F0D");
		fm.topicHashList.add("aAFF6300E3C9BAC889763AE1A06901A7D");
		
		/*
		for(String filename : xmlfile)
		{
			sc = new Scanner(new File("testData/"+filename));
			
			while(sc.hasNextLine())
			{
				temp += sc.nextLine();
			}
			fileList.add(temp.getBytes());
			
			temp = "";
		}
		*/
		
		/*
		sc = new Scanner(new File("testData/"+"testPubSubAll.xml"));
		
		while(sc.hasNextLine())
		{
			temp += sc.nextLine();
		}
		fileList.add(temp.getBytes());
		
		fm.useFilterTest(fileList);
		*/
		
		/*
		fm.AccessTime=0;
		fm.setTime();
		System.out.println(fm.time);
		System.out.println(fm.AccessTime);
		fm.setTime();
		System.out.println(fm.time);
		System.out.println(fm.AccessTime);
		fm.setTime();
		System.out.println(fm.time);
		System.out.println(fm.AccessTime);
		fm.setTime();
		System.out.println(fm.time);
		System.out.println(fm.AccessTime);
		fm.setTime();
		*/
		/*
		fm.timeIsNotNull = true;
		fm.setSensorNum(4);
		fm.setTopic();
		fm.AccessTime= new Long("1480232584476") ;
		fm.time = "hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bg";
		for(int i=0 ; i<4 ; i++)
		{
			fm.selectData();
		}
		*/
		
	}

	
}
