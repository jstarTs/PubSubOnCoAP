import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import ahe.*;
import ahe.Matrix.*;
import ahe.RandomNumber.CompositiveRandoms;

public class Main 
{
	File a;
	
	long timeStart;
	long timeEnd;
	
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
		File filep = new File("D:\\[CSIE]\\_WorkSpaces\\Java\\XMLforJava\\src\\"+filename);
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
		File filep = new File("D:\\[CSIE]\\_WorkSpaces\\Java\\XMLforJava\\src\\"+filename);
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
	
	public static void main(String[] args )
	{
		//argc??->(int)args[0]  , argv?? -> String[]
		int argc = args.length; 
		Main Implementation = new Main();
		
		if(argc>=2)
		{
			if(args[0].equalsIgnoreCase("testmeterdata"))
			{
				if(argc>=3)
				{
					// [0]             [1]          [2]           
					// testmeterdata   matrixFile   randomFile    
					CompositiveMatrix matrixA = Implementation.LoadMatrixFromFile(args[1]);
					CompositiveRandoms randomVariable = Implementation.LoadRandomVariableFromFile(args[2]);
					float[] meterInput = { 26.5f, 33.5f ,30.5f ,40,50,60,70,80,90,100};
					ArrayList<SingleMeterOutput> meterOutput = Ahe.GetMeterOutput(meterInput, 9, matrixA.getMatrix(), matrixA.getNumberOfRows(), matrixA.getNumberOfColumns(), randomVariable.getRandoms());
					
					int i = 0;
					for( i = 0; i < 9; ++i) 
					{
						if( i != 0) 
						{
							System.out.printf(" %d %f",meterOutput.get(i).id, meterOutput.get(i).output);
						}
						else 
						{
							System.out.printf("%d %f",meterOutput.get(i).id, meterOutput.get(i).output);
						}
					}
					
					matrixA.clearMatrix();;
					randomVariable.clearRandomsList();
				}
			}
			if(args[0].equalsIgnoreCase("readrandom"))
			{
				if(argc>=2)
				{
					// [0]          [1]
					// readrandom   fileName
					CompositiveRandoms result = Implementation.LoadRandomVariableFromFile(args[1]);
					
					for(int i = 0 ; i < result.getSize() ; i++ )
					{
						if(i!=0)
						{
							System.out.printf(" %d", result.getRandoms().get(i));
						}
						else 
						{
							System.out.printf("%d", result.getRandoms().get(i));
						}
					}
					result.clearRandomsList();
				}
			}
			else if(args[0].equalsIgnoreCase("writerandom"))
			{
				if(argc>=4)
				{
					// [0]          [1]         [2]   [3]
					// writerandom  fileName    size  maxValue
					Implementation.WriteToRandomVariableFile(args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
				}
			}
			else if(args[0].equalsIgnoreCase("readmatrix"))
			{
				if(argc>=2)
				{
					// [0]          [1]       [2] (optional)
					// readmatrix   fileName  id
					CompositiveMatrix matrixA = Implementation.LoadMatrixFromFile(args[1]);
					if(argc>=3)
					{
						int id = Integer.parseInt(args[2]), i = 0;
						ArrayList<Integer> output = Ahe.GetInputMatrixForSpecificID(id,matrixA.getMatrix(),matrixA.getNumberOfRows(),matrixA.getNumberOfColumns());
						for( i = 0; i < matrixA.getNumberOfColumns(); ++i) 
						{
							if( i != 0) 
							{
								System.out.printf(",%d", output.get(i));
							} 
							else 
							{
								System.out.printf("%d", output.get(i));
							}
						}
					}
					else
					{
						System.out.printf("%d,%d",matrixA.getNumberOfRows(), matrixA.getNumberOfColumns());
						int i = 0, j = 0;
						for( i = 0; i < matrixA.getNumberOfRows(); ++i) 
						{
							for( j = 0; j < matrixA.getNumberOfColumns(); ++j) 
							{
								System.out.printf(",%d", matrixA.getMatrix().get(i).get(j));
							}
						}
					}
					
				}
			}
			else if(args[0].equalsIgnoreCase("writematrix"))
			{
				if(argc>=5)
				{
					// [0]          [1]         [2]   		  [3]              [4]
					// writematrix  fileName    numberOfRows  numberOfColumns  maxValue
					Implementation.WriteToMatrixFile(args[1],Integer.parseInt(args[2]), Integer.parseInt(args[3]) ,Integer.parseInt(args[4]));
				}
			}
			else if(args[0].equalsIgnoreCase("sink"))
			{
				// [0]   [1]             [2] [3]
				// sink  matrix_file.txt id1 number1 id2 number2 id3 number3 ...
				if(argc>=4)
				{
					CompositiveMatrix cMatrix = Implementation.LoadMatrixFromFile(args[1]);
					int countOfRecords = (argc-2);
					if(countOfRecords%2 == 0)
					{
						countOfRecords /= 2;
						ArrayList<SingleMeterOutput> meterOutput = new ArrayList<SingleMeterOutput>();
						int i=2 ,count=0;
						for( i = 2; i < argc; i += 2 ) 
						{
							SingleMeterOutput smo = new SingleMeterOutput();
							smo.id = Integer.parseInt(args[i]);
							smo.output = Float.parseFloat(args[i+1]);
							meterOutput.add(smo);
							count++;
						}
						SinkOutput outputDataFromSink = Ahe.GetSinkOutput(meterOutput, count, cMatrix.getMatrix(), cMatrix.getNumberOfRows(), cMatrix.getNumberOfColumns());
						System.out.printf("%f %d", outputDataFromSink.S , cMatrix.getNumberOfColumns());
						for( i = 0; i < cMatrix.getNumberOfColumns(); ++i ) {
							System.out.printf(" %f", outputDataFromSink.y.get(i));
						}
					}
				}
			}
			else if(args[0].equalsIgnoreCase("server"))
			{
				if(argc>=3)
				{
					// [0]    [1]        [2]        [3] [4]
					// server S          numberOfYs y1  y2 ... yn, r1, r2,...,rk
					SinkOutput outputDataFromSink = new SinkOutput();
					outputDataFromSink.S = Integer.parseInt(args[1]);
					int count = Integer.parseInt(args[2]);
					// int sinkOutputDataSize = count;
					outputDataFromSink.y = new ArrayList<Float>();
					int index = 3;
					for(index = 3; index < count + 3; ++index) 
					{
						outputDataFromSink.y.add(Float.parseFloat(args[index]));
					}
					
					int totalSizeOfAllocatedInts = count;
					ArrayList<Integer> randomVariable = new ArrayList<Integer>();
					for(int i = count + 3; i < args.length; ++i) {
						randomVariable.add(Integer.parseInt(args[i]));
					}
					
					if(randomVariable.size() > 0) 
					{
						System.out.printf("%f", Ahe.GetServerOutput(outputDataFromSink, randomVariable, randomVariable.size()));
					}
				}
			}
		}
		System.out.print(argc);
	}
}
