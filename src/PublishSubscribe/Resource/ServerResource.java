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
			
			String topicname = new String(exchange.getRequestPayload());
			String topicHashMD5 = hash.hashMd5Table.useHashMD5(topicname);
			
			//創造主題
			
			ResultSet result = st.executeQuery("SELECT COUNT(TopicName) AS total from TopicTable where TopicName=" + "\'"+topicname+"\';");
			result.next();
			if(result.getInt("total")>0)
			{
				exchange.respond("Repeating Creation");
				System.out.println("Repeating Created The Topic : "+ topicname);
			}	
			else
			{
				
				st.executeUpdate("INSERT INTO TopicTable VALUES (\'"+topicname+"\',\'"+topicHashMD5+"\');");
				exchange.respond("Created this Topic");
				
				//創造主題
				add(new SubscribedTopic(topicname));
				exchange.respond("Thx Creating!");
				
				System.out.println("OK Created The Topic : "+ topicname);
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
