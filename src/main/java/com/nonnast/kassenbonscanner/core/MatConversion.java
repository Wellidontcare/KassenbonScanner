package com.nonnast.kassenbonscanner.core;

import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;


import java.io.ByteArrayInputStream;

public class MatConversion {
    public static Image mat_to_javafx_image(Mat in){
        var bytes = new MatOfByte();
        Imgcodecs.imencode(".tiff", in, bytes);
        return new Image(new ByteArrayInputStream(bytes.toArray()));
    }

    public static java.awt.Image mat_to_buffered_image(Mat in){
        return HighGui.toBufferedImage(in);
    }
}
