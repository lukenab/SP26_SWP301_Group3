# 📚 ENGLISH CODE DOCUMENTATION

## 🎯 Project Structure Overview

This document provides comprehensive English explanations for the Room, Schedule, and Assessment modules of the Language Center Management System.

---

## 📦 MODULE 1: ROOM MANAGEMENT

### ✅ Room.java (Model)
**Purpose**: Entity class representing a physical classroom/training room

**Key Attributes**:
- `roomId`: Unique identifier
- `roomName`: Name/label of the room (e.g., "Room A101")
- `capacity`: Maximum number of students
- `type`: Room classification (e.g., "Classroom", "Lab", "Conference")
- `status`: Active (true) or Disabled (false)

**Key Methods**:
- Getters/Setters for all attributes
- `toString()`: Returns formatted string for debugging

---

### 🔧 RoomDAO.java (Data Access Object)
**Purpose**: Handles all database operations for rooms

**READ Operations**:
- `getAllRoom()`: Fetch all rooms including disabled ones
- `getActiveRooms()`: Fetch only enabled rooms (for scheduling)
- `getRoomByID(int id)`: Get specific room by ID
- `getClassesUsingRoom(int roomId)`: Get all classes using this room

**CREATE Operation**:
- `createRoom(String name, int capacity, String type, int status)`: Add new room
  - Validates: name not empty, capacity > 0, unique name

**UPDATE Operations**:
- `updateRoom(int id, String name, int capacity, String type, int status)`: Modify room
- `enableRoom(int id)`: Set status = 1 (active)
- `disableRoom(int id)`: Set status = 0 (disabled)

**DELETE Operation**:
- `deleteRoombyID(int id)`: Permanently remove room (only if not in use)

**VALIDATION Methods**:
- `checkRoomNameExists(String roomName)`: Prevent duplicate names
- `isRoomInUse(int roomId)`: Check if room has schedules or assigned classes
- `hasClassRoomIdColumn()`: Helper to check database structure

---

### 🎮 RoomController.java (Web Controller)
**Purpose**: Handles HTTP requests for room management

**GET Actions**:
- `all`: Display all rooms with usage status
- `detail`: Show details of specific room
- `create`: Display create room form
- `delete`: Show delete confirmation
- `disable`: Show disable confirmation
- `enable`: Show enable confirmation
- `update`: Display edit room form

**POST Actions**:
- `create`: Process room creation
  - VALIDATION 1: Name not empty
  - VALIDATION 2: Type not empty
  - VALIDATION 3: Name unique check
  - VALIDATION 4: Capacity > 0 validation

- `update`: Process room modification (same validations as create)
- `delete`: Remove room (checks if in use first)
- `disable/enable`: Change room status

---

### 📄 Room JSP Pages

1. **manageRoom.jsp** - Lists all rooms with action buttons
2. **createRoom.jsp** - Form to create new room
3. **updateRoom.jsp** - Form to edit existing room
4. **deleteRoom.jsp** - Deletion confirmation page
5. **disableRoom.jsp** - Disable confirmation page
6. **enableRoom.jsp** - Enable confirmation page
7. **roomDetail.jsp** - Shows room details and classes using it

---

## 📦 MODULE 2: SCHEDULE MANAGEMENT

### ✅ Schedule.java (Model)
**Purpose**: Entity class representing a class session/schedule

**Key Attributes**:
- `scheduleId`: Unique identifier
- `classes`: The class being scheduled
- `room`: The classroom where class will be held
- `slot`: Time slot (start and end time)
- `learningDate`: Date of the class session
- `employee`: Teacher conducting the class
- `attendanceStatus`: Whether attendance has been recorded (true/false)

---

### 🔧 ScheduleDAO.java (Data Access Object)
**Purpose**: Manages all schedule database operations with complex validations

**Key Features**:
- Prevents duplicate schedules
- Prevents class scheduling conflicts
- Prevents room double-booking
- Prevents teacher teaching multiple classes simultaneously
- Validates class size fits room capacity
- Validates learning dates within class date range
- Supports recurring schedules (daily, weekly, custom patterns)

**READ Operations**:
- `getAll()`: Fetch all schedules
- `getScheduleById(int scheduleId)`: Get specific schedule with details
- `getSchedulesForManagement(String selectedDate, Integer classId, Integer roomId)`: Get filtered schedules for academic staff
- `getScheduleByStudentWeek(int userId, String startDate, String endDate)`: Get student's weekly schedule

