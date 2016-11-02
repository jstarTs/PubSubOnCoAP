

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.xpath.XPathFactoryConfigurationException;

import XMLfilter.callSaxonFilter;
import XMLfilter.callXMLDogFilter;

public class testSinkUsingSaxon 
{

	public static void main(String[] args) throws MalformedURLException, XPathFactoryConfigurationException
	{
		// [0]Thread number  [1]Stream number  [2]Total document Run   
		/*
		int threadNum = Integer.parseInt(args[0]);
		int streamNum = Integer.parseInt(args[1]);
		int totalDocNum = Integer.parseInt(args[2]);
		*/
		
		int threadNum = 10;
		int streamNum = 100;
		int totalDocNum = 1000;
		
		int meterNum = 100;
		
		System.gc();
		
		//long StartTime = System.currentTimeMillis();
		/*
		meterStream[] ms = new meterStream[meterNum]; 
		Thread myThreads[] = new Thread[threadNum];
		callSaxonFilter[] test = new callSaxonFilter[streamNum];
		
		float value = 0f,answerValue = 0f;
		int finishedJobs = 0 ,allFinishedJobs = 0;
		long starttime = System.currentTimeMillis();
		
		for(int i = 0 ; i < meterNum ; i++)
		{
			ms[i] = new meterStream();
			ms[i].setUrl("http://service2.allenworkspace.net/xml/xmldata/testxml"+(i+1)+".xml");
			
		}
		
		for(int i = 0 ; i < streamNum ; i++)
		{
			test[i] = new callSaxonFilter();
			//myThreads[i] = new Thread(test[i%threadNum]);
		}

		int t , usedThread = 0 , hasfinishion = 0;
		boolean hasfinished = false;
		for(int i = 0 ; i < totalDocNum ; i++ )
		{
			// Check thread is usable
			for(t = 0 ; t < threadNum ; t++)
			{
				if(i < threadNum)
					break;
				else if(!myThreads[t].isAlive() )
					break;
				else if(t == threadNum-1)
				{
					t = -1;
				}
			}
			usedThread++;
			
			try 
			{
				test[t].setInputStream(ms[i%meterNum].getStream());
				myThreads[t] = new Thread(test[t]);
				myThreads[t].run();
				
				for(t = 0 ; t < threadNum ; t++)
				{
					if(test[t].isFinished)
					{
						hasfinished = true;
						hasfinishion++;
					}
				}
				
				if( (usedThread == threadNum) || hasfinished)
				{
					if(usedThread == threadNum)
						hasfinishion = usedThread;
					while((finishedJobs != hasfinishion) ) 
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
								usedThread--;
								//System.out.println("b");
								//System.out.println(finishedJobs);
								System.out.println(test[j].total);
								
								allFinishedJobs++;
							}
						}
					}
					finishedJobs = 0;
					hasfinishion = 0;
					for(int j = 0 ; j < threadNum ; j++)
					{
						answerValue += test[j].getValue();
					}
					hasfinished = false;
					System.out.println(allFinishedJobs);
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(allFinishedJobs);
		
	
		long endtime = System.currentTimeMillis();
		System.out.println(value+","+answerValue);
		System.out.println((endtime-starttime));
			*/
	}
}
