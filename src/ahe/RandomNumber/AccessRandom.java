package ahe.RandomNumber;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import ahe.*;

public class AccessRandom 
{
	public void WriteToRandomVariableFile(String filename , int size, int maxValue)
	{
		File filep = new File(filename);
		if (size <= 0 || maxValue <= 0) 
		{
			System.out.printf("Errors with out of ranges of size or maximum value\n");
		}
		/*
		if (filep.exists())
		{
			System.out.printf("Error opening file on WriteToRandomVariableFile()!\n");
			System.exit(1);
		}
		*/
		
		ArrayList<Integer> randomVariables = Ahe.GenerateRandomNumbers(size,maxValue);
		PrintWriter pw ;
		
		try 
		{
			pw = new PrintWriter(filep);
			pw.print(size);
			int i = 0; 
			for(i = 0 ; i < size ; i++)
			{
				pw.print(","+randomVariables.get(i));
				//System.out.println(randomVariables.get(i));
			}
			randomVariables.clear();
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}
		
	}
	
	public CompositiveRandoms LoadRandomVariableFromFile(String filename)
	{
		File filep = new File("D:\\[CSIE]\\_WorkSpaces\\Java\\XMLforJava\\"+filename);
		//File filep = new File(filename);
		//System.out.println(filename);
		if (!filep.exists())
		{
			System.out.printf("Error opening file on LoadRandomVariableFromFile()!\n");
			System.exit(1);
		}
		
		CompositiveRandoms result = new CompositiveRandoms();
		String str = null;
		String[] strArr ;
		Scanner sc;
		
		try 
		{
			sc = new Scanner(filep);
			if(sc.hasNextLine())
			{
				str = sc.nextLine();
				strArr = str.split(",");
				int size = Integer.parseInt(strArr[0]);
				//result.size;
				result.setSize(size);
				if(size >= 0)
				{
					//result.randoms = new ArrayList<Integer>();
					result.setNewRandomsList();
					for(int index = 1 ; index <= size ; index++)
					{
						//result.randoms.add(Integer.parseInt(strArr[index]));
						result.addRandomsList(Integer.parseInt(strArr[index]));
					}
				}
			}
			sc.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}

		return result;
	}
}
