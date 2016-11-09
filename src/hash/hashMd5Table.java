package hash;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class hashMd5Table 
{
	public hashMd5Table() 
	{
		Security.addProvider(new gnu.crypto.jce.GnuCrypto());
	}
	
	
	public Map<String, String> settableFromFile(String sourceFilepath , String EleAttrMD5Filepath)
	{
		Scanner scSource , scEleAttrMD5 ;
		List<String> sourceList = new ArrayList<String>() , EleAttrMD5List = new ArrayList<String>(); 
		Map<String, String> scMapMD5 = new HashMap<String, String>();
		
		try 
		{
			scSource = new Scanner(new File(sourceFilepath));
			scEleAttrMD5 = new Scanner(new File(EleAttrMD5Filepath));
			
			while(scSource.hasNextLine())
			{
				sourceList.add(scSource.nextLine().trim());
			}
			while(scEleAttrMD5.hasNextLine())
			{
				EleAttrMD5List.add(scEleAttrMD5.nextLine().trim());
			}
			
			for(int i=0 ; i<sourceList.size() ; i++)
			{
				scMapMD5.put(sourceList.get(i), EleAttrMD5List.get(i));
			}
			
			scSource.close();
			scEleAttrMD5.close();
			return scMapMD5;
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
		
		return null;
	}
	
	public List<String> scanElementAttribute(String EleAttrFilepath)
	{
		List<String> EleAttrList = new ArrayList<String>();
		try 
		{
			Scanner sc = new Scanner(new File(EleAttrFilepath));
			while(sc.hasNextLine())
			{
				EleAttrList.add(sc.nextLine().trim());
			}
			return EleAttrList;
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
		
		return null;
	}
	
	public void writeElementAttributes(List<String> list)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new File("EleAttrMD5Hash.txt"));
			
			for(String s : list)
			{
				pw.println(s);
			}
			pw.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
	}
	
	public static String useHashMD5(String input)
	{
		byte[] bytesToDigest = input.getBytes();
		
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytesToDigest);
            byte[] digest = messageDigest.digest();
            String result = bytesToHex(digest);
            
            return result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
		
		return "Error";
	}
	
	public String useHashMd5(String input)
	{
		byte[] bytesToDigest = input.getBytes();
		
		try
		{
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytesToDigest);
            byte[] digest = messageDigest.digest();
            String result = bytesToHex(digest);
            
            return result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
		
		return "Error";
	}
	
	public List<String> strToHashMD5(List<String> sourcelist)
	{
		List<String> md5List = new ArrayList<String>();
		
		for(int i=0 ; i<sourcelist.size() ; i++)
		{
			md5List.add("a"+useHashMd5(sourcelist.get(i)));
		}
		
		return md5List;
	}
	
	public static String bytesToHex(byte[] b) {

        StringBuffer sb = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                sb.append("0").append(stmp);
            } else {
                sb.append(stmp);
            }
            /*
            if (n < b.length - 1) {
                sb.append(":");
            }
            */
        }
        return sb.toString().toUpperCase();
    }
	
	public static void main(String[] args) 
	{
		hashMd5Table test = new hashMd5Table();
		
		test.writeElementAttributes(test.strToHashMD5(test.scanElementAttribute("xmlelement.txt")));
		
		
        /*
		// 程式執行前須先將 Provider 加入
        Security.addProvider(new gnu.crypto.jce.GnuCrypto());

        // 初始化測試資料
        String stringToDigest = "這是要被hash(或稱雜湊、摘要)的字串";
        String sc = "a";
        System.out.println(sc);
        byte[] bytesToDigest = sc.getBytes();

        System.out.println(bytesToDigest.length);
        
        // Hash 範例：MD5
        // 要用 SHA, WHIRLPOOL 的話只要把 "MD5" 改為 "SHA"、"WHIRLPOOL"
        try {
            // 設定 Hash 演算法
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytesToDigest);
            byte[] digest = messageDigest.digest();

            // 輸出 MD5 Hash：18:50:1F:84:0C:FE:2A:10:CE:89:B0:94:8E:FA:A0:42
            System.out.println(bytesToHex(digest));
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        */
        /*
        // Hash 範例：WHIRLPOOL
        // 更多 Hash 演算法可見：http://www.gnu.org/software/gnu-crypto/algorithms.html
        try {
            // 設定 Hash 演算法
            MessageDigest messageDigest = MessageDigest.getInstance("WHIRLPOOL");
            messageDigest.update(bytesToDigest);
            byte[] digest = messageDigest.digest();

            // 輸出 WHIRLPOOL Hash：
            // 2D:2E:C0:FE:70:71:23:51:7C:FD:E0:1C:9D:D0:CC:00:72:D7:42:38:1D:BD:
            // 10:7E:26:AC:DA:7B:D5:5A:03:7C:A9:17:B2:9C:29:EE:E7:88:7E:B4:DA:90:
            // DF:64:9B:F2:13:D6:FF:75:88:8C:05:E9:46:C5:94:B5:BB:BA:36:CE
            System.out.println(bytesToHex(digest));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    // byte array 轉 hex 字串
    
}
