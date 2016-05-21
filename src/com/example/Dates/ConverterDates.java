/*******************************************************************************
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasasdo@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurica@elpuig.xeill.net 
 * 
 *******************************************************************************/

package com.example.Dates;

import java.util.Date;

public class ConverterDates {

	private String month;
	private String initialDate;
	private String[] date;
	private String lastMonth;
	public String converterDate(Date initialDate) {

		this.initialDate = initialDate.toLocaleString();
		date = this.initialDate.substring(0, 11).split("-");
		month = getMonth(date[1]);
		
		return date[0] + "-" + month + "-" + date[2];

	}

	public  String converterDate2(String initialDate) {

		date = initialDate.substring(0, initialDate.length()).split(" ");
		month = getMonth(date[1]);
		
		return date[2] + "-" + month + "-" + date[5];
	}

	public  String getMonth(String month) {

		switch (month) {
		// English format
		case "jan":
			lastMonth = "01";
			break;
		case "feb":
			lastMonth = "02";

			break;
		case "mar":
			lastMonth = "03";

			break;
		case "apr":
			lastMonth = "04";

			break;
		case "may":
			lastMonth = "05";

			break;
		case "jun":
			lastMonth = "06";

			break;
		case "jul":
			lastMonth = "07";

			break;
		case "aug":
			lastMonth = "08";

			break;
		case "sep":
			lastMonth = "09";

			break;
		case "oct":
			lastMonth = "10";

			break;
		case "nov":
			lastMonth = "11";

			break;
		case "dec":
			lastMonth = "12";

			break;

		// Catalan and Spanish format

		case "ene":
			lastMonth = "01";
			break;

		case "gen":
			lastMonth = "01";
			break;

		case "abr":
			lastMonth = "04";

			break;
		case "mai":
			lastMonth = "05";

			break;

		case "ago":
			lastMonth = "08";

			break;

		case "dic":
			lastMonth = "12";

			break;

		////////////// MAYUS DATES

		// English format
		case "Jan":
			lastMonth = "01";
			break;
		case "Feb":
			lastMonth = "02";

			break;
		case "Mar":
			lastMonth = "03";

			break;
		case "Apr":
			lastMonth = "04";

			break;
		case "May":
			lastMonth = "05";

			break;
		case "Jun":
			lastMonth = "06";

			break;
		case "Jul":
			lastMonth = "07";

			break;
		case "Aug":
			lastMonth = "08";

			break;
		case "Sep":
			lastMonth = "09";

			break;
		case "Oct":
			lastMonth = "10";

			break;
		case "Nov":
			lastMonth = "11";

			break;
		case "Dec":
			lastMonth = "12";

			break;

		// Catalan and Spanish format

		case "Ene":
			lastMonth = "01";
			break;

		case "Gen":
			lastMonth = "01";
			break;

		case "Abr":
			lastMonth = "04";

			break;
		case "Mai":
			lastMonth = "05";

			break;

		case "Ago":
			lastMonth = "08";

			break;

		case "Dic":
			lastMonth = "12";

			break;

		default:
			System.out.println("Error month:  " + month);
			break;
		}

		return lastMonth;
	}

}
