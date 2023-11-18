module com.example.new_login_page {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;


    opens com.example.new_login_page to javafx.fxml;
    exports com.example.new_login_page;
}