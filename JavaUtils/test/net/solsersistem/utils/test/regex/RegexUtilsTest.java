package mx.solsersistem.utils.test.regex;

import java.util.List;

import mx.solsersistem.utils.regex.RegexUtils;

import org.junit.Assert;
import org.junit.Test;

public class RegexUtilsTest {
    @Test
    public final void whenExtractTextThenTextIsExtracted() {
        final List<String> tagsList =
                RegexUtils.extractText("[&", "&]", "Sample [&text&] for testing: [&., matches ñ expression&] . [&&]");
        Assert.assertTrue("Error al extraer la cadena [&text&]", tagsList.contains("[&text&]"));
        Assert.assertTrue("Error al extraer la cadena [&., matches ñ expression&]",
                tagsList.contains("[&., matches ñ expression&]"));
        Assert.assertTrue("Error al extraer la cadena [&&]", tagsList.contains("[&&]"));
    }
}
