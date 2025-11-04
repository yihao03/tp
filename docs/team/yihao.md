# Yi Hao

Hi I'm Yi Hao. I want a job. Thank you.

## Project Overview

Our project, TutBook aims to create the ideal addressbook for tution center
administrators. It is designed to help them manage their students, tutors,
and classes in a efficient, centralized and reliable manner.

## My Contributions (112 commits)

This section is generated with Claude code based on git log.

### Major Features

#### **Role System**

- Created `PersonType` enum and role classes (Tutor, Student, Admin)
- Implemented role-based functionality across the application

#### **Class Management**

- Implemented `addclass` and `joinclass` commands with full logic and parsers
- Added methods to retrieve classes by name
- Created PUML diagrams for class-related features

#### **Session Management**

- Implemented `viewsession` command with detailed session display
- Added `getSessionDetail()` method for comprehensive session views
- Integrated session updates with class editing

#### **Attendance System** (#149)

- Enhanced attendance timestamp functionality with dedicated tracking system
- Prevented students from being marked present/absent if not enrolled

#### **Case Insensitivity**

- Made all commands and name comparisons case insensitive
- Updated tests to reflect case insensitivity

### UI/UX Improvements

- Redesigned UI with modern card layout for persons
- Moved command input to bottom of screen
- Implemented real-time UI updates when data changes using JavaFX bindings

### Code Quality & Documentation

- Added comprehensive test coverage and logging across commands
- Implemented input validation with length limits
- Updated User Guide to be more user-centric with interface overview
- Added Java installation instructions and clarified app purpose

### Bug Fixes

- Fixed high priority bugs (#118)
- Fixed JSON formation bugs and proper error handling
