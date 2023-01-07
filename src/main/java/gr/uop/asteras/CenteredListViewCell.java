package gr.uop.asteras;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

/**
 * <a href="https://stackoverflow.com/questions/51574266/align-contents-of-a-listview-using-javafx">
 *     Centered List View
 * </a>
 */
public final class CenteredListViewCell extends ListCell<Hyperlink>
{
	@Override
	protected void updateItem(Hyperlink item, boolean empty) {
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