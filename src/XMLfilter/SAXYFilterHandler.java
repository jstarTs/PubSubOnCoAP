package XMLfilter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.mutable.MutableInt;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
*/

//import xml2rdf.util.rdf.GenericResourceValidator;

public class SAXYFilterHandler extends DefaultHandler {
	
	public InterfaceOfYFilter currentFilter = null;
	//static GenericResourceValidator validator = new GenericResourceValidator();
	public String outputStream = new String();
	protected Map<String, MutableInt> elementCountsTable = new HashMap<String, MutableInt>();
	
	//public Model model = ModelFactory.createDefaultModel();
	//public Resource rootResource = null;
	//public Stack<Resource> resourceStack = new Stack<Resource>();
	
	public String relationshipUri = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public String xmlns = "";
	public String rootResourceNamespace = "http://example.com/";
//	protected boolean isFirstElement = true;
//	//Use automatic namespace mapping.
//	public boolean useAutomaticNamespaceMapping = false;
	
	public List<String> id = new ArrayList<String>();  
	public List<String> value;
	
	/**
	 * Xpath List. Record current xpaht elements.
	 */
	protected List<String> currentXPathList = new ArrayList<String>() {
			
		/**
		 * 
		 */
		private static final long serialVersionUID = 1189586247780969224L;//?

		@Override 
		public String toString() {
			String result = "";
			for( int i = 0; i < this.size(); ++i ) {
				result += "/" + this.get(i);			
			}
			return result;
		}
	};
	
	/**
	 * Xpath List. Record current xpaht elements.
	 */
	protected List<String> outputXPathList = new ArrayList<String>() {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 
		 */
		

		@Override 
		public String toString() {
			String result = "";
			for( int i = 0; i < this.size(); ++i ) {
				result += "/" + this.get(i);			
			}
			return result;
		}
	};
	
	
	/**
	 * 
	 */		
	
	
	public SAXYFilterHandler() { 
		
	}
	
	public void init()
	{
		id.clear();
		value = new ArrayList<String>(); 
	}
	
	public void setHandler(InterfaceOfYFilter srcFilter) {
		setFilter(srcFilter);
	}
	
	public void setWriter(StringWriter writer) {
		// this.outputStream = writer;
	}

	
	public void setFilter(InterfaceOfYFilter srcFilter) {
		currentFilter = srcFilter; 
	}
	
	public String exportCurrentXPath() {
		return this.currentXPathList.toString();
	}
	
	public String exportOutputXPath() {
		return this.outputXPathList.toString();
	}	
	
	@Override
	public void startElement(String uri, String localName,String qName, Attributes attributes) 
	{

		if( currentFilter != null ) 
		{
			
			currentFilter.ReadElement(qName);
			
			if(currentFilter.IsAccept()) 
			{				
				// output the stream.
				// TODO: Create RDF Resource type.
				try 
				{

				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		else 
		{
			try 
			{
				//TODO: Create RDF Resource type.
				// If it's in accept status, check the attributes in it or not.
				System.out.println("currentFilter is null");
			} 
			catch (Exception e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}				
		}
	}
	
	@Override
	public void endElement(String uri, String localName,String qName) 
	{
		//System.out.println("End\n");
		// Pop out the last one.
		if(currentXPathList.size() > 0) {
			currentXPathList.remove(currentXPathList.size() -1);
		}
		if(outputXPathList.size() > 0) {
			outputXPathList.remove(outputXPathList.size() -1);
		}
		
		if(currentFilter != null) {
			if(currentFilter.IsAccept()) {
				//resourceStack.pop();
			}
			currentFilter.PopElement();						
		} 
		else
		{
			System.out.println("currentFilter is null");
		}
	}
	
    public void characters(char ch[], int start, int length) throws SAXException {
    	String value = new String(ch, start, length).trim();
    	value = value.trim();
    	System.out.println("..."+value);
    	try 
    	{
    		if(value.length()>0) 
    		{
    			if(currentFilter != null && currentFilter.IsAccept())
    			{
    				this.value.add(value);
    			}
    		}
		} 
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void ignorableWhitespace(char ch[], int start, int length) throws SAXException 
    {
    	
    }
}