**CREATE Operation**:
- `createSchedule(int classId, int roomId, int slotId, Date learningDate, int teacherId, boolean attendanceStatus)`: Add new schedule
  - **STEP 0**: Check if learning date is within class date range
  - **STEP 0.5**: Check if class size fits room capacity
  - **STEP 1**: Check for duplicate schedules
  - **STEP 2**: Check for class scheduling conflicts
  - **STEP 3**: Check for room double-booking
  - **STEP 4**: Check for teacher conflicts

**UPDATE Operation**:
- `editSchedule()`: Modify schedule with same validations (excluding current schedule)

**DELETE Operations**:
- `deleteSchedule(int scheduleId)`: Remove single schedule
- `deleteSimilarSchedules(int scheduleId)`: Remove all similar schedules in a series

**VALIDATION Methods**:
- `isDuplicateSchedule()`: Check exact duplicate
- `hasClassConflictInSlot()`: Check class doesn't have multiple sessions same time
- `hasRoomConflictInSlot()`: Check room not double-booked
- `hasTeacherConflictInSlot()`: Check teacher not teaching multiple classes simultaneously
- `isClassSizeFitsRoomCapacity()`: Validate class size ≤ room capacity
- `isLearningDateWithinClassRange()`: Validate date within class start-end dates

**Recurring Schedule Support**:
- `generateScheduleDates()`: Generate dates for recurring patterns (daily, weekly, custom)
- `createMultipleSchedules()`: Batch create recurring schedules

---

### 🎮 ScheduleController.java (Web Controller)
**Purpose**: Handles HTTP requests for schedule management

**GET Actions**:
- `view`: Display teacher's schedule for week
- `manage`: Display academic staff schedule management interface
- `viewByClass`: Show specific class schedule
- `get`: Get schedule details as JSON
- `createForm`: Display create schedule form
- `editForm`: Display edit schedule form
- `viewDetail`: Show schedule details
- `delete`: Show delete confirmation
- `studentView`: Display student's weekly schedule

**POST Actions**:
- `create`: Process schedule creation (single or batch/recurring)
  - Validates all STEPS 0-4 from DAO
  - Handles recurring patterns

- `update`: Process schedule modification
  - Re-validates with same checks

- `delete`: Process schedule deletion
  - Checks if attendance already taken
  - Supports single or series deletion

---

### 📄 Schedule JSP Pages

1. **manageSchedule.jsp** - Academic staff schedule management with filters
2. **createSchedule.jsp** - Form to create single or recurring schedules
3. **editSchedule.jsp** - Form to modify existing schedule
4. **viewSchedule.jsp** - View schedule details
5. **deleteSchedule.jsp** - Delete confirmation with series option
6. **teacher_schedule.jsp** - Teacher's personal schedule view
7. **view_class_schedule.jsp** - Specific class schedule view
8. **studentSchedule.jsp** - Student's weekly schedule view

---

## 📦 MODULE 3: ASSESSMENT MANAGEMENT

### ✅ Assessment.java (Model)
**Purpose**: Entity class representing an evaluation method for a course

**Key Attributes**:
- `assessmentId`: Unique identifier
- `course`: The course this assessment belongs to
- `assessmentName`: Name of assessment (e.g., "Midterm Exam", "Assignments")
- `weight`: Percentage contribution to final grade (0.0 to 1.0)

**Examples**:
```
Course: English 101
├── Midterm Exam (weight: 0.30 = 30%)
├── Final Exam (weight: 0.40 = 40%)
└── Assignments (weight: 0.30 = 30%)
Total Weight = 1.0 (100%)
```

---

### 🔧 AssessmentDAO.java (Data Access Object)
**Purpose**: Handles all assessment database operations

**READ Operations**:
- `getAssessmentsByClass(int classId)`: Get all assessments for a class
- `getAssessmentsByCourse(int courseId)`: Get all assessments for a course
- `getAssessmentById(int assessmentId)`: Get specific assessment
- `getTotalWeightByCourse(int courseId)`: Sum all weights for a course

**CREATE Operation**:
- `addAssessment(int courseId, String assessmentName, double weight)`: Add new assessment
  - Example: Add "Participation" with weight 0.10

**UPDATE Operation**:
- `updateAssessment(int assessmentId, String assessmentName, double weight)`: Modify assessment

**DELETE Operation**:
- `deleteAssessment(int assessmentId)`: Remove assessment

**VALIDATION Methods**:
- `checkAssessmentNameExists(int courseId, String assessmentName)`: Prevent duplicate names within course

---

### 🎮 AssessmentController.java (Web Controller)
**Purpose**: Handles HTTP requests for assessment management

**GET Actions**:
- `delete`: Remove assessment and redirect to course page

**POST Actions**:
- `add`: Process new assessment creation
  - Validates: name not empty, name unique, weight 0-100, total weight ≤ 100%

- `update`: Process assessment modification with same validations

---

### 📄 Assessment JSP Pages

