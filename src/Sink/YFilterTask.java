package Sink;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import jlibs.xml.sax.SAXProducer;
import XMLfilter.InterfaceOfYFilter;
import XMLfilter.SAXYFilterHandler;

public class YFilterTask 
{
	public InterfaceOfYFilter myFilter;
	public SAXYFilterHandler myHandler;
	public SAXParser saxParser;
	public List<String> xpathList = new ArrayList<String>();
	
	public void AddXPath(String xpath) 
	{
			xpathList.add(xpath);			
	}
	
	public YFilterTask() {

		try 
		{
			saxParser = SAXParserFactory.newInstance().newSAXParser();
		} 
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
