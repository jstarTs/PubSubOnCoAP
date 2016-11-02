package XMLfilter;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import Sink.YFilterTask;

public class callYFilter implements Callable<List<String>>
{	
	URL url;
	String strURI ;
	InputStream input;

	int id;
	private List<YFilterTask> taskList = null;
	
	public callYFilter() 
	{
		
	}
	
	public callYFilter(String URI)  
	{
		strURI = URI; 
		/*factory = SAXParserFactory.newInstance();
		try 
		{
			saxParser = factory.newSAXParser();
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
		} */
	}
	
	public void init()
	{
		try 
		{
			setUrl(this.strURI);
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setUrl(String URI) throws MalformedURLException
	{
		this.url = new URL(URI);
	}
	
	public void setURI(String URI) 
	{
		strURI = new String(URI);
	}
	
	public void SetTaskList(List<YFilterTask> list) 
	{
		this.taskList = list;
	}
	
	@Override
	public List<String> call() throws Exception 
	{
		// TODO Auto-generated method stub
		int threadId = (int) Thread.currentThread().getId();
		init();
		YFilterTask mytask = taskList.get(threadId%taskList.size());
		
		mytask.myFilter.reset();
		mytask.myHandler.init();

		mytask.myHandler.setFilter(mytask.myFilter);
		
		input = url.openStream();
		mytask.saxParser.parse(input, mytask.myHandler);

		input.close();
		
		return mytask.myHandler.value;
	}
}
