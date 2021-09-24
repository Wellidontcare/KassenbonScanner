import com.nonnast.kassenbonscanner.core.Preprocessing;
import nu.pattern.OpenCV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opencv.imgcodecs.Imgcodecs;

public class TestPreprocessing {
    static{
        OpenCV.loadLocally();
    }
    @Test
    public void test_skew_correction(){
    var image_skewed = Imgcodecs.imread("./src/main/resources/sample_images/rewe_skewed.jpg", Imgcodecs.IMREAD_GRAYSCALE);
    var deskew_result = Preprocessing.deskew(image_skewed, -10, 10, 0.5);
    var best_angle = deskew_result.angle;
    Assertions.assertEquals(best_angle, 7.0);

    //Imgproc.resize(image_unskewed, image_unskewed, new Size(0, 0), 0.2, 0.2);
    //HighGui.namedWindow("Unskewed", HighGui.WINDOW_NORMAL);
    //HighGui.imshow("Unskewed", image_unskewed);
    //HighGui.imshow("Skewed", image_skewed);
    //HighGui.waitKey();
    //HighGui.destroyAllWindows();
    }

    @Test
    public void test_text_segmentation(){
        var image_to_segment = Imgcodecs.imread("./src/main/resources/sample_images/");
    }
}
