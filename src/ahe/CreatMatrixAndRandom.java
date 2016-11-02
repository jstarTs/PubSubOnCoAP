package ahe;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ahe.Matrix.AccessMatrix;
import ahe.RandomNumber.AccessRandom;

public class CreatMatrixAndRandom 
{
	public static void main(String[] args)
	{
		Ahe createMatrixRandom = new Ahe();
		mainFunction mf = new mainFunction();
		AccessRandom ar = new AccessRandom();
		AccessMatrix am = new AccessMatrix();
		
		am.WriteToMatrixFile("testmatrix.txt", 100, 20, 125);
		ar.WriteToRandomVariableFile("testRandom.txt", 20, 87);
		
	}
	
}
