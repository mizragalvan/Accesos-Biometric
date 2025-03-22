package mx.pagos.admc.util.back;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import mx.pagos.admc.exceptions.back.DESEncryptionException;

public class DESEncryption {
	public static final String DES_ENCRYPTION_SCHEME = "DESede";
    private static final String UNICODE_FORMAT = "UTF8";
    private Cipher cipher;
    private String myEncryptionKey;
    private SecretKey key;
 
    public DESEncryption() throws DESEncryptionException {
        try {
	        this.myEncryptionKey = "SDOADASHBOARDSOLSER20062014";
	        this.cipher = Cipher.getInstance(DES_ENCRYPTION_SCHEME);
	        this.key = SecretKeyFactory.getInstance(DES_ENCRYPTION_SCHEME).generateSecret(new DESedeKeySpec(this.
	        		myEncryptionKey.getBytes(UNICODE_FORMAT)));
        } catch (GeneralSecurityException | UnsupportedEncodingException encryptionException) {
            throw new DESEncryptionException("Error generation instance of DESEncryption class", encryptionException);
        }
    }
 
    public final String encrypt(final String unencryptedString) throws DESEncryptionException {
    	char[] encryptedChar = null;
        try {
        	this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
        	final byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
        	final byte[] encryptedText = this.cipher.doFinal(plainText);
        	encryptedChar = Base64Coder.encode(encryptedText);
        } catch (InvalidKeyException | IllegalBlockSizeException | UnsupportedEncodingException
        		| BadPaddingException e) {
        	throw new DESEncryptionException(e);
        }
        return new String(encryptedChar);
    }

    public final String decrypt(final String encryptedString) throws DESEncryptionException {
        String decryptedText = null;
        try {
        	this.cipher.init(Cipher.DECRYPT_MODE, this.key);
            final byte[] encryptedText = Base64Coder.decodeLines(encryptedString);
            final byte[] plainText = this.cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
        	throw new DESEncryptionException(e);
        }
        return decryptedText;
    }
    
    private static String bytes2String(final byte[] bytes) {
        final StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
            stringBuffer.append((char) bytes[i]);
        return stringBuffer.toString();
    }
 
    /**
     * Testing the DES Encryption And Decryption Technique
     */
    public static void main(String args []) throws Exception {
        DESEncryption myEncryptor= new DESEncryption(); 
        String stringToEncrypt="userNotExist";
        String encrypted=myEncryptor.encrypt(stringToEncrypt);
        String decrypted=myEncryptor.decrypt(encrypted); 
    }
}
