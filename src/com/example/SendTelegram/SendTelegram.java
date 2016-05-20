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

	}
	
	public void sendWarning(String contactName,String sourceToFile) {
		
		String msg = "Atenció:_El_seu_fill/a_ha_sigut_amonestat_a_l'Institut_Puig_Castellar.__"
				+ "L'hi_adjuntem_un_fitxer_PDF_amb_l'amonestació,_on_podrá_veure_els_motius_mes_detalladament.___"
				+ "Gracies__" + "Att.Institut_Puig_Castellar_____"
				+ "Avís:Aquest_es_un_missatge_auto-generat,_siusplau_no_intenti_posarse_en_contacte_amb_aquest_telefón._____"
				+ "Developed_by:_GA";
		
//		String msg = "pruebas";
		
		System.out.println("SendTelegram"+sourceToFile);

		String command = "/cd";
		try {
			String homedir = System.getProperty("user.home");
			File wd = new File(homedir);
			Process pwd = Runtime.getRuntime().exec("./sendWarning.sh " + contactName + " "+msg+" "+sourceToFile, null, wd);

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