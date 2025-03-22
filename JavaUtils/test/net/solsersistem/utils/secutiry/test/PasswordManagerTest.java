package mx.solsersistem.utils.secutiry.test;

import mx.solsersistem.utils.secutiry.InvalidPasswordException;
import mx.solsersistem.utils.secutiry.PasswordManager;

import org.junit.Test;

public class PasswordManagerTest {
    private final PasswordManager passwordManager = new PasswordManager();
    
    @Test
    public final void whenSetEightTosixteenCharactersRuleAndPasswordHasitThenPasswordIsValid()
            throws InvalidPasswordException {
        this.passwordManager.setEightToSixteenCharacters(true);
        this.passwordManager.isPasswordValid("rt542378");
    }
    
    @Test(expected = InvalidPasswordException.class)
    public final void whenSetEightTosixteenCharactersRuleAndPasswordHasSevenThenInvalidPasswordException()
            throws InvalidPasswordException {
        this.passwordManager.setEightToSixteenCharacters(true);
        this.passwordManager.isPasswordValid("rt54237");
    }
    
    public final void whenSetEightToSixteenCharactersRuleAndPasswordHasNineThenPasswordIsvalid()
            throws InvalidPasswordException {
        this.passwordManager.setEightToSixteenCharacters(true);
        this.passwordManager.isPasswordValid("Trt542377");
    }
    
    @Test(expected = InvalidPasswordException.class)
    public final void whenSetEightCharactersRuleAndPasswordHasSeventeenThenInvalidPasswordException()
            throws InvalidPasswordException {
        this.passwordManager.setEightToSixteenCharacters(true);
        this.passwordManager.isPasswordValid("Trt5423774r56t4DF");
    }
    
    @Test
    public final void whenSetUpperLowerCombinationAndPasswordHasitThenPasswordIsValid()
            throws InvalidPasswordException {
        this.passwordManager.setUpperLowerCombination(true);
        this.passwordManager.isPasswordValid("rT5423a8");
    }
    
    @Test(expected = InvalidPasswordException.class)
    public final void whenSetUpperLowerCombinationAndPasswordOnlyHasUpperThenInvalidPasswordException()
            throws InvalidPasswordException {
        this.passwordManager.setUpperLowerCombination(true);
        this.passwordManager.isPasswordValid("rT54KG18");
    }
    
    @Test
    public final void whenSetEightAndUpperLowerAndPasswordHasThenPasswordIsValid()
            throws InvalidPasswordException {
        this.passwordManager.setEightToSixteenCharacters(true);
        this.passwordManager.setUpperLowerCombination(true);
        this.passwordManager.isPasswordValid("a6ThyK80");
    }
    
    @Test(expected = InvalidPasswordException.class)
    public final void whenSetEightAndUpperLowerAndPasswordHasNotThenInvalidPasswordException()
            throws InvalidPasswordException {
        this.passwordManager.setEightToSixteenCharacters(true);
        this.passwordManager.setUpperLowerCombination(true);
        this.passwordManager.isPasswordValid("a6thyk80");
    }
    
    @Test(expected = InvalidPasswordException.class)
    public final void whenSetEightAndUpperLowerAndPasswordHasSevenThenInvalidPasswordException()
            throws InvalidPasswordException {
        this.passwordManager.setEightToSixteenCharacters(true);
        this.passwordManager.setUpperLowerCombination(true);
        this.passwordManager.isPasswordValid("a6thk80");
    }
}
