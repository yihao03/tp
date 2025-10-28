package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.classroom.TuitionClass;

/**
 * Panel containing the list of classes.
 */
public class ClassListPanel extends UiPart<Region> {
    private static final String FXML = "ClassListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ClassListPanel.class);

    @FXML
    private ListView<TuitionClass> classListView;

    /**
     * Creates a {@code ClassListPanel} with the given {@code ObservableList}.
     */
    public ClassListPanel(ObservableList<TuitionClass> classList) {
        super(FXML);
        classListView.setItems(classList);
        classListView.setCellFactory(listView -> new ClassListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code TuitionClass} using a {@code ClassCard}.
     */
    class ClassListViewCell extends ListCell<TuitionClass> {
        @Override
        protected void updateItem(TuitionClass tuitionClass, boolean empty) {
            super.updateItem(tuitionClass, empty);

            if (empty || tuitionClass == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ClassCard(tuitionClass, getIndex() + 1).getRoot());
            }
        }
    }

}
