/**
 * 
 */
package mx.engineer.utils.secutiry;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import mx.engineer.utils.general.Base64Coder;
import mx.engineer.utils.general.exceptions.EncryptionException;

public final class SecurityEncrypt  {

    private static final int SALT_LENGTH = 8;
    private static final int PASS_LENGTH = 10;
    private static final String ALLOWED_SALT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final String ALLOWED_PASS_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /* (non-Javadoc)
	 * @see com.zermat.webzer.service.impl.PasswordService#generateSalt()
	 */
    public static final String generateSalt() {
        final SecureRandom random = new SecureRandom();
        final StringBuilder salt = new StringBuilder();
        for (int i = 0; i < SALT_LENGTH; i++) {
            salt.append(ALLOWED_SALT_CHARS.charAt(random.nextInt(ALLOWED_SALT_CHARS.length())));
        }
        return salt.toString();
    }
    /* (non-Javadoc)
     * @see com.zermat.webzer.service.impl.PasswordService#generatePass()
     */
    public static final String generatePass() {
    	final SecureRandom random = new SecureRandom();
    	final StringBuilder salt = new StringBuilder();
    	for (int i = 0; i < PASS_LENGTH; i++) {
    		salt.append(ALLOWED_PASS_CHARS.charAt(random.nextInt(ALLOWED_PASS_CHARS.length())));
    	}
    	return salt.toString();
    }

    /* (non-Javadoc)
	 * @see com.zermat.webzer.service.impl.PasswordService#encrypt(java.lang.String, java.lang.String)
	 */
    public static final String encrypt(final String plaintext, final String salt) {
        if (plaintext == null) {
            throw new NullPointerException();
        }
        if (salt == null) {
            throw new NullPointerException();
        }

        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            md.update((plaintext + salt).getBytes("UTF-8"));
            return    Base64Coder.encodeLines(md.digest()).trim();
        }
        catch (NoSuchAlgorithmException e) {
            throw new EncryptionException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new EncryptionException(e);
        }
    }

    
    public static void main(String[] args) {
    
    	
    	String salt = SecurityEncrypt.generateSalt();
//    	String salt = "E8RPsZMS";
    	String pass=SecurityEncrypt.encrypt("xxxxx",Base64Coder.encodeString(salt));
    	
    	// System.out.println(salt);
    	// System.out.println(pass);

    
    	//Y3Bze+tGRThx8gcs3aANNWjoKvA=

    
	}
    
}