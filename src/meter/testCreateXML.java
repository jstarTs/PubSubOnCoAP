package meter;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class testCreateXML 
{
	public static void main(String[] args)
	{
		CreateXML test = new CreateXML();
		
		String filename = "test20T";
		
		List<String> ListElement = new ArrayList<String>();
		
		try 
		{
			Scanner sc = new Scanner(new File("./ListElement"));
			
			while(sc.hasNextLine())
			{
				ListElement.add(sc.nextLine().trim());
			}
			
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0 ; i< 500 ; i++)
		{
			
			
			//test.create(filename+(i+1),(i+1),new BigInteger("10"),ListElement);
			
			
			test.setIdAndValue((i+1), 10f);
			test.create(filename+(i+1),(i+1),ListElement);
		}
		
	}
}
