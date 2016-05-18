package com.example.Dates;

import java.util.Date;

public class ConverterDates {
	
	
	
	public static String converterDate(Date datainici) {
		String fechaFinal = null;
		String month = null;
		//System.out.println("DAta sin cambiar: "+datainici.toLocaleString());
		String datainicial = datainici.toLocaleString();

		String[] fecha = datainicial.substring(0, 11).split("-");

		month=getMonth(fecha[1]);

		fechaFinal = fecha[0] + "-" + month + "-" + fecha[2];

		//System.out.println(fechaFinal);

		return fechaFinal;

	}
	
	
	
	
	
	public static String converterDate2(String datainici) {
		String fechaFinal = null;
		String month = null;
		//System.out.println("DAta sin cambiar: "+datainici.toLocaleString());

		String[] fecha = datainici.substring(0, datainici.length()).split(" ");
		month=getMonth(fecha[1]);
		

		//System.out.println(fechaFinal);
		fechaFinal = fecha[2] + "-" + month + "-" + fecha[5];

		return fechaFinal;
	}
	
	
	public static String getMonth(String month) {
		String monthFinal=null;
	
		switch (month) {
		//English format
		case "jan":
			monthFinal = "01";
			break;
		case "feb":
			monthFinal = "02";

			break;
		case "mar":
			monthFinal = "03";

			break;
		case "apr":
			monthFinal = "04";

			break;
		case "may":
			monthFinal = "05";

			break;
		case "jun":
			monthFinal = "06";

			break;
		case "jul":
			monthFinal = "07";

			break;
		case "aug":
			monthFinal = "08";

			break;
		case "sep":
			monthFinal = "09";

			break;
		case "oct":
			monthFinal = "10";

			break;
		case "nov":
			monthFinal = "11";

			break;
		case "dec":
			monthFinal = "12";

			break;
			
			
			//Catalan and Spanish format
			
		case "ene":
			monthFinal = "01";
			break;
			
		case "gen":
			monthFinal = "01";
			break;

		case "abr":
			monthFinal = "04";

			break;
		case "mai":
			monthFinal = "05";

			break;
			

		case "ago":
			monthFinal = "08";

			break;	
		
		case "dic":
			monthFinal = "12";

			break;
			
			
		////////////// MAYUS DATES	
			
			//English format
			case "Jan":
				monthFinal = "01";
				break;
			case "Feb":
				monthFinal = "02";

				break;
			case "Mar":
				monthFinal = "03";

				break;
			case "Apr":
				monthFinal = "04";

				break;
			case "May":
				monthFinal = "05";

				break;
			case "Jun":
				monthFinal = "06";

				break;
			case "Jul":
				monthFinal = "07";

				break;
			case "Aug":
				monthFinal = "08";

				break;
			case "Sep":
				monthFinal = "09";

				break;
			case "Oct":
				monthFinal = "10";

				break;
			case "Nov":
				monthFinal = "11";

				break;
			case "Dec":
				monthFinal = "12";

				break;
				
				
				//Catalan and Spanish format
				
			case "Ene":
				monthFinal = "01";
				break;
				
			case "Gen":
				monthFinal = "01";
				break;

			case "Abr":
				monthFinal = "04";

				break;
			case "Mai":
				monthFinal = "05";

				break;
				

			case "Ago":
				monthFinal = "08";

				break;	
			
			case "Dic":
				monthFinal = "12";

				break;

		default:
			System.out.println("error mes:  " + month);
			break;
		}

		
		
		return monthFinal;
	}
		
}
