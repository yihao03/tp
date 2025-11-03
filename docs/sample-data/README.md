# Sample Seed Data for TutBook
This directory contains sample data files that can be used to quickly populate TutBook with realistic test data.

## Files

### `seed-addressbook.json`

Comprehensive seed data including:
- **25 Persons**:
  - 10 Students (Primary 4-6, Secondary 1-4)
  - 5 Tutors (Mathematics, Science, English, Chemistry, Chinese)
  - 10 Parents (linked to students)

- **7 Tuition Classes**:
  - Secondary 3 Mathematics (2 students, 4 sessions)
  - Secondary 2 Science (2 students, 3 sessions)
  - Primary 6 English (2 students, 4 sessions)
  - Secondary 4 Chemistry (1 student, 3 sessions)
  - Primary 5 Mathematics (1 student, 3 sessions)
  - Secondary 1 Chinese (1 student, 2 sessions)
  - Primary 4 Science (1 student, 3 sessions)

- **21 Class Sessions** with realistic dates/times and locations

## How to Use

### Method 1: Copy to Data Directory (Quickstart)
```bash
cp docs/sample-data/seed-addressbook.json data/addressbook.json
```

Then launch TutBook - it will load with all the sample data.

### Method 2: Import via Application (if import feature exists)
1. Launch TutBook
2. Go to File → Import
3. Select `docs/sample-data/seed-addressbook.json`

### Method 3: For Testing
Use this file as a reference for creating test data or for manual testing scenarios.

## Sample Data Details

### Students
All students have realistic Singapore addresses and are tagged with their level and activities:
- Emily Tan (Primary 6, scholarship student)
- Marcus Wong (Secondary 3, Science Olympiad)
- Sarah Lim (Secondary 2, Maths Club)
- And 7 more...

### Tutors
All tutors have professional qualifications:
- Dr Rachel Lim (Mathematics, MOE-certified)
- Mr James Tan (Science, Physics, NIE-trained)
- Ms Michelle Ng (English, Literature)
- Mr David Lee (Chemistry, Biology, PhD)
- Mrs Jennifer Wong (Chinese, MOE-certified)

### Classes
Each class has:
- Assigned tutor from the tutor list
- Enrolled students from the student list
- Multiple scheduled sessions with dates in November 2025
- Realistic locations (classrooms, labs)

### Sessions
Sessions are scheduled throughout November 2025 with:
- Descriptive names (e.g., "Week 1 - Quadratic Equations")
- Realistic times (mornings for primary, afternoons for secondary)
- Appropriate locations (Science Lab for science, Classroom for theory)

## Resetting Data

To start fresh after using seed data:
```bash
rm data/addressbook.json
```

TutBook will create a new empty address book on next launch.

## Creating Your Own Seed Data

See the JSON schema in `src/main/java/seedu/address/storage/` for the correct format. Key points:

1. **Persons array**: Each person must have `name`, `phone`, `email`, `address`, `role` (STUDENT/TUTOR/PARENT), and `tags` array
2. **Classes array**: Each class must have `name`, optional `tutor` object, `students` array, and `sessions` array
3. **Sessions**: Each session needs `sessionName`, `dateTime` (ISO format), and `location`

The storage layer automatically handles bidirectional relationships between persons and classes.
