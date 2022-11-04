module gymmanager.gymproject3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens gymmanager.gymproject3 to javafx.fxml;
    exports gymmanager.gymproject3;
}