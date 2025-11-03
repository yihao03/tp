package seedu.address.ui;

import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.classroom.ClassSession;

/**
 * An UI component that displays information of a {@code ClassSession}.
 */
public class SessionCard extends UiPart<Region> {

    private static final String FXML = "SessionListCard.fxml";
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

    public final ClassSession session;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label sessionName;
    @FXML
    private Label dateTime;
    @FXML
    private Label sessionLocation;
    @FXML
    private Label attendance;
    @FXML
    private ImageView sessionIcon;

    /**
     * Creates a {@code SessionCard} with the given {@code ClassSession} and index to display.
     */
    public SessionCard(ClassSession session, int displayedIndex) {
        super(FXML);
        this.session = session;
        id.setText(displayedIndex + ". ");
        sessionName.setText(session.getSessionName());
        dateTime.setText(session.getDateTime().format(DATE_TIME_FORMATTER));

        // Handle nullable location
        String loc = session.getLocation();
        if (loc != null && !loc.isEmpty()) {
            sessionLocation.setText(loc);
            sessionLocation.setVisible(true);
            sessionLocation.setManaged(true);
        } else {
            sessionLocation.setText("No location");
            sessionLocation.setVisible(true);
            sessionLocation.setManaged(true);
        }

        // Display attendance information
        long attendanceCount = session.getAttendanceCount();
        long totalStudents = session.getParentClass().getStudents().size();
        attendance.setText(String.format("%d/%d present", attendanceCount, totalStudents));

        // Set session icon
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/session_icon.png"));
            sessionIcon.setImage(image);
        } catch (Exception e) {
            // If icon loading fails, fallback to info icon
            try {
                Image fallbackImage = new Image(getClass().getResourceAsStream("/images/info_icon.png"));
                sessionIcon.setImage(fallbackImage);
            } catch (Exception fallbackException) {
                // If even the fallback fails, set no image
                sessionIcon.setImage(null);
            }
        }

        // Apply modern styling to the card
        cardPane.getStyleClass().add("modern-card");
    }
}
