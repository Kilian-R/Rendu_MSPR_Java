import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class main {
	public static void main(String[] args) {
		// On initialise le path du folder vers lequel on veut que l'application soit générée
		String folder = "C:\\Users\\Anakin\\Desktop\\MSPR\\";
		try {
			// On crée les dossiers agent et mspr s'ils n'existent pas
			Files.createDirectories(Paths.get(folder + "mspr\\agent\\"));

			// On crée la base du HTML pour le fichier index.html
			String output = "<html lang='en'><head><meta charset='utf8'><link rel='stylesheet' href='app.css'><title>MSPR Java</title></head><body>";

			// On ajoute un titre au fichier index.html
			output += "<h1 class='title center'>Liste des agents</h1><div class='container'>";

			// On crée un scanner sur la liste des agents (staff.txt)
			File myObj = new File(folder + "staff.txt");
			Scanner myReader = new Scanner(myObj);

			// On lit les informations du fichier staff.txt, on met ces valeurs dans une liste
			List<String> agents = new ArrayList<String>();
			while (myReader.hasNextLine()) {
				agents.add(myReader.nextLine());
			}

			// Tri de la liste dans l'ordre alphabétique
			Collections.sort(agents);

			// Récupération de l'obet Enumeration qui sera utilisé pour parcourir la liste agents
			Enumeration enumeration = Collections.enumeration(agents);

			// Tant qu'on a pas lu l'entièreté du fichier
			while (enumeration.hasMoreElements()) {
				// On lit la prochaine valeur de la liste agents
				Object data = enumeration.nextElement();

				// On crée la base du HTML pour les fichiers nomagent.html
				String agentHtml = "<html lang='en'><head><meta charset='utf8'><link rel='stylesheet' href='../app.css'><title>Agent " + data + "</title></head><body>";

				// On crée un scanner sur chaque fiche agent
				File agent = new File(folder + "agent\\" + data + ".txt");
				Scanner readAgent = new Scanner(agent);

				// On lit les informations du fichier nomagent.txt
				String nom = readAgent.nextLine();
				String prenom = readAgent.nextLine();
				String mission = readAgent.nextLine();
				String mdpHtpasswd = readAgent.nextLine();
				readAgent.nextLine();
				List<String> materiel = new ArrayList<String>();
				while (readAgent.hasNextLine()) {
					materiel.add(readAgent.nextLine());
				}

				// On crée le bouton HTML qui permettra d'accéder aux détails de chaque agent
				output += "<div class='mt'><a href='agent/" + data + ".html'>" + data + "</a></div>";

				// On convertit la Carte Nationale d'Identité (CNI) de chaque agent en Base64
				String b64Img = LoadAndConvertImg(folder + "agent-ci\\" + data + ".JPG");

				// On ajoute un titre à la fiche de chaque agent avec leur nom
				agentHtml += "<h1 class='title center'>Fiche agent " + data + "</h1>";

				agentHtml += "<div class='row'>";

				// On ajoute une balise image contenant des datas en Base 64 (CNI)
				agentHtml += "<div class='column center left'>";
				agentHtml += "<img src='data:image/jpeg;base64, " + b64Img + "' />";
				agentHtml += "</div>";

				// On crée le tableau en HTML pour afficher les détails de chaque agent.
				agentHtml += "<div class='column center right'>";
				agentHtml += "<table><thead><tr><th>Nom</th><th>Prénom</th><th>Mission</th><th>Matériels</th></tr></thead><tbody>";
				agentHtml += "<tr><td>" + nom + "</td>";
				agentHtml += "<td>" + prenom + "</td>";
				agentHtml += "<td>" + mission + "</td>";
				agentHtml += "<td>" + materiel.toString() + "</td></tr>";
				agentHtml += "</tbody></table>";
				agentHtml += "</div>";

				// On ajoute un bouton retour pour revenir a la page principale
				agentHtml += "<div><a href='../index.html'><- Retour</a></div>";

				WriteToFile(folder + "mspr\\agent\\" + data + ".html", agentHtml + "</body></html>");

				readAgent.close();
				}
				myReader.close();
				WriteToFile(folder + "mspr\\index.html", output + "</div></body></html>");

				// Ci-dessous le CSS de l'application
				String css = ".title{font-size:200%;font-weight:700}.center{text-align:center}.row{display:flex;justify-content:center;align-items:center}.row:after{content:\"\";display:table;clear:both}.column{float:left;padding:10px}.left{width:25%}.right{width:60%}table{font-family:Arial,Helvetica,sans-serif;border-collapse:collapse;width:100%}table td,table th{border:1px solid #000000;padding:8px}table tr:nth-child(even){background-color:#f2f2f2}table tr:hover{background-color:#ddd}table th{padding-top:12px;padding-bottom:12px;text-align:left;background-color:#659224;color:#fff}.container{margin:5vh}a{display:block;width:115px;height:25px;background:#379EC1;padding:10px;text-align:center;border-radius:5px;color:#fff;font-weight:700;line-height:25px}.mt{margin-top:1vh}";
				WriteToFile(folder + "mspr\\app.css", css);
			} catch(Exception e){
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}

		// Fonction pour convertir une image en Base 64
		public static String LoadAndConvertImg (String imgPath){
			String b64Img = "";

			File file = new File(imgPath);
			try {
				FileInputStream imageInFile = new FileInputStream(file);
				// Reading a Image file from file system
				byte imageData[] = new byte[(int) file.length()];
				imageInFile.read(imageData);
				b64Img = Base64.getEncoder().encodeToString(imageData);
			} catch (FileNotFoundException e) {
				System.out.println("Image not found" + e);
			} catch (IOException ioe) {
				System.out.println("Exception while reading the Image " + ioe);
			}

			return b64Img;
		}

		// Fonction pour écrire des données dans un fichier
		public static void WriteToFile (String filePath, String data){
			try {
				File file = new File(filePath);
				Files.deleteIfExists(file.toPath());
				FileWriter fileWriter = new FileWriter(file);
				fileWriter.write(data);
				fileWriter.close();
			} catch (Exception e) {
				System.out.println("An error occurred wile creatine file.");
				e.printStackTrace();
			}
		}
	}
