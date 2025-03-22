package mx.pagos.admc.util.shared;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Holidays {
    public static List<LocalDate> getHolidays() {
        List<LocalDate> holidays = new ArrayList<>();

        holidays.add(LocalDate.of(2023, 1, 1));   // Año Nuevo
        holidays.add(LocalDate.of(2023, 2, 6));   // Día de la Constitución
        holidays.add(LocalDate.of(2023, 3, 20));  // Natalicio de Benito Juárez
        holidays.add(LocalDate.of(2023, 4, 13));  // Jueves Santo
        holidays.add(LocalDate.of(2023, 4, 14));  // Viernes Santo
        holidays.add(LocalDate.of(2023, 5, 1));   // Día del Trabajo
        holidays.add(LocalDate.of(2023, 9, 16));  // Día de la Independencia
        holidays.add(LocalDate.of(2023, 11, 20)); // Revolución Mexicana
        holidays.add(LocalDate.of(2023, 12, 25)); // Navidad

        // Agrega más días festivos según sea necesario

        return holidays;
    }
}
