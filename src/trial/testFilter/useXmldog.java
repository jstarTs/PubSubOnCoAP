package trial.testFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathException;

import org.jaxen.saxpath.SAXPathException;
import org.xml.sax.InputSource;

import jlibs.xml.DefaultNamespaceContext;
import jlibs.xml.Namespaces;
import jlibs.xml.sax.dog.NodeItem;
import jlibs.xml.sax.dog.XMLDog;
import jlibs.xml.sax.dog.XPathResults;
import jlibs.xml.sax.dog.expr.Expression;

public class useXmldog implements Runnable
{
	static DefaultNamespaceContext nsContext = new DefaultNamespaceContext();
	XMLDog dog = new XMLDog(nsContext);
	XPathResults results ;
	String xpath,file;
	public List<Float> list;
	public boolean isFinished = false;
	
	public useXmldog()
	{
		//nsContext = new DefaultNamespaceContext();
		//dog = new XMLDog(nsContext);
		setDeclarePrefix("xs");
	}
	
	public void setDeclarePrefix(String xsd)
	{
		nsContext.declarePrefix(xsd, Namespaces.URI_XSD);
	}
	
	public Expression addXpath(String xpathString)
	{
		Expression xpath;
		try 
		{
			xpath = dog.addXPath(xpathString);
			return xpath;
		} 
		catch (SAXPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void setfile(String filename)
	{
		file = filename;
	}
	
	public XPathResults setXMLFile(String file)
	{
		try 
		{
			XPathResults results = dog.sniff(new InputSource(file));
			return results;
		} 
		catch (XPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Float> inputdata(List<NodeItem> list)
	{
		List<Float> floatlist = new ArrayList<Float>();
		
		for(int i=0 ; i<list.size() ; i++)
		{
			floatlist.add(Float.parseFloat(list.get(i).value));
		}
		
		return floatlist;
	}
	
	public List<Float> getList()
	{
		return list;
	}
	
	public float[] inputvalue(List<Float> list)
	{
		float[] fa = new float[list.size()];
		
		for(int i=0 ; i<list.size() ; i++)
		{
			fa[i] = list.get(i);
		}
		
		return fa;
	}
	
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		
		file = "testxml1.xml";
		xpath = "/a/c/@value";
		Expression ex;
		try 
		{
			ex = dog.addXPath(xpath);
			System.out.println(xpath+"run");
			XPathResults results = setXMLFile(file);
			results.getResult(ex);
			List<NodeItem> lt = (List<NodeItem>)results.getResult(ex);
			System.out.println(lt.size()+"@run");
			
			list = inputdata(lt);
			System.out.println(list.size()+"$run");
			
			if(list.size()>0)
				isFinished = true;
		} catch (SAXPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args)
	{
		DefaultNamespaceContext nsContext = new DefaultNamespaceContext(); // an implementation of javax.xml.namespace.NamespaceContext
		nsContext.declarePrefix("xs", Namespaces.URI_XSD);
		
		useXmldog test = new useXmldog();
		Expression ex = test.addXpath("/a/c/@value");
		//List<NodeItem> list = (List<NodeItem>)(test.setXMLFile("testxml1.xml").getResult(ex));
		//System.out.println(list.size());
		
		XMLDog dog = new XMLDog(nsContext);
		XPathResults results;
		try 
		{
				
			//Expression xpath1 = dog.addXPath("/a/b/@id");
			//Expression xpath2 = dog.addXPath("/a/c/@value");
			
			/*
			Expression xpath1 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/text()");
			Expression xpath2 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/text()");
			*/
			
			
			Expression xpath1 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/a5196F1040C636101B5797C3CBC926613//text()");
			Expression xpath2 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/aE0AC20ADCE6FFEE48C7151B070AA5737/a490AA6E856CCF208A054389E47CE0D06//text()");
			Expression xpath3 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/aA76D4EF5F3F6A672BBFAB2865563E530//text()");
			Expression xpath4 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/a6A0D9EAEE314C567FD72FB97EE707A36//text()");
			
			
			/*
			Expression xpath1 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/a5196F1040C636101B5797C3CBC926613/EncryptedData/CipherData/CipherValue/text()");
			Expression xpath2 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/aE0AC20ADCE6FFEE48C7151B070AA5737/a490AA6E856CCF208A054389E47CE0D06/EncryptedData/CipherData/CipherValue/text()");
			Expression xpath3 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/aA76D4EF5F3F6A672BBFAB2865563E530/EncryptedData/CipherData/CipherValue/text()");
			Expression xpath4 = dog.addXPath("/a85EF20040AB4A17A1908D5575C2A1F0D/a6A0D9EAEE314C567FD72FB97EE707A36/EncryptedData/CipherData/CipherValue/text()");
			*/
			
			System.out.println(xpath1.getXPath());
			
			QName resultType = xpath1.resultType.qname;
			System.out.println(resultType); 
			
			
			//results = dog.sniff(new InputSource("./xmldata/testxml1.xml"));
			
			
			File fi = new File("./xmldata/testE1.xml");
			InputStream is = new FileInputStream(fi);
			
			results = dog.sniff(new InputSource(is));
			
			List<NodeItem> list1 = (List<NodeItem>)results.getResult(xpath1);
			List<NodeItem> list2 = (List<NodeItem>)results.getResult(xpath2);
			List<NodeItem> list3 = (List<NodeItem>)results.getResult(xpath3);
			List<NodeItem> list4 = (List<NodeItem>)results.getResult(xpath4);
			System.out.println(list1.size()+","+list2.size());
			System.out.println(list3.size()+","+list4.size());
			//System.out.println(list.isEmpty() ? null : list.get(0).value);
			for(int i=0 ; i<list1.size() ; i++)
			{
				System.out.println(list1.get(i).value);
				System.out.println(list2.get(i).value);
				System.out.println(list3.get(i).value);
				System.out.println(list4.get(i).value);
			}
			
		} 
		catch (XPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SAXPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
