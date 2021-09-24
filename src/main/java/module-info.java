module com.nonnast.kassenbonscanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires opencv;
    requires  tess4j;

    opens com.nonnast.kassenbonscanner to javafx.fxml;
    exports com.nonnast.kassenbonscanner;
    exports com.nonnast.kassenbonscanner.core;
}