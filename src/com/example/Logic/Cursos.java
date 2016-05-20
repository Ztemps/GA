/*******************************************************************************
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative Commons. 
 * Para ver una copia de esta licencia, visite http://creativecommons.org/licenses/by-nc-nd/4.0/.
 *  
 * @author Francisco Javier Casado Moreno - fcasasdo@elpuig.xeill.net 
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net 
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net 
 * @author Xavier Murcia Gámez - xmurica@elpuig.xeill.net 
 *******************************************************************************/
package com.example.Logic;

import java.sql.Timestamp;

public class Cursos {

	public static String ObtenerCursoActual() {
		String curso = null;
		int any;
		int anyAnterior;
		int anySeguent;
		int mes;
		String fecha;
		String fecha1;
		String fechaparseadastring;
		java.util.Date date = new java.util.Date();
		Timestamp data = new Timestamp(date.getTime());
		fechaparseadastring = String.valueOf(data);
		any = Integer.parseInt(fechaparseadastring.substring(2, 4));
		mes = Integer.parseInt(fechaparseadastring.substring(5, 7));
		anyAnterior = any - 1;
		anySeguent = any + 1;
		fecha = anyAnterior + "/" + fechaparseadastring.substring(2, 4);

		if (mes < 9) {
			curso = anyAnterior + "/" + any;

		} else if (mes >= 9) {
			curso = any + "/" + anySeguent;
		}

		return curso;
	}

}
