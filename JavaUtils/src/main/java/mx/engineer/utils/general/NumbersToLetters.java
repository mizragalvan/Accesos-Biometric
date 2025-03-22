package mx.engineer.utils.general;



public class NumbersToLetters {
	private static int flag;
	private static String num;
	private static int numeroTemp;
	private static String num_letra;
	private static String num_letras;
	private static String num_letram;
	private static String num_letradm;
	private static String num_letracm;
	private static String num_letramm;
	private static String num_letradmm;
	private static String num_letracmm = "";
		
	public NumbersToLetters() {		
		flag = 0;
	}
	public NumbersToLetters(long n) {		
		flag = 0;
	}

	private static String unidad(long numero) {
		numeroTemp = (int)numero;
		switch (numeroTemp) {
		case 9:
			num = "nueve";
				break;
		case 8:
			num = "ocho";
				break;
		case 7:
			num = "siete";
				break;
		case 6:
			num = "seis";
				break;
		case 5:
			num = "cinco";
				break;
		case 4:
			num = "cuatro";
				break;
		case 3:
			num = "tres";
				break;
		case 2:
			num = "dos";
				break;
		case 1:
				if (flag == 0)
					num = "uno";
				else 
					num = "un";
				break;
		case 0:
			num = "";
				break;
		}
		return num;
	}
	
	private static String decena(long numero){
		numeroTemp = (int)numero;
		if (numeroTemp >= 90 && numeroTemp <= 99) {
			num_letra = "noventa ";
			if (numeroTemp > 90)
				num_letra = num_letra.concat("y ").concat(unidad(numeroTemp - 90));
		}
		else if (numeroTemp >= 80 && numeroTemp <= 89) {
			num_letra = "ochenta ";
			if (numeroTemp > 80)
				num_letra = num_letra.concat("y ").concat(unidad(numeroTemp - 80));
		}
		else if (numeroTemp >= 70 && numeroTemp <= 79) {
			num_letra = "setenta ";
			if (numeroTemp > 70)
				num_letra = num_letra.concat("y ").concat(unidad(numeroTemp - 70));
		}
		else if (numeroTemp >= 60 && numeroTemp <= 69) {
			num_letra = "sesenta ";
			if (numeroTemp > 60)
				num_letra = num_letra.concat("y ").concat(unidad(numeroTemp - 60));
		}
		else if (numeroTemp >= 50 && numeroTemp <= 59) {
			num_letra = "cincuenta ";
			if (numeroTemp > 50)
				num_letra = num_letra.concat("y ").concat(unidad(numeroTemp - 50));
		}
		else if (numeroTemp >= 40 && numeroTemp <= 49)
		{
			num_letra = "cuarenta ";
			if (numeroTemp > 40)
				num_letra = num_letra.concat("y ").concat(unidad(numeroTemp - 40));
		}
		else if (numeroTemp >= 30 && numeroTemp <= 39) {
			num_letra = "treinta ";
			if (numeroTemp > 30)
				num_letra = num_letra.concat("y ").concat(unidad(numeroTemp - 30));
		}
		else if (numeroTemp >= 20 && numeroTemp <= 29) {
			if (numeroTemp == 20)
				num_letra = "veinte ";
			else {
				if (numero - 20 == 2)
					num_letra = "veintidós";
				else if (numero - 20 == 3)
					num_letra = "veintitrés";
				else if (numero - 20 == 6)
					num_letra = "veintiséis";
				else
					num_letra = "veinti".concat(unidad(numero - 20));
			}
		}
		else if (numeroTemp >= 10 && numeroTemp <= 19) {
			switch (numeroTemp) {
			case 10:
				num_letra = "diez";
				break;
			case 11:
				num_letra = "once ";
				break;
			case 12:
				num_letra = "doce ";
				break;
			case 13:
				num_letra = "trece ";
				break;
			case 14:
				num_letra = "catorce ";
				break;
			case 15:
				num_letra = "quince ";
				break;
			case 16:
				num_letra = "dieciseis ";
				break;
			case 17:
				num_letra = "diecisiete ";
				break;
			case 18:
				num_letra = "dieciocho ";
				break;
			case 19:
				num_letra = "diecinueve ";
				break;
			}	
		}
		else
			num_letra = unidad(numeroTemp);
	return num_letra;
	}	

