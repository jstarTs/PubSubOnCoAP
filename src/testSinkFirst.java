

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Sink.meterStream;
import Sink.myTaskReducer;
import XMLfilter.callXMLDogFilter;

public class testSinkFirst 
{
	
	public static void main(String[] args) throws MalformedURLException
	{
		// [0]Thread number  [1]Stream number  [2]Total document Run   
		
		int threadNum = Integer.parseInt(args[0]);
		//int meterStreamNum = Integer.parseInt(args[1]);
		int totalDocNum = Integer.parseInt(args[2]);
		
		/*
		int threadNum = 10;
		//int streamNum = 100;
		int totalDocNum = 10000;
		*/
		int meterNum = 100;
		
		System.gc();
		
		//long StartTime = System.currentTimeMillis();
		
		meterStream[] ms = new meterStream[meterNum]; 
		Thread myThreads[] = new Thread[threadNum];
		// runnableXMLDogFilter[] testFilter = new runnableXMLDogFilter[threadNum];
		//myTask[] taskList = new myTask[streamNum];
		
		float value = 0f,answerValue = 0f;
		int finishedJobs = 0 ,allFinishedJobs = 0;
		
		
		for(int i = 0 ; i < meterNum ; i++)
		{
			ms[i] = new meterStream();
			//ms[i].setUrl("http://service2.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
			ms[i].setUrl("http://program.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
						
			//myThreads[i] = new Thread(testFilter[i%threadNum]);
			//testFilter[i].setFileStream("./xmldata/testxml"+(i+1)+".xml");
			
		}
//		for(int i = 0; i < threadNum ; ++i) {
//			testFilter[i] = new runnableXMLDogFilter();
//		}
		
		long starttime = System.currentTimeMillis();
		
		int t , usedThread = 0 , hasfinishion = 0;
		boolean hasfinished = false;
		// List<Integer> taskList = new ArrayList(totalDocNum);
		/*
		for(t = 0 ; t < threadNum ; t++)
		{
			
			try 
			{
				taskList.add(t);
				long streamlatency = System.currentTimeMillis();
				System.out.println(streamlatency+" zzzaaa");
				testFilter[t].setStream(ms[t%meterNum].getStream());
				myThreads[t] = new Thread(testFilter[t]);
				myThreads[t].run();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} */
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		 ExecutorService reducerService = Executors.newCachedThreadPool(); 
		List<Future<Float>> resultList = null;
		
		for(int i = 0 ; i < totalDocNum ; i++ )
		{
			// Check thread is usable
			try 
			{
//				for(t = 0 ; t < threadNum ; t++)
//				{
//					if(!myThreads[t].isAlive() )
//						break;
//					else if(t == threadNum-1)
//					{
//						t = -1;
//					}
//				}
//				usedThread++;
				
				// taskList.add(i);
				//testFilter[t].setStream(ms[i%meterNum].getStream("./xmldata/testxml"+(i+1)+".xml"));
				long streamlatency = System.currentTimeMillis();
				//System.out.println(streamlatency+" zzz");
				callXMLDogFilter myFilter = new callXMLDogFilter();
				myFilter.setStream(ms[i%meterNum].getStream());
				/* myThreads[t] = new Thread(testFilter[t]);
				myThreads[t].start(); */
//				Future<Float> result = executorService.submit(myFilter);
				if(i % meterNum == 0) {
					resultList = new ArrayList<Future<Float>>();
				} 
//				resultList.add(result);
				if(resultList.size() == meterNum) {
					//ExecuteService myReducer = Executors.newFixedThreadPool(threadNum)
					String taskId = (i/meterNum)+"";
					myTaskReducer reducer = new myTaskReducer(taskId);
					reducer.resultList = resultList;					
					reducerService.execute(reducer);
				}
				// resultList.add(result);
				
//				if( (usedThread == threadNum) || hasfinished)
//				{
//					if(usedThread == threadNum)
//						hasfinishion = usedThread;
//					while((finishedJobs != hasfinishion) ) 
//					{
//						//finishedJobs = 0;
//						//value = 0f;
//						for(int j = 0; j < threadNum; ++j) 
//						{
//							if( test[j].isFinished) 
//							{
//								++finishedJobs;
//								value += test[j].getValue();
//								test[j].isFinished=false;
//								usedThread--;
//								//System.out.println("b");
//								//System.out.println(finishedJobs);
//								
//								allFinishedJobs++;
//							}
//						}
//					}
//					finishedJobs = 0;
//					hasfinishion = 0;
//					for(int j = 0 ; j < threadNum ; j++)
//					{
//						answerValue += test[j].getValue();
//					}
//					hasfinished = false;
//					System.out.println(allFinishedJobs);
//				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		executorService.shutdown();  
		reducerService.shutdown();
//		answerValue = 0.0f;
//		for(Future<Float> result: resultList) {
//			try{
//				while(!result.isDone());
//				answerValue +=  result.get();
//			 }catch(ExecutionException e){   
//                 e.printStackTrace();   
//             } catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}finally{   
//                 //启动一次顺序关闭，执行以前提交的任务，但不接受新任务  
//                 executorService.shutdown();   
//             } 
//		}
//		do{
//			hasfinishion = 0;
//			answerValue = 0.0f;
//			for( int i = 0; i < taskList.size(); ++i) {
//				if(testFilter[taskList.get(i)%threadNum].isFinished) {
//					hasfinishion++;
//					answerValue += testFilter[taskList.get(i)%threadNum].getValue();
//					System.out.println(taskList.get(i)+" , "+taskList.size() );
//					//test[taskList.get(i)].isFinished = false;
//				}
//			}
//		} while(hasfinishion != taskList.size());				

//		System.out.println(allFinishedJobs);
		
		//long endtime = System.currentTimeMillis();
//		System.out.println(answerValue);
		System.out.println(("start 1time:" + starttime));
	}
}


/*
for(int i = 0 ; i < 100 ; i++)
{
	test[i%threadNum] = new runnableXMLDogFilter();
	//test[i%threadNum].setFileStream("./xmldata/testxml"+(i+1)+".xml");
	test[i%threadNum].setUrl("http://service2.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
	myThreads[i%threadNum] = new Thread(test[i%threadNum]);
	
	myThreads[i%threadNum].run();

}
*/


/*
while((finishedJobs != threadNum) ) 
{
	//finishedJobs = 0;
	//value = 0f;
	for(int j = 0; j < threadNum; ++j) 
	{
		if( test[j].isFinished) 
		{
			++finishedJobs;
			value += test[j].getValue();
			test[j].isFinished=false;
			allFinishedJobs++;
			//System.out.println("b");
			//System.out.println(finishedJobs);
		}
	}
}
finishedJobs = 0;
for(int j = 0 ; j < threadNum ; j++)
{
	answerValue += test[j].getValue();
}
*/


/*
if((i%threadNum==(threadNum-1)))//假設一定要滿足全thread數
{
	while((finishedJobs != threadNum)) 
	{
		//finishedJobs = 0;
		//value = 0f;
		for(int j = 0; j < threadNum; ++j) 
		{
			if( test[j].isFinished) 
			{
				++finishedJobs;
				value += test[j].getValue();
				test[j].isFinished=false;
				allFinishedJobs++;
				//System.out.println("b");
				//System.out.println(finishedJobs);
			}
		}
	}
	finishedJobs = 0;
	for(int j = 0 ; j < threadNum ; j++)
	{
		answerValue += test[j].getValue();
	}
}
else if(i==99)
{
	while((finishedJobs != ((i+1)%threadNum))) 
	{
		//finishedJobs = 0;
		//value = 0f;
		for(int j = 0; j < threadNum; ++j) 
		{
			if( test[j].isFinished) 
			{
				++finishedJobs;
				value += test[j].getValue();
				test[j].isFinished=false;
				allFinishedJobs++;
				//System.out.println("b");
				//System.out.println(finishedJobs);
			}
		}
	}
	for(int j = 0 ; j < finishedJobs ; j++)
	{
		answerValue += test[j].getValue();
	}
}*/