package seedu.address.model;

import java.time.LocalDateTime;

/**
 * Represents an attendance record with presence status and timestamp.
 */
public class Attendance {
    private boolean isPresent;
    private LocalDateTime timestamp;

    /**
     * Constructs an Attendance with the given presence status and timestamp.
     *
     * @param isPresent true if the student is present, false otherwise.
     * @param timestamp the timestamp of the attendance.
     */
    public Attendance(boolean isPresent, LocalDateTime timestamp) {
        this.isPresent = isPresent;
        this.timestamp = timestamp;
    }

    /**
     * Returns true if the student is present.
     *
     * @return true if present, false otherwise.
     */
    public boolean isPresent() {
        return isPresent;
    }

    /**
     * Returns the timestamp of the attendance.
     *
     * @return the timestamp.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
