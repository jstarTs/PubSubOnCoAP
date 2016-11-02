package Sink;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import net.sf.saxon.lib.NamespaceConstant;
import net.sf.saxon.sxpath.XPathExpression;

public class SaxonTask 
{
	public SAXSource saxon;
	//public List<XPathExpression> xpathList = new ArrayList<XPathExpression>();
	public List<String> xpathList = new ArrayList<String>();
	XPathFactory xpf;
	XPath xpe ;
	
	public SaxonTask() throws XPathFactoryConfigurationException 
	{
		// TODO Auto-generated constructor stub
		System.setProperty("javax.xml.xpath.XPathFactory:"+NamespaceConstant.OBJECT_MODEL_SAXON,"net.sf.saxon.xpath.XPathFactoryImpl");
		xpf = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
		xpe = xpf.newXPath();
	}
	
	public void AddXPath(String xpath) 
	{
			xpathList.add(xpath);
	}
	
	/*
	public void AddXPath(String xpath) 
	{
		try 
		{
			xpathList.add((XPathExpression) xpe.compile(xpath));
		} 
		catch (XPathExpressionException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
}
