package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s1-cs2103t-w09-3.github.io/tp/";
    public static final String HELP_MESSAGE = "Quick Command Reference\n\n"
            + "=== PERSON MANAGEMENT ===\n"
            + "add - Add a person\n"
            + "edit - Edit a person\n"
            + "delete - Delete a person\n"
            + "list - List all persons\n"
            + "find - Find persons by keyword\n"
            + "filter - Filter persons by role\n\n"
            + "=== CLASS MANAGEMENT ===\n"
            + "addclass - Add a tuition class\n"
            + "editclass - Edit a class\n"
            + "deleteclass - Delete a class\n"
            + "listclass - List all classes\n"
            + "join - Add person to class\n"
            + "unjoin - Remove person from class\n"
            + "liststudents - List students in class\n\n"
            + "=== SESSION MANAGEMENT ===\n"
            + "addsession - Add a session\n"
            + "deletesession - Delete a session\n"
            + "listsessions - List sessions of class\n"
            + "viewsession - View session details\n\n"
            + "=== ATTENDANCE ===\n"
            + "attend - Mark attendance\n\n"
            + "=== PARENT-CHILD RELATIONSHIPS ===\n"
            + "link - Link parent to child\n"
            + "childrenof - List children of parent\n"
            + "parentsof - List parents of child\n\n"
            + "=== GENERAL ===\n"
            + "clear - Clear all entries\n"
            + "help - Show this help window\n"
            + "exit - Exit the program\n\n"
            + "For detailed documentation, visit:\n" + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
