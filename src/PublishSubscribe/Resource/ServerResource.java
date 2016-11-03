package PublishSubscribe.Resource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.server.resources.CoapExchange;

/*  On Server
 * 
 */

public class ServerResource extends CoapResource
{

	public ServerResource() {
		super("testResource");
		getAttributes().setTitle("Test Resource)");
		setVisible(false);
	}
	
	/**
	 * Use Create Topic
	 * 
	 * (non-Javadoc)
	 * @see org.eclipse.californium.core.CoapResource#handlePOST(org.eclipse.californium.core.server.resources.CoapExchange)
	 */
	@Override
	public void handlePOST(CoapExchange exchange)
	{
		getAttributes().setTitle(exchange.getRequestPayload().toString());
		
		try 
		{
			Connection conn = PublishSubscribe.MySQL.getConnection();
			Statement st = conn.createStatement();
			
			//創造主題
			
			ResultSet result = st.executeQuery("SELECT COUNT(TopicName) AS total from TopicTable where TopicName=" + "\'"+exchange.getRequestPayload()+"\';");
			result.next();
			if(result.getInt("total")>0)
			{
				exchange.respond("Repeating Creation");
				System.out.println("Repeating Created The Topic : "+ new String(exchange.getRequestPayload()));
			}	
			else
			{
				
				st.executeUpdate("INSERT INTO TopicTable VALUES (\'"+exchange.getRequestPayload()+"\');");
				exchange.respond("Created this Topic");
				
				//創造主題
				add(new SubscribedTopic(new String(exchange.getRequestPayload())));
				exchange.respond("Thx Creating!");
				
				System.out.println("OK Created The Topic : "+ new String(exchange.getRequestPayload()));
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
		
}
