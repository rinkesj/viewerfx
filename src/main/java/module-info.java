module com.dere.viewerfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.dere.viewerfx to javafx.fxml;
    exports com.dere.viewerfx;
}