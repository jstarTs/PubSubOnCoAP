package meter;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.jaxen.saxpath.SAXPathException;

import Sink.XMLDogTaskReducer;
import jlibs.xml.DefaultNamespaceContext;
import jlibs.xml.Namespaces;
import jlibs.xml.sax.dog.NodeItem;
import jlibs.xml.sax.dog.XMLDog;
import jlibs.xml.sax.dog.XPathResults;
import jlibs.xml.sax.dog.expr.Expression;

public class testMeterValue 
{
	String filename; 
	URL url;
	InputStream input;
	DefaultNamespaceContext nsContext;
	XMLDog dog ;
	Expression xpath1 , xpath2;
	XPathResults results;
	List<NodeItem> nodeItemID,nodeItemValue;
	public XMLDogTaskReducer refTask ;
	int id;
	float value;
	
	public boolean isFinished = false;
	public long endtime ;
	
	public testMeterValue()
	{
		nsContext = new DefaultNamespaceContext(); // an implementation of javax.xml.namespace.NamespaceContext
		nsContext.declarePrefix("xsd", Namespaces.URI_XSD);
		dog = new XMLDog(nsContext);
		try 
		{
			/*
			xpath1 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/@aB80BB7740288FDA1F201890375A60C8F");
			xpath2 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/@a2063C1608D6E0BAF80249C42E2BE5804");	
			*/
			xpath1 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/aC5D782A175804CA0FB082EA4CFA8C98C");
			xpath2 = dog.addXPath("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/aC5D782A175804CA0FB082EA4CFA8C98C");	
		
			//aC5D782A175804CA0FB082EA4CFA8C98C
		} 
		catch (SAXPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void main()
	{
		
	}
}
