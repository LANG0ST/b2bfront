module b2bfront {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires okhttp3;
    requires static lombok;
    requires java.net.http;

    opens org.fx.b2bfront to javafx.fxml;
    opens org.fx.b2bfront.controller.auth to javafx.fxml;
    opens org.fx.b2bfront.controller.search to javafx.fxml;
    opens org.fx.b2bfront.controller.home to javafx.fxml;
    opens org.fx.b2bfront.controller.components to javafx.fxml;
    opens org.fx.b2bfront.controller.categories to javafx.fxml;
    opens org.fx.b2bfront.controller.product to javafx.fxml;
    opens org.fx.b2bfront.controller.dashboard to javafx.fxml;
    opens org.fx.b2bfront.controller.cart to javafx.fxml;
    opens org.fx.b2bfront.controller.reviews to javafx.fxml;
    opens org.fx.b2bfront.controller.Admin to javafx.fxml;
    opens org.fx.b2bfront.controller.checkout to javafx.fxml;
    opens org.fx.b2bfront.model to javafx.base, javafx.fxml,com.google.gson;
    opens org.fx.b2bfront.dto to com.google.gson;




    exports org.fx.b2bfront;
    exports org.fx.b2bfront.controller.search   ;
    exports org.fx.b2bfront.controller.auth;
    exports org.fx.b2bfront.controller.home;
    exports org.fx.b2bfront.controller.components;
    exports org.fx.b2bfront.controller.categories;
    exports org.fx.b2bfront.controller.product;
    exports org.fx.b2bfront.controller.cart;
    exports org.fx.b2bfront.controller.reviews;
    exports org.fx.b2bfront.controller.Admin;
    exports org.fx.b2bfront.controller.checkout;



}
