package projet_grp8.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import projet_grp8.methode.*;
import projet_grp8.util.*;

/**
 * Class de test pour tout les méthodes
 * @author willy
 *
 */
public class Main {
	
	public static void main (String[] args) throws IOException  {

		File file1 = new File("Bdd" + File.separator+"3.jpg"); 				//
		File file2 = new File("Bdd" + File.separator+"8.jpg"); 				//
		File file3 = new File("Bdd" + File.separator+"eescalier.jpg"); 		//TTT
		File file4 = new File("Bdd" + File.separator+"escalier_11.jpg"); 	//
		File file5 = new File("Bdd" + File.separator+"escalier_12.jpg");	//
		File file6 = new File("Bdd" + File.separator+"escalier212.jpg");	//TTT OK
		File file7 = new File("Bdd" + File.separator+"EscalierLeo.jpg"); 	//TTT
		File file8 = new File("Bdd" + File.separator+"escalierPerso.jpg"); 	//TTT OK
		File file9 = new File("Bdd" + File.separator+"Imag1.jpg"); 			//
		File file10 = new File("Bdd" + File.separator+"imag5.jpg"); 		//
		File file11 = new File("Bdd" + File.separator+"nuloom.jpg"); 		//
		File file12 = new File("Bdd" + File.separator+"escalier31.jpg"); 	//TTT
		
		
		BufferedImage img = null;
		
		// creation de class pour utiliser les methodes disponible
		ImgController ic = new ImgController();
		MedianFilter mf = new MedianFilter();
		Otsu o = new Otsu();
		Sobel sb = new Sobel();
		ErosionDilatation ed = new ErosionDilatation();
		Egalisation eg = new Egalisation();
		
		
		try {
			img = ImageIO.read(file11);
			Imshow.imshow(img);
		} catch (IOException e1) {
			System.err.println("Erreur de lecture de fichier");
		}		
//		Imshow.imshow(img);
		
		//test 1.0 : gris > otsu > seuil > median
		BufferedImage img1 = ic.gris(img);
//		Imshow.imshow(img1);
		BufferedImage bj = mf.median(img1);
//		Imshow.imshow(bj);
		float seuil = o.otsu(img);
		System.out.println("otsu seuil : "+seuil);
		float seuila = seuil-25;
		img1 = ic.seuillage(img, seuila); //image en NB avec un seuil auto grace a otsu
		bj = mf.median(img1);  //supprime les bruits avec median 3
//		Imshow.imshow(bj);
		bj = ed.close(bj, 3); // rayon de n-pixels
//		Imshow.imshow(bj);
		BufferedImage bjMedian = mf.median(bj);
//		Imshow.imshow(bjMedian);
		
		//test 1.1 : sobel > border > median > fusion
		BufferedImage imgSobel = sb.sobel(img); // application de sobel sur l'image original
//		Imshow.imshow(imgSobel);
		imgSobel = mf.median(imgSobel);
//		Imshow.imshow(imgSobel);
		float seuilb = seuil;
		seuilb -= (seuil*0.65);
		System.out.println("seuilb vaut "+seuilb+".");
		imgSobel = ic.seuillage(imgSobel, seuilb);
//		Imshow.imshow(imgSobel);
		BufferedImage imgFinal = ic.fusionImgEtSobel(bjMedian, imgSobel, (int)seuil-20); //applique les contours de sobel sur l'image bruite
		imgFinal = mf.median(imgFinal);
//		Imshow.imshow(imgFinal);

		/**
		 * Generique [ok] : test qui fonctionne ne pas toucher, 
		 * connexite pour compter le nombre d'objet (marche) noir sur l'image
		 * 
		 * affichage du resultat
		 */
		imgFinal = ic.inverseBinary(imgFinal); //change les marches "Noir" en "Blanc"
		BufferedImage cc = Label8.getCC(imgFinal);
		Imshow.imshow(cc);
		int nbColor = Label8.getNumberOfCC(cc);
		System.out.println("Nombre de marche avec label8 : "+nbColor);
	}
}
