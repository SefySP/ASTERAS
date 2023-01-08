package gr.uop.asteras;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gr.uop.BibFileFields;
import gr.uop.lucene.LuceneController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController
{

	protected static final String[]         searchableFields = new String[]{BibFileFields.AUTHOR, BibFileFields.EDITOR,
																			BibFileFields.BOOKTITLE,
																			BibFileFields.TITLE,
																			BibFileFields.ANNOTATION
			, BibFileFields.PUBLISHER, BibFileFields.YEAR};
	private          LuceneController luceneController;
	@FXML
	private                TextField        searchTextField;

	@FXML
	private HBox advancedSettingsBox;

	@FXML
	private Spinner<Integer> maxSearchResults;

	public MainController()
	{
		luceneController = new LuceneController();
		var files = new File(LuceneController.INDEX_DIR).listFiles();
		assert files != null;
		if (files.length <= 1)
		{
			luceneController.createIndex();
		}
	}

	@FXML
	private void initialize()
	{
		for (String field : searchableFields)
		{
			CheckBox checkBox = new CheckBox();
			checkBox.setSelected(true);
			checkBox.setText(field);

			advancedSettingsBox.getChildren().add(checkBox);
		}

		maxSearchResults.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 10));
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
		if (searchQuery.isEmpty())
		{
			return;
		}

		List<String> searchFields = new ArrayList<>();

		addCheckFields(searchFields);

		isFieldEmpty(searchFields);

		try
		{
			var list = luceneController.search(searchQuery, searchFields.toArray(new String[0]), maxSearchResults.getValue());
			System.out.println(list);
			System.out.println(luceneController.getTime());
			showResults(list, searchQuery);
		}
		catch (IOException | ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void addCheckFields(List<String> searchFields)
	{
		ObservableList<Node> children = advancedSettingsBox.getChildren();
		for (int i = 1; i < children.size(); i++)
		{
			Node     check    = children.get(i);
			CheckBox checkBox = (CheckBox) check;
			if (checkBox.isSelected())
			{
				System.out.println(checkBox.getText());
				searchFields.add(checkBox.getText());
			}
		}
	}

	private void isFieldEmpty(List<String> searchFields)
	{
		if (searchFields.isEmpty())
		{
			searchFields.addAll(Arrays.stream(searchableFields).toList());
		}
	}

	private void showResults(List<File> resultList, String searchQuery) throws IOException
	{
		ShowResultsController showResultsController = new ShowResultsController();
		File                  file                  = new File("src/main/resources/gr/uop/asteras/ShowResults.fxml");
		FXMLLoader            loader                = new FXMLLoader(file.toURI().toURL());
		loader.setController(showResultsController);
		Parent root  = loader.load();
		Scene  scene = new Scene(root);
		Stage  stage = new Stage();

		showResultsController.setResultList(resultList);
		showResultsController.setResultScores(luceneController.getTopScores());
		showResultsController.setListViewResult(searchQuery);

		stage.setTitle("Results");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(root.getScene().getWindow());
		stage.setMinHeight(400.0);
		stage.setMinWidth(400.0);

		setIcon(stage);
		stage.setScene(scene);

		stage.showAndWait();
	}

	@FXML
	void enterPressed(KeyEvent event)
	{
		if (event.getCode() == KeyCode.ENTER)
		{
			search(new ActionEvent());
		}
	}

	@FXML
	void addBibFile(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Add Bib File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bib File", "*.bib"));

		var selectedFile = fileChooser.showOpenMultipleDialog(advancedSettingsBox.getScene().getWindow());

		if (selectedFile == null)
			return;

		for (File file : selectedFile)
		{
			try
			{
				luceneController.addFileToIndex(copyFileToDefaultDataDirectory(file));
				System.out.println(file.getName());
			}
			catch (FileAlreadyExistsException exception)
			{
				System.out.println(file + " exists");
			}
		}
	}

	private File copyFileToDefaultDataDirectory(File file) throws FileAlreadyExistsException
	{
		Path sourcePath = Paths.get(file.toURI());
		Path targetPath = Paths.get(new File(LuceneController.DATA_DIR + File.separator + file.getName()).toURI());

		System.out.println("File: " + file.getName());
		System.out.println("Source: " + sourcePath.getFileName());
		System.out.println("Target: " + targetPath.getFileName());

		try
		{
			Files.copy(sourcePath, targetPath);
			return targetPath.toFile();
		}
		catch (FileAlreadyExistsException exception)
		{
			throw new FileAlreadyExistsException(file.getName());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@FXML
	void deleteBibFile(ActionEvent event)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Delete Bib File");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bib File", "*.bib"));
		fileChooser.setInitialDirectory(new File(LuceneController.DATA_DIR));
		var selectedFile = fileChooser.showOpenMultipleDialog(advancedSettingsBox.getScene().getWindow());

		if (selectedFile == null)
			return;

		for (int i = 0; i < selectedFile.size(); i++)
		{
			Path targetPath = selectedFile.get(i).toPath();
			try
			{
				Files.delete(targetPath);
				luceneController.deleteIndexDir();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
		luceneController = new LuceneController();
		luceneController.createIndex();
	}

	private void setIcon(Stage stage) throws IOException
	{
		File  iconFile = new File("src/main/resources/gr/uop/asteras/icons/search32.png");
		Image icon     = new Image(iconFile.toURI().toURL().toString());

		stage.getIcons().add(icon);
	}
}