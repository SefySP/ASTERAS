package gr.uop.asteras;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 * <a href="https://stackoverflow.com/questions/51574266/align-contents-of-a-listview-using-javafx">
 *     Centered List View
 * </a>
 */
public final class CenteredListViewCell extends ListCell<Label>
{
	@Override
	protected void updateItem(Label item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
		} else {
			// Create the HBox
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER);

			hBox.getChildren().add(item);
			setGraphic(hBox);
		}
	}
}