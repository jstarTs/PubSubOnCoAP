
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import XMLfilter.callXMLDogFilter;
import XMLfilter.runnableXMLfilter;
import ahe.Matrix.CompositiveMatrix;
import ahe.RandomNumber.CompositiveRandoms;
import ahe.mainFunction;
import jlibs.xml.sax.dog.NodeItem;
import jlibs.xml.sax.dog.XPathResults;
import jlibs.xml.sax.dog.expr.Expression;
import trial.testFilter.useXmldog;

public class testMulti 
{
	static List<Float> list = new ArrayList<Float>();
	static List<Float> listtest = Arrays.asList(new Float[10]);
	static List<Integer> idList = Collections.synchronizedList(new ArrayList<Integer>());
	static long EndTime;
	static float totalValue = 0f;
	
	public static void main(String[] args)
	{
		mainFunction mf = new mainFunction();
		CompositiveMatrix cm = new CompositiveMatrix();
		CompositiveRandoms cr = new CompositiveRandoms();
		
		ExecutorService service = Executors.newFixedThreadPool(10);
		
		
		long StartTime = System.currentTimeMillis();
		final int threadnum=200;
		
		Thread myThreads[] = new Thread[threadnum];
		callXMLDogFilter[] test = new callXMLDogFilter[threadnum];
		
		/*
		for(int i = 0 ; i < threadnum ; i++)
		{
			test[i] = new runnableXMLDogFilter();
			test[i].setFileStream("./xmldata/testxml"+(i+1)+".xml");
			myThreads[i] = new Thread(test[i]);
		}
		*/
		
		float value = 0f,answerValue = 0f;
		int finishedJobs = 0 ,allFinishedJobs = 0;
		long starttime = System.currentTimeMillis();
		/*
		for(int i = 0 ; i < 200 ; i++)
		{
			test[i%threadnum] = new runnableXMLDogFilter();
			test[i%threadnum].setFileStream("./xmldata/testxml"+(i+1)+".xml");
			myThreads[i%threadnum] = new Thread(test[i%threadnum]);
			
			myThreads[i%threadnum].run();
			
			if((i%threadnum==(threadnum-1)))//假設一定要滿足全thread數
			{
				while((finishedJobs != threadnum)) 
				{
					//finishedJobs = 0;
					//value = 0f;
					for(int j = 0; j < threadnum; ++j) 
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
				for(int j = 0 ; j < threadnum ; j++)
				{
					answerValue += test[j].getValue();
				}
			}
			else if(i==199)
			{
				while((finishedJobs != ((i+1)%threadnum))) 
				{
					//finishedJobs = 0;
					//value = 0f;
					for(int j = 0; j < threadnum; ++j) 
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
			}
		}
		System.out.println(allFinishedJobs);
		
		/*
		for(int i = 0 ; i < 200 ; i++)
		{
			answerValue += test[i].getValue();
		}
		*/
		/*
		int finishedJobs = 0;
		while(finishedJobs != 200) 
		{
			//finishedJobs = 0;
			//value = 0f;
			for(int i = 0; i < threadnum; ++i) 
			{
				if( test[i].isFinished) 
				{
					++finishedJobs;
					value += test[i].getValue();
					test[i].isFinished=false;
				}
			}
			System.out.println(finishedJobs);
		}
		*/
		long endtime = System.currentTimeMillis();
		System.out.println(value+","+answerValue);
		System.out.println((endtime-starttime));
		
		/*
		for(int i=0 ; i<200 ; i++)
		{
			//GetXMLvalue test = new GetXMLvalue();
			//Thread testthread = new Thread(test);
			final int count = i;
			Runnable runnable = 
                    new Runnable() {
                        public void run() 
                        {
                        	useXmldog test = new useXmldog();
                        	
                        	long starttime = System.currentTimeMillis();
                        	
                        	//Expression xpath1 = test.addXpath("/a/b/@id");
                        	Expression xpath1 = test.addXpath("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/@aB80BB7740288FDA1F201890375A60C8F");
                        	//Expression xpath2 = test.addXpath("/a/c/@value");
                        	Expression xpath2 = test.addXpath("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/@a2063C1608D6E0BAF80249C42E2BE5804");
                        	//int number = (int)Math.random()*10;
                        	int number = 1;
                        	XPathResults results = test.setXMLFile("./xmldata/testxml"+(count+1)+".xml");
                        	List<NodeItem> idlt = (List<NodeItem>)results.getResult(xpath1);
                        	List<NodeItem> valuelt = (List<NodeItem>)results.getResult(xpath2);
                        	
                    		//System.out.println(valuelt.size()+"run");
                        	//System.out.println(System.currentTimeMillis());

                    		List<Float> valuelist = test.inputdata(valuelt);
                    		
                    		
                    		
                    		
                    		//System.out.println(valuelist.size()+"qq");
                    		
                    		synchronized(idList)
                    		{
                    			long midtime = System.currentTimeMillis()-starttime; 
                    			if(idList.size()!=threadnum)
                    			{
                    				idList.add(Integer.parseInt(idlt.get(0).value));
                    				totalValue += valuelist.get(0);
                    				
                    				if(midtime>=100)
                        			{
                        				System.out.println(idList.size()+","+totalValue);
                        				long endtime = System.currentTimeMillis();
                        				System.out.println(idList.size()+","+(endtime-starttime)+"----");
                        				idList.clear();
                        				totalValue = 0f;
                        				System.out.println("c");
                        			}
                    				else if(count ==199)
                        			{
                        				System.out.println(idList.size()+","+totalValue);
                        				long endtime = System.currentTimeMillis();
                        				System.out.println(idList.size()+","+(endtime-starttime)+"++++");
                        				idList.clear();
                        				totalValue = 0f;
                        				System.out.println("d");
                        				EndTime = endtime;
                        				System.out.println((EndTime-StartTime)+"//////");
                        			}
                    				else
                    				{
                    					long endtime = System.currentTimeMillis();
                    					System.out.println(endtime-starttime);
                        				System.out.println("a");
                    				}
                    			}
                    			else if(idList.size()==threadnum)
                    			{
                    				System.out.println(idList.size()+","+totalValue);
                    				long endtime = System.currentTimeMillis();
                    				System.out.println(idList.size()+","+(endtime-starttime)+"....");
                    				idList.clear();
                    				totalValue = 0f;
                    				System.out.println("b");
                    			}
                    			
                    			
                    		}
                    		
                    		
                    		/*
                    		synchronized(valuelist)
                    		{
                    			list.add(valuelist.get(0));
                    			if(list.size()==20)
                    			{
                    				String[] s ={"sink","testMatrix.txt","1",list.get(0)+"","2",list.get(1)+"","3",list.get(2)+"","4",list.get(3)+"","5",list.get(4)+"","6",list.get(5)+"","7",list.get(6)+"","8",list.get(7)+"","9",list.get(8)+"","10",list.get(9)+""};
                    				System.out.println(valuelist.size()+"..................................");
                    				//listtest = Arrays.asList(new Float[10]);
                    				list = new ArrayList<Float>();
                    				Main.main(s);
                    				System.out.println();
                    			}
                    			if(count==199)
                    			{
                    				
                    				if(list.size()<10)
                    				{
                    					//String[] s ={"sink","testMatrix.txt","1",list.get(0)+"","2",list.get(1)+"","3",list.get(2)+"","4",list.get(3)+"","5",list.get(4)+"","6",list.get(5)+"","7",list.get(6)+"","8",list.get(7)+"","9",list.get(8)+"","10",list.get(9)+""};
                    					List<Integer> idList = new ArrayList<Integer>();
                    					for(int i = 0 ; i<list.size() ; i++)
                    					{
                    						idList.add((i+1));
                    					}
                    					
                    					mf.sink("testMatrix.txt", idList, list);
                    					System.out.println(list.size()+"..................................");
                        				//listtest = Arrays.asList(new Float[10]);
                        				list = new ArrayList<Float>();
                        				//Main.main(s);
                        				System.out.println();
                    				}
                    				
                    				EndTime = System.currentTimeMillis();
                    				long ExecutionTime = EndTime - StartTime;
                    				System.out.println(ExecutionTime+"////////////////////////");
                    			}
                    		}
                    		
                        }
                    };
			
			//test.setfile("testxml.xml");
			//test.addXpath("/a/c/@value");
			service.submit(runnable);
			
			//testthread.start();
			/*
			listn= test.getList();
			if(list.size()==10)
			{
				Main.main(s);
				list.clear();
			}
			else if(!listn.isEmpty())
			{
				list.add(listn.get(0));
			}
			//System.out.println(i+"QQ");
		}
		service.shutdown();*/
		
	}
}
