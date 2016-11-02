package XMLfilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import javax.xml.xpath.XPathVariableResolver;

import org.xml.sax.InputSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.lib.NamespaceConstant;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.om.TreeInfo;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import net.sf.saxon.trans.XPathException;
import net.sf.saxon.xpath.XPathFactoryImpl;

public class runnableSaxonFilter implements Runnable, XPathVariableResolver
{
	int id ; 
	float value;
	
	InputStream input;
	File filename ;
	XPathFactory xpf;
	XPath xpe ;
	
	public boolean isFinished = false;
	public long endtime , starttime , total;
	
	private String currentWord;
	
	public runnableSaxonFilter() throws XPathFactoryConfigurationException 
	{
		// TODO Auto-generated constructor stub
		System.setProperty("javax.xml.xpath.XPathFactory:"+NamespaceConstant.OBJECT_MODEL_SAXON,"net.sf.saxon.xpath.XPathFactoryImpl");
		xpf = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
		xpe = xpf.newXPath();
        
	}
	
	public void setFile(String filename)
	{
		this.filename = new File(filename);
	}
	
	public void setInputStream(InputStream instream)
	{
		this.input = instream;
	}
	
	public int getID()
	{
		return id;
	}
	
	public float getValue()
	{
		return value;
	}
	
	public long getProcessingTime()
	{
		return total;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		starttime = System.currentTimeMillis();
		// Build the source document. This is outside the scope of the XPath API, and is therefore Saxon-specific.
		try 
		{
			//instream = new FileInputStream(filename);
			InputSource is = new InputSource(input);
	        //InputSource is = new InputSource(filename);
	        SAXSource ss = new SAXSource(is);
	        
	        Configuration config = ((XPathFactoryImpl) xpf).getConfiguration();
	        //DocumentInfo doc = config.buildDocument(ss);
	        TreeInfo tree = config.buildDocumentTree(ss);
	
	        // Declare a variable resolver to return the value of variables used in XPath expressions
	        xpe.setXPathVariableResolver(this);

	        // Compile the XPath expressions used by the application

	        //xpe.setNamespaceContext(this);

	        XPathExpression findID = xpe.compile("/a0CC175B9C0F1B6A831C399E269772661/a92EB5FFEE6AE2FEC3AD71C777531578F/@aB80BB7740288FDA1F201890375A60C8F");
	        //XPathExpression findID = xpe.compile("//LINE[contains(., $word)]");	
	        XPathExpression findValue = xpe.compile("/a0CC175B9C0F1B6A831C399E269772661/a4A8A08F09D37B73795649038408B5F33/@a2063C1608D6E0BAF80249C42E2BE5804");
	        //XPathExpression findValue = xpe.compile("concat(ancestor::ACT/TITLE, ' ', ancestor::SCENE/TITLE)");
	        //XPathExpression findSpeaker = xpe.compile("//d/text()");*/
	        //XPathExpression findValue = xpe.compile("string(ancestor::SPEECH/SPEAKER[1])");
	        
	        List matchedLines = (List)findID.evaluate(tree, XPathConstants.NODESET);
	        NodeInfo idNode = (NodeInfo)matchedLines.iterator().next();
	        id = Integer.parseInt(idNode.getStringValue());
	        //System.out.println(id);
	        
	        List matchedLocation = (List)findValue.evaluate(tree, XPathConstants.NODESET);
	        NodeInfo valueNode = (NodeInfo)matchedLocation.iterator().next();
	        value = Float.parseFloat(valueNode.getStringValue());
	        //System.out.println(value);
	        
	        isFinished = true;
			
	        input.close();
			endtime = System.currentTimeMillis();
	        total = endtime - starttime;
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (XPathException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args)
	{
		try 
		{
			runnableSaxonFilter rsf = new runnableSaxonFilter();
			rsf.setFile("testxml.xml");
			rsf.run();
		} 
		catch (XPathFactoryConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
