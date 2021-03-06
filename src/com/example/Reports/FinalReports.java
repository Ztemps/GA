
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
package com.example.Reports;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.persistence.Query;

import com.example.Dates.ConverterDates;
import com.example.Entities.Group;
import com.example.Logic.GroupJPAManager;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.converter.Converter.ConversionException;

public class FinalReports {
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String FILE_HEADER = "ALUMNE,A,E";
	ResourceBundle rb = ResourceBundle.getBundle("GA");
	private ReportQuerys query;
	private ArrayList<Date> dates;
	private GroupJPAManager jpa;
	private List<Group> grupos;

	/**
	 * 
	 */
	
	

	
	
	public FinalReports() {
		// TODO Auto-generated constructor stub
		dates = new ArrayList<Date>();

	}

	public void calcularTotalporAlumnos() {
		// CALCULO DE FECHAS
		// VARIABLE A COJER
		try {
			dates = readFile();
		} catch (ReadOnlyException e1) {
			e1.printStackTrace();
		} catch (ConversionException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Date diaIniciTrimestre;
		Calendar diaIniciCal;

		List calculoAmonest;
		List calculoExpuls;
		Date diaFinalTrimestre;

		long diff;
		long numSetmanes;
		int totalAmonest = 0;
		int totalExpuls = 0;
		FileWriter fileWriter = null;
		int total = 0;
		jpa = new GroupJPAManager();
		grupos = new ArrayList<>();
		grupos = jpa.getGroups();

		for (int x = 0; x < grupos.size(); x++) {

			diaIniciTrimestre = dates.get(0);
			diaIniciCal = Calendar.getInstance();
			diaIniciCal.setTime(diaIniciTrimestre);

			calculoAmonest = null;
			calculoExpuls = null;
			diaFinalTrimestre = dates.get(1);

			diff = diaFinalTrimestre.getTime() - diaIniciTrimestre.getTime();
			numSetmanes = (diff / (24 * 60 * 60 * 1000)) / 7;

			// FOR STUDENT ID
			query = new ReportQuerys();
			List ids = query.getIdAlumnes(grupos.get(x).getId());

			// query.closeTransaction();

			List idList = new ArrayList<>();

			for (int i = 0; i < ids.size(); i++) {
				idList.add(ids.get(i));

				// System.out.println(ids.get(i));
			}

			// FOR NOMS
			query = new ReportQuerys();
			List noms = query.getNomAlumnes(grupos.get(x).getId());
			// query.closeTransaction();

			List nomsList = new ArrayList<>();

			for (int i = 0; i < noms.size(); i++) {
				nomsList.add(noms.get(i));

			}

			// FOR COGNOMS

			query = new ReportQuerys();
			List cognoms = query.getCognomsAlumnes(grupos.get(x).getId());
			// query.closeTransaction();

			List cognomsList = new ArrayList<>();

			for (int i = 0; i < cognoms.size(); i++) {
				cognomsList.add(cognoms.get(i));

			}

			try {
				File f = new File(rb.getString("zip_folder"));
				if (!f.exists()) {
					f.mkdirs();
				}
				else{
					
					f.delete();
					f.mkdir();
				}

				fileWriter = new FileWriter(rb.getString("zip_folder") + grupos.get(x).getId() + ".xls");
				query = new ReportQuerys();
				String dateCurs = query.getDateCurs();
				// query.closeTransaction();

				fileWriter.append("TOTAL  Curs: " + dateCurs);
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(NEW_LINE_SEPARATOR);
				fileWriter.append(NEW_LINE_SEPARATOR);

				String grup = grupos.get(x).getId();
				fileWriter.append(grup);
				fileWriter.append(COMMA_DELIMITER);

				// CONSULTA

				fileWriter.append("1r Trimestre");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append("2r Trimestre");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append("3r Trimestre");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(COMMA_DELIMITER);

				fileWriter.append("ACUMULAT");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(NEW_LINE_SEPARATOR);

				// Headers
				fileWriter.append("ALUMNE");
				fileWriter.append(COMMA_DELIMITER);

				for (int i = 0; i < 4; i++) {
					fileWriter.append("A");
					fileWriter.append(COMMA_DELIMITER);
					fileWriter.append("E");
					fileWriter.append(COMMA_DELIMITER);

				}

				fileWriter.append("TOTAL");
				fileWriter.append(COMMA_DELIMITER);
				// fileWriter.append(COMMA_DELIMITER);

				fileWriter.append(NEW_LINE_SEPARATOR);

				// ADD STUDENTS
				for (int i = 0; i < nomsList.size(); i++) {
					totalExpuls = 0;
					totalAmonest = 0;

					fileWriter.append(nomsList.get(i).toString() + " " + cognomsList.get(i).toString());
					fileWriter.append(COMMA_DELIMITER);
					// ADD STUDENTS
					for (int trim = 0; trim < 3; trim++) {
						if (trim == 0) {
							diaIniciTrimestre = dates.get(0);
							diaFinalTrimestre = dates.get(1);
						}

						else if (trim == 1) {
							diaIniciTrimestre = dates.get(2);
							diaFinalTrimestre = dates.get(3);
						}

						else if (trim == 2) {
							diaIniciTrimestre = dates.get(4);
							diaFinalTrimestre = dates.get(5);
						}

						calculoAmonest = new ArrayList<>();
						calculoExpuls = new ArrayList<>();

						// Debería de pasarle solo el id del alumnno
						calculoAmonest = calcularAmonestadosPorSemana(Integer.parseInt(idList.get(i).toString()), diaIniciTrimestre, diaFinalTrimestre);
						calculoExpuls.add(calculoAmonest.get(1).toString());
						/*
						 * for (int n=0; n<calculoAmonest.size(); n++){
						 * totalAmonest=totalAmonest+Integer.parseInt(
						 * calculoAmonest.get(n).toString()); }
						 * 
						 * for (int n=0; n<calculoExpuls.size(); n++){
						 * totalExpuls=totalExpuls+Integer.parseInt(
						 * calculoExpuls.get(n).toString()); }
						 */

						if (calculoAmonest.get(0).toString().equals("0")) {
							fileWriter.append("");

						} else {
							fileWriter.append(calculoAmonest.get(0).toString());

						}
						fileWriter.append(COMMA_DELIMITER);

						if (calculoExpuls.get(0).toString().equals("0")) {
							fileWriter.append("");

						} else {
							fileWriter.append(calculoExpuls.get(0).toString());

						}
						fileWriter.append(COMMA_DELIMITER);

						totalExpuls = totalExpuls + Integer.parseInt(calculoAmonest.get(0).toString());
						totalAmonest = totalAmonest + Integer.parseInt(calculoExpuls.get(0).toString());

					}

					total = totalExpuls + totalAmonest;

					if (totalExpuls == 0) {
						fileWriter.append("");

					} else {
						fileWriter.append(String.valueOf(totalExpuls));

					}

					fileWriter.append(COMMA_DELIMITER);

					if (totalAmonest == 0) {
						fileWriter.append("");

					} else {
						fileWriter.append(String.valueOf(totalAmonest));

					}
					fileWriter.append(COMMA_DELIMITER);

					if (total == 0) {
						fileWriter.append("");

					} else {
						fileWriter.append(String.valueOf(total));

					}

					fileWriter.append(NEW_LINE_SEPARATOR);

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//System.out.println("FINAL: Grup " + grupos.get(x).getId() + "finalitzat");

		}
	}

	public void calcularResumenTotal() {
		FileWriter fileWriter = null;

		Date diaIniciTrimestre;
		Calendar diaIniciCal;
		List calculoAmonest = null;
		List calculoExpuls = null;
		Date diaFinalTrimestre;
		long diff;
		long numSetmanes;
		int totalAmonest = 0;
		int totalExpuls = 0;
		int total = 0;
		int totalAcumulat = 0;
		int totalAcumulatAmon = 0;
		int totalAcumulatExpul = 0;

		try {

			dates = readFile();
			File f = new File(rb.getString("zip_folder"));
			if (!f.exists()) {
				f.mkdirs();
			}	else{
				
				f.delete();
				f.mkdir();
			}
			fileWriter = new FileWriter(rb.getString("zip_folder")+"resumen.xls");
			query = new ReportQuerys();
			String dateCurs = query.getDateCurs();
			// query.closeTransaction();

			jpa = new GroupJPAManager();
			grupos = new ArrayList<>();
			grupos = jpa.getGroups();
			// jpa.closeTransaction();

			diaIniciTrimestre = dates.get(0);
			diaIniciCal = Calendar.getInstance();
			diaIniciCal.setTime(diaIniciTrimestre);

			calculoAmonest = null;
			calculoExpuls = null;
			diaFinalTrimestre = dates.get(1);

			diff = diaFinalTrimestre.getTime() - diaIniciTrimestre.getTime();
			numSetmanes = (diff / (24 * 60 * 60 * 1000)) / 7;

			fileWriter.append("TOTAL   Curs: " + dateCurs);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Headers

			// CONSULTA
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(COMMA_DELIMITER);

			fileWriter.append("1r Trimestre");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(COMMA_DELIMITER);

			fileWriter.append("2r Trimestre");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(COMMA_DELIMITER);

			fileWriter.append("3r Trimestre");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(COMMA_DELIMITER);

			fileWriter.append("ACUMULAT");
			fileWriter.append(COMMA_DELIMITER);

			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append("GRUP");
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append("Nº ALUMNES");
			fileWriter.append(COMMA_DELIMITER);

			for (int i = 0; i < 4; i++) {
				fileWriter.append("A");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("E");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("total");
				fileWriter.append(COMMA_DELIMITER);

			}

			fileWriter.append(NEW_LINE_SEPARATOR);

			for (int i = 0; i < grupos.size(); i++) {
				fileWriter.append(grupos.get(i).getId());
				fileWriter.append(COMMA_DELIMITER);

				// FOR STUDENT ID
				query = new ReportQuerys();
				List ids = query.getIdAlumnes(grupos.get(i).getId());

				// query.closeTransaction();

				List idList = new ArrayList<>();

				for (int j = 0; j < ids.size(); j++) {
					idList.add(ids.get(j));

					// System.out.println(ids.get(j));
				}
				fileWriter.append(String.valueOf(idList.size()));
				fileWriter.append(COMMA_DELIMITER);

				/////////////////////////////

				totalAcumulat = 0;
				totalAcumulatAmon = 0;
				totalAcumulatExpul = 0;
				for (int trim = 0; trim < 3; trim++) {

					totalAmonest = 0;
					totalExpuls = 0;
					total = 0;
					calculoExpuls = new ArrayList<>();
					calculoAmonest = new ArrayList<>();
					if (trim == 0) {
						diaIniciTrimestre = dates.get(0);
						diaFinalTrimestre = dates.get(1);
					}

					else if (trim == 1) {
						diaIniciTrimestre = dates.get(2);
						diaFinalTrimestre = dates.get(3);
					}

					else if (trim == 2) {
						diaIniciTrimestre = dates.get(4);
						diaFinalTrimestre = dates.get(5);
					}

					calculoAmonest = calcularAmonestadosPorSemana2(idList, diaIniciTrimestre, diaFinalTrimestre);
					calculoExpuls = calcularExpulsadosPorSemana2(idList, diaIniciTrimestre, diaFinalTrimestre);

					for (int n = 0; n < calculoAmonest.size(); n++) {
						totalAmonest = totalAmonest + Integer.parseInt(calculoAmonest.get(n).toString());
					}

					for (int n = 0; n < calculoExpuls.size(); n++) {
						totalExpuls = totalExpuls + Integer.parseInt(calculoExpuls.get(n).toString());
					}

					total = totalExpuls + totalAmonest;
					totalAcumulatAmon = totalAcumulatAmon + totalAmonest;
					totalAcumulatExpul = totalAcumulatExpul + totalExpuls;

					if (totalAmonest == 0) {
						fileWriter.append("");

					} else {
						fileWriter.append(String.valueOf(totalAmonest));

					}
					fileWriter.append(COMMA_DELIMITER);

					if (totalExpuls == 0) {
						fileWriter.append("");

					} else {
						fileWriter.append(String.valueOf(totalExpuls));

					}
					fileWriter.append(COMMA_DELIMITER);

					if (total == 0) {
						fileWriter.append("");

					} else {
						fileWriter.append(String.valueOf(total));

					}
					fileWriter.append(COMMA_DELIMITER);

					////////////////////////////////////
				}
				totalAcumulat = totalAcumulatAmon + totalAcumulatExpul;

				if (totalAcumulatAmon == 0) {
					fileWriter.append("");

				} else {
					fileWriter.append(String.valueOf(totalAcumulatAmon));

				}
				fileWriter.append(COMMA_DELIMITER);

				if (totalAcumulatExpul == 0) {
					fileWriter.append("");

				} else {
					fileWriter.append(String.valueOf(totalAcumulatExpul));

				}

				fileWriter.append(COMMA_DELIMITER);

				if (totalAcumulat == 0) {
					fileWriter.append("");

				} else {
					fileWriter.append(String.valueOf(totalAcumulat));

				}

				fileWriter.append(NEW_LINE_SEPARATOR);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
		//System.out.println("FINAL resumen finalitzat");

	}


	

	private List calcularAmonestadosPorSemana(int idList, Date semana1, Date semana2) {

		List amonestacions1;

		amonestacions1 = new ArrayList<>();

			query = new ReportQuerys();
			amonestacions1.add(query.getWarningCurs(idList, semana1, semana2));
			query = new ReportQuerys();
			amonestacions1.add(query.getExpulsionCurs(idList, semana1, semana2));

			// query.closeTransaction();
			//System.out.println(amonestacions1.get(0));

		

		return amonestacions1;

	}

	
	
	
	private List calcularAmonestadosPorSemana2(List idList, Date semana1, Date semana2) {

		List amonestacions1;

		amonestacions1 = new ArrayList<>();

		for (int j = 0; j < idList.size(); j++) {
			query = new ReportQuerys();
			amonestacions1.add(query.getWarningCurs(Integer.parseInt(idList.get(j).toString()), semana1, semana2));
			// query.closeTransaction();

		}

		return amonestacions1;

	}

	
	private List calcularExpulsadosPorSemana2(List idList, Date semana1, Date semana2) {

		List expulsions1;
		List expulsionsList1 = null;

		expulsions1 = new ArrayList<>();

		for (int j = 0; j < idList.size(); j++) {
			query = new ReportQuerys();
			expulsions1.add(query.getExpulsionCurs(Integer.parseInt(idList.get(j).toString()), semana1, semana2));
			// query.closeTransaction();

		}

		return expulsions1;
	}
	
	
	public ArrayList<Date> readFile() throws ReadOnlyException, ConversionException, IOException {
		FileReader reader;
		File currDir = new File(".");
		String linea = null;
		Date fechaIniciTrimestre1 = null;
		Date fechaFinalTrimestre3 = null;
		Date fechafinaltrimestre1 = null;
		Date fechafinaltrimestre2 = null;
		Date fechaIniciTrimestre3 = null;
		Date fechaIniciTrimestre2 = null;
		ArrayList<Date> fechas = new ArrayList<Date>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


		File f = new File(rb.getString("file_settings"));		
		BufferedReader br = new BufferedReader(new FileReader(f));
		if (br.readLine() == null) {
			//System.out.println("No Hay fecha en el documento settings");

		} else {

			reader = new FileReader(f);
			BufferedReader flux = new BufferedReader(reader);

			while ((linea = flux.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(linea, ",");
				while (st.hasMoreTokens()) {

					try {

						fechaIniciTrimestre1 = formatter.parse(st.nextToken());
						fechafinaltrimestre1 = formatter.parse(st.nextToken());
						fechaIniciTrimestre2 = formatter.parse(st.nextToken());
						fechafinaltrimestre2 = formatter.parse(st.nextToken());
						fechaIniciTrimestre3 = formatter.parse(st.nextToken());
						fechaFinalTrimestre3 = formatter.parse(st.nextToken());

					} catch (ParseException e) {
						e.printStackTrace();
					}

					st.nextToken();
					st.nextToken();
					st.nextToken();

				}
				fechas.add(fechaIniciTrimestre1);
				fechas.add(fechafinaltrimestre1);
				fechas.add(fechaIniciTrimestre2);
				fechas.add(fechafinaltrimestre2);
				fechas.add(fechaIniciTrimestre3);
				fechas.add(fechaFinalTrimestre3);

			}

		}
		return fechas;
	}

	public void closeAllConnections() {
		query.closeTransaction();
		jpa.closeTransaction();
	}
}