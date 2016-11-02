package PublishSubscribe.Resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/*  On Server
 * 
 */

public class SubscribedTopic extends CoapResource 
{
	
	public SubscribedTopic(String topicName)
	{
		super(topicName);
		
		getAttributes().setTitle(topicName);
	}
	
	
	
	/**
	 * Use Publish
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handlePUT(org.eclipse.californium.core.server.resources.CoapExchange)
	 */
	/* 在PubSubServer用不到的功能
	@Override
	public void handlePUT(CoapExchange exchange)
	{
		
	}
	*/
	
	/**
	 * Use Subscribe
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handleGET(org.eclipse.californium.core.server.resources.CoapExchange)
	 */
	@Override 
	public void handleGET(CoapExchange exchange)
	{
		String SQL;
		
		exchange.getSourceAddress().getHostAddress();
		this.getName();
		
		exchange.respond("Hello test!");
		
		//訂閱 topic ( 此加入 topic and subscriber uri)
		try 
		{
			Statement st = PublishSubscribe.MySQL.getConnection().createStatement();
			
			st.executeUpdate("INSERT INTO SubscriberData VALUES (\'"+this.getName()+"\',\'"+exchange.getSourceAddress().getHostAddress()+"\');");
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handleDELETE(org.eclipse.californium.core.server.resources.CoapExchange)
	 */
	@Override
	public void handleDELETE(CoapExchange exchange)
	{
		exchange.respond("DETELE "+ this.getName());
		
		//刪除 topic DB data (topic and subscriber data)
		
		//刪除topic Resource
		delete();
	}
	
}
