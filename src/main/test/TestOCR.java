import com.nonnast.kassenbonscanner.core.Preprocessing;
import com.nonnast.kassenbonscanner.core.ProductPriceDetection;
import com.nonnast.kassenbonscanner.core.ReceiptItem;
import com.nonnast.kassenbonscanner.core.ReceiptScanner;
import com.recognition.software.jdeskew.ImageDeskew;
import net.sourceforge.tess4j.TesseractException;
import nu.pattern.OpenCV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashMap;

public class TestOCR {
    static{
        OpenCV.loadLocally();
    }
    @Test
    public void test_ocr(){
        var image = Imgcodecs.imread("./src/main/resources/sample_images/rewe_good.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        //image = Preprocessing.segment_text(image, 30);
        //var result = Preprocessing.deskew(image, -2, 2, 0.1);
        image = image.submat(new Rect(new Point(200, 1100), new Point(1300, 2600)));

        var result = new ArrayList<ReceiptItem>();
        try {
            result = ProductPriceDetection.detect(image, image);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        String[] expected = {"ziegenfrischkaes", "wurzelbrot hell", "ja! eiersalat", "hummus tahini", "feige blau"};

        for(int i = 0; i < expected.length; ++i){
            Assertions.assertEquals(expected[i], result.get(i).name);
        }
    }

    @Test
    public void test_price_string_to_double(){

        String[] in = {"2.5...", " 2.5 ..", "..2 . 5.", "2,5.", ".,2.5,,", "  2.5 ,"};
        for(var price_string : in){
            var res = ProductPriceDetection.price_string_to_double(price_string);
            Assertions.assertEquals(2.5, res);
        }
    }
}
