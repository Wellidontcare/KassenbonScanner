package com.nonnast.kassenbonscanner.core;

import javafx.scene.image.Image;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class ReceiptScanner {
    static{
        OpenCV.loadLocally();
    }
    private Mat loaded_image_original;
    private Mat processed;
    private final Mat proxy = new Mat();

    private double adaptive_thresh_const = 0;
    private double skew_angle = 0;

    public Image load_image(String filename){
        adaptive_thresh_const = 0;
        loaded_image_original = Imgcodecs.imread(filename, Imgcodecs.IMREAD_GRAYSCALE);
        processed = loaded_image_original.clone();
        Imgproc.resize(loaded_image_original, proxy, new Size(400, 600));
        return MatConversion.mat_to_javafx_image(proxy);
    }

    public ArrayList<ReceiptItem> read_receipt(Rect product_roi, Rect price_roi) throws TesseractException {
        var processed_image = new Mat();

        if(adaptive_thresh_const != 0){
            processed_image = Preprocessing.segment_text(loaded_image_original, adaptive_thresh_const);
        } else {
            processed_image = loaded_image_original.clone();
        }
        if(skew_angle != 0){
            processed_image = Preprocessing.rotate(processed_image, skew_angle);
        }
        return ProductPriceDetection.detect(processed_image.submat(product_roi), processed_image.submat(price_roi));
    }

    public Image preprocess_image_preview(double adaptive_thresh_mean_offset){
        adaptive_thresh_const = adaptive_thresh_mean_offset;
        if(adaptive_thresh_const == 0){
            Imgproc.resize(loaded_image_original, proxy, new Size(400, 600));
            return MatConversion.mat_to_javafx_image(proxy);
        }
        return MatConversion.mat_to_javafx_image(Preprocessing.segment_text(proxy, adaptive_thresh_mean_offset));
    }

    public Image correct_skew_preview(){
        var processed_image = new Mat();
        if(adaptive_thresh_const != 0){
            processed_image = Preprocessing.segment_text(loaded_image_original, adaptive_thresh_const);
        } else {
            processed_image = loaded_image_original.clone();
        }
        var correction_result = Preprocessing.deskew(processed_image, -10, 10, 0.5);
        Imgproc.resize(correction_result.image, proxy, new Size(400, 600));
        skew_angle = correction_result.angle;
        System.out.printf("Skew angle: %f\n", skew_angle);
        return MatConversion.mat_to_javafx_image(proxy);
    }

    public Rect get_image_rect(double x1, double y1, double x2, double y2){
        var width = 400;
        var height = 600;
        var image_size = loaded_image_original.size();
        var width_ratio = image_size.width / width;
        var height_ratio = image_size.height / height;
        x1 = x1 * width_ratio;
        x2 = x2 * width_ratio;
        y1 = y1 * height_ratio;
        y2 = y2 * height_ratio;
        return new Rect(new Point(x1, y1), new Point(x2, y2));
    }

    public Rect get_product_rect(Rect image_rect){
        var product_rect = image_rect.clone();
        product_rect.width = (int)(product_rect.width*0.66);
        return product_rect;
    }

    public Rect get_price_rect(Rect image_rect){
        var price_rect = image_rect.clone();
        price_rect.x = price_rect.x + (int)(price_rect.width*0.66);
        price_rect.width = price_rect.width - (int)(price_rect.width*0.66);
        return price_rect;
    }

    public void display_cutout(Rect rect){
        HighGui.imshow("Cutout", loaded_image_original.submat(rect));
        HighGui.waitKey();
        HighGui.destroyAllWindows();
    }



}
