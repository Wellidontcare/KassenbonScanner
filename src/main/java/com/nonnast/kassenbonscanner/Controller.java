package com.nonnast.kassenbonscanner;

import com.nonnast.kassenbonscanner.core.ReceiptItem;
import com.nonnast.kassenbonscanner.core.ReceiptScanner;
import com.nonnast.kassenbonscanner.core.RectangleDraw;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.TesseractException;

import java.util.ArrayList;

public class Controller {
    private Stage root_stage;
    private final ReceiptScanner scanner = new ReceiptScanner();
    private final RectangleDraw rect_draw = new RectangleDraw();
    private final ObservableList<ReceiptItem> item_store = FXCollections.observableArrayList();

    public void set_root_stage(Stage root){
        this.root_stage = root;
    }

    public void register_callbacks(){
        table_view.setEditable(true);
        rect_draw.set_graphics_ctx(image_canvas.getGraphicsContext2D());
        image_canvas.setOnMouseClicked(rect_draw);
        product_view.setCellValueFactory(product -> product.getValue().name_property());
        price_view.setCellValueFactory(price -> price.getValue().price_property());
        table_view.setItems(item_store);
    }


    @FXML
    private TableView<ReceiptItem> table_view;

    @FXML
    private TableColumn<ReceiptItem, String> product_view;

    @FXML
    private TableColumn<ReceiptItem, String> price_view;

    @FXML
    private TableColumn<ReceiptItem, String> date_view;

    @FXML
    private Canvas image_canvas;

    @FXML
    private Slider thresh_slider;

    @FXML
    private Button skew_button;

    @FXML
    private Button scan_button;

    @FXML
    private void on_load_image(){
        var file_chooser = new FileChooser();
        var file = file_chooser.showOpenDialog(root_stage);
        var ctx = image_canvas.getGraphicsContext2D();
        var image = scanner.load_image(file.getAbsolutePath());
        image_canvas.setDisable(false);
        ctx.drawImage(image, 0, 0);
        thresh_slider.setDisable(false);
        skew_button.setDisable(false);
        scan_button.setDisable(false);
        rect_draw.set_image(image);
    }

    @FXML
    private void on_correct_skew(){
        var ctx = image_canvas.getGraphicsContext2D();
        var skew_corrected = scanner.correct_skew_preview();
        rect_draw.set_image(skew_corrected);
        ctx.drawImage(skew_corrected, 0, 0);
    };

    @FXML
    private void on_thresh_changed(){
        var thresh = thresh_slider.getValue();
        var processed = scanner.preprocess_image_preview(thresh);
        var ctx = image_canvas.getGraphicsContext2D();
        rect_draw.set_image(processed);
        ctx.drawImage(processed, 0, 0);
    };

    @FXML
    private void on_scan_clicked(){
        if(!rect_draw.selection_set){
            var alert = new Alert(Alert.AlertType.ERROR, "Bitte erst Scanbereich selektieren", ButtonType.OK);
            alert.showAndWait();
        } else {
            var rect = scanner.get_image_rect(rect_draw.first.x, rect_draw.first.y, rect_draw.second.x, rect_draw.second.y);
            var product_rect = scanner.get_product_rect(rect);
            var price_rect = scanner.get_price_rect(rect);
            var items = new ArrayList<ReceiptItem>();
            try {
                items = scanner.read_receipt(product_rect, price_rect);
            } catch (TesseractException e) {
                var alert = new Alert(Alert.AlertType.ERROR, "Tesseract konnte dieses Bild nicht verarbeiten!", ButtonType.OK);
                alert.showAndWait();
            }
            item_store.addAll(items);
        }
    }

    @FXML
    private void on_reset_list_clicked(){
        item_store.clear();
    }
}
