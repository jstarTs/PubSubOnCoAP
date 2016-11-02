package ahe.Matrix;

import java.util.ArrayList;

public class CompositiveMatrix 
{
	int numberOfRows;
	int numberOfColumns;
	ArrayList<ArrayList<Integer>> matrix;
	
	public void setRows(int row)
	{
		numberOfRows = row;
	}
	public void setColumns(int columns)
	{
		numberOfColumns = columns;
	}
	public void setNewMatrix()
	{
		matrix = new ArrayList<ArrayList<Integer>>();
	}
	
	public int getNumberOfRows()
	{
		return numberOfRows;
	}
	
	public int getNumberOfColumns()
	{
		return numberOfColumns;
	}
	
	public void addRow(ArrayList<Integer> list)
	{
		matrix.add(list);
	}
	
	public ArrayList<ArrayList<Integer>> getMatrix()
	{
		return matrix;
	}
	
	public ArrayList<Integer> getRowOfMatrix(int rowsNumber)
	{
		return matrix.get(rowsNumber);
	}
	
	public ArrayList<Integer> getColumnOfMatrix(int columnsNumber)
	{
		ArrayList<Integer> columnElementList = new ArrayList<Integer>();
		
		for(int i = 0 ; i < numberOfRows ; i++)
		{
			columnElementList.add(matrix.get(i).get(columnsNumber));
		}
		
		return columnElementList;
	}
	
	public void clearMatrix()
	{
		matrix.clear();
	}
	
}
