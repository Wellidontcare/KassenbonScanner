package com.nonnast.kassenbonscanner.core;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;

public class Preprocessing {
    public static class DeskewResult{
        public Mat image;
        public double angle;

        public DeskewResult(Mat rotated, Double best_angle) {
            this.image = rotated;
            this.angle = best_angle;
        }
    }

    public static DeskewResult deskew(Mat image, double angle_start, double angle_end, double angle_step) {
        var required_capacity = (int) ((angle_end - angle_start) / angle_step);

        var row_mean_diff = new ArrayList<Double>(required_capacity);
        var angles = new ArrayList<Double>(required_capacity);

        var image_size = image.size();
        var left_half = new Mat();
        var right_half = new Mat();
        var rotated = new Mat();
        for (double a = angle_start; a < angle_end; a += angle_step) {
            var rot_mat = Imgproc.getRotationMatrix2D(new Point(image_size.width / 2, image_size.height / 2), a, 1);
            Imgproc.warpAffine(image, rotated, rot_mat, image_size);
            Core.reduce(rotated.submat(0, (int) image_size.height, 0, (int) image_size.width / 2), left_half, 1, Core.REDUCE_AVG);
            Core.reduce(rotated.submat(0, (int) image_size.height, (int) image_size.width / 2, (int) image_size.width), right_half, 1, Core.REDUCE_AVG);
            var diff_mat = new Mat();
            Core.absdiff(left_half, right_half, diff_mat);
            row_mean_diff.add(Core.sumElems(diff_mat).val[0]);
            angles.add(a);
        }
        var best_angle = angles.get(row_mean_diff.lastIndexOf(Collections.min(row_mean_diff)));
        var rot_mat = Imgproc.getRotationMatrix2D(new Point(image_size.width / 2, image_size.height / 2), best_angle, 1);
        Imgproc.warpAffine(image, rotated, rot_mat, image_size);
        return new DeskewResult(rotated, best_angle);
    }

    public static Mat segment_text(Mat image, double adaptive_thresh_mean_offset){
        var image_size = image.size();
        var block_size = (int)image_size.width / 30;
        block_size = block_size % 2 == 0 ? block_size + 1 : block_size;
        var binary_image = new Mat();
        Imgproc.adaptiveThreshold(image, binary_image, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, block_size, adaptive_thresh_mean_offset);
        //Imgproc.morphologyEx(binary_image, binary_image, Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3)));
        //Imgproc.morphologyEx(binary_image, binary_image, Imgproc.MORPH_ERODE, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3)));
        return binary_image;
    }

    public static Mat rotate(Mat image, double angle){
        var rotated = new Mat();
        var image_size = image.size();
        var rot_mat = Imgproc.getRotationMatrix2D(new Point(image_size.width / 2, image_size.height / 2), angle, 1);
        Imgproc.warpAffine(image, rotated, rot_mat, image_size);
        return rotated;
    }


}
