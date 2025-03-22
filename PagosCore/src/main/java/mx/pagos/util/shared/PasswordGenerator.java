package mx.pagos.util.shared;

import java.util.Random;

public class PasswordGenerator {
    private static final int RANDOM_NEXTINT = 3;
    private static final Integer PASSWORD_LENGTH = 8;
    private static final Integer PASSWORD_LENGTH_MAXIMUM = 10;
    private static final String[] ALPHA = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
        "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final String[] BETA = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
        "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final String[] GAMMA = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
      
    public static String getPassword() {
        final Random random = new Random();
        final StringBuffer password = new StringBuffer();
        for (Integer i = 1; i <= PASSWORD_LENGTH; i++) {
            switch (random.nextInt(RANDOM_NEXTINT)) {
                case 0:
                    password.append(ALPHA[random.nextInt(ALPHA.length)]);
                break;
                case 1:
                    password.append(BETA[random.nextInt(BETA.length)]);
                break;
                case 2:
                    password.append(GAMMA[random.nextInt(GAMMA.length)]);
                break;
                default:
            }
        }
        if (password.length() > PASSWORD_LENGTH_MAXIMUM)
            password.delete(PASSWORD_LENGTH, password.length());
        return password.toString();
    }

    public final Integer getPwdlength() {
        return PASSWORD_LENGTH;
    }

    public final Integer getPwdlengthmax() {
        return PASSWORD_LENGTH_MAXIMUM;
    }
}
