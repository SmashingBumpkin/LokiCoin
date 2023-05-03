import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class CryptographyReencoding {
    public static String bytesAsString(byte[] bytes){
        StringBuffer hashStringBuffer = new StringBuffer();
        for (byte byt : bytes) {
            String bytesAsHex = Integer.toHexString(128 + byt);
            if (bytesAsHex.length() == 1) {
                bytesAsHex = "0" + bytesAsHex;
            }

            hashStringBuffer.append(bytesAsHex);
        }
        return hashStringBuffer.toString();
    }
    
    public static byte[] stringAsBytes(String hashString){
        byte[] bytes = new byte[(hashString.length()+1 / 2)];
        // System.out.print(bytes.length);
        for (int i = 0; i < hashString.length(); i += 2) {
            String hexByte = hashString.substring(i, i + 2);
            bytes[i / 2] = (byte) (Integer.parseInt(hexByte, 16) - 128);
        }
    
        return bytes;
    }

    public static String pubKeyAsString(PublicKey pubKey){
        byte[] pubKeyAsBytes = pubKey.getEncoded();
        String output = CryptographyReencoding.bytesAsString(pubKeyAsBytes);
        // output.substring(52, 315);
        return output; 
    }
    public static String pubKeyAsString2(PublicKey pubKey){
        byte[] pubKeyAsBytes = pubKey.getEncoded();
        String output = CryptographyReencoding.bytesAsString(pubKeyAsBytes);
        output = output.substring(58, 315);
        return output; 
    }

    public static PublicKey stringAsPubKey(String str){
        byte[] keyBytes = CryptographyReencoding.stringAsBytes(str);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory;
        PublicKey pubKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKey;
    }
}
