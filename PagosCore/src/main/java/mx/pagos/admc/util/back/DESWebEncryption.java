package mx.pagos.admc.util.back;

import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESWebEncryption {
 
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] keyAsBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;
 
    public DESWebEncryption(String EncryptionKey) throws Exception
    {
        myEncryptionKey = EncryptionKey+"SOLSER20062014@#SDOADASHBOARD";
        myEncryptionScheme = DES_ENCRYPTION_SCHEME;
        keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
        myKeySpec = new DESKeySpec(keyAsBytes);
        mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
        cipher = Cipher.getInstance(myEncryptionScheme);
        key = mySecretKeyFactory.generateSecret(myKeySpec);
    }
 
    /**
     * Method To Encrypt The String
     */
    public String encrypt(String unencryptedString) {
    	char[] encryptedChar = null;
        
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
             encryptedChar  = Base64Coder.encode(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(encryptedChar);
    }
    /**
     * Method To Decrypt An Ecrypted String
     */
    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64Coder.decodeLines(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= bytes2String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
    /**
     * Returns String From An Array Of Bytes
     */
    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
       
		for (int i = 0; i< bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }
 
    /**
     * Testing the DES Encryption And Decryption Technique
     */
    public static void main(String args []) throws Exception
    {
        DESWebEncryption myEncryptor= new DESWebEncryption("");
 
        String stringToEncrypt="Sanjaal.com";
        String encrypted=myEncryptor.encrypt(stringToEncrypt);
        String decrypted=myEncryptor.decrypt(encrypted);
 
    }  
 
}