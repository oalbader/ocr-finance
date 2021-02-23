package io.oalbader.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.regex.Pattern;

//@SpringBootApplication
public class OcrApplication {

	public static void main(String[] args) throws TesseractException, IOException {
//		SpringApplication.run(OcrApplication.class, args);
		org.apache.log4j.PropertyConfigurator.configure("src/main/resources/log4j.properties"); // sets properties file for log4j
		File image = new File("src/main/resources/images/test.pdf");
		PDDocument document = PDDocument.load(image);
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		Tesseract tesseract = new Tesseract();
		tesseract.setLanguage("eng");
		tesseract.setPageSegMode(4);
		tesseract.setOcrEngineMode(1);
		Integer[] pages = {3, 4, 5};

		for (Integer page: pages) {
			BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
			String result = tesseract.doOCR(bim);
			result = result.replaceAll("(?m)^[ \t]*\r?\n", "");
			result = result.replace(",", "");
			String[] splitLines = result.split("[\\r\\n]+");

			for (String line : splitLines) {
				if (line.toLowerCase().contains("loan")) {
					System.out.println(line);
					String[] numbers = line.split("(?<=\\D)(?=\\d)");
					for (int i=0; i<numbers.length; i++) {
						numbers[i] = numbers[i].trim();
					}
					if (Integer.parseInt(numbers[1]) <= 30) {
						System.out.println(numbers[2]);
					} else {
						System.out.println(numbers[1]);
					}
				}
			}
		}


	}

}
