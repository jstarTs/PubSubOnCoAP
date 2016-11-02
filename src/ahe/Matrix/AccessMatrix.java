package ahe.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import ahe.*;

public class AccessMatrix 
{
	public void WriteToMatrixFile(String filename,int numberOfRows, int numberOfColumns, int maxValue)
	{
		File filep = new File(filename);
		if (numberOfRows <= 0 || numberOfColumns <= 0|| maxValue <= 0) 
		{
			System.out.printf("Errors with out of ranges of numberOfRows, numberOfColumns or maximum value\n");
		}
		
		/*
		if (filep.exists())
		{
			System.out.printf("Error opening file on WriteToMatricxFile()!\n");
			System.exit(1);
		}
		*/
		
		ArrayList<ArrayList<Integer>> matrixA = Ahe.GenerateMatrix(numberOfRows,numberOfColumns,maxValue);
		PrintWriter pw ;
		
		try 
		{
			pw = new PrintWriter("./"+filep);
			pw.print(numberOfRows+","+numberOfColumns);
			int i = 0,j = 0;
			
			for( i = 0; i < numberOfRows; ++i) {
				for( j = 0; j < numberOfColumns; ++j) {
					pw.print(","+matrixA.get(i).get(j));
				}
			}
			matrixA.clear();
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	public CompositiveMatrix LoadMatrixFromFile(String filename)
	{
		File filep = new File(filename);
		//File filep = new File(filename);
		//System.out.println(filename);
		if (!filep.exists())
		{
			System.out.printf("Error opening file on LoadMatrixFromFile()!\n");
			System.exit(1);
		}
		
		String str ;
		String[] strArr ;
		Scanner sc;
		CompositiveMatrix result = new CompositiveMatrix();
		//result.matrix = new ArrayList<ArrayList<Integer>>();
		result.setNewMatrix();
		
		try 
		{
			sc = new Scanner(filep);
			if(sc.hasNextLine())
			{
				str = sc.nextLine();
				//System.out.println(str);
				strArr = str.split(",");
				//System.out.println(strArr[0]);
				int numberOfRows = Integer.parseInt(strArr[0]);
				//result.numberOfRows = numberOfRows;
				result.setRows(numberOfRows);
				//System.out.println(strArr[1]);
				int numberOfColumns = Integer.parseInt(strArr[1]);
				//result.numberOfColumns = numberOfColumns;
				result.setColumns(numberOfColumns);
				int index = 2;
				
				for(int i = 0; i < numberOfRows; ++i) 
				{
					ArrayList<Integer> matrixR = new ArrayList<Integer>();
					for(int j = 0; j < numberOfColumns; ++j) 
					{
						matrixR.add(Integer.parseInt(strArr[index++]));
					}
					//result.matrix.add(matrixR);
					result.addRow(matrixR);
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
