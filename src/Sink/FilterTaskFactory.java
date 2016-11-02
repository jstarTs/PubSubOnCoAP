package Sink;

import java.util.List;

import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPathFactoryConfigurationException;

import XMLfilter.ImprovedYFilter;
import XMLfilter.SAXYFilterHandler;
import XMLfilter.YFilter;
import jlibs.xml.DefaultNamespaceContext;
import jlibs.xml.Namespaces;
import jlibs.xml.sax.dog.XMLDog;

public class FilterTaskFactory {
	public static Object Create(String type, List<String> xpathList) {
		Object object = null;
		if(type.trim().toLowerCase().equals("xmldog")) {
			XMLDogTask myTask = new XMLDogTask();
			DefaultNamespaceContext  nsContext = new DefaultNamespaceContext(); // an implementation of javax.xml.namespace.NamespaceContext
			nsContext.declarePrefix("xsd", Namespaces.URI_XSD);
			
			myTask.dog =  new XMLDog(nsContext);

			for(int i = 0; i < xpathList.size(); ++i) {
				myTask.AddXPath(xpathList.get(i));
			}
			object = myTask;
		}
		else if(type.trim().toLowerCase().equals("saxon"))
		{
			try 
			{
				SaxonTask myTask = new SaxonTask();
				
				myTask.saxon = new SAXSource();
				
				for(int i = 0; i < xpathList.size(); ++i) 
				{
					myTask.AddXPath(xpathList.get(i));
				}
				object = myTask;
			} 
			catch (XPathFactoryConfigurationException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(type.trim().toLowerCase().equals("yfilter"))
		{
			YFilterTask myTask = new YFilterTask();
			
			myTask.myFilter = new YFilter();
			myTask.myHandler = new SAXYFilterHandler();
			
			for(int i = 0; i < xpathList.size(); ++i) 
			{
				myTask.AddXPath(xpathList.get(i));
				myTask.myFilter.AppendXPath(xpathList.get(i));
			}

			object = myTask;			
		}
		else if(type.trim().toLowerCase().equals("improvedyfilter"))
		{
			YFilterTask myTask = new YFilterTask();
			
			myTask.myFilter = new ImprovedYFilter();
			myTask.myHandler = new SAXYFilterHandler();
			
			for(int i = 0; i < xpathList.size(); ++i) 
			{
				myTask.AddXPath(xpathList.get(i));
				myTask.myFilter.AppendXPath(xpathList.get(i));
			}

			object = myTask;			
		}
		
		return object;
	}
}
