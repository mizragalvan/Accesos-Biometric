package mx.pagos.admc.util.back;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import mx.pagos.admc.exceptions.back.DESEncryptionException;
import mx.pagos.admc.exceptions.back.EncryptionException;

@Service("SecurityService")
public final class SecurityService {

    private static final int SALT_LENGTH = 8;
    private static final int PASS_LENGTH = 10;
    private static final String ALLOWED_SALT_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final String ALLOWED_PASS_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public SecurityService() { }

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


    public static void main(String[] args) throws DESEncryptionException {
        final String username = "pbeller";
        final String password = "bad_password";
        final DESEncryption myEncryptor = new DESEncryption();
        final String salt = "jdfifnusdpo";//SecurityService.generateSalt();
        final String pass = SecurityService.encrypt(password, Base64Coder.encodeString(salt));
        final String encryptedUsername = myEncryptor.encrypt(username);
        System.out.println("Username: " + encryptedUsername);
        // System.out.println("Password: " + pass);
        // System.out.println("Salt: " + salt);
  

        //    	String salt = "WJLqGmr5";

        //    	String usuario="APONCE";
        //    	String password="aponce";
        //    	
        //    	
        //    	String encryptedUser="";
        //    	String encryptedPass="";
        //    	
        //    	String salt= "VvdIL51f";
        ////    	String salt= SecurityService.generateSalt();
        //    	
        //    	String encodedsalt=Base64Coder.encodeString(salt);
        //    	
        //    	String pass=SecurityService.encrypt(password,encodedsalt);
        //    	String pass=SecurityService.encrypt(password,encodedsalt);

        //    	DESEncryption encryptor = null;
        //    	
        //    	try {
        //			 encryptor = new DESEncryption();
        //    	} catch (Exception e) {
        //			// TODO Auto-generated catch block
        //			e.printStackTrace();
        //			return;
        //		}


    }

}