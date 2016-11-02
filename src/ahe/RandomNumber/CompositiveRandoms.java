package ahe.RandomNumber;
import java.util.ArrayList;

public class CompositiveRandoms 
{
	int size;
	ArrayList<Integer> randoms;
	
	public void setSize(int size)
	{
		this.size = size;
	}
	
	public void setNewRandomsList()
	{
		randoms = new ArrayList<Integer>();
	}
	
	public int getSize()
	{
		return size;
	}
	
	public ArrayList<Integer> getRandoms()
	{
		return randoms;
	}
	
	public void addRandomsList(int value)
	{
		randoms.add(value);
	}
	
	public void clearRandomsList()
	{
		randoms.clear();
	}
}
