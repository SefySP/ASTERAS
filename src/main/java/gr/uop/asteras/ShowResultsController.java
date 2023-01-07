package gr.uop.asteras;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class ShowResultsController implements Initializable
{
	@FXML
	public ListView<Label> listViewResult;

	private List<File> resultList;
	private List<Float> resultScores;

	public void setResultList(List<File> resultList)
	{
		this.resultList = resultList;
	}

	public void setListViewResult()
	{
		listViewResult.getItems().clear();
		int i = 0;
		for (File file: resultList)
		{
			String researcher = file.getName().split("\\.")[0].toUpperCase();
			Label link = new Label(researcher + " | " + resultScores.get(i));
			i++;
			listViewResult.getItems().add(link);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		listViewResult.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		listViewResult.setCellFactory(hyperlinkListView -> new CenteredListViewCell());
	}

	public void setResultScores(List<Float> resultScores)
	{
		this.resultScores = resultScores;
	}
}
