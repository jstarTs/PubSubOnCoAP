package PublishSubscribe.Server;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.core.network.config.NetworkConfig;

import PublishSubscribe.Resource.ServerResource;

public class testPubSubServer extends CoapServer
{
	private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);
	
	 public testPubSubServer() throws SocketException 
	 {
	        
	        // provide an instance of a Hello-World resource
		 add(new ServerResource());
	 }
	
	public static void main(String[] args) {
        
        try {

            // create server
        	testPubSubServer server = new testPubSubServer();
            // add endpoints on all IP addresses
            server.addEndpoints();
            server.start();

        } catch (SocketException e) {
            System.err.println("Failed to initialize server: " + e.getMessage());
        }
        //updateToFig();
    }
	
	/**
     * Add individual endpoints listening on default CoAP port on all IPv4 addresses of all network interfaces.
     */
    private void addEndpoints() {
    	for (InetAddress addr : EndpointManager.getEndpointManager().getNetworkInterfaces()) {
    		// only binds to IPv4 addresses and localhost
			if (addr instanceof Inet4Address || addr.isLoopbackAddress()) {
				InetSocketAddress bindToAddress = new InetSocketAddress(addr, COAP_PORT);
				addEndpoint(new CoapEndpoint(bindToAddress));
			}
		}
    }
    
    
    /*
     *   預其update 至  Fog Node ， Subscriber data 、   subscribed  data of topic , 加密key 和  md5 tag
     *   
     */
    private static void updateToFig()
    {
    	
    	try 
    	{
			Connection conn = PublishSubscribe.MySQL.getConnection();
    		
    		Statement st = conn.createStatement();
			
			String subscriberData = "",Query="";
			
			String SQL = "SELECT Topic, URI FROM SubscriberData";
			
			ResultSet rs = st.executeQuery(SQL);
			
			while(rs.next())
			{
				if(rs.isLast())
				{
					subscriberData += hash.hashMd5Table.useHashMD5(rs.getString("Topic")) +","+ rs.getString("URI");
					//subscriberData += rs.getString("Topic")+","+ rs.getString("URI");
				}
				else
				{
					subscriberData += hash.hashMd5Table.useHashMD5(rs.getString("Topic")) +","+ rs.getString("URI")+",";
					//subscriberData += rs.getString("Topic")+","+ rs.getString("URI")+",";
				}
			}
			
			conn.close();
			
			System.out.println(subscriberData);
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
    
    
    
	
}
