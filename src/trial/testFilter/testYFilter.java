package trial.testFilter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import XMLfilter.InterfaceOfYFilter;
import XMLfilter.SAXYFilterHandler;
import XMLfilter.YFilter;

public class testYFilter  extends DefaultHandler
{
	
	
	public static void main(String[] args)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		
		try {
			SAXParser saxParser = factory.newSAXParser();
			//InputStream    xmlInput  = new FileInputStream("STELL-I_3.rtml");
			//InputStream    xmlInput  = new FileInputStream("testxml.xml");
			InputStream    xmlInput  = new FileInputStream("xmldata/testxml1.xml");
			//InputStream    xmlInput  = new FileInputStream("xmldata/testE1.xml");
			InterfaceOfYFilter myFilter = new YFilter();
			SAXYFilterHandler handler = new SAXYFilterHandler();
//			myFilter.AppendXPath("/a/b");
//			myFilter.AppendXPath("/a/c");
//			myFilter.AppendXPath("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/");
//			myFilter.AppendXPath("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/");
			myFilter.AppendXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/a5196F1040C636101B5797C3CBC926613/");
			myFilter.AppendXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/a6A0D9EAEE314C567FD72FB97EE707A36//");
//			myFilter.ReadElement("a");
//			myFilter.ReadElement("c");
			//myFilter.ReadElement("");
			//myFilter.AcceptThenPrint(myFilter);
			
			handler.setFilter(myFilter);
			
			saxParser.parse(xmlInput , handler);
			
			System.out.println(handler.value.size());
			for(String value :handler.value)
			{
				System.out.println(value+":");
			}
			
		} 
		catch (ParserConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
