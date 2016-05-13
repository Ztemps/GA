package com.example.Dates;

import java.util.Date;

public class ConverterDates {
	
	
	
	public static String converterDate(Date datainici) {
		String fechaFinal = null;
		String month = null;
		//System.out.println("DAta sin cambiar: "+datainici.toLocaleString());
		String datainicial = datainici.toLocaleString();

		String[] fecha = datainicial.substring(0, 11).split("-");

		switch (fecha[1]) {
		//English format
		case "jan":
			month = "01";
			break;
		case "feb":
			month = "02";

			break;
		case "mar":
			month = "03";

			break;
		case "apr":
			month = "04";

			break;
		case "may":
			month = "05";

			break;
		case "jun":
			month = "06";

			break;
		case "jul":
			month = "07";

			break;
		case "aug":
			month = "08";

			break;
		case "sep":
			month = "09";

			break;
		case "oct":
			month = "10";

			break;
		case "nov":
			month = "11";

			break;
		case "dec":
			month = "12";

			break;
			
			
			//Catalan and Spanish format
		case "gen":
			month = "01";
			break;

		case "abr":
			month = "04";

			break;
		case "mai":
			month = "05";

			break;
			

		case "ago":
			month = "08";

			break;	
		
		case "dic":
			month = "12";

			break;
			

		default:
			System.out.println("error mes");
			break;
		}

		fechaFinal = fecha[0] + "-" + month + "-" + fecha[2];

		//System.out.println(fechaFinal);

		return fechaFinal;

	}
}
