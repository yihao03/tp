package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.classroom.TuitionClass;

/**
 * An UI component that displays information of a {@code TuitionClass}.
 */
public class ClassCard extends UiPart<Region> {

    private static final String FXML = "ClassListCard.fxml";

    public final TuitionClass tuitionClass;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label className;
    @FXML
    private Label tutorName;
    @FXML
    private Label studentCount;
    @FXML
    private Label sessionCount;
    @FXML
    private ImageView classIcon;

    /**
     * Creates a {@code ClassCard} with the given {@code TuitionClass} and index to display.
     */
    public ClassCard(TuitionClass tuitionClass, int displayedIndex) {
        super(FXML);
        this.tuitionClass = tuitionClass;
        id.setText(displayedIndex + ". ");
        className.setText(tuitionClass.getClassName());

        // Display tutor name or "Unassigned"
        String tutor = tuitionClass.isAssignedToTutor()
                ? tuitionClass.getTutor().getName().fullName
                : "Unassigned";
        tutorName.setText("Tutor: " + tutor);

        // Display student count
        int students = tuitionClass.getStudents().size();
        studentCount.setText(String.format("%d student%s", students, students == 1 ? "" : "s"));

        // Display session count
        int sessions = tuitionClass.getAllSessions().size();
        sessionCount.setText(String.format("%d session%s", sessions, sessions == 1 ? "" : "s"));

        // Set class icon
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/class_icon.png"));
            classIcon.setImage(image);
        } catch (Exception e) {
            // If icon loading fails, fallback to info icon
            try {
                Image fallbackImage = new Image(getClass().getResourceAsStream("/images/info_icon.png"));
                classIcon.setImage(fallbackImage);
            } catch (Exception fallbackException) {
                // If even the fallback fails, set no image
                classIcon.setImage(null);
            }
        }

        // Apply modern styling to the card
        cardPane.getStyleClass().add("modern-card");
    }
}
