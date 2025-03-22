package mx.solsersistem.utils.test.general;

import mx.solsersistem.utils.general.NumberToEnglishLetters;

import org.junit.Assert;
import org.junit.Test;

public class NumberToEnglishLettersTest {
    
    @Test
    public final void when0ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 0 convertido incorrectamente a letra", "zero", NumberToEnglishLetters.convert(0));
    }
    
    @Test
    public final void when1ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 1 convertido incorrectamente a letra", "one", NumberToEnglishLetters.convert(1));
    }
    
    @Test
    public final void when16ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 16 convertido incorrectamente a letra",
                "sixteen", NumberToEnglishLetters.convert(16));
    }
    
    @Test
    public final void when100ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 100 convertido incorrectamente a letra",
                "one hundred", NumberToEnglishLetters.convert(100));
    }
    
    @Test
    public final void when118ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 118 convertido incorrectamente a letra",
                "one hundred eighteen", NumberToEnglishLetters.convert(118));
    }
    
    @Test
    public final void when200ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 200 convertido incorrectamente a letra",
                "two hundred", NumberToEnglishLetters.convert(200));
    }
    
    @Test
    public final void when219ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 219 convertido incorrectamente a letra",
                "two hundred nineteen", NumberToEnglishLetters.convert(219));
    }
    
    @Test
    public final void when800ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 800 convertido incorrectamente a letra",
                "eight hundred", NumberToEnglishLetters.convert(800));
    }
    
    @Test
    public final void when801ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 801 convertido incorrectamente a letra",
                "eight hundred one", NumberToEnglishLetters.convert(801));
    }
    
    @Test
    public final void when1316ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 1316 convertido incorrectamente a letra",
                "one thousand three hundred sixteen", NumberToEnglishLetters.convert(1316));
    }
    
    @Test
    public final void when1000000ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 1000000 convertido incorrectamente a letra",
                "one million", NumberToEnglishLetters.convert(1000000));
    }
    
    @Test
    public final void when2000000ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 2000000 convertido incorrectamente a letra",
                "two million", NumberToEnglishLetters.convert(2000000));
    }
    
    @Test
    public final void when3000200ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 3000200 convertido incorrectamente a letra",
                "three million two hundred", NumberToEnglishLetters.convert(3000200));
    }
    
    @Test
    public final void when700000ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 700000 convertido incorrectamente a letra",
                "seven hundred thousand", NumberToEnglishLetters.convert(700000));
    }
    
    @Test
    public final void when9000000ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 9000000 convertido incorrectamente a letra",
                "nine million", NumberToEnglishLetters.convert(9000000));
    }
    
    @Test
    public final void when123456789ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 123456789 convertido incorrectamente a letra",
                "one hundred twenty three million four hundred fifty six thousand seven hundred eighty nine",
                NumberToEnglishLetters.convert(123456789));
    }
    
    @Test
    public final void when2147483647ThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 2147483647 convertido incorrectamente a letra",
                "two billion one hundred forty seven million four hundred eighty three thousand six hundred "
                + "forty seven",
                NumberToEnglishLetters.convert(2147483647));
    }
    
    @Test
    public final void when3000000010LThenCorrectEnglishNumber() {
        Assert.assertEquals("Número 3000000010L convertido incorrectamente a letra",
                "three billion ten", NumberToEnglishLetters.convert(3000000010L));
    }
}
