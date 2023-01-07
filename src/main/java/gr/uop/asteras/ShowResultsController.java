package gr.uop.asteras;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class ShowResultsController implements Initializable
{
	@FXML
	private ListView<Hyperlink> listViewResult;

	private List<File> resultList;

	public void setResultList(List<File> resultList)
	{
		this.resultList = resultList;
	}

	public void setHyperLinkListViewResult()
	{
		listViewResult.getItems().clear();
		for (File file: resultList)
		{
			Hyperlink link = new Hyperlink(file.getName());
			link.setOnAction(event ->
			{
			});
			listViewResult.getItems().add(link);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		listViewResult.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listViewResult.setCellFactory(hyperlinkListView -> new CenteredListViewCell());
	}
}
