package mx.solsersistem.utils.test.string;

import mx.solsersistem.utils.string.StringUtils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {
    
    @Test
    public final void whenGetObjectStringValueThenStringValueReturned() {
        final String value = "Object";
        Assert.assertEquals("Error al recuperar el valor", value, StringUtils.getObjectStringValue(value));
    }
    
    @Test
    public final void whenGetObjectStringNullValueThenNullValueReturned() {
        final String value = null;
        Assert.assertEquals("Error al recuperar el valor nulo", value, StringUtils.getObjectStringValue(value));
    }
    
    @Test
    public final void whenGetObjectStringValueNotNullThenStringValueReturned() {
        final String value = "Object_Value";
        Assert.assertEquals("Error al recuperar el valor no nulo",
                value, StringUtils.getObjectStringValueNotNull(value));
    }
    
    @Test
    public final void whenGetObjectStringValueNotNullIsNullThenEmptyStringReturned() {
        Assert.assertEquals("Error al recuperar un cadena vacía", "", StringUtils.getObjectStringValueNotNull(null));
    }
    
    @Test
    public final void whenGetStringValueThenStringValueReturned() {
        final String value = "String";
        Assert.assertEquals("Error al recuperar la cadena", value, StringUtils.getStringNotNull(value));
    }
    
    @Test
    public final void whenGetStringNullValueThenEmptyValueReturned() {
        Assert.assertEquals("Error al recuperar la cadena vacía", "", StringUtils.getStringNotNull(null));
    }
}
