
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import trial.testFilter.useXmldog;

public class test 
{
	
	//  static List<Float> listtest = new ArrayList<Float>();
	static List<Float> listtest = Arrays.asList(new Float[10]);
	static long EndTime ;
	public static void main(String[] args)
	{
		/*
		DefaultNamespaceContext nsContext = new DefaultNamespaceContext(); // an implementation of javax.xml.namespace.NamespaceContext
		nsContext.declarePrefix("xs", Namespaces.URI_XSD);
		
		XMLDog dog = new XMLDog(nsContext);
		XPathResults results;
		try 
		{
			Expression xpath1 = dog.addXPath("/a/b/@id");
			Expression xpath2 = dog.addXPath("/a/c/@value");
			System.out.println(xpath1.getXPath());
			
			
			results = dog.sniff(new InputSource("testxml1.xml"));
			List<NodeItem> list1 = (List<NodeItem>)results.getResult(xpath1);
			List<NodeItem> list2 = (List<NodeItem>)results.getResult(xpath2);
			System.out.println(list1.size()+","+list2.size());
			//System.out.println(list.isEmpty() ? null : list.get(0).value);
			for(int i=0 ; i<list1.size() ; i++)
			{
				System.out.println(list1.get(i).value);
				System.out.println(list2.get(i).value);
			}
			
		} 
		catch (XPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*
		int finishedJobs = 0;
		while(finishedJobs != 10) {
			finishedJobs = 0;
			for(int i = 0; i < 10; ++i) {
				if( xxx.isFinished) {
					++finishedJobs;
				}
			}
		}*/
		
		List<Float> list = new ArrayList<Float>() , listn;
		
		//String[] s = {"sink","testMatrix.txt","testRandom.txt"};
		//ExecutorService service = Executors.newFixedThreadPool(50);    
		long StartTime = System.currentTimeMillis();
		
		
		
		int threadnum=10;
		Thread myThreads[] = new Thread[threadnum];
		useXmldog[] test = new useXmldog[threadnum];
		
		for(int i = 0 ; i < threadnum ; i++)
		{
			test[i] = new useXmldog();
			myThreads[i] = new Thread(test[i]);
		}
		
		for(int num = 0 ; num < 100 ; num+=10)
		{
			for(int t = 0 ; t < threadnum ; t++)
			{
				if(!myThreads[t].isAlive())
				{
					test[t] = new useXmldog();
					myThreads[t] = new Thread(test[t]);
					myThreads[t].start();
				}
				
			}
			
			int finishedJobs = 0;
			while(finishedJobs != 10) 
			{
				listtest= new ArrayList<Float>();
				finishedJobs = 0;
				for(int i = 0; i < threadnum; ++i) 
				{
					if( test[i].isFinished) 
					{
						++finishedJobs;
						listtest.add(test[i].list.get(0));
					}
				}
			}
			String[] s ={"sink","testMatrix.txt","1",listtest.get(0)+"","2",listtest.get(1)+"","3",listtest.get(2)+"","4",listtest.get(3)+"","5",listtest.get(4)+"","6",listtest.get(5)+"","7",listtest.get(6)+"","8",listtest.get(7)+"","9",listtest.get(8)+"","10",listtest.get(9)+""};
			System.out.println(listtest.size()+"..................................");
			listtest.clear();
			Main.main(s);
			System.out.println();
		}
		EndTime = System.currentTimeMillis();
		long ExecutionTime = EndTime - StartTime;
		System.out.println(ExecutionTime+"////////////////////////");
		
		
		/*
		for(int i=0 ; i<1000 ; i++)
		{
			//GetXMLvalue test = new GetXMLvalue();
			//Thread testthread = new Thread(test);
			final int count = i;
			Runnable runnable = 
                    new Runnable() {
                        public void run() 
                        {
                        	GetXMLvalue test = new GetXMLvalue();
                        	Expression xpath1 = test.addXpath("/a/c/@value");
                        	XPathResults results = test.setXMLFile("testxml1.xml");
                        	List<NodeItem> lt = (List<NodeItem>)results.getResult(xpath1);
                        	
                        	
                    		System.out.println(lt.size()+"run");
                    		
                    		List<Float> list = test.inputdata(lt);
                    		//System.out.println(list.get(0)+"qq");
                    		
                    		
                    		
                    		
                    		synchronized(this)
                    		{
                    			listtest.add(list.get(0));
                    			if(listtest.size()==10)
                    			{
                    				String[] s ={"sink","testMatrix.txt","1",listtest.get(0)+"","2",listtest.get(1)+"","3",listtest.get(2)+"","4",listtest.get(3)+"","5",listtest.get(4)+"","6",listtest.get(5)+"","7",listtest.get(6)+"","8",listtest.get(7)+"","9",listtest.get(8)+"","10",listtest.get(9)+""};
                    				System.out.println(listtest.size()+"..................................");
                    				listtest = Arrays.asList(new Float[10]);
                    				Main.main(s);
                    				System.out.println();
                    			}
                    			if(count==999)
                    			{
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
			*/
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
		}*/
		
		//service.shutdown();
		//System.out.println();
		
	}
}
