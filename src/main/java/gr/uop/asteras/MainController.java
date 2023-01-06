package gr.uop.asteras;

import gr.uop.BibFileFields;
import gr.uop.lucene.LuceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    LuceneController luceneController = new LuceneController();
    private final String[] searchableFields = new String[]{BibFileFields.AUTHOR, BibFileFields.EDITOR, BibFileFields.BOOKTITLE, BibFileFields.TITLE};
    @FXML
    private TextField searchTextField;

    @FXML
    private HBox advancedSettingsBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        var files = new File(LuceneController.INDEX_DIR).listFiles();
        assert files != null;
        if (files.length == 0)
        {
            luceneController.createIndex();
        }

        for (String field: searchableFields)
        {
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(true);
            checkBox.setText(field);

            advancedSettingsBox.getChildren().add(checkBox);
        }
    }
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
        List<String> searchFields = new ArrayList<>();
        int i = 0;

        for (Node check :
                advancedSettingsBox.getChildren())
        {
            CheckBox checkBox = (CheckBox) check;
            if (checkBox.isSelected())
            {
                System.out.println(checkBox.getText());
                searchFields.add(checkBox.getText());
                i++;
            }
        }

        if (i == 0)
            searchFields.addAll(Arrays.stream(searchableFields).toList());

        try
        {
            var list = luceneController.search(searchTextField.getText().trim(), searchFields.toArray(new String[0]));
            System.out.println(list);
            System.out.println(luceneController.getTime());
        }
        catch (IOException | ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void enterPressed(KeyEvent event)
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            search(new ActionEvent());
        }
    }

}