package meter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Paillier.Paillier;
import ahe.SingleMeterOutput;
import ahe.Matrix.AccessMatrix;
import ahe.Matrix.CompositiveMatrix;
import ahe.RandomNumber.AccessRandom;
import ahe.RandomNumber.CompositiveRandoms;

public class CreateXML 
{
	int id=0;
	float value=0f;
	Map<String, String> scMapMD5 ;
	CompositiveMatrix cm ;
	CompositiveRandoms cr;
	ahe.Ahe ahe ;
	Paillier paillier;
	
	public CreateXML()
	{
		scMapMD5 = new hash.hashMd5Table().settableFromFile("xmlelement.txt", "EleAttrMD5Hash.txt");
		cm = new AccessMatrix().LoadMatrixFromFile("testMatrix.txt");
		cr = new AccessRandom().LoadRandomVariableFromFile("testRandom.txt");
		ahe = new ahe.Ahe();
		paillier = new Paillier(true);
	}
	
	public void create(String file)
	{
		try 
		{
			SingleMeterOutput meterTransport = ahe.GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(id, value, cm.getRowOfMatrix(id%cm.getNumberOfRows()), cm.getNumberOfColumns(), cr.getRandoms());
			PrintWriter pw = new PrintWriter(new File("xmldata/"+file+".xml"));
			//pw.println(LocalTime.now());
			
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			/*
			pw.println("<"+"a"+">");
			pw.println("\t"+"<"+"b"+" "+"id"+"="+"\""+meterTransport.id+"\""+" >");
			pw.println("\t"+"</"+"b"+">");
			pw.println("\t"+"<"+"c"+" "+"value"+"="+"\""+meterTransport.output+"\""+" >");
			pw.println("\t"+"</"+"c"+">");
			pw.println("</"+"a"+">");
			*/
			pw.println("<"+scMapMD5.get("a")+">");
			/*
			pw.println("\t"+"<"+scMapMD5.get("b")+" "+scMapMD5.get("id")+"="+"\""+meterTransport.id+"\""+" >");
			pw.println("\t"+"</"+scMapMD5.get("b")+">");
			pw.println("\t"+"<"+scMapMD5.get("c")+" "+scMapMD5.get("value")+"="+"\""+meterTransport.output+"\""+" >");
			pw.println("\t"+"</"+scMapMD5.get("c")+">");
			*/
			pw.println("\t"+"<"+scMapMD5.get("b")+" "+scMapMD5.get("id")+"="+"\""+meterTransport.id+"\""+" >");
			pw.println("\t"+"</"+scMapMD5.get("b")+">");
			pw.println("\t"+"<"+scMapMD5.get("c")+" >"+10);
			pw.println("\t"+"</"+scMapMD5.get("c")+">");
			
			pw.println("</"+scMapMD5.get("a")+">");
			
			
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
	}
	
	public void create(String file , int id , BigInteger value)
	{
		
		BigInteger eValue = paillier.Encryption(value);
		try 
		{
			//SingleMeterOutput meterTransport = ahe.GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(id, value, cm.getRowOfMatrix(id%cm.getNumberOfRows()), cm.getNumberOfColumns(), cr.getRandoms());
			PrintWriter pw = new PrintWriter(new File("xmldata/"+file+".xml"));
			//pw.println(LocalTime.now());
			
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			pw.println("<"+scMapMD5.get("UsageData")+">");
			
			pw.println("\t"+"<"+scMapMD5.get("PowerData")+">");
			
			pw.print("\t\t"+"<"+scMapMD5.get("Householder")+">");
			pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
			pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
			pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bg=</CipherValue></CipherData></EncryptedData>");
			pw.println("</"+scMapMD5.get("Householder")+">");
			
			pw.println("\t\t"+"<"+scMapMD5.get("Device")+">");
			pw.print("\t\t\t"+"<"+scMapMD5.get("Id")+">");
			pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
			pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
			pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnHHrkBzmRArmKDTxYzYnXdo=</CipherValue></CipherData></EncryptedData>");
			pw.println("</"+scMapMD5.get("Id")+">");
			pw.println("\t\t"+"</"+scMapMD5.get("Device")+">");
			
			pw.print("\t\t"+"<"+scMapMD5.get("Time")+">");
			pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
			pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
			pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnBOAWi/7Pghiutb0og0mDothce2cR+m/kwOjCj+ZeGDT</CipherValue></CipherData></EncryptedData>");
			pw.println("</"+scMapMD5.get("Time")+">");
			
			pw.print("\t\t"+"<"+scMapMD5.get("Record")+">");
			pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
			pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
			pw.print("<CipherData><CipherValue>"+eValue+"</CipherValue></CipherData></EncryptedData>");
			pw.println("</"+scMapMD5.get("Record")+">");
			
			pw.println("\t"+"</"+scMapMD5.get("PowerData")+">");
			
			
			pw.println("</"+scMapMD5.get("UsageData")+">");
			
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
	}
	
	public void create(String file , int id , List<String> ListElement)
	{
		
		try 
		{
			SingleMeterOutput meterTransport = ahe.GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(id, value, cm.getRowOfMatrix(id%cm.getNumberOfRows()), cm.getNumberOfColumns(), cr.getRandoms());
			PrintWriter pw = new PrintWriter(new File("xmldata/"+file+".xml"));
			//pw.println(LocalTime.now());
			
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			pw.println("<"+scMapMD5.get("UsageData")+">");
			
			for(String element : ListElement)
			{
				pw.println("\t"+"<"+scMapMD5.get(element)+">");
				
				pw.print("\t\t"+"<"+scMapMD5.get("Householder")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bg=</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Householder")+">");
				
				pw.println("\t\t"+"<"+scMapMD5.get("Device")+">");
				pw.print("\t\t\t"+"<"+scMapMD5.get("Id")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnHHrkBzmRArmKDTxYzYnXdo=</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Id")+">");
				pw.println("\t\t"+"</"+scMapMD5.get("Device")+">");
				
				pw.print("\t\t"+"<"+scMapMD5.get("Time")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnBOAWi/7Pghiutb0og0mDothce2cR+m/kwOjCj+ZeGDT</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Time")+">");
				
				pw.print("\t\t"+"<"+scMapMD5.get("Record")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>"+meterTransport.output+"</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Record")+">");
				
				pw.println("\t"+"</"+scMapMD5.get(element)+">");
				
			}

			pw.println("</"+scMapMD5.get("UsageData")+">");
			
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
	}
	
	public void create(String file , int id , BigInteger value , List<String> ListElement)
	{
		
		BigInteger eValue = paillier.Encryption(value);
		try 
		{
			//SingleMeterOutput meterTransport = ahe.GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(id, value, cm.getRowOfMatrix(id%cm.getNumberOfRows()), cm.getNumberOfColumns(), cr.getRandoms());
			PrintWriter pw = new PrintWriter(new File("xmldata/"+file+".xml"));
			//pw.println(LocalTime.now());
			
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			pw.println("<"+scMapMD5.get("UsageData")+">");
			
			for(String element : ListElement)
			{
				pw.println("\t"+"<"+scMapMD5.get(element)+">");
				
				pw.print("\t\t"+"<"+scMapMD5.get("Householder")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnPD/V+FoGLR05+ZoYx6t5Bg=</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Householder")+">");
				
				pw.println("\t\t"+"<"+scMapMD5.get("Device")+">");
				pw.print("\t\t\t"+"<"+scMapMD5.get("Id")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnHHrkBzmRArmKDTxYzYnXdo=</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Id")+">");
				pw.println("\t\t"+"</"+scMapMD5.get("Device")+">");
				
				pw.print("\t\t"+"<"+scMapMD5.get("Time")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>hnClcI14k/DCCLPkEfwUnBOAWi/7Pghiutb0og0mDothce2cR+m/kwOjCj+ZeGDT</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Time")+">");
				
				pw.print("\t\t"+"<"+scMapMD5.get("Record")+">");
				pw.print("<EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\">");
				pw.print("<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" />");
				pw.print("<CipherData><CipherValue>"+eValue+"</CipherValue></CipherData></EncryptedData>");
				pw.println("</"+scMapMD5.get("Record")+">");
				
				pw.println("\t"+"</"+scMapMD5.get(element)+">");
				
			}

			pw.println("</"+scMapMD5.get("UsageData")+">");
			
			pw.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
			System.out.println(e.getMessage());
		}
	}
	
	public static float GetMeterOutputFromSingleMachineBySpecificPartOfMatrix(int id,float input, ArrayList<Integer> specificPartOfMatrix, int sizeOfColumn, ArrayList<Integer> randomVariables)
	{
		int tempIndex = 0;
		float maskValue = 0;
		for(tempIndex = 0; tempIndex < sizeOfColumn; ++tempIndex) {
			maskValue += (specificPartOfMatrix.get(tempIndex)*randomVariables.get(tempIndex));
		}
		
		SingleMeterOutput obj = new SingleMeterOutput();
		obj.id =id ;
		obj.output = input + maskValue;
		
		return 0f;
	}
	
	public CompositiveRandoms LoadRandomVariableFromFile(String filename)
	{
		File filep = new File("C:\\CSIE\\_Workspaces\\LearningEclipse\\AdditiveHomomorphicEncryption\\src\\"+filename);
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
	
	public CompositiveMatrix LoadMatrixFromFile(String filename)
	{
		File filep = new File("C:\\CSIE\\_Workspaces\\LearningEclipse\\AdditiveHomomorphicEncryption\\src\\"+filename);
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
	
	public void setIdAndValue(int id , float value)
	{
		this.id = id;
		this.value = value;
	}
	
	public static void main(String[] args)
	{
		CreateXML test = new CreateXML();
		
		test.create("ss");
	}
}
