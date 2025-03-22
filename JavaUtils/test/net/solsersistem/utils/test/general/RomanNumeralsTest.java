package mx.solsersistem.utils.test.general;

import mx.solsersistem.utils.general.RomanNumeralUtils;
import mx.solsersistem.utils.general.exceptions.RomanNumeralCastException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RomanNumeralsTest {
    
    private ExpectedException expectedException;
    
    @Rule
    public final ExpectedException checkExceptionRule() {
        if (this.expectedException == null) {
            this.expectedException = ExpectedException.none();
        }
        return this.expectedException;
    }
    
    @Test
    public final void whenNumberZeroThenRomanNumeralCastException() throws RomanNumeralCastException {
        this.checkExceptionRule().expect(RomanNumeralCastException.class);
        this.checkExceptionRule().expectMessage("El Número Cero No Existe");
        RomanNumeralUtils.arabicToRoman(0);
    }
    
    @Test
    public final void whenGreaterThanThreeThousandThenRomanNumeralCastException() throws RomanNumeralCastException {
        this.checkExceptionRule().expect(RomanNumeralCastException.class);
        this.checkExceptionRule().expectMessage("Número no representable");
        RomanNumeralUtils.arabicToRoman(30001);
    }
    
    @Test
    public final void when1ArabicIsGivenThenRomanNumeral1RomanIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es I", "I", RomanNumeralUtils.arabicToRoman(1));
    }
    
    @Test
    public final void when4ArabicIsGivenThenRomanNumeral4RomanIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es IV", "IV", RomanNumeralUtils.arabicToRoman(4));
    }
    
    @Test
    public final void when9ArabicIsGivenThenRomanNumeral9RomanIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es IX", "IX", RomanNumeralUtils.arabicToRoman(9));
    }
    
    @Test
    public final void when30ArabicIsGivenThenRomanNumeral30RomanIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es XXX", "XXX", RomanNumeralUtils.arabicToRoman(30));
    }
    
    @Test
    public final void when49ArabicIsGivenThenRomanNumeral49RomanIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es XLIX", "XLIX", RomanNumeralUtils.arabicToRoman(49));
    }
    
    @Test
    public final void when299ArabicIsGivenThenRomanNumeral299RomanIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es CCXCIX", "CCXCIX", RomanNumeralUtils.arabicToRoman(299));
    }
    
    @Test
    public final void when980ArabicIsGivenThenRomanNumeral980IsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es CMLXXX", "CMLXXX", RomanNumeralUtils.arabicToRoman(980));
    }
    
    @Test
    public final void when1153ArabicIsGivenThenRomanNumeral1153RomanIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es MCLIII", "MCLIII", RomanNumeralUtils.arabicToRoman(1153));
    }
    
    @Test
    public final void when3000ArabicIsGivenThenRomanNumeralMMMReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número romano no es MMM", "MMM", RomanNumeralUtils.arabicToRoman(3000));
    }
    
    @Test
    public final void when49RomanIsGivenThen49ArabicIsReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número arábigo no es XLIX", 49, RomanNumeralUtils.romanToArabic("XLIX"));
    }
    
    @Test
    public final void when1153RomanIsGivenThen1153IsArabicReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número arábigo no es MCLIII", 1153, RomanNumeralUtils.romanToArabic("MCLIII"));
    }
    
    @Test
    public final void when299RomanIsGivenThen299IsArabicReturned() throws RomanNumeralCastException {
        Assert.assertEquals("El número arábigo no es CCXCIX", 299, RomanNumeralUtils.romanToArabic("CCXCIX"));
    }
}
