package Sink;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class meterStream 
{
	URL url;
	URLConnection urlc;
	public String strURI = "";
	
	public void setUrl(String url) throws MalformedURLException
	{
		this.strURI = new String(url);
		this.url = new URL(url);		
	}
	
	public InputStream getStream() throws IOException
	{
		urlc = url.openConnection();
		//urlc.setConnectTimeout(100);
		//urlc.setReadTimeout(100);
		//return urlc.getInputStream();
		return url.openStream();
	}
	
	public InputStream getStream(String filep) throws IOException
	{
		InputStream input = new FileInputStream(new File(filep));
		
		return input;
	}
	
	// Server Client new , 20161011
	public byte[] bytef;
	
	public void setByteArray(byte[] bytef)
	{
		this.bytef = bytef;
	}
	
	public InputStream getStream(byte[] bytef)
	{
		InputStream input = new ByteArrayInputStream(bytef);
		
		return input;
	}
}
