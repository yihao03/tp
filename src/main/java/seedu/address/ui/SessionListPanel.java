package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.classroom.ClassSession;

/**
 * Panel containing the list of sessions.
 */
public class SessionListPanel extends UiPart<Region> {
    private static final String FXML = "SessionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(SessionListPanel.class);

    @FXML
    private ListView<ClassSession> sessionListView;

    /**
     * Creates a {@code SessionListPanel} with the given {@code ObservableList}.
     */
    public SessionListPanel(ObservableList<ClassSession> sessionList) {
        super(FXML);
        sessionListView.setItems(sessionList);
        sessionListView.setCellFactory(listView -> new SessionListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ClassSession} using a {@code SessionCard}.
     */
    class SessionListViewCell extends ListCell<ClassSession> {
        @Override
        protected void updateItem(ClassSession session, boolean empty) {
            super.updateItem(session, empty);

            if (empty || session == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new SessionCard(session, getIndex() + 1).getRoot());
            }
        }
    }

}
