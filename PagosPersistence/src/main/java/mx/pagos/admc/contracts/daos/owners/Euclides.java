package mx.pagos.admc.contracts.daos.owners;

public class Euclides {

    public static void main(String []args) {
        System.out.println("Euclides: " + calcularMCD(412, 184));
    }
    
    public static Integer calcularMCD(Integer primerNumero, Integer segundoNumero) {
        do {
            if (primerNumero > segundoNumero) 
                primerNumero -= segundoNumero; 
            else if (segundoNumero > primerNumero)
                segundoNumero -= primerNumero;
        } while (!primerNumero.equals(segundoNumero));
        return primerNumero;
    }
}
