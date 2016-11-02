package hash;
import java.security.MessageDigest;
import java.util.Scanner;
import javax.xml.bind.DatatypeConverter;


import java.security.MessageDigest;
import java.security.Security;

/**
* Java：計算 MD5, SHA, WHIRLPOOL等雜湊碼 hash code的程式教學
*
* @author werdna at http://werdna1222coldcodes.blogspot.com/
*/

public class hashMD5 {

    // 本文應用的套件為：gnu-crypto-2.0.1
    // GNU Crypto - GNU Project - Free Software Foundation (FSF)
    // http://www.gnu.org/software/gnu-crypto/
    // 支授的演算法可見：http://www.gnu.org/software/gnu-crypto/algorithms.html
    public static void main(String[] args) {

        // 程式執行前須先將 Provider 加入
        Security.addProvider(new gnu.crypto.jce.GnuCrypto());

        // 初始化測試資料
        String stringToDigest = "這是要被hash(或稱雜湊、摘要)的字串";
        byte[] bytesToDigest = stringToDigest.getBytes();

        // Hash 範例：MD5
        // 要用 SHA, WHIRLPOOL 的話只要把 "MD5" 改為 "SHA"、"WHIRLPOOL"
        try {
            // 設定 Hash 演算法
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytesToDigest);
            byte[] digest = messageDigest.digest();

            // 輸出 MD5 Hash：18:50:1F:84:0C:FE:2A:10:CE:89:B0:94:8E:FA:A0:42
            System.out.println(bytesToHex(digest));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // byte array 轉 hex 字串
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
            if (n < b.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString().toUpperCase();
    }
}