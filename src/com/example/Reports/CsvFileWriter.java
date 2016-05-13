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
import java.util.StringTokenizer;

import javax.persistence.Query;

import com.example.Dates.ConverterDates;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.converter.Converter.ConversionException;

public class CsvFileWriter {
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	static public ReportQuerys query;
	public Query queryDate = null;
	static ArrayList<Date> dates = new ArrayList<Date>();
	// CSV file header
	private static final String FILE_HEADER = "ALUMNE,A,E";

	public static void main(String[] args) {

		calcularPrimerTrimestre();
	}

	public static void calcularPrimerTrimestre() {
		// CALCULO DE FECHAS
		// VARIABLE A COJER
		try {
			dates = readFile();
		} catch (ReadOnlyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConversionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Date diaInici = dates.get(0);
		Date diaFinal = dates.get(1);
		Calendar diaIniciCal = Calendar.getInstance();
		diaIniciCal.setTime(diaInici);
		Calendar diaInicisetmanes = Calendar.getInstance();
		diaInicisetmanes.setTime(diaInici);
		List calculoAmonest=null;
		List calculoExpuls=null;
		int contador=0;
		Date diaFinalTrimestre1 = dates.get(2);
		Date diaFinalTrimestre2 = dates.get(3);
		long diff = diaFinalTrimestre2.getTime() - diaFinalTrimestre1.getTime();
		long numSetmanes = (diff / (24 * 60 * 60 * 1000)) / 7;
		//System.out.print(numSetmanes + " setmanes, ");
		// Delimiter used in CSV file

		FileWriter fileWriter = null;

		// Create new students objects
		String grupCurs = "ESO 1A";

		// FOR STUDENT ID
		query = new ReportQuerys();
		List ids = query.getIdAlumnes(grupCurs);
		query.closeTransaction();

		List idList = new ArrayList<>();

		for (int i = 0; i < ids.size(); i++) {
			idList.add(ids.get(i));

			// System.out.println(ids.get(i));
		}

		// FOR NOMS
		query = new ReportQuerys();
		List noms = query.getNomAlumnes(grupCurs);
		query.closeTransaction();

		List nomsList = new ArrayList<>();

		for (int i = 0; i < noms.size(); i++) {
			nomsList.add(noms.get(i));

			// System.out.println(noms.get(i));

		}

		// FOR COGNOMS

		query = new ReportQuerys();
		List cognoms = query.getCognomsAlumnes(grupCurs);
		query.closeTransaction();

		List cognomsList = new ArrayList<>();

		for (int i = 0; i < cognoms.size(); i++) {
			cognomsList.add(cognoms.get(i));

			// System.out.println(cognomsList.get(i));

		}

		// //FOR 2rstWEEK
		// List amonestacions2 =new ArrayList<>();
		// List expulsions2 = new ArrayList<>();
		//
		//
		// diaInicisetmanes.add(Calendar.DATE, 7);
		// Date semana3=diaInicisetmanes.getTime();
		//
		//
		// for (int j=0; j<idList.size(); j++){
		// query = new ReportQuerys();
		// amonestacions2.add(query.getWarningCurs(Integer.parseInt(idList.get(j).toString()),semana2,semana3));
		// query.closeTransaction();
		//
		// query = new ReportQuerys();
		// expulsions2.add(query.getExpulsionCurs(Integer.parseInt(idList.get(j).toString()),semana2,semana3));
		// query.closeTransaction();
		//
		// }
		// List amonestacionsList2 = new ArrayList<>();
		// List expulsionsList2 = new ArrayList<>();
		//
		// for (int i = 0; i < amonestacions2.size(); i++) {
		// amonestacionsList2.add(amonestacions2.get(i));
		//
		// System.out.println(amonestacionsList2.get(i));
		//
		// }
		//
		// for (int i = 0; i < expulsions2.size(); i++) {
		// expulsionsList2.add(expulsions2.get(i));
		//
		// System.out.println(expulsionsList2.get(i));
		//
		// }

		try {
			fileWriter = new FileWriter("/home/katano/Escritorio/alumnes.xls");
			query = new ReportQuerys();
			String dateCurs = query.getDateCurs();
			query.closeTransaction();

			fileWriter.append("1r Trimestre   Any: " + dateCurs);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(NEW_LINE_SEPARATOR);

			// CONSULTA habrá que preguntarlo al usuario con un checkbox
			String grup = "1r A";
			fileWriter.append(grup);
			fileWriter.append(COMMA_DELIMITER);

			// CONSULTA
			String setmana = "Set. ";

			for (int i = 1; i <= numSetmanes; i++) {
				setmana.equals(numSetmanes + i);
				fileWriter.append(setmana + i);
				fileWriter.append(COMMA_DELIMITER);
				// CONSULTA mejorar con date y la data incii y final que
				// introduzca el admin
				diaIniciCal.add(Calendar.DATE, 7);
				Date diaInicial = diaIniciCal.getTime();
				String fechainicialBuena = ConverterDates.converterDate(diaInicial);

				fileWriter.append(fechainicialBuena);
				// diaInici=aux;
				fileWriter.append(COMMA_DELIMITER);

			}
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Headers
			fileWriter.append("ALUMNE");
			fileWriter.append(COMMA_DELIMITER);

			for (int i = 0; i < numSetmanes; i++) {
				fileWriter.append("A");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("E");
				fileWriter.append(COMMA_DELIMITER);

			}

			// fileWriter.append(COMMA_DELIMITER);

			fileWriter.append(NEW_LINE_SEPARATOR);
			Date semana2 = null;
			Date semana1 = diaInicisetmanes.getTime();
			diaInicisetmanes.add(Calendar.DATE, 7);

			semana2 = diaInicisetmanes.getTime();

			// ADD STUDENTS
			for (int i = 0; i < nomsList.size(); i++) {
				fileWriter.append(nomsList.get(i).toString() + " " + cognomsList.get(i).toString());
				fileWriter.append(COMMA_DELIMITER);

				// SEMANA 1
				
				
				int resta=1;
				for (int l = 0; l < numSetmanes; l++) {
					while (resta<numSetmanes){
						resta++;
						System.out.println("Faltan "+(numSetmanes-resta) +"semanas");

					

					calculoAmonest= new ArrayList<>();
					calculoAmonest = new ArrayList<>();
					
			
					
					calculoAmonest =	calcularAmonestadosPorSemana(idList,semana1,semana2);
					calculoExpuls= calcularExpulsadosPorSemana(idList,semana1,semana2);
					
					semana1 = semana2;
					diaInicisetmanes.add(Calendar.DATE, 7);
					semana2 = diaInicisetmanes.getTime();

				if (calculoAmonest.get(i).toString().equals("0")) {
					fileWriter.append("");

				} else {
					fileWriter.append(calculoAmonest.get(i).toString());

				}
				fileWriter.append(COMMA_DELIMITER);
				
				
				

				if (calculoExpuls.get(i).toString().equals("0")) {
					fileWriter.append("");

				} else {
					fileWriter.append(calculoExpuls.get(i).toString());

				}
				fileWriter.append(COMMA_DELIMITER);
					}
				}
				 fileWriter.append(NEW_LINE_SEPARATOR);

				
				
				// //SEMANA 2
				//
				// if (amonestacionsList2.get(i).toString().equals("0")){
				// fileWriter.append("");
				//
				// }
				// else{
				// fileWriter.append(amonestacionsList2.get(i).toString());
				//
				// }
				// fileWriter.append(COMMA_DELIMITER);
				//
				// if (expulsionsList2.get(i).toString().equals("0")){
				// fileWriter.append("");
				//
				// }
				// else{
				// fileWriter.append(expulsionsList2.get(i).toString());
				//
				// }
				// fileWriter.append(NEW_LINE_SEPARATOR);

				//
				// fileWriter.append(cognomsList.get(i).toString());
				// fileWriter.append(NEW_LINE_SEPARATOR);

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try {
		// fileWriter = new FileWriter(fileName);
		//
		// //Write the CSV file header
		// fileWriter.append(FILE_HEADER.toString());
		//
		// //Add a new line separator after the header
		// fileWriter.append(NEW_LINE_SEPARATOR);
		//
		// //Write a new student object list to the CSV file
		// for (Student student : students) {
		// fileWriter.append(String.valueOf(student.getId()));
		// fileWriter.append(COMMA_DELIMITER);
		// fileWriter.append(student.getFirstName());
		// fileWriter.append(COMMA_DELIMITER);
		// fileWriter.append(student.getLastName());
		// fileWriter.append(COMMA_DELIMITER);
		// fileWriter.append(student.getGender());
		// fileWriter.append(COMMA_DELIMITER);
		//
		// fileWriter.append(String.valueOf(student.getAge()));
		//
		// fileWriter.append(NEW_LINE_SEPARATOR);
	}

	// System.out.println("CSV file was created successfully !!!");
	//
	// } catch (Exception e) {
	// System.out.println("Error in CsvFileWriter !!!");
	// e.printStackTrace();
	// } finally {
	//
	// try {
	// fileWriter.flush();
	// fileWriter.close();
	// } catch (IOException e) {
	//
	// System.out.println("Error while flushing/closing fileWriter !!!");
	//
	// e.printStackTrace();
	//
	// }

	// }

	private static List  calcularAmonestadosPorSemana(List idList,Date semana1, Date semana2) {

		// FOR 1rstWEEK
		List amonestacions1;
		List expulsions1;
	
		List amonestacionsList1=null;
		System.out.println("Semana 1: "+semana2.toString());
		System.out.println("Semana 2: "+semana1.toString());

			expulsions1 = new ArrayList<>();
			amonestacions1 = new ArrayList<>();
			
			

			for (int j = 0; j < idList.size(); j++) {
				query = new ReportQuerys();
				amonestacions1.add(query.getWarningCurs(Integer.parseInt(idList.get(j).toString()), semana2, semana1));
				query.closeTransaction();

			}
			amonestacionsList1 = new ArrayList<>();

			for (int i = 0; i < amonestacions1.size(); i++) {
				amonestacionsList1.add(amonestacions1.get(i));
				if	(!amonestacionsList1.get(i).toString().equals(0)){

				System.out.println("amo :"+amonestacionsList1.get(i));
				}
			}
		
		return amonestacionsList1;

	}

	private static List calcularExpulsadosPorSemana(List idList,Date semana1, Date semana2) {

		// FOR 1rstWEEK
		List amonestacions1;
		List expulsions1;
		List expulsionsList1 = null;


			expulsions1 = new ArrayList<>();
			amonestacions1 = new ArrayList<>();
			

			for (int j = 0; j < idList.size(); j++) {
				query = new ReportQuerys();
				expulsions1.add(query.getExpulsionCurs(Integer.parseInt(idList.get(j).toString()), semana2, semana1));
				query.closeTransaction();

			}
			 expulsionsList1 = new ArrayList<>();

			for (int i = 0; i < expulsions1.size(); i++) {
				expulsionsList1.add(expulsions1.get(i));
		
				if	(!expulsionsList1.get(i).toString().equals(0)){
				System.out.println("exp: " +expulsionsList1.get(i));

				}

			}
		
		return expulsionsList1;
	}

	private static ArrayList<Date> readFile() throws ReadOnlyException, ConversionException, IOException {
		FileReader reader;
		String path2 = null;
		File currDir = new File(".");
		String linea = null;
		Date fechaInici = null;
		Date fechaFi = null;
		Date fechafinaltrimestre1 = null;
		Date fechafinaltrimestre2 = null;
		ArrayList<Date> fechas = new ArrayList<Date>();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

		try {
			path2 = currDir.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		File f = new File("/home/katano/Escritorio/settings.txt");

		BufferedReader br = new BufferedReader(new FileReader(f));
		if (br.readLine() == null) {
			System.out.println("No Hay fecha en el documento settings");

		} else {

			reader = new FileReader(f);
			BufferedReader flux = new BufferedReader(reader);

			while ((linea = flux.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(linea, ",");
				while (st.hasMoreTokens()) {

					try {

						fechaInici = formatter.parse(st.nextToken());
						fechaFi = formatter.parse(st.nextToken());
						fechafinaltrimestre1 = formatter.parse(st.nextToken());
						fechafinaltrimestre2 = formatter.parse(st.nextToken());

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					st.nextToken();
					st.nextToken();
					st.nextToken();

				}
				fechas.add(fechaInici);
				fechas.add(fechaFi);
				fechas.add(fechafinaltrimestre1);
				fechas.add(fechafinaltrimestre2);

			}

		}
		return fechas;
	}

}

// }
