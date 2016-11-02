package ahe;

import java.util.ArrayList;
import java.util.List;

import ahe.Matrix.CompositiveMatrix;
import ahe.Matrix.AccessMatrix;
import ahe.RandomNumber.CompositiveRandoms;
import ahe.RandomNumber.AccessRandom;

public class mainFunction 
{
	
	
	
	public void testmeterdata(List<Float> list ,String matrixFile , String randomFile)
	{
		CompositiveMatrix matrixA = new AccessMatrix().LoadMatrixFromFile(matrixFile);
		CompositiveRandoms randomVariable = new AccessRandom().LoadRandomVariableFromFile(randomFile);
		float[] meterInput = new float[list.size()];

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
	
	//之後要看情況改要回傳
	public void sink(String matrixfile , List<Integer> idList , List<Float> valuelist)
	{
		CompositiveMatrix cMatrix = new AccessMatrix().LoadMatrixFromFile(matrixfile);
		ArrayList<SingleMeterOutput> meterOutput = new ArrayList<SingleMeterOutput>(); 
			
		for(int i = 0 ; i < idList.size() ; i++ )
		{
			SingleMeterOutput smo = new SingleMeterOutput();
			smo.id = idList.get(i);
			smo.output = valuelist.get(i);
			meterOutput.add(smo);
		}
		SinkOutput outputDataFromSink = Ahe.GetSinkOutput(meterOutput, idList.size(), cMatrix.getMatrix(), cMatrix.getNumberOfRows(), cMatrix.getNumberOfColumns());
		
		System.out.printf("%f %d", outputDataFromSink.S , cMatrix.getNumberOfColumns());
		
		for(int i = 0; i < cMatrix.getNumberOfColumns(); ++i ) 
		{
			System.out.printf(" %f", outputDataFromSink.y.get(i));
		}
	}
	/*
	public void transportServer()
	{
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
	*/
}
