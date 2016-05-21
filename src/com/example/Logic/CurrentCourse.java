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
package com.example.Logic;

import java.sql.Timestamp;
import java.util.Date;

public class CurrentCourse {

	private Date date;
	private String dateParsing;
	private Timestamp data;
	int month, year, previousYear, nextYear;
	private String currentCourse;
	private String Date;

	public String currentCourse() {
		date = new Date();
		data = new Timestamp(date.getTime());
		dateParsing = String.valueOf(data);
		year = Integer.parseInt(dateParsing.substring(2, 4));
		month = Integer.parseInt(dateParsing.substring(5, 7));
		previousYear = year - 1;
		nextYear = year + 1;
		Date = previousYear + "/" + dateParsing.substring(2, 4);

		if (month < 9) {
			currentCourse = previousYear + "/" + year;

		} else if (month >= 9) {
			currentCourse = year + "/" + nextYear;
		}

		return currentCourse;
	}

}
