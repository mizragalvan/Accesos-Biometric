package mx.pagos.admc.util.back;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import mx.pagos.admc.exceptions.back.DESEncryptionException;

public class DESEncryption_OLD {
    public static final String DES_ENCRYPTION_SCHEME = "DES";
    private static final String UNICODE_FORMAT = "UTF8";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    private byte[] keyAsBytes;
    private SecretKey key;
 
    public DESEncryption_OLD() throws DESEncryptionException {
        try {
            this.myEncryptionKey = "SDOADASHBOARDSOLSER20062014";
            this.myEncryptionScheme = DES_ENCRYPTION_SCHEME;
            this.keyAsBytes = this.myEncryptionKey.getBytes(UNICODE_FORMAT);
            this.myKeySpec = new DESKeySpec(this.keyAsBytes);
            this.mySecretKeyFactory = SecretKeyFactory.getInstance(this.myEncryptionScheme);
            this.cipher = Cipher.getInstance(this.myEncryptionScheme);
            this.key = this.mySecretKeyFactory.generateSecret(this.myKeySpec);
        } catch (GeneralSecurityException | UnsupportedEncodingException encryptionException) {
            throw new DESEncryptionException("Error generation instance of DESEncryption class", encryptionException);
        }
    }
 
    public final String encrypt(final String unencryptedString) {
        char[] encryptedChar = null;
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
            final byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            final byte[] encryptedText = this.cipher.doFinal(plainText);
             encryptedChar  = Base64Coder.encode(encryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(encryptedChar);
    }

    public final String decrypt(final String encryptedString) {
        String decryptedText = null;
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.key);
            final byte[] encryptedText = Base64Coder.decodeLines(encryptedString);
            final byte[] plainText = this.cipher.doFinal(encryptedText);
            decryptedText = bytes2String(plainText);
        } catch (Exception e) {
            //e.printStackTrace();
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
        DESEncryption_OLD myEncryptor= new DESEncryption_OLD();
        
//      String stringToEncrypt="admin";
//      String encrypted=myEncryptor.encrypt(stringToEncrypt);
//      String decrypted=myEncryptor.decrypt(encrypted);
//

      
      final String[][] users = {
              {"1","RnBrWbBNOAU=", "QtHNWwdwNk7Zx7BZlarLdDNzHN0="},
              {"2","il3pSm9C85s=", "v1CYuutCrsTAoX/GzFFxbfIL9l4="},
              {"3915","Vlgum/LJOer9YtRs7g5rBQ==", "miETIrO8PI8u+U30KeYlxGkRAX0="},
              {"7020","kk5TCmZ13tM=", "NpEyIgiXqused3vBRU+Sk8vmgRQ="},
              {"7021","mYsYdDP5dZA=", "NpEyIgiXqused3vBRU+Sk8vmgRQ="},
              {"21207","lgqJsMhM+IH9YtRs7g5rBQ==", "sed8gy5VMAjkMhAQNhxmSf5eqYQ="},
              {"21208","uybBBnNgDef9YtRs7g5rBQ==", "/lQiWr71OuLhs3M+trwOlflEyFc="},
              {"21209","F2oQ0l9pVjM=", "yD6ieP/iulQInVap0MvK6Lb5i/U="},
              {"21494","SFfwOXdf6H0=", "6IKZ9U8NCE9IPLsjBSmS75/qj+U="},
              {"22571","tBOoUTSsFxI=", "P7XXEe+1TTCQz+/0bwyMLr20CPg="},
              {"22572","HNfB/0Dn5k4=", "st/Yc10gbzSeFoDVZUtHw9BgVuM="},
              {"23017","Px2J47A5Km8=", "T0qmD6UELktnIcaOYeRYBHLCr64="},
              {"29527","khCI+kUvljM=", "yXvNJfaWJWkimFkMACbNqRNouLw="},
//              {"31526","jtPh5koO/xj9YtRs7g5rBQ==", "NULL"},
              {"31687","zNBeE/7kaf/9YtRs7g5rBQ==", "2q05A6QoSftL+EsotHs0EJSWvLY="},
              {"37444","FV8ApEbuCg0=", "DWlnqPjScROe2iHaq06a3lugP+8="},
              {"40134","9OIsSmaV5rc=", "0ywU9f8pYjmfOfU+MhBYYuMWHCU="},
              {"41833","BqEshDtFMTX9YtRs7g5rBQ==", "ohp29kcy92hFDssu7RkY07oTM6U="},
              {"49590","BbPkb06rdXo=", "Ho02e1H0suxRkVzMh/Ci1em9/pM="},
              {"54599","eYcElr6aaxHePllrecnSRw==", "d4JxjHZ8nuRcoNPeRp+MxWIvRPI="},
//              {"56054","2/8TAU/9An0=", "NULL"},
//              {"56056","GCGPqCqDIRj9YtRs7g5rBQ==", "NULL"},
              {"64802","ISvceaJyTKg=", "ppHgFbsp1sX5BgfMW2iA/lOEHsM="},
              {"75217","H+geUJZ4kbj9YtRs7g5rBQ==", "mqKI2inAEAkoIKMxkPzQwOMytXw="},
              {"75218","YvW/kjpMAX39YtRs7g5rBQ==", "/3AvabHLITaNdV60auvWty2LT1s="},
              {"75219","H3itxpTfsT79YtRs7g5rBQ==", "tFJDhhBHLBMWGyj5E9sTTECWU44="},
              {"76510","AVJ9uG8nfZv9YtRs7g5rBQ==", "rgdh0Y6PMb8DrPzoohnsgx7PNWw="},
              {"83016","a0GumfPeYoo=", "ygAvzWCIeO/yqBpOK8YI0q1exEs="},
              {"83017","OMfn9SmHb2k=", "tWCT3wYWDA2958m+xnSKklJUPU4="},
//              {"90416","mYPaaeSyGvn9YtRs7g5rBQ==", "NULL"},
              {"101877","NULL", "ajsbq0iK46jguX3bbqJqTxX3iew="},
              {"109601","r6LoDQWJLVv9YtRs7g5rBQ==", "zHqkK0U0sEek3/egQC+Kx8rjdfI="},
              {"109603","4SvJQgML/S8=", "UfjczOk0jOIyrzkJNoMe1piCXIk="},
//              {"121219","kmondragon", "NULL"},
//              {"121220","HCASTRO", "NULL"},
//              {"121221","rcalbarran", "NULL"},
//              {"121222","tesperilla", "NULL"},
//              {"121223","MGMEDINA", "NULL"},
//              {"121224","jggmarquez", "NULL"},
//              {"121225","locken", "NULL"},
//              {"121226","csantibanez", "NULL"},
//              {"121227","edsosa", "NULL"},
//              {"121228","etarroyo", "NULL"},
//              {"121229","EMEDINAH", "NULL"},
//              {"121230","avaldesl", "NULL"},
//              {"121231","jacevedo", "NULL"},
//              {"121232","ualcalag", "NULL"},
              {"154852","tlkLL5GiPewMjKD6aZbvkw==", "noUetB888rG4/ozQLZtgfTjXpEY="}
      };
      
      for (int i = 0; i < users.length; i++) {
          System.out.println("{\"" + users[i][0] + "\", \"" + myEncryptor.decrypt(users[i][1]) + "\"},");
          
      }
    }
}
