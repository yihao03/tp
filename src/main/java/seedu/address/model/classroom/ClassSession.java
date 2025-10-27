package seedu.address.model.classroom;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;
import seedu.address.model.person.Student;

/**
 * Represents a single teaching session under a {@link TuitionClass}.
 * Tracks attendance for enrolled students.
 */
public class ClassSession {

    /** The class this session belongs to. */
    private final TuitionClass parentClass;

    /** Descriptive name for the session (e.g., "Week 3 Tutorial"). */
    private String sessionName;

    /** Scheduled date and time of the session. */
    private LocalDateTime dateTime;

    /** Optional location (e.g., "COM1-B103"). */
    private String location;

    /** Optional remarks about the session. */
    private String remarks;

    /**
     * Records attendance and time stamp of attendance marking for each student.
     * Key: Student
     * Value: Pair<Boolean, LocalDateTime> where Boolean indicates presence (true = present, false = absent)
     * and LocalDateTime indicates the time attendance was marked.
     */
    private Map<Student, Pair<Boolean, LocalDateTime>> attendanceRecord;

    /**
     * Constructs a {@code ClassSession}. Attendance is initialized for all current students in the parent class.
     *
     * @param parentClass parent class
     * @param sessionName session title
     * @param dateTime    scheduled time
     * @param location    location (nullable)
     */
    public ClassSession(TuitionClass parentClass, String sessionName, LocalDateTime dateTime, String location) {
        if (parentClass == null) {
            throw new IllegalArgumentException("Parent class cannot be null for session: " + sessionName);
        }
        if (parentClass.hasSessionName(sessionName)) {
            throw new IllegalArgumentException("This session name already exists for this class: " + sessionName);
        }
        this.parentClass = parentClass;
        this.sessionName = sessionName;
        this.dateTime = dateTime;
        this.location = location;
        this.attendanceRecord = new HashMap<>();
        initializeAttendance();
    }

    // Getters / setters

    public TuitionClass getParentClass() {
        return parentClass;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Attendance

    /** Initializes attendance records for all students (default: absent, timestamp = now). */
    public void initializeAttendance() {
        LocalDateTime now = LocalDateTime.now();
        for (Student s : parentClass.getStudents()) {
            attendanceRecord.putIfAbsent(s, new Pair<>(false, now));
        }
    }

    public void markPresent(Student student) {
        attendanceRecord.put(student, new Pair<>(true, LocalDateTime.now()));
    }

    public void markAbsent(Student student) {
        attendanceRecord.put(student, new Pair<>(false, LocalDateTime.now()));
    }

    public boolean hasAttended(Student student) {
        Pair<Boolean, LocalDateTime> record = attendanceRecord.getOrDefault(student,
                new Pair<>(false, LocalDateTime.now()));
        return record.getKey();
    }

    public Map<Student, Pair<Boolean, LocalDateTime>> getAttendanceRecord() {
        return attendanceRecord;
    }

    public long getAttendanceCount() {
        return attendanceRecord.values().stream().filter(p -> p.getKey()).count();
    }

    /**
     * Returns a multi-line string describing detailed session information including
     * date/time, location, remarks and per-student attendance.
     */
    public String getSessionDetails() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append("Session: ").append(sessionName == null ? "N/A" : sessionName).append(System.lineSeparator());
        sb.append("Date/Time: ").append(dateTime == null ? "N/A" : dateTime.format(fmt)).append(System.lineSeparator());
        if (location != null && !location.isEmpty()) {
            sb.append("Location: ").append(location).append(System.lineSeparator());
        }
        if (remarks != null && !remarks.isEmpty()) {
            sb.append("Remarks: ").append(remarks).append(System.lineSeparator());
        }
        sb.append("Attendance: ").append(getAttendanceCount()).append("/").append(attendanceRecord.size())
                .append(" present").append(System.lineSeparator());

        // Split attendance into Present and Absent sections
        StringBuilder presentSb = new StringBuilder();
        StringBuilder absentSb = new StringBuilder();
        for (Map.Entry<Student, Pair<Boolean, LocalDateTime>> entry : attendanceRecord.entrySet()) {
            Student student = entry.getKey();
            Pair<Boolean, LocalDateTime> pair = entry.getValue();
            Boolean present = pair == null ? false : pair.getKey();
            LocalDateTime ts = pair == null ? null : pair.getValue();
            String timeStr = (ts == null) ? "" : " (marked: " + ts.format(fmt) + ")";
            String line = "- " + (student == null ? "Unknown student" : student.getName()) + timeStr
                    + System.lineSeparator();
            if (Boolean.TRUE.equals(present)) {
                presentSb.append(line);
            } else {
                absentSb.append(line);
            }
        }

        sb.append("Present:").append(System.lineSeparator());
        if (presentSb.length() == 0) {
            sb.append("None").append(System.lineSeparator());
        } else {
            sb.append(presentSb.toString());
        }

        sb.append("Absent:").append(System.lineSeparator());
        if (absentSb.length() == 0) {
            sb.append("None").append(System.lineSeparator());
        } else {
            sb.append(absentSb.toString());
        }

        return sb.toString().trim();
    }

    @Override
    public String toString() {
        String dateFormatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String attendanceSummary = String.format("(%d/%d present)", getAttendanceCount(), attendanceRecord.size());
        String loc = (location != null && !location.isEmpty()) ? " @ " + location : "";
        String tail = attendanceRecord.isEmpty() ? "" : " " + attendanceSummary;
        return (sessionName + " on " + dateFormatted + loc + tail).trim();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ClassSession)) {
            return false;
        }
        ClassSession that = (ClassSession) other;
        return parentClass.equals(that.parentClass)
                && sessionName.equals(that.sessionName)
                && dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return parentClass.hashCode() ^ sessionName.hashCode() ^ dateTime.hashCode();
    }
}
