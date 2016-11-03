package PublishSubscribe.Resource;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.sql.Connection;
import java.sql.ResultSet;
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
		
		//exchange.getSourceAddress().getHostAddress();
		//this.getName();
		
		exchange.respond("Hello test!");
		
		
		
		//訂閱 topic ( 此加入 topic and subscriber uri)
		try 
		{
			Connection conn = PublishSubscribe.MySQL.getConnection();
			Statement st = conn.createStatement();
			
			//判斷是否有訂閱過
			ResultSet result = st.executeQuery("SELECT COUNT(Topic) AS total from SubscriberData where Topic=" + "\'"+this.getName()+"\' and URI=\'"+exchange.getSourceAddress().getHostAddress()+"\';");
			result.next();
			if(result.getInt("total")>0)
				exchange.respond("Repeating subscription");
			else
			{
				st.executeUpdate("INSERT INTO SubscriberData VALUES (\'"+this.getName()+"\',\'"+exchange.getSourceAddress().getHostAddress()+"\');");
				exchange.respond("Subscribed this Topic");
			}
			
			conn.close();
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
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
