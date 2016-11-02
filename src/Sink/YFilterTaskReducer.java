package Sink;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import Paillier.Paillier;
import jlibs.xml.sax.dog.NodeItem;

public class YFilterTaskReducer implements Runnable {
	public List<Future<List<String>>> resultList = new ArrayList<Future<List<String>>>();	
	public String taskId;
	private String lamda;
	public BigInteger n, nsquare;
	public int typeNum;
	public int queryNumPerType;
	public YFilterTaskReducer() 
	{
		/*
		try 
		{
			Scanner sc = new Scanner(new File("./lamdaN"));
			lamda = sc.nextLine().trim();
			n = sc.nextLine().trim();;
			sc.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		lamda = "1131384130833571369641250211562769696895214634101869914952003925287884112846985190398202410098871342992711713287479814803579249837743797601523397253944300";
		n = new BigInteger("6788304785001428217847501269376618181371287804611219489712023551727304677082084697534614452377940561481995922955448396705881182951917545790230186124142487");
		nsquare = n.multiply(n);
	}
	
	public YFilterTaskReducer(String id) {
	}
	public void SetID(String id) {
		taskId = new String(id);
	}
	public void run() 
	{
		List<AtomicBoolean> checkList = new ArrayList<AtomicBoolean>();
		for(int i = 0; i < resultList.size(); ++i) {
			checkList.add(new AtomicBoolean(false));
		}		
		
		// TODO Auto-generated method stub
		//Float answerValue = 0.0f;
		//float[] AnswerValueArray = new float[typeNum];
		
		//BigInteger answerValue  = null; 
		BigInteger[] AnswerValueArray = new BigInteger[typeNum];
		
		int doneTasks = 0;
		//System.out.println("RESULT SIZE:" + resultList.size());
		do {
			doneTasks = 0; 
			int index = 0;
			//System.out.println(resultList.size());
			for(Future<List<String>> result: resultList) {
				try{
					if(result.isDone()) {
						++doneTasks;
						if(checkList.get(index).get() == false) {
							checkList.get(index).set(true);
							
							if(doneTasks == 1) 
							{
								//answerValue = new BigInteger(result.get().get(3));
								for(int i = 0 ; i < typeNum ; i++)
								{
									AnswerValueArray[i] = new BigInteger(result.get().get(3+i*queryNumPerType));
								}
							} 
							else 
							{
								//answerValue = answerValue.multiply(new BigInteger(result.get().get(3))).mod(nsquare);
								for(int i = 0 ; i < typeNum ; i++)
								{
									AnswerValueArray[i] = AnswerValueArray[i].multiply(new BigInteger(result.get().get(3+i*queryNumPerType))).mod(nsquare);
								}
							}
							
													
							//System.out.println("decrypted m: " + paillier.Decryption(m).toString());
							// answerValue = answerValue.multiply(m).mod(paillier.nsquare);	
							
							//System.out.println(Float.parseFloat((String) result.get().get(1)));
							//System.out.println(" : " + answerValue);
							//answerValue +=  Float.parseFloat((String) result.get().get(1));
							/*
							if(doneTasks == 1) 
							{
								//answerValue = new BigInteger(result.get().get(3));
								for(int i = 0 ; i < typeNum ; i++)
								{
									//System.out.println(result.get().size());
									AnswerValueArray[i] = Float.parseFloat(result.get().get(3+i*queryNumPerType));
								}
							} 
							else 
							{
								//answerValue = answerValue.multiply(new BigInteger(result.get().get(3))).mod(nsquare);
								for(int i = 0 ; i < typeNum ; i++)
								{
									AnswerValueArray[i] += Float.parseFloat(result.get().get(3+i*queryNumPerType));
								}
							}	
							*/
						}
					}
					++index;
				 }catch(ExecutionException e){   
	                 e.printStackTrace();   
	             } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}while(doneTasks != resultList.size());
		//System.out.println("task name:" + Thread.currentThread().getName() + ", value :" + answerValue);
		// System.out.println("end time:" + System.currentTimeMillis());
		
		//System.out.println("decrypted sum: " + paillier.Decryption(answerValue).toString());
	}
}
