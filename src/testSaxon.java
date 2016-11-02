import java.io.File;

import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.xml.sax.InputSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.lib.NamespaceConstant;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.xpath.XPathFactoryImpl;

public class testSaxon 
{

	public static void main(String[] args) throws XPathFactoryConfigurationException, XPathException
	{
		System.setProperty("javax.xml.xpath.XPathFactory:"+NamespaceConstant.OBJECT_MODEL_SAXON,
	              "net.sf.saxon.xpath.XPathFactoryImpl");

	        XPathFactory xpf = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
	        XPath xpe = xpf.newXPath();
	        System.err.println("Loaded XPath Provider " + xpe.getClass().getName());
	        
	        //InputSource is = new InputSource(new File("testxml1.xml").toURL().toString());
	        InputSource is = new InputSource("testxml1.xml");
	        SAXSource ss = new SAXSource(is);
	        Configuration config = ((XPathFactoryImpl) xpf).getConfiguration();
	        DocumentInfo doc = config.buildDocument(ss);
	        
	}
}
