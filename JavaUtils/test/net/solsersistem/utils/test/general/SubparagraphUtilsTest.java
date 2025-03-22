package mx.solsersistem.utils.test.general;

import org.junit.Assert;
import org.junit.Test;

import mx.solsersistem.utils.general.SubparagraphUtils;
import mx.solsersistem.utils.general.exceptions.SubparagraphException;

public class SubparagraphUtilsTest {
    private static final String CONVERT_NUMBER_TO_SUBPARAGRAPH_MESSAGE_ERROR =
            "Error al convertir el número a inciso consecutivo";

    @Test
    public final void whenConvertIntegerValueThenReturnSubparagraph() throws SubparagraphException {
        Assert.assertEquals("Error al convertir el número a inciso", "a) ",
                SubparagraphUtils.convertNumberToSubparagraph(1));
    }

    @Test
    public final void whenConvertIntegerValueIndexOfOutTheAlphabetThenReturnSubparagraph() 
            throws SubparagraphException {
        final Integer numberToConvert = 28;
        Assert.assertEquals(CONVERT_NUMBER_TO_SUBPARAGRAPH_MESSAGE_ERROR, "ab) ", 
                SubparagraphUtils.convertNumberToSubparagraph(numberToConvert));
    }

    @Test
    public final void whenConvertIntegerValue53TheAlphabetThenReturnSubparagraphba() 
            throws SubparagraphException {
        final Integer numberToConvert = 53;
        Assert.assertEquals(CONVERT_NUMBER_TO_SUBPARAGRAPH_MESSAGE_ERROR, "ba) ", 
                SubparagraphUtils.convertNumberToSubparagraph(numberToConvert));
    }
    
    @Test
    public final void whenConvertSubparagraphThenReturnIntegerIndex() throws SubparagraphException {
        final String subparagraphToConvert = "d) ";
        Assert.assertEquals(CONVERT_NUMBER_TO_SUBPARAGRAPH_MESSAGE_ERROR, 4, 
                SubparagraphUtils.convertSubparagraphToInteger(subparagraphToConvert));
    }

    @Test
    public final void whenConvertSubparagraphBAThenReturnIntegerIndex() throws SubparagraphException {
        final String subparagraphToConvert = "ba) ";
        Assert.assertEquals(CONVERT_NUMBER_TO_SUBPARAGRAPH_MESSAGE_ERROR, 53, 
                SubparagraphUtils.convertSubparagraphToInteger(subparagraphToConvert));
    }

    @Test
    public final void whenConvertSubparagraphAThenReturnIntegerIndex() throws SubparagraphException {
        final String subparagraphToConvert = "a) ";
        Assert.assertEquals(CONVERT_NUMBER_TO_SUBPARAGRAPH_MESSAGE_ERROR, 1, 
                SubparagraphUtils.convertSubparagraphToInteger(subparagraphToConvert));
    }
    
    @Test(expected = SubparagraphException.class)
    public final void whenConvertSubparagraphHaveAProblemThenSubparagraphException() throws SubparagraphException {
        SubparagraphUtils.convertSubparagraphToInteger("sdf) ");
    }

    @Test(expected = SubparagraphException.class)
    public final void whenConvertSubparagraphIntegerValueHaveAProblemThenSubparagraphException() 
            throws SubparagraphException {
        SubparagraphUtils.convertSubparagraphToInteger("d5) ");
    }

    @Test(expected = SubparagraphException.class)
    public final void whenConvertSubparagraphWithSintaxisErrorHaveAProblemThenSubparagraphException() 
            throws SubparagraphException {
        SubparagraphUtils.convertSubparagraphToInteger("d5");
    }

    @Test(expected = SubparagraphException.class)
    public final void whenConvertIntegerValueHaveAProblemThenSubparagraphException() throws SubparagraphException {
        SubparagraphUtils.convertNumberToSubparagraph(0);
    }
    
    @Test(expected = SubparagraphException.class)
    public final void whenConvertNegativeIntegerValueHaveAProblemThenSubparagraphException() 
            throws SubparagraphException {
        SubparagraphUtils.convertNumberToSubparagraph(-1);
    }
    
    @Test(expected = SubparagraphException.class)
    public final void whenConvertNullValueThenSubparagraphException() throws SubparagraphException {
        SubparagraphUtils.convertNumberToSubparagraph(null);
    }
}
