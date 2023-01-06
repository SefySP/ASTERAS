package gr.uop.asteras;

import gr.uop.lucene.LuceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

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
        String searchQuery = searchTextField.getText().trim();
        System.out.println(searchQuery);
        LuceneController controller = new LuceneController();
        try {
            controller.search(searchTextField.getText().trim(), new String[]{"title"});
        } catch (IOException | ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}