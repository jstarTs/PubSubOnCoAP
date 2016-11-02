package ahe;

//typedef struct { int id; float output; } SingleMeterOutput;
//#ifndef _AHE_METER_INCLUDE_
//#define _AHE_METER_INCLUDE_

public class meter 
{
	
	//SingleMeterOutput GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(int id,float input, int * specificPatOfMatrix, int sizeOfColumn, int * randomVariable)

	//SingleMeterOutput * GetMeterOutput(float inputArray[], int length, int ** matrixA, int numOfRows, int numOfColumns, int * randomVariable)
	
	public static void main(String[] args)
	{
		double j = (((Math.random()*1000)%(10+1))+1);
		int i = (int)j;
		
		System.out.print(i+":"+j);
	}
}
