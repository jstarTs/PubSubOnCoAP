package Sink;

import java.util.ArrayList;
import java.util.List;

import org.jaxen.saxpath.SAXPathException;

import jlibs.xml.sax.dog.XMLDog;
import jlibs.xml.sax.dog.expr.Expression;

public class XMLDogTask {
	public XMLDog dog;
	public List<Expression> xpathList = new ArrayList<Expression>();
	
	public void AddXPath(String xpath) {
		try {
			xpathList.add(dog.addXPath(xpath));
		} catch (SAXPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