	private static String centena(long numero) {
		if (numero >= 100) {
			if (numero >= 900 && numero <= 999) {
				 num_letra = "novecientos ";
				if (numero > 900)
					num_letra = num_letra.concat(decena(numero - 900));
			}
			else if (numero >= 800 && numero <= 899) {
				num_letra = "ochocientos ";
				if (numero > 800)
					num_letra = num_letra.concat(decena(numero - 800));
			}
			else if (numero >= 700 && numero <= 799) {
				num_letra = "setecientos ";
				if (numero > 700)
					num_letra = num_letra.concat(decena(numero - 700));
			}
			else if (numero >= 600 && numero <= 699) {
				num_letra = "seiscientos ";
				if (numero > 600)
					num_letra = num_letra.concat(decena(numero - 600));
			}
			else if (numero >= 500 && numero <= 599) {
				num_letra = "quinientos ";
				if (numero > 500)
					num_letra = num_letra.concat(decena(numero - 500));
			}
			else if (numero >= 400 && numero <= 499) {
				num_letra = "cuatrocientos ";
				if (numero > 400)
					num_letra = num_letra.concat(decena(numero - 400));
			}
			else if (numero >= 300 && numero <= 399) {
				num_letra = "trescientos ";
				if (numero > 300)
					num_letra = num_letra.concat(decena(numero - 300));
			}
			else if (numero >= 200 && numero <= 299) {
				num_letra = "doscientos ";
				if (numero > 200)
					num_letra = num_letra.concat(decena(numero - 200));
			}
			else if (numero >= 100 && numero <= 199) {
				if (numero == 100)
					num_letra = "cien ";
				else
					num_letra = "ciento ".concat(decena(numero - 100));
			}
		}
		else
			num_letra = decena(numero);
		
		return num_letra;	
	}	

	private static String miles(long numero) {
		if (numero >= 1000 && numero <2000) {
			num_letram = ("mil ").concat(centena(numero%1000));
		}
		if (numero >= 2000 && numero <10000) {
			flag = 1;
			num_letram = unidad(numero/1000).concat(" mil ").concat(centena(numero%1000));
		}
		if (numero < 1000)
			num_letram = centena(numero);
		
		return num_letram;
	}		

	private static String decmiles(long numero) {
		if (numero == 10000)
			num_letradm = "diez mil";
		if (numero > 10000 && numero <20000) {
			flag = 1;
			num_letradm = decena(numero/1000).concat(" mil ").concat(centena(numero%1000));		
		}
		if (numero >= 20000 && numero <100000) {
			flag = 1;
			num_letradm = decena(numero/1000).concat(" mil ").concat(miles(numero%1000));		
		}
		
		if (numero < 10000)
			num_letradm = miles(numero);
		
		return num_letradm;
	}		

	private static String cienmiles(long numero) {
		if (numero == 100000)
			num_letracm = "cien mil";
		if (numero >= 100000 && numero <1000000) {
			flag = 1;
			num_letracm = centena(numero/1000).concat(" mil ").concat(centena(numero%1000));		
		}
		if (numero < 100000)
			num_letracm = decmiles(numero);
		return num_letracm;
	}		

	private static String millon(long numero) {
		if (numero >= 1000000 && numero <2000000) {
			flag = 1;
			num_letramm = ("Un millon ").concat(cienmiles(numero%1000000));
		}
		if (numero >= 2000000 && numero <10000000) {
			flag = 1;
			num_letramm = unidad(numero/1000000).concat(" millones ").concat(cienmiles(numero%1000000));
		}
		if (numero < 1000000)
			num_letramm = cienmiles(numero);
		
		return num_letramm;
	}		
	
	private static String decmillon(long numero) {
		if (numero == 10000000)
			num_letradmm = "diez millones";
		if (numero > 10000000 && numero <20000000) {
			flag = 1;
			num_letradmm = decena(numero/1000000).concat(" millones ").concat(cienmiles(numero%1000000));		
		}
		if (numero >= 20000000 && numero <100000000) {
			flag = 1;
			num_letradmm = decena(numero/1000000).concat(" millones ").concat(millon(numero%1000000));		
		}
		
		if (numero < 10000000)
			num_letradmm = millon(numero);
		return num_letradmm;
	}
	
	private static String centenasMillon(long numero){
		if(numero >=100000000 && numero <1000000000){
			num_letracmm = centena(numero/1000000).concat(" millones ").concat(millon(numero%1000000));
		}else{
			num_letracmm = decmillon(numero);
		}			
		return num_letracmm;
	}
	
	private static String milesMillon(long numero){
		if(numero >= 1000000000 && numero < 10000000000L){
			num_letracmm = miles(numero/1000000).concat(" millones ").concat(cienmiles(numero%1000000));;
		}else{
			num_letracmm = centenasMillon(numero);
		}			
		return num_letracmm;
	}
	
	private static String decMilesMillon(long numero){
		if(numero >= 10000000000L && numero < 100000000000L){
			num_letracmm = cienmiles(numero/1000000).concat(" millones ").concat(cienmiles(numero%1000000));;
		}else{
			num_letracmm = milesMillon(numero);
		}			
		return num_letracmm;
	}

	public static String letterConvert(long numero) {
		num_letras = decMilesMillon(numero);
		return num_letras;
	}

}