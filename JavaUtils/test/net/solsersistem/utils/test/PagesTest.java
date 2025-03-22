package mx.solsersistem.utils.test;

import mx.solsersistem.utils.pages.Pages;

import org.junit.Assert;
import org.junit.Test;

public class PagesTest {

    @Test
    public final void whenGetTotalPagesThenTotalPagesCalculatedCorrectly() {
        final Integer totalItems = 11;
        final Integer itemsPerPage = 10;
        Assert.assertEquals("Error al calcular el número total de páginas",
                new Integer(2), Pages.getTotalPages(totalItems, itemsPerPage));
    }
}
