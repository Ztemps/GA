package com.example.SendTelegram;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.example.Entities.Student;
import com.example.Logic.EntityManagerUtil;

public class SendTelegram {

	public void main(String[] args) throws InterruptedException, IOException {

		// addContactList();
//		String contactName = "Xavi_Murcia";
//		String sourceToFile = "/home/javi/Escritorio/amonestacio.pdf";

//		sendmsg(contactName);
//		sendFile(contactName, sourceToFile);

	}

	public void addContactList() {
		EntityManagerUtil entman = new EntityManagerUtil();
		EntityManager em = entman.getEntityManager();
		Query query = null;

		em.getTransaction().begin();

		// A l'hora de carregar els pares desde la db hem de tenir en compte
		// diferents punts:
		// Si el pare/mare no te telegram instalat encara que l'afegim a la
		// llista de contactes no apareixera.
		// Telegram nomes accepta un contacte en el següent format <phone>
		// <firstname> <last_name> per tant,
		// qualsevol espai ens donará error i no intrdouirá el contacte.
		String consulta = "SELECT nom,cognoms,telefon_pares FROM alumne LIMIT 10";
		List<Object[]> results = em.createNativeQuery(consulta).getResultList();
		em.getTransaction().commit();

		List dades = new ArrayList<>();
		results.stream().forEach((record) -> {
			String firstName = (String) record[0];
			String lastName = (String) record[1];
			String telf = (String) record[2];

			dades.add(record[0].toString().replaceAll("\\s+", "") + " " + record[1].toString().replaceAll("\\s+", "")
					+ record[2]);
		});

		// En el caso de que se quieran introducir los padres
		String nomContacte = null;
		String telfContacte = null;
		String msg = null;
		List contacteAlumne = new ArrayList<>();
		for (int i = 0; i < dades.size(); i++) {

			// NOM COGNOM alumne
			nomContacte = dades.get(i).toString().replaceFirst("[(].*?[)]", "").replaceAll("[1234567890+-]", "")
					.replace(")", "");

			// TELEFON pares
			telfContacte = String.valueOf(Integer.parseInt(dades.get(i).toString().replaceFirst("[(].*?[)]", "")
					.replaceAll("\\+34-", "").replaceAll("[\\D]", "")));

			contacteAlumne.add(nomContacte + telfContacte);
			System.out.println(contacteAlumne.get(i));

		}

		// telfs pruebas:
		// 673492265 Gerard Paulino
		// 626793580 Xavi Murcia
		// 656879526 Pedrito

		List contactProba = new ArrayList<>();

		contactProba.add("673492265 Gerard Paulino");
		contactProba.add("626793580 Xavi Murcia");

		String command = "/cd";
		try {
			String homedir = System.getProperty("user.home");
			File wd = new File(homedir);

			for (int i = 0; i < contactProba.size(); i++) {

				Process pwd = Runtime.getRuntime().exec("./addContactsList.sh " + contactProba.get(i), null, wd);
				Scanner scanner = new Scanner(pwd.getInputStream());
				while (scanner.hasNextLine()) {
					System.out.println(scanner.nextLine());
				}
				scanner.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void sendmsg(String contactName) {
		
		String msg = "Atenció:_El_seu_fill/a_ha_sigut_amonestat_a_l'Institut_Puig_Castellar.__"
				+ "L'hi_adjuntem_un_fitxer_PDF_amb_l'amonestació,_on_podrá_veure_els_motius_mes_detalladament.___"
				+ "Gracies__" + "Att.Institut_Puig_Castellar_____"
				+ "Avís:Aquest_es_un_missatge_auto-generat,_siusplau_no_intenti_posarse_en_contacte_amb_aquest_telefón._____"
				+ "Developed_by:_GA_4_estudiantes_y_1_gato";
		
		String command = "/cd";
		try {
			String homedir = System.getProperty("user.home");
			File wd = new File(homedir);
			Process pwd = Runtime.getRuntime().exec("./sendMessage.sh " + contactName + " " + msg, null, wd);

			Scanner scanner = new Scanner(pwd.getInputStream());
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
			}
			scanner.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void sendFile(String contactName, String sourceToFile) {
		String command = "/cd";
		try {
			String homedir = System.getProperty("user.home");
			File wd = new File(homedir);
			System.out.println("SendTelegram"+sourceToFile);
			Process pwd = Runtime.getRuntime().exec("./sendFile.sh " + contactName + " "+sourceToFile, null, wd);

			Scanner scanner = new Scanner(pwd.getInputStream());
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
			}
			scanner.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}