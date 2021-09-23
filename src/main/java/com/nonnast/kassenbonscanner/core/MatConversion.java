package com.nonnast.kassenbonscanner.core;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayInputStream;

public class MatConversion {
    static Image mat_to_image(Mat in){
        var bytes = new MatOfByte();
        Imgcodecs.imencode(".tiff", in, bytes);
        return new Image(new ByteArrayInputStream(bytes.toArray()));
    }
}
