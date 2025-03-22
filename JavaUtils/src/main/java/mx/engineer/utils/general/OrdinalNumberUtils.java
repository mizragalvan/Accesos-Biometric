package mx.engineer.utils.general;

public final class OrdinalNumberUtils {
    private static final Integer ORDINAL_NUMBER_PARAM10 = 10;
    private static final Integer ORDINAL_NUMBER_PARAM100 = 100;
    private static final String WHITE_SPACE = " ";
    private OrdinalNumberUtils() { }
    
    public static String getOrdinalNumber(final Integer number) {
        final String[] unit = {"", "Primera", "Segunda", "Tercera", "Cuarta", "Quinta", "Sexta", "Septima", "Octava", 
                "Novena", };
        final String[] ten = {"", "Décima", "Vigésima", "Trigésima", "Cuadragésima", "Quincuagésima", "Sexagésima",
                "Septuagésima", "Octogésima", "Nonagésima", };
        final String[] hundred = {"", "Centsima", "Ducentésima", "Tricentésima", " Cuadringentésima", " Quingentésima",
                " Sexcentésima", " Septingentésima", " Octingentésima", " Noningentésima", };
        final int u = number % ORDINAL_NUMBER_PARAM10;  
        final int d = (number / ORDINAL_NUMBER_PARAM10) % ORDINAL_NUMBER_PARAM10;  
        final int c = number / ORDINAL_NUMBER_PARAM100;  
        if (number >= ORDINAL_NUMBER_PARAM100)
            return new String(hundred[c] + WHITE_SPACE + ten[d] + WHITE_SPACE + unit[u]).toUpperCase().trim();
        else {  
            if (number >= ORDINAL_NUMBER_PARAM10) 
                return new String(ten[d] + WHITE_SPACE + unit[u]).toUpperCase().trim();
            else 
                return new String(unit[number]).toUpperCase().trim();
        } 
    }

}
