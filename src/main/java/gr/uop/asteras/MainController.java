package gr.uop.asteras;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController
{
    @FXML
    public TextField searchTextField;

    @FXML
    void lucky(ActionEvent event)
    {
        System.out.println(searchTextField.getText().replace('a', '2').trim());
    }

    @FXML
    void search(ActionEvent event)
    {
        System.out.println(searchTextField.getText().trim());
    }
}