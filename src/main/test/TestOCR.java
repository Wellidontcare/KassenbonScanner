import com.nonnast.kassenbonscanner.core.Preprocessing;
import com.nonnast.kassenbonscanner.core.ProductPriceDetection;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.HashMap;

public class TestOCR {
    static{
        OpenCV.loadLocally();
    }
    @Test
    public void test_ocr(){
        var image = Imgcodecs.imread("./src/main/resources/sample_images/rewe_good.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        //submat(1182, 1182+1415, 333, (int)((333+2061)*0.66)),
        image = Preprocessing.segment_text(image, 60);
        //var result = Preprocessing.deskew(image, -2, 2, 0.1);
        //image = result.image;

        var result = new HashMap<String, Double>();
        try {
            result = ProductPriceDetection.detect(image, image);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        System.out.print(result.toString());
    }

    @Test
    public void test_price_string_to_double(){

        String[] in = {"2.5...", " 2.5 ..", "..2 . 5.", "2,5.", ".,2.5,,", "  2 . 5 ,"};
        for(var price_string : in){
            var res = ProductPriceDetection.price_string_to_double(price_string);
            Assertions.assertEquals(res, 2.5);
        }
    }
}
