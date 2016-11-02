package XMLfilter;

import java.util.Set;

public interface InterfaceOfYFilter {

	public abstract void init();

	public abstract void reset();

	public abstract void ReadElement(String elementName);

	public abstract Set<Integer> AcceptedStates();

	public abstract boolean IsAccept();

	public abstract void PopElement();

	public abstract boolean AppendXPath(String xpath);
	/*
	public boolean AppendXPath(char[] xpath) {
		if(CreateXPathIndex(ChangeXPathExpression(new String(xpath)))) {
			return true;
		}
		return false;
	}*/

}