1. **assessment_management.jsp** - List all assessments for a course
2. **assessment_edit_confirm.jsp** - Edit confirmation page
3. **assessment_delete_confirm.jsp** - Delete confirmation page

---

## 🔐 VALIDATION SUMMARY

### Room Validations:
✅ Name not empty
✅ Type not empty
✅ Capacity > 0
✅ Unique room name
✅ Can't delete/disable if in use

### Schedule Validations:
✅ Learning date within class date range
✅ Class size ≤ room capacity
✅ No duplicate schedules (same class/room/slot/date/teacher)
✅ No class has multiple sessions in same slot/date
✅ No room double-booked in same slot/date
✅ No teacher teaching multiple classes in same slot/date

### Assessment Validations:
✅ Assessment name not empty
✅ Unique name per course
✅ Weight between 0-100%
✅ Total course weight ≤ 100%

---

## 🎯 IMPORTANT BUSINESS RULES

### Room Capacity:
- Class size must NOT exceed room capacity
- Prevents overcrowding and safety issues

### Schedule Conflicts:
- Each class can have only ONE session in each time slot per day
- Each room can be used by only ONE class in each time slot per day
- Each teacher can teach only ONE class in each time slot per day
- Dates must fall within class start-end date range

### Assessment Weights:
- All assessments for a course should total 100% (1.0)
- Used to calculate final student grades
- Example: Final Grade = Midterm(30%) + Final(40%) + Assignments(30%)

---

## 📊 DATA FLOW

### Creating a Schedule:
```
User Input (Form)
    ↓
ScheduleController.doPost("create")
    ↓
Validation (Steps 0-4)
    ↓
ScheduleDAO.createSchedule()
    ↓
Database Insert
    ↓
Success/Error Message
    ↓
Redirect to Schedule List
```

### Creating a Room:
```
User Input (Form)
    ↓
RoomController.doPost("create")
    ↓
Validations (Name, Type, Capacity, Unique)
    ↓
RoomDAO.createRoom()
    ↓
Database Insert
    ↓
Success/Error Message
    ↓
Redirect to Room List
```

### Adding an Assessment:
```
User Input (Form)
    ↓
AssessmentController.doPost("add")
    ↓
Validations (Name, Unique, Weight %, Total %)
    ↓
AssessmentDAO.addAssessment()
    ↓
Database Insert
    ↓
Success/Error Message
    ↓
Redirect to Course Page
```

---

## 🔄 RELATIONSHIPS

### Schedule - Room:
- Many schedules CAN use one room
- One schedule uses ONE room
- Relationship: One-to-Many

### Schedule - Class:
- Many schedules can exist for one class (different dates)
- One schedule is for ONE class
- Relationship: One-to-Many

### Schedule - Teacher:
- Many schedules can have one teacher
- One schedule has ONE teacher
- Relationship: One-to-Many

### Assessment - Course:
- Many assessments define ONE course's grading
- One assessment belongs to ONE course
- Relationship: One-to-Many

---

## ⚠️ ERROR HANDLING

### Common Error Scenarios:

**1. Creating Schedule:**
- ❌ Learning date outside class date range → Silent skip
- ❌ Class size > room capacity → ERROR: "Class size exceeds room capacity"
- ❌ Duplicate schedule exists → ERROR: "Duplicate schedule already exists"
- ❌ Class already scheduled in slot/date → ERROR: "Class already has schedule"
- ❌ Room already booked in slot/date → ERROR: "Room already has schedule"
- ❌ Teacher already teaching in slot/date → ERROR: "Teacher already has schedule"

**2. Creating Room:**
- ❌ Name empty → ERROR: "Room name cannot be empty"
- ❌ Type empty → ERROR: "Room type cannot be empty"
- ❌ Capacity ≤ 0 → ERROR: "Capacity must be greater than 0"
- ❌ Name already exists → ERROR: "Room name already exists"

**3. Adding Assessment:**
- ❌ Name empty → ERROR: "Assessment name cannot be empty"
- ❌ Name already exists in course → ERROR: "Assessment name already exists"
- ❌ Weight < 0 or > 100 → ERROR: "Weight must be between 0 and 100"
- ❌ Total weight > 100% → ERROR: "Total weight cannot exceed 100%"

---

## 🔑 KEY TAKEAWAYS

1. **Schedules** prevent all types of conflicts (class, room, teacher)
2. **Rooms** can be enabled/disabled without deletion
3. **Assessments** must total 100% for accurate grading
4. All major operations have comprehensive validation
5. Error messages guide users to fix issues
6. Database consistency is maintained through validation layers

---

**Document Version**: 1.0
**Last Updated**: March 23, 2026
**Language**: English

