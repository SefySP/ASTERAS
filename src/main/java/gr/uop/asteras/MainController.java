package gr.uop.asteras;

import gr.uop.BibFileFields;
import gr.uop.lucene.LuceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;

public class MainController
{
    private String[] searchableFields = new String[]{BibFileFields.AUTHOR, BibFileFields.BOOKTITLE, BibFileFields.TITLE};
    @FXML
    public TextField searchTextField;

    @FXML
    void lucky(ActionEvent event)
    {
        System.out.println(searchTextField.getText().trim());
    }

    @FXML
    void search(ActionEvent event)
    {
        String searchQuery = searchTextField.getText().trim();
        System.out.println(searchQuery);
        LuceneController controller = new LuceneController();
        try
        {
            controller.search(searchTextField.getText().trim(), searchableFields);
        }
        catch (IOException | ParseException e)
        {
            throw new RuntimeException(e);
        }
    }
}