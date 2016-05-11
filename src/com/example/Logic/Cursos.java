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
