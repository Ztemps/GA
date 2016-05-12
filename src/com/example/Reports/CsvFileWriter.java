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
	static public ReportQuerys query ;
	public Query queryDate = null;
	static ArrayList<Date> dates = new ArrayList<Date>();
	// CSV file header
	private static final String FILE_HEADER = "ALUMNE,A,E";

	
	
	public static void main(String[] args) {
		
		
		calcularPrimerTrimestre();
	}
	
	
	
	public static void calcularPrimerTrimestre(){
		
	

	// Delimiter used in CSV file
	
		FileWriter fileWriter = null;

		// Create new students objects
		query = new ReportQuerys();
		String grupCurs = "ESO 1A";
		List noms = query.getNomAlumnes(grupCurs);
		query.closeTransaction();
		query = new ReportQuerys();
		List cognoms = query.getCognomsAlumnes(grupCurs);
		query.closeTransaction();


		// FOR NOMS
		List nomsList = new ArrayList<>();

		for (int i = 0; i < noms.size(); i++) {
			nomsList.add(noms.get(i));

			System.out.println(noms.get(i));

		}

		// FOR COGNOMS
		List cognomsList = new ArrayList<>();

		for (int i = 0; i < cognoms.size(); i++) {
			cognomsList.add(cognoms.get(i));

			System.out.println(cognomsList.get(i));

		}

		try {
			fileWriter = new FileWriter("/home/katano/Escritorio/alumnes.xls");
			query = new ReportQuerys();
			String dateCurs = query.getDateCurs();
			query.closeTransaction();

			fileWriter.append("1r Trimestre   Any: "+dateCurs);
			fileWriter.append(COMMA_DELIMITER);
			fileWriter.append(NEW_LINE_SEPARATOR);
			fileWriter.append(NEW_LINE_SEPARATOR);

			
			// CONSULTA habrá que preguntarlo al usuario con un checkbox
			String grup = "1r A";
			fileWriter.append(grup);
			fileWriter.append(COMMA_DELIMITER);

			//CONSULTA
			String setmana = "Set. ";

			//VARIABLE A COJER
			dates=readFile();
			
			
			Date diaInici = dates.get(0);
			Date diaFinal = dates.get(1);
			Calendar diaIniciCal = Calendar.getInstance();
			diaIniciCal.setTime(diaInici);
			
			
			
			//CALCULAR
			Date diaFinalTrimestre1 = dates.get(2);
			Date diaFinalTrimestre2 = dates.get(3);

			long diff = diaFinalTrimestre2.getTime() - diaFinalTrimestre1.getTime();
			long numSetmanes = (diff / (24 * 60 * 60 * 1000))/7;
			System.out.print(numSetmanes + " setmanes, ");

			
			for (int i = 1;i<=numSetmanes;i++){
				setmana.equals(numSetmanes+i);
				fileWriter.append(setmana+i);
				fileWriter.append(COMMA_DELIMITER);
				//CONSULTA mejorar con date y la data incii y final que introduzca el admin
				diaIniciCal.add(Calendar.DATE, 7);
				Date diaInicial=diaIniciCal.getTime(); 
				String fechainicialBuena = ConverterDates.converterDate(diaInicial);
				
				
				fileWriter.append(fechainicialBuena);
			//	diaInici=aux;
				fileWriter.append(COMMA_DELIMITER);
			
				
			}
			fileWriter.append(NEW_LINE_SEPARATOR);

			// Headers
			fileWriter.append("ALUMNE");
			fileWriter.append(COMMA_DELIMITER);

			for (int i = 0; i<numSetmanes;i++){
				fileWriter.append("A");
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append("E");
				fileWriter.append(COMMA_DELIMITER);
				
				
			}
			

//			fileWriter.append(COMMA_DELIMITER);

			fileWriter.append(NEW_LINE_SEPARATOR);

			for (int i = 0; i < nomsList.size(); i++) {
				fileWriter.append(nomsList.get(i).toString() + " " + cognomsList.get(i).toString());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(NEW_LINE_SEPARATOR);

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

	
	
	
	private static ArrayList<Date> readFile() throws ReadOnlyException, ConversionException, IOException {
		FileReader reader;
		String path2 = null;
		File currDir = new File(".");
		String linea = null;
		Date fechaInici=null;
		Date fechaFi=null;
		Date fechafinaltrimestre1=null;
		Date fechafinaltrimestre2=null;
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
			
			}
			else{
				
			
			
			reader = new FileReader(f);
			BufferedReader flux = new BufferedReader(reader);

			while ((linea = flux.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(linea, ",");
				while (st.hasMoreTokens()) {

					try {
						
						fechaInici =formatter.parse(st.nextToken());
						fechaFi =formatter.parse(st.nextToken());
						fechafinaltrimestre1 =formatter.parse(st.nextToken());
						fechafinaltrimestre2 =formatter.parse(st.nextToken());


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
