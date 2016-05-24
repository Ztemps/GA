
package com.example.SendTelegram;

import java.io.File;
import java.util.ResourceBundle;
import java.util.Scanner;

/*******************************************************************************
 * 
 * Gestió d'Amonestacions v1.0
 *
 * Esta obra está sujeta a la licencia
 * Reconocimiento-NoComercial-SinObraDerivada 4.0 Internacional de Creative
 * Commons. Para ver una copia de esta licencia, visite
 * http://creativecommons.org/licenses/by-nc-nd/4.0/.
 * 
 * @author Francisco Javier Casado Moreno - fcasado@elpuig.xeill.net
 * @author Daniel Pérez Palacino - dperez@elpuig.xeill.net
 * @author Gerard Enrique Paulino Decena - gpaulino@elpuig.xeill.net
 * @author Xavier Murcia Gámez - xmurcia@elpuig.xeill.net
 * 
 *******************************************************************************/

/**
 * Esta clase consiste en realizar el envio mediante el sistema de mensajeria Telegram a
 * traves de su cliente instalado en el servidor, utilizando la API oficial de
 * Telegram.
 **/
public class SendTelegram {

	/**Método el cual se comunica con el terminal de nuestro sistema i ejecuta un script creado especificamente
	 * para el uso de dicho metodo.
	 * @param contactName
	 * @param sourceToFile
	 */
	public void sendWarning(String contactName, String sourceToFile) {

		//Carga del script mediante el fichero properties
		ResourceBundle rb = ResourceBundle.getBundle("GA");
		
		//Mensaje que se envia 
		String msg = "Atenció:_El_seu_fill/a_ha_sigut_amonestat_a_l'Institut_Puig_Castellar.__"
				+ "L'hi_adjuntem_un_fitxer_PDF_amb_l'amonestació,_on_podrá_veure_els_motius_mes_detalladament.___"
				+ "Gracies__" + "Att.Institut_Puig_Castellar_____"
				+ "Avís:Aquest_es_un_missatge_auto-generat,_siusplau_no_intenti_posarse_en_contacte_amb_aquest_telefón._____"
				+ "Developed_by:_GA";

		try {
			//Definimos la ruta por defecto 
			String homedir = System.getProperty("user.home");
			File wd = new File(homedir);
			//Ejecutamos el proceso
			Process pwd = Runtime.getRuntime()
					.exec(rb.getString("send_telegram") +" "+ contactName + " " + msg + " " + sourceToFile, null, wd);

		

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}