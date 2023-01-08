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

	public void setListViewResult(String searchQuery)
	{
		listViewResult.getItems().clear();
		int i = 0;
		for (File file: resultList)
		{
			String researcher = file.getName().split("\\.")[0].toUpperCase();
			String field;
			String info = "";
			for (int j = 0; j < MainController.searchableFields.length; j++)
			{
				field = AddingBib.getBibFieldData(MainController.searchableFields[j], file);
				var elements = field.split("\n");
				for (String element : elements)
				{
					if (element.toLowerCase().contains(searchQuery.toLowerCase()))
					{
						System.out.println(element);
						info = element;
						break;
					}
				}
			}
			Label link = new Label(researcher + " | " + resultScores.get(i));
			Label infoLabel = new Label(info);
			i++;
			listViewResult.getItems().addAll(link, infoLabel);
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
