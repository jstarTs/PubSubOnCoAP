import jlibs.xml.sax.dog.NodeItem;
import jlibs.xml.sax.dog.XMLDog;
import jlibs.xml.sax.dog.XPathResults;
import jlibs.xml.sax.dog.expr.Expression;

import org.jaxen.saxpath.SAXPathException;
import org.xml.sax.InputSource;

import jlibs.xml.DefaultNamespaceContext;
import jlibs.xml.Namespaces;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathException;

public class TestXML 
{
	
	
	
	public static void main(String[] args)
	{
		DefaultNamespaceContext nsContext = new DefaultNamespaceContext();
		nsContext.declarePrefix("xs", Namespaces.URI_XSD);
		
		XMLDog dog = new XMLDog(nsContext);

		try 
		{
			Expression xpath1 = dog.addXPath("/xs:schema/@targetNamespace");
			Expression xpath2 = dog.addXPath("/xs:schema/xs:complexType/@name");
			Expression xpath3 = dog.addXPath("/xs:schema/xs:*/@name");
			Expression xpath4 = dog.addXPath("/moreovernews/article/url/text()");
			
			System.out.println(xpath2.getXPath());
			
			QName resultType = xpath1.resultType.qname;
			System.out.println(resultType);
			
			XPathResults results = dog.sniff(new InputSource("moreover.xml"));

			List<NodeItem> lt = (List<NodeItem>)results.getResult(xpath4);
			for(int i = 0 ; i < lt.size() ; i++)
			{
				System.out.println(lt.get(i).value);
			}
			
			System.out.println("...................");
			results.print(dog.getXPaths(), System.out);
			
		} 
		catch (SAXPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (XPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
}
