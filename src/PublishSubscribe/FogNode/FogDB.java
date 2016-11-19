package PublishSubscribe.FogNode;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FogDB 
{
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static final String DB_URL = "jdbc:mysql://140.120.15.136:3306/PubSub";
	
	private static final String USER = "poshin";
	private static final String PASS = "QQqq123654@@";
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		Class.forName(JDBC_DRIVER);
		Connection con = DriverManager.getConnection(DB_URL,USER,PASS);
		
		return con;
	}
}
