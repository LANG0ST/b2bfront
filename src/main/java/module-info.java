module b2bfront {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.fx.b2bfront to javafx.fxml;
    opens org.fx.b2bfront.controller.auth to javafx.fxml;
    opens org.fx.b2bfront.controller.home to javafx.fxml;
    opens org.fx.b2bfront.controller.components to javafx.fxml;
    opens org.fx.b2bfront.controller.categories to javafx.fxml;
    opens org.fx.b2bfront.controller.Admin to javafx.fxml;
    opens org.fx.b2bfront.model to javafx.base, javafx.fxml;




    exports org.fx.b2bfront;
    exports org.fx.b2bfront.controller.auth;
    exports org.fx.b2bfront.controller.home;
    exports org.fx.b2bfront.controller.components;
    exports org.fx.b2bfront.controller.categories;
    exports org.fx.b2bfront.controller.Admin;



}
