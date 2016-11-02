package Sink;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class computeEncryptionValue 
{
	List<String> tagList ;
	int threadnumber;
	
	public computeEncryptionValue()
	{
		tagList = setTag("EleAttrMD5Hash.txt");
		
	}
	
	public List<String> setTag(String file)
	{
		List<String> tagList = new ArrayList<String>();
		try 
		{
			Scanner sc = new Scanner(new File(file));
			while(sc.hasNextLine())
			{
				tagList.add(sc.nextLine());
			}
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tagList;
	}
	
	public void setThreadNumber(int number)
	{
		this.threadnumber = number;
	}
	
	public void runComputeEncryptionValue()
	{
		ExecutorService service = Executors.newFixedThreadPool(threadnumber);
		
		Runnable runConEnV = new Runnable() 
		{
			
			
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				
			}
		};
		
		
		
	}
	
	public static void main(String[] args)
	{
		
	}
}
