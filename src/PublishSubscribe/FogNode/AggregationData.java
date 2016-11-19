package PublishSubscribe.FogNode;

public class AggregationData 
{
	
	 public void aggregationXML(String dataType ,String time ,String record)
	 {
		 
		 String document ="<a37B5EE95457D600206446C2F0BC3E055><"+dataType+"><aA76D4EF5F3F6A672BBFAB2865563E530><EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" /><CipherData><CipherValue>"+time+"</CipherValue></CipherData></EncryptedData></aA76D4EF5F3F6A672BBFAB2865563E530><a6A0D9EAEE314C567FD72FB97EE707A36><EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" /><CipherData><CipherValue>"+record+"</CipherValue></CipherData></EncryptedData></a6A0D9EAEE314C567FD72FB97EE707A36></dataType></a37B5EE95457D600206446C2F0BC3E055>";
		 //
		 System.out.println(document);
		 
	 }
	
	 
	 public static void main(String[] args)
	 {
		 String dataType="a85EF20040AB4A17A1908D5575C2A1F0D" , time="hnClcI14k/DCCLPkEfwUnBOAWi/7Pghiutb0og0mDothce2cR+m/kwOjCj+ZeGDT" , record="10663.0";
		 
		 AggregationData test = new AggregationData();
		 
		 test.aggregationXML(dataType, time, record);
		 
		 //System.out.println("<aA76D4EF5F3F6A672BBFAB2865563E530><EncryptedData Type=\"http://www.w3.org/2001/04/xmlenc#Content\" xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#aes256-cbc\" /><CipherData><CipherValue>"+"123"+"</CipherValue></CipherData></EncryptedData></aA76D4EF5F3F6A672BBFAB2865563E530>");
	 }
}
