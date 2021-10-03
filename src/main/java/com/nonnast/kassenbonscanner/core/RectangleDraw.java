package com.nonnast.kassenbonscanner.core;

import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.opencv.core.Point;

public class RectangleDraw implements EventHandler<MouseEvent> {
    public Point first;
    public Point second;
    public boolean is_first = true;
    public boolean selection_set = false;
    private GraphicsContext ctx;
    private Image image;

    public void set_graphics_ctx(GraphicsContext ctx){
        this.ctx = ctx;
    }

    public void set_image(Image image){
        this.image = image;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(is_first){
            first = new Point(mouseEvent.getX(), mouseEvent.getY());
            is_first = false;
            ctx.drawImage(image, 0, 0);
            ctx.setFill(Paint.valueOf("red"));
            ctx.fillOval(first.x, first.y, 10, 10);
        } else {
            second = new Point(mouseEvent.getX(), mouseEvent.getY());
            if(first.y > second.y){
                var temp = this.first;
                this.first = this.second;
                this.second = temp;
            }
            var width =  Math.abs(first.x - second.x);
            var height = Math.abs(first.y - second.y);
            if(first.x > second.x){
                first.x -= width;
                second.x += width;
            }
            is_first = true;
            ctx.drawImage(image, 0, 0);
            ctx.setStroke(Color.rgb(255, 0, 0, 1));
            ctx.setLineWidth(3);
            ctx.strokeRect(first.x, first.y,width, height);
            selection_set = true;
        }
    }
}
