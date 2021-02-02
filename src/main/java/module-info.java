module com.dere.viewerfx {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires org.jfxtras.styles.jmetro;
	requires javafx.base;

    opens com.dere.viewerfx to javafx.fxml;
    exports com.dere.viewerfx;
}