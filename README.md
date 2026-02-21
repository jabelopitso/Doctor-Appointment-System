# 🩺 Doctor Appointment Booking System

> A Java console application for managing medical appointments — built with clean OOP principles.

**Author:** Jabelo Pitso &nbsp;|&nbsp; **Language:** Java 17 &nbsp;|&nbsp; **Type:** Console Application

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [How to Use](#how-to-use)
- [OOP Design](#oop-design)
- [Class Reference](#class-reference)
- [Future Improvements](#future-improvements)

---

## Overview

The Doctor Appointment Booking System lets patients register, browse doctors, book time slots, and cancel appointments — all from the terminal. It prevents double-booking, tracks appointment history, and restores slots automatically when an appointment is cancelled.

Built as a learning project to demonstrate real-world Java OOP design across eight collaborating classes.

---

## Features

### Patient
- Register with username and password
- Login with credential validation
- View all available doctors and their open slots
- Book an appointment from a live slot list
- Cancel a scheduled appointment (slot is restored to the doctor)
- View full appointment history with status (SCHEDULED / CANCELLED / COMPLETED)

### System
- Conflict prevention — a patient cannot book two appointments at the same time slot
- Slot management — slots are removed on booking and restored on cancellation
- Enum-driven status transitions with validation
- In-memory data storage using Java Collections

---

## Project Structure

```
Doctor-Appointment-System/
└── src/
    ├── Main.java                 # Entry point — menus and user interaction
    ├── Doctor.java               # Doctor model with slot management
    ├── Patient.java              # Patient model with appointment tracking
    ├── Appointment.java          # Appointment model with status enum
    ├── AppointmentManager.java   # Central in-memory data store
    ├── DoctorService.java        # Doctor data access and display
    ├── PatientService.java       # Patient registration and login
    └── AppointmentService.java   # Core booking and cancellation logic
```

All files live flat in `src/` — no subpackages. This keeps the project simple to compile and run directly from the terminal.

---

## Getting Started

### Requirements

- Java 17 or higher
- Terminal / Command Line

Check your Java version:
```bash
java -version
```

### Clone the project

```bash
git clone https://github.com/your-username/Doctor-Appointment-System.git
cd Doctor-Appointment-System/src
```

### Compile

```bash
javac *.java
```

### Run

```bash
java Main
```

That's it — no Maven, no dependencies, no config files needed.

---

## How to Use

When you run the app you'll see the main menu:

```
  +======================================================+
  |      DOCTOR APPOINTMENT BOOKING SYSTEM               |
  |      Author: Jabelo Pitso                            |
  +======================================================+

  ========================================
               MAIN MENU
  ========================================
  1. Register as Patient
  2. Patient Login
  3. View All Doctors
  4. Exit
  ========================================
  Choice:
```

### Typical flow

```
1  →  Register with your name, age, contact, username, password
2  →  Login with your username and password
       ↓
       Patient Menu appears
       ↓
  1  →  Browse doctors and their available time slots
  2  →  Pick a doctor by ID, choose a slot, give a reason
  3  →  View your full appointment history
  4  →  Cancel a scheduled appointment by ID
  5  →  Logout
```

### Sample booking session

```
  Enter Doctor ID : 2

  Slots for Dr. Michael Dlamini:
  1. 2024-06-10 08:00
  2. 2024-06-10 14:00
  3. 2024-06-12 09:00

  Select slot number : 1
  Reason for visit   : Annual checkup

  SUCCESS: Appointment booked!

  +---------------------------------------------+
  | Appointment ID : 1                          |
  | Patient        : Jabelo Pitso               |
  | Doctor         : Dr. Michael Dlamini        |
  | Slot           : 2024-06-10 08:00           |
  | Reason         : Annual checkup             |
  | Status         : SCHEDULED                  |
  +---------------------------------------------+
```

---

## OOP Design

| Principle | How it's applied |
|---|---|
| **Encapsulation** | All fields are `private`. Access is through getters and setters only |
| **Abstraction** | `Main.java` calls services without knowing storage details |
| **Separation of concerns** | Models hold data. Services hold logic. Main holds UI |
| **Enum** | `Appointment.Status` enforces valid state transitions with runtime validation |
| **Single Responsibility** | Each class has one clear job |

---

## Class Reference

### `Doctor.java`
Holds doctor details and a list of available time slots. Provides `addAvailableSlot()`, `removeSlot()`, and `isSlotAvailable()` for slot management.

### `Patient.java`
Holds patient personal info, login credentials, and a list of appointment IDs linked to that patient.

### `Appointment.java`
Represents a single booking. Contains a `Status` enum (`SCHEDULED`, `CANCELLED`, `COMPLETED`) with `cancel()` and `complete()` methods that enforce valid transitions.

### `AppointmentManager.java`
The central in-memory store for all appointments. Handles creation, lookup by ID, lookup by patient, lookup by doctor, and conflict detection.

### `DoctorService.java`
Manages the doctor list. Pre-loads three sample doctors on startup. Provides `findById()`, `getAllDoctors()`, and `displayAllDoctors()`.

### `PatientService.java`
Handles patient registration (with duplicate username check) and login (with credential validation).

### `AppointmentService.java`
Core business logic. `book()` checks slot availability and patient conflicts before confirming. `cancel()` validates ownership before cancelling and restores the slot to the doctor.

### `Main.java`
Drives the console menus. Wires all services together and handles all user input and display.

---

## Future Improvements

- **File persistence** — save appointments to `.csv` or `.txt` so data survives a restart
- **MySQL integration** — replace in-memory store with a real database
- **Doctor login** — give doctors their own menu to view schedules and manage availability
- **Date validation** — reject booking slots that are in the past
- **Unit tests** — JUnit test coverage for service classes
- **GUI interface** — JavaFX or Swing front end
- **Email/SMS notifications** — confirmation messages on booking and cancellation

---

## License

This project is for educational purposes. Feel free to fork, modify, and build on it.

---

*Built by Jabelo Pitso — Software Development Graduate*