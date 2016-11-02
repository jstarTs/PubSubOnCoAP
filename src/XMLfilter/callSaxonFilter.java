package XMLfilter;

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

import javax.xml.namespace.QName;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathVariableResolver;

import org.xml.sax.InputSource;

import Sink.SaxonTask;
import net.sf.saxon.Configuration;
import net.sf.saxon.lib.NamespaceConstant;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.TreeInfo;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import net.sf.saxon.trans.XPathException;
import net.sf.saxon.xpath.XPathFactoryImpl;

public class callSaxonFilter implements Callable, XPathVariableResolver
{
	InputStream input;
	//File filename ;
	XPathFactory xpf;
	XPath xpe ;
	
	URL url;
	String strURI ;
	Configuration config;
	
	List<String> value;
	private String currentWord;
	
	private List<SaxonTask> taskList = null;
	
	
	
	public callSaxonFilter() 
	{
		// TODO Auto-generated constructor stub
		System.setProperty("javax.xml.xpath.XPathFactory:"+NamespaceConstant.OBJECT_MODEL_SAXON,"net.sf.saxon.xpath.XPathFactoryImpl");
		try 
		{
			xpf = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
		} 
		catch (XPathFactoryConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xpe = xpf.newXPath();
		xpe.setXPathVariableResolver(this);
		config = ((XPathFactoryImpl) xpf).getConfiguration();
	}
	
	public callSaxonFilter(String URI) 
	{
		this();
		strURI = new String(URI);
	}
	
	public void SetTaskList(List<SaxonTask> list) 
	{
		this.taskList = list;
	}
	
	private void init()
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
	
	public void setURI(String URI) {
		strURI = new String(URI);
	}
	
	public void setInputStream(InputStream instream)
	{
		this.input = instream;
	}
	
	@Override
	public List<String> call() 
	{
		// TODO Auto-generated method stub
		// Build the source document. This is outside the scope of the XPath API, and is therefore Saxon-specific.
		
		try 
		{
			int threadId = (int) Thread.currentThread().getId();
			init();
			SaxonTask task = taskList.get(threadId%taskList.size());
			input = url.openStream();
			task.saxon = new SAXSource(new InputSource(input));
	        TreeInfo tree = config.buildDocumentTree(task.saxon);
	        input.close();
	        
	       value = new ArrayList<String>();
	        for(String xpath : task.xpathList)
	        {
	        	XPathExpression  xpathExpression = xpe.compile(xpath);
	        	List matchedLines = (List)xpathExpression.evaluate(tree, XPathConstants.NODESET);
	        	NodeInfo vNode = (NodeInfo)matchedLines.iterator().next();
	        	value.add(vNode.getStringValue());
	        }
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return value;
	}
	
	public static void main(String[] args)
	{

	}

	@Override
	/**
     * This class serves as a variable resolver. The only variable used is $word.
     * @param qName the name of the variable required
     * @return the current value of the variable
     */
    public Object resolveVariable(QName qName) {
        if (qName.getLocalPart().equals("word")) {
            return currentWord;
        } else {
            return null;
        }
    }
}
