package XMLfilter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.xpath.XPathException;

import org.jaxen.saxpath.SAXPathException;
import org.xml.sax.InputSource;

import Sink.XMLDogTask;
import Sink.XMLDogTaskReducer;
import jlibs.xml.DefaultNamespaceContext;
import jlibs.xml.Namespaces;
import jlibs.xml.sax.dog.NodeItem;
import jlibs.xml.sax.dog.XMLDog;
import jlibs.xml.sax.dog.XPathResults;
import jlibs.xml.sax.dog.expr.Expression;

public class callXMLDogFilter implements Callable<List<String>>
{
	String filename; 
	
	URL url;
	String strURI ;
	InputStream input;
	//DefaultNamespaceContext nsContext;
	//XMLDog dog ;
	// Expression xpath1 , xpath2;	
	private List<XMLDogTask> taskList = null;
	
	List<NodeItem> nodeItemID,nodeItemValue;

	int id;
	List<String> value;
	public int maxThreads = 0;
	
	public boolean isFinished = false;
	public long endtime ;
	
	//server client new , 20161011
	byte[] bytef;
	
	public callXMLDogFilter(String URI)  {
		strURI = new String(URI);
	}
	
	public callXMLDogFilter(byte[] bytef)  {
		this.bytef = bytef;
	}
	
	public callXMLDogFilter()
	{
		
	}
	
	public void SetTaskList(List<XMLDogTask> list) {
		this.taskList = list;
	}
	
	private void init() 
	{
		try {
			setUrl(this.strURI);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		try {
			setUrl(this.strURI);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		nsContext = new DefaultNamespaceContext(); // an implementation of javax.xml.namespace.NamespaceContext
		nsContext.declarePrefix("xsd", Namespaces.URI_XSD);
		dog = new XMLDog(nsContext);
		try 
		{
			xpath1 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/@aB80BB7740288FDA1F201890375A60C8F");
			xpath2 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/@a2063C1608D6E0BAF80249C42E2BE5804");	
		} 
		catch (SAXPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */ 
	}
	
	public void setFileStream(String filename)
	{
		this.filename = filename;
	}
	
	private void setUrl(String URI) throws MalformedURLException
	{
		this.url = new URL(URI);
	}
	
	public void setURI(String URI) {
		strURI = new String(URI);
	}
	
	public void setStream(InputStream inStream)
	{
		this.input = inStream;
	}
	
	public int getID()
	{
		return id;
	}
	
	public List<String> getValue()
	{
		return value;
	}
	
	@Override
	public List<String> call() 
	{
		// TODO Auto-generated method stub
		try 
		{		
			int threadId = (int) Thread.currentThread().getId();
			
			//server client new , 2016
			//init();
			
			XMLDogTask task = taskList.get(threadId%taskList.size());
			
			/* 
			input = url.openStream();
			XPathResults results = task.dog.sniff(new InputSource(input));
			input.close();
			*/
			
			//server client new , 2016
			input = new ByteArrayInputStream(bytef);
			XPathResults results = task.dog.sniff(new InputSource(input));
			input.close();
			
			value = new ArrayList<String>();
			
			for(Expression xpath: task.xpathList) 
			{
				List<NodeItem> list = (List<NodeItem>) results.getResult(xpath);
				//System.out.println(list.isEmpty());
				if(list.isEmpty())
				{
					value.add("0");
					//System.out.println(0);
				}
				else
				{
					//System.out.println(list.get(0).value);
					value.add(list.get(0).value);
				}
			}
			//System.out.println(value.size());
			/*
			nodeItemID = (List<NodeItem>)results.getResult(xpath1);
			nodeItemValue = (List<NodeItem>)results.getResult(xpath2);
			
			id = Integer.parseInt(nodeItemID.get(0).value);
			value = Float.parseFloat(nodeItemValue.get(0).value);
			*/		
			
		} 
		catch (XPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
	
	public static void main(String[] args)
	{
		
	}
	
}
