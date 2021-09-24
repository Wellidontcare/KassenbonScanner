package com.nonnast.kassenbonscanner.core;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.*;

public class ProductPriceDetection {
    static private final Tesseract product_tesseract = new Tesseract();
    static private final Tesseract price_tesseract = new Tesseract();


    static {
        product_tesseract.setDatapath("./src/main/resources/tesseract_train_data");
        product_tesseract.setLanguage("deu");
        product_tesseract.setOcrEngineMode(1);
        product_tesseract.setPageSegMode(4);

        price_tesseract.setDatapath("./src/main/resources/tesseract_train_data");
        price_tesseract.setLanguage("deu");
        price_tesseract.setOcrEngineMode(1);
        product_tesseract.setPageSegMode(4);
        price_tesseract.setTessVariable("-c ", "tessedit_char_whitelist=0123456789,.");
    }

    public static double price_string_to_double(String num){
        num = num.replace(",", ".");
        num = num.replace(" ", "");
        num = num.replaceAll("[^1234567890.]", "");

        while(num.startsWith(".")){
            num = num.substring(1);
        }
        while(num.endsWith(".")){
            num = num.substring(0, num.length()-1);
        }
        var result = 0.0;
        try{
            result = Double.parseDouble(num);
            return result;
        } catch (NumberFormatException e){
            return result;
        }
    }

    public static ArrayList<ReceiptItem> detect(Mat product_image, Mat price_image) throws TesseractException {
        //product_tesseract.setConfigs(new List<String>{""});
        String product_text = product_tesseract.doOCR((BufferedImage) MatConversion.mat_to_buffered_image(product_image));
        String price_text = price_tesseract.doOCR((BufferedImage) MatConversion.mat_to_buffered_image(price_image));

        var products = product_text.split("\n");
        var prices = price_text.split("\n");
        var items = new ArrayList<ReceiptItem>();
        try {
            for (int i = 0, j = 0; i < products.length; i++, j++) {
                var current_product = products[i].toLowerCase(Locale.ROOT);
                if (current_product.contains("stk") && current_product.contains("x")) {
                    j--;
                    continue;
                }
                items.add(new ReceiptItem(current_product.replaceAll("\\d", ""), price_string_to_double(prices[j])));
            }
        } catch(IndexOutOfBoundsException e){
            return items;
        }
        return items;
    }
}
