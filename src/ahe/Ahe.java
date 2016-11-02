package ahe;

import java.util.ArrayList;
import java.util.List;

import ahe.*;

public class Ahe {

	// ahe_matrix
	public static ArrayList<ArrayList<Integer>> GenerateMatrix(int numOfRows, int numOfColumns, int maxValue) {
		if (numOfRows <= 0 || numOfColumns <= 0 || maxValue <= 0) {
			return null;
		}
		int row = 0, column = 0, randomVariable, computation;
		ArrayList<Integer> matrixRow;
		ArrayList<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();

		for (row = 0; row < numOfRows; row++) {
			column = 0;
			matrixRow = new ArrayList<Integer>();
			for (column = 0; column < numOfColumns; column++) {
				computation = (int) (Math.random() * 1000000);
				randomVariable = ((computation % (maxValue + 1)) + 1);
				matrixRow.add(randomVariable);
			}
			matrix.add(matrixRow);
		}

		return matrix;
	}

	public static ArrayList<Integer> GetInputMatrixForSpecificID(int id, ArrayList<ArrayList<Integer>> matrix,
			int numOfRows, int numOfColumns) {
		// System.out.println("GetInputMatrixForSpecificID_1");
		if (id > numOfRows || id < 0) {
			// System.out.println("GetInputMatrixForSpecificID_null");
			return null;
		}
		// System.out.println("GetInputMatrixForSpecificID_2");
		int i = 0, index = id - 1;
		ArrayList<Integer> matrixRow = matrix.get(index), result = new ArrayList<Integer>();

		for (i = 0; i < numOfColumns; i++) {
			// System.out.println(matrixRow.get(i));
			result.add(matrixRow.get(i));
			// System.out.println(matrixRow.size());
		}

		return result;
	}

	// ahe_meter

	public static SingleMeterOutput GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(int id, float input,
			ArrayList<Integer> specificPartOfMatrix, int sizeOfColumn, ArrayList<Integer> randomVariables) {
		int tempIndex = 0;
		float maskValue = 0;
		for (tempIndex = 0; tempIndex < sizeOfColumn; ++tempIndex) {
			maskValue += (specificPartOfMatrix.get(tempIndex) * randomVariables.get(tempIndex));
		}

		SingleMeterOutput obj = new SingleMeterOutput();
		obj.id = id;
		obj.output = input + maskValue;

		return obj;
	}

	public static ArrayList<SingleMeterOutput> GetMeterOutput(List<Integer> idList , List<Float> inputvalueList, int length, ArrayList<ArrayList<Integer>> matrixA, int numOfRows, int numOfColumns, ArrayList<Integer> randomVariable)
	{
		ArrayList<SingleMeterOutput> tempArray = new ArrayList<SingleMeterOutput>();

		//System.out.println("GetMeterOutput_1");
		
		int i = 0 , id = 0; 
		ArrayList<Integer> matrixOfColumn = new ArrayList<Integer>();

		//System.out.println("GetMeterOutput_2");
		
		for(i=0 ; i<length ; i++)
		{
			id = idList.get(i);
			//System.out.println(id+" "+numOfRows+" "+numOfColumns);
			matrixOfColumn = GetInputMatrixForSpecificID(id, matrixA, numOfRows, numOfColumns);
			//System.out.println(matrixOfColumn.size());
			//System.out.println("GetMeterOutput_3");
			if(matrixOfColumn != null) 
			{
				tempArray.add(GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(id,inputvalueList.get(i), matrixOfColumn, numOfColumns, randomVariable));
				//System.out.println("GetMeterOutput_4");
			}
		}
		//System.out.println("GetMeterOutput_End");
		return tempArray;
	}

	public static ArrayList<SingleMeterOutput> GetMeterOutput(float inputArray[], int length,
			ArrayList<ArrayList<Integer>> matrixA, int numOfRows, int numOfColumns, ArrayList<Integer> randomVariable) {
		ArrayList<SingleMeterOutput> tempArray = new ArrayList<SingleMeterOutput>();

		// System.out.println("GetMeterOutput_1");

		int i = 0, id = 0;
		ArrayList<Integer> matrixOfColumn = new ArrayList<Integer>();

		// System.out.println("GetMeterOutput_2");

		for (i = 0; i < length; i++) {
			id = i + 1;
			// System.out.println(id+" "+numOfRows+" "+numOfColumns);
			matrixOfColumn = GetInputMatrixForSpecificID(id, matrixA, numOfRows, numOfColumns);
			// System.out.println(matrixOfColumn.size());
			// System.out.println("GetMeterOutput_3");
			if (matrixOfColumn != null) {
				tempArray.add(GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(id, inputArray[i], matrixOfColumn,
						numOfColumns, randomVariable));
				// System.out.println("GetMeterOutput_4");
			}
		}
		// System.out.println("GetMeterOutput_End");
		return tempArray;
	}

	// ahe_random

	public static ArrayList<Integer> GenerateRandomNumbers(int size, int maxValue) {
		if (size <= 0 || maxValue <= 0) {
			return null;
		}

		int index = 0, randomVariable;
		ArrayList<Integer> randomVariables = new ArrayList<Integer>();

		for (index = 0; index < size; index++) {
			if ((int) (Math.random() * 10000) % 2 == 0) {
				randomVariable = (int) (((Math.random() * 1000000) % (maxValue + 1)) + 1);
				randomVariables.add(randomVariable);
			} else {
				randomVariable = 0 - (int) (((Math.random() * 1000000) % (maxValue + 1)) + 1);
				randomVariables.add(randomVariable);
			}
		}

		return randomVariables;
	}

	// ahe_sink

	public static SinkOutput GetSinkOutput(ArrayList<SingleMeterOutput> outputArrayFromMeter, int length,
			ArrayList<ArrayList<Integer>> matrixA, int numOfRows, int numOfColumns) {
		SinkOutput output = new SinkOutput();
		output.S = 0;
		output.y = new ArrayList<Float>();

		// Compute S, where S = z*v. v = [1,1,1,....,1]
		int index = 0;
		ArrayList<Integer> inputIDs = new ArrayList<Integer>();
		for (index = 0; index < length; ++index) {
			output.S += outputArrayFromMeter.get(index).output;
			inputIDs.add(outputArrayFromMeter.get(index).id);
		}

		// Compute y[k], where k = 1 ~ m. y[k] = a_(1,k)*1 + a_(2,k)*1 + ....+
		// a_(n,k)*1.
		int k = 0;
		float var;
		for (k = 0; k < numOfColumns; ++k) {
			var = 0;

			for (index = 0; index < length; ++index) {
				int rowIndex = inputIDs.get(index) - 1;
				var += matrixA.get(rowIndex).get(k);
				output.y.add(var);
			}
		}

		return output;
	}

	// ahe_server

	public static float GetServerOutput(SinkOutput outputFromSink, ArrayList<Integer> randomVariables, int length) {
		float S2 = 0;
		int index = 0;
		for (index = 0; index < length; ++index) {
			S2 += (outputFromSink.y.get(index) * randomVariables.get(index));
		}

		return outputFromSink.S - S2;
	}

}
