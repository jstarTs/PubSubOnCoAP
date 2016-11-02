package Sink;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class myTaskReducer implements Runnable {
	public List<Future<Float>> resultList = new ArrayList<Future<Float>>();	
	public String taskID = null;
	public myTaskReducer(String id) {
		this.taskID = id;
		//System.out.println(id);
	}
	public void run() 
	{
		List<AtomicBoolean> checkList = new ArrayList<AtomicBoolean>();
		for(int i = 0; i < resultList.size(); ++i) {
			checkList.add(new AtomicBoolean(false));
		}
		// TODO Auto-generated method stub
		Float answerValue = 0.0f;
		int doneTasks = 0;
		//System.out.println("RESULT SIZE:" + resultList.size());
		do {
			doneTasks = 0; 
			int index = 0;
			for(Future<Float> result: resultList) {
				try{
					if(result.isDone()) {
						++doneTasks;
						if(checkList.get(index).get() == false) {
							checkList.get(index).set(true);
							answerValue +=  result.get();

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
		System.out.println("end time:" + System.currentTimeMillis());
	}
}
