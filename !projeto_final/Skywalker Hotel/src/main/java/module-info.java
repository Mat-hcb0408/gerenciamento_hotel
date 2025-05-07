module org.skywalkerhotel.skywalkerhotel {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;


    exports org.skywalkerhotel.skywalkerhotel.Controller;
    opens org.skywalkerhotel.skywalkerhotel.Controller to javafx.fxml;
    exports org.skywalkerhotel.skywalkerhotel.Main;
    opens org.skywalkerhotel.skywalkerhotel.Main to javafx.fxml;
}