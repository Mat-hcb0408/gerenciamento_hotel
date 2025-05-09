module org.skywalkerhotel.skywalkerhotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;



    exports org.skywalkerhotel.skywalkerhotel.Model.Entitys;
    opens org.skywalkerhotel.skywalkerhotel.Model.Entitys to javafx.base;
    exports org.skywalkerhotel.skywalkerhotel.Controller;
    opens org.skywalkerhotel.skywalkerhotel.Controller to javafx.fxml;
    exports org.skywalkerhotel.skywalkerhotel.Main;
    opens org.skywalkerhotel.skywalkerhotel.Main to javafx.fxml;
}