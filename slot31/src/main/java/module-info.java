module fu.edu.sba301.ui {
    requires javafx.controls;
    requires javafx.fxml;
    requires chapter18.hibernate.crud;
    requires org.hibernate.orm.core;
    requires java.sql;

    opens fu.edu.sba301.ui.controller to javafx.fxml;
    opens fu.edu.sba301.ui to javafx.graphics;
    exports fu.edu.sba301.ui;
}
