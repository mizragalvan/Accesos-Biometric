package mx.engineer.utils.secutiry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordManager {
    private Boolean eightToSixteenCharacters = false;
    private Boolean upperLowerCombination = false;
    private String password;
    
    public final void isPasswordValid(final String passwordParameter) throws InvalidPasswordException {
        this.password = passwordParameter;
        this.validateEightToSixteenCharacters();
        this.validateUpperLowerCombination();
    }
    
    private void validateEightToSixteenCharacters() throws InvalidPasswordException {
        if (this.eightToSixteenCharacters)
            this.validateMatch("[a-zA-Z0-9]{8,16}");
    }
    
    private void validateUpperLowerCombination() throws InvalidPasswordException {
        if (this.upperLowerCombination)
            this.validateMatch("((?=.*[a-z])(?=.*[A-Z]).+){2}");
    }
    
    private void validateMatch(final String regularExpression) throws InvalidPasswordException {
        final Pattern pattern = Pattern.compile(regularExpression);
        final Matcher matcher = pattern.matcher(this.password);
        if (!matcher.matches())
            throw new InvalidPasswordException("La contraseña no cumple las reglas de validez");
    }

    public final void setEightToSixteenCharacters(final Boolean eightToSixteenCharactersParameter) {
        this.eightToSixteenCharacters = eightToSixteenCharactersParameter;
    }

    public final void setUpperLowerCombination(final Boolean upperLowerCombinationParameter) {
        this.upperLowerCombination = upperLowerCombinationParameter;
    }
}
