# 🔍 SCHEDULE SYSTEM - CODE EXECUTION FLOW (Line-by-Line)

## 1. MANAGE ACTION - Filter by ClassId & RoomId

### Scenario A: All Classes (classId=0) + All Rooms (roomId=0)

```
┌─────────────────────────────────────────────────────────────────┐
│ ScheduleController.java - doGet()                               │
└─────────────────────────────────────────────────────────────────┘

Line 115: case "manage":
Line 117: ScheduleDAO scheduleDAO = new ScheduleDAO();
Line 118: ClassDAO classDAO = new ClassDAO();
Line 120: String filterClassId = request.getParameter("classId");
          → "0"
Line 121: String filterRoomId = request.getParameter("roomId");
          → "0"
Line 122: Integer classFilterId = null;
Line 123: Integer roomFilterId = null;
Line 124: List<Schedule> managementScheduleList = new ArrayList<>();

Line 127: if (filterClassId != null && !filterClassId.isEmpty() && !filterClassId.equals("0"))
          → ("0" != null) ✅ 
          → (!isEmpty()) ✅ 
          → !equals("0") ❌ FALSE
          → Line 127 condition: FALSE
          → Block NOT executed

Line 127-130: SKIP (classFilterId remains null)
              
Line 133: if (filterRoomId != null && !filterRoomId.isEmpty() && !filterRoomId.equals("0"))
          → ("0" != null) ✅ 
          → (!isEmpty()) ✅ 
          → !equals("0") ❌ FALSE
          → Line 133 condition: FALSE
          → Block NOT executed

Line 133-136: SKIP (roomFilterId remains null)

Line 139: managementScheduleList = scheduleDAO.getSchedulesForManagement(
                                      selectedDate, 
                                      classFilterId,  // null
                                      roomFilterId    // null
                                  );

┌─────────────────────────────────────────────────────────────────┐
│ ScheduleDAO.java - getSchedulesForManagement()                  │
│ Line 643: public List<Schedule> getSchedulesForManagement(      │
│               String selectedDate, Integer classId, Integer roomId)
└─────────────────────────────────────────────────────────────────┘

Line 643: classId = null, roomId = null

Line 652: StringBuilder sql = new StringBuilder();
Line 653: sql.append("SELECT s.ScheduleID, s.ClassID, s.RoomID, ...");
Line 654: sql.append("FROM Schedule s");
Line 655: sql.append("INNER JOIN Class c ON s.ClassID = c.ClassID");
...
Line 659: sql.append("WHERE 1=1 ");

Line 661: if (selectedDate != null && !selectedDate.isEmpty())
          → TRUE (assuming date is "2026-03-22")
          → Line 662: sql.append("AND s.LearningDate >= DATEADD(...)")
          → Line 663: sql.append("AND s.LearningDate < DATEADD(...)")

Line 666: if (classId != null && classId > 0)
          → (null != null) ❌ FALSE
          → Block NOT executed
          → NO "AND s.ClassID = ?" added

Line 671: if (roomId != null && roomId > 0)
          → (null != null) ❌ FALSE
          → Block NOT executed
          → NO "AND s.RoomID = ?" added

Line 675: sql.append("ORDER BY s.LearningDate ASC, s.SlotID ASC");

Final SQL:
  SELECT s.ScheduleID, s.ClassID, s.RoomID, s.SlotID, s.LearningDate, ...
  FROM Schedule s
  INNER JOIN Class c ON s.ClassID = c.ClassID
  INNER JOIN Course co ON c.CourseID = co.CourseID
  LEFT JOIN [User] u ON s.TeacherID = u.UserID
  WHERE 1=1 
  AND s.LearningDate >= DATEADD(day, -(DATEPART(weekday, '2026-03-22') - 2), CAST('2026-03-22' AS DATE))
  AND s.LearningDate < DATEADD(day, 7 - (DATEPART(weekday, '2026-03-22') - 2), CAST('2026-03-22' AS DATE))
  -- NO CLASS FILTER
  -- NO ROOM FILTER
  ORDER BY s.LearningDate ASC, s.SlotID ASC

Result: ✅ ALL schedules for the week (all classes, all rooms, all slots)

Line 677: try (PreparedStatement ps = conn.prepareStatement(sql.toString()))
Line 678: int paramIndex = 1;

Line 680: if (selectedDate != null && !selectedDate.isEmpty())
          → TRUE
          → Line 681: ps.setString(1, "2026-03-22");
          → Line 682: ps.setString(2, "2026-03-22");
          → Line 683: ps.setString(3, "2026-03-22");
          → Line 684: ps.setString(4, "2026-03-22");
          → paramIndex = 5

Line 686: if (classId != null && classId > 0)
          → (null != null) ❌ FALSE
          → Block NOT executed
          → ps.setInt() NOT called

Line 690: if (roomId != null && roomId > 0)
          → (null != null) ❌ FALSE
          → Block NOT executed
          → ps.setInt() NOT called

Line 694: try (ResultSet rs = ps.executeQuery())
          → Execute SQL with 4 date parameters
          → Returns all schedules for week

Line 710: while (rs.next())
          → Loop through all rows
          → Build Schedule objects
          → Add to scheduleList

Result: scheduleList contains ALL schedules for the entire week
```

### Scenario B: Specific Class (classId=5) + All Rooms (roomId=0)

```
Line 120: String filterClassId = request.getParameter("classId");
          → "5"

Line 127: if (filterClassId != null && !filterClassId.isEmpty() && !filterClassId.equals("0"))
          → ("5" != null) ✅ 
          → (!isEmpty()) ✅ 
          → !equals("0") ✅ TRUE
          → Block EXECUTED

Line 128: classFilterId = Integer.parseInt("5");
          → classFilterId = 5

Line 133: if (filterRoomId != null && !filterRoomId.isEmpty() && !filterRoomId.equals("0"))
          → ("0" != null) ✅ 
          → (!isEmpty()) ✅ 
          → !equals("0") ❌ FALSE
          → Block NOT executed
          → roomFilterId = null

Line 139: scheduleDAO.getSchedulesForManagement(selectedDate, 5, null)

In ScheduleDAO.getSchedulesForManagement():
Line 666: if (classId != null && classId > 0)
          → (5 != null) ✅ 
          → (5 > 0) ✅ TRUE
          → Block EXECUTED
          → Line 667: sql.append("AND s.ClassID = ? ");
          → SQL now has: "... AND s.ClassID = ?"

Line 671: if (roomId != null && roomId > 0)
          → (null != null) ❌ FALSE
          → Block NOT executed

Line 680: if (selectedDate != null && !selectedDate.isEmpty())
          → TRUE
          → ps.setString(1, date);
          → ps.setString(2, date);
          → ps.setString(3, date);
          → ps.setString(4, date);
          → paramIndex = 5

Line 686: if (classId != null && classId > 0)
          → (5 != null) ✅ 
          → (5 > 0) ✅ TRUE
          → Line 687: ps.setInt(5, 5);  ✅ Set classId = 5
          → paramIndex = 6

Line 690: if (roomId != null && roomId > 0)
          → (null != null) ❌ FALSE
          → Block NOT executed

Final SQL executed:
  SELECT ... FROM Schedule s ...
  WHERE 1=1
  AND s.LearningDate >= ...
  AND s.LearningDate < ...
  AND s.ClassID = 5
  -- NO ROOM FILTER
  ORDER BY ...
  
  Parameters: [date, date, date, date, 5]

Result: ✅ Only Class 5 schedules for the week
```

---

## 2. CREATE ACTION - Single Schedule

```
┌─────────────────────────────────────────────────────────────────┐
│ ScheduleController.java - doPost() - CREATE ACTION              │
└─────────────────────────────────────────────────────────────────┘

Line 424: case "create":
Line 427: int classId = Integer.parseInt("5");              → 5
Line 428: int roomId = Integer.parseInt("3");               → 3
Line 429: int slotId = Integer.parseInt("2");               → 2
Line 430: String learningDateStr = "2026-03-22";
Line 431: Date learningDate = Date.valueOf(learningDateStr); → 2026-03-22

Line 433: String recurringType = request.getParameter("recurringType");
          → "none"
Line 434: if (recurringType == null || recurringType.isEmpty())
          → ("none" == null) ❌ FALSE
          → recurringType = "none" (unchanged)

Line 438: ClassDAO classDAO = new ClassDAO();
Line 439: int teacherId = classDAO.getTeacherIdByClassId(5);
          → teacherId = 1 (assume teacher ID)

Line 442: if (!"none".equals("none"))
          → FALSE
          → Block NOT executed (skip batch creation)
          
Line 452-475: SINGLE SCHEDULE BLOCK ✅ Executed

┌─────────────────────────────────────────────────────────────────┐
│ VALIDATION CHAIN                                                 │
└─────────────────────────────────────────────────────────────────┘

Line 454: if (scheduleDAO.hasScheduleConflict(5, 2, 2026-03-22, -1))
          → Call ScheduleDAO.hasScheduleConflict()

          ScheduleDAO.java Line 421:
          SELECT COUNT(*) FROM Schedule 
          WHERE ClassID=5 AND SlotID=2 AND LearningDate=2026-03-22 AND ScheduleID != -1

          Result: count = 0 (no conflicts)
          Return: false
          
          → Line 454 condition: FALSE
          → Block NOT executed (no error)

Line 461: if (!scheduleDAO.isRoomAvailable(3, 2, 2026-03-22, -1))
          → Call ScheduleDAO.isRoomAvailable()

          ScheduleDAO.java Line 373:
          SELECT COUNT(*) FROM Schedule 
          WHERE RoomID=3 AND SlotID=2 AND LearningDate=2026-03-22 AND ScheduleID != -1

          Result: count = 0 (room available)
          Return: true (room is available)
          
          → !true = FALSE
          → Line 461 condition: FALSE
          → Block NOT executed (no error)

Line 467: if (!scheduleDAO.isTeacherAvailable(1, 2, 2026-03-22, -1))
          → Call ScheduleDAO.isTeacherAvailable()

          ScheduleDAO.java Line 396:
          SELECT COUNT(*) FROM Schedule 
          WHERE TeacherID=1 AND SlotID=2 AND LearningDate=2026-03-22 AND ScheduleID != -1

          Result: count = 0 (teacher free)
          Return: true (teacher available)
          
          → !true = FALSE
          → Line 467 condition: FALSE
          → Block NOT executed (no error)

Line 473: if (scheduleDAO.teacherExceedsWeeklyLimit(1, 2026-03-22, -1))
          → Call ScheduleDAO.teacherExceedsWeeklyLimit()

          ScheduleDAO.java Line 468:
          SELECT COUNT(*) FROM Schedule 
          WHERE TeacherID=1 
          AND LearningDate >= (Monday of week containing 2026-03-22)
          AND LearningDate < (Next Monday)
          AND ScheduleID != -1

          Result: count = 3 (teacher has 3 slots this week)
          
          → count >= 5 ? 
          → 3 >= 5 ? FALSE
          Return: false
          
          → Line 473 condition: FALSE
          → Block NOT executed (no error)

Line 477: boolean success = scheduleDAO.createSchedule(5, 3, 2, 2026-03-22, 1, false);
          → Call ScheduleDAO.createSchedule()

          ScheduleDAO.java Line 114:
          Line 117: if (hasClassSlotConflict(5, 2, 2026-03-22, -1))
                   → Result: false (no conflict)
                   → Block NOT executed

          Line 122: INSERT INTO Schedule (ClassID, RoomID, SlotID, LearningDate, TeacherID, AttendanceStatus)
                    VALUES (5, 3, 2, 2026-03-22, 1, false);
          → executeUpdate() > 0
          → Return: true

          Line 480: if (true)
                    → Block EXECUTED
                    → Line 481: session.setAttribute("message", "Schedule created successfully!");
                    → Line 482: session.setAttribute("messageType", "success");

Line 490: response.sendRedirect("schedule?action=manage&classId=5&roomId=0&date=2026-03-22");

Result: ✅ 1 schedule created successfully
        ✅ User redirected to manage page with filters saved
        ✅ Success message displayed
```

---

## 3. CREATE ACTION - Batch/Recurring Schedule

```
┌─────────────────────────────────────────────────────────────────┐
│ ScheduleController.java - doPost() - CREATE BATCH ACTION        │
└─────────────────────────────────────────────────────────────────┘

Line 433: String recurringType = "weekly"

Line 434: if ("weekly" == null || "weekly".isEmpty())
          → FALSE
          → recurringType = "weekly" (unchanged)

Line 442: if (!"none".equals("weekly"))
          → !"none".equals("weekly") = true
          → Block EXECUTED (batch creation) ✅

Line 444-457: BATCH SCHEDULE BLOCK

Line 445: String endCondition = request.getParameter("endCondition");
          → "after"
Line 446: String endDateStr = request.getParameter("endDate");
          → null (not used for "after")
Line 447: String occurrencesStr = request.getParameter("occurrences");
          → "8"

Line 449: Date endDate = null;
Line 450: Integer occurrences = null;

Line 452: if ("on".equals("after"))
          → FALSE
          → endDate remains null

Line 456: if ("after".equals("after"))
          → TRUE
          → Line 457: occurrences = Integer.parseInt("8");
                      occurrences = 8

Line 459-462: Get custom days (skip for weekly pattern)
              recurringDays = ""

Line 465: int createdCount = scheduleDAO.createMultipleSchedules(
                                  5, 3, 2, 2026-03-22, 1,
                                  "weekly", "", "after", null, 8
                              );

┌─────────────────────────────────────────────────────────────────┐
│ ScheduleDAO.java - createMultipleSchedules()                    │
│ Line 175                                                         │
└─────────────────────────────────────────────────────────────────┘

Line 179: List<Date> scheduleDates = generateScheduleDates(
              2026-03-22, "weekly", "", "after", null, 8);

          Call generateScheduleDates():
          Line 253: calendar.setTime(2026-03-22)
          Line 254: startDayOfWeek = SUNDAY (day of week for 22/3/2026)
          
          Loop iterations:
            Iteration 1: Check if SUNDAY pattern + within 8 occurrences
                        → 22/3 is SUNDAY ✅ → Add to list
                        dates = [22/3]
            Iteration 2: 23/3 is MONDAY ❌ pattern mismatch
            Iteration 3: 24/3 is TUESDAY ❌
            ...
            Iteration 8: 29/3 is SUNDAY ✅ → Add to list
                        dates = [22/3, 29/3]
            ...continuing for 8 occurrences total...
            dates = [22/3, 29/3, 5/4, 12/4, 19/4, 26/4, 3/5, 10/5]

          Result: scheduleDates = [22/3, 29/3, 5/4, 12/4, 19/4, 26/4, 3/5, 10/5]

Line 183: if (scheduleDates.isEmpty())
          → FALSE (have 8 dates)
          → Block NOT executed

Line 195: conn.setAutoCommit(false);
Line 196: int createdCount = 0;

Line 198: try (PreparedStatement ps = ...)
Line 199: for (Date date : scheduleDates)

          ┌─────────────────────────────────────┐
          │ ITERATION 1: date = 22/3            │
          └─────────────────────────────────────┘
          
          Line 201: if (hasClassSlotConflict(5, 2, 22/3, -1))
                    → SELECT COUNT(*) FROM Schedule WHERE ...
                    → count = 0
                    → FALSE
                    → Block NOT executed

          Line 206: if (!isRoomAvailable(3, 2, 22/3, -1))
                    → SELECT COUNT(*) FROM Schedule WHERE ...
                    → count = 0
                    → isRoomAvailable() returns true
                    → !true = FALSE
                    → Block NOT executed

          Line 211: if (!isTeacherAvailable(1, 2, 22/3, -1))
                    → SELECT COUNT(*) FROM Schedule WHERE ...
                    → count = 0
                    → isTeacherAvailable() returns true
                    → !true = FALSE
                    → Block NOT executed

          Line 216: if (teacherExceedsWeeklyLimit(1, 22/3, -1))
                    → SELECT COUNT(*) FROM Schedule WHERE ... (week of 22/3)
                    → count = 3
                    → 3 >= 5 ? FALSE
                    → Block NOT executed

          Line 223: ps.setInt(1, 5);
          Line 224: ps.setInt(2, 3);
          Line 225: ps.setInt(3, 2);
          Line 226: ps.setDate(4, 22/3);
          Line 227: ps.setInt(5, 1);
          Line 228: ps.setBoolean(6, false);
          Line 229: ps.addBatch();  ✅ Queued

          ┌─────────────────────────────────────┐
          │ ITERATION 2: date = 29/3            │
          └─────────────────────────────────────┘
          → All checks pass
          → ps.addBatch() ✅ Queued

          ┌─────────────────────────────────────┐
          │ ITERATION 3: date = 5/4             │
          └─────────────────────────────────────┘
          Line 216: if (teacherExceedsWeeklyLimit(1, 5/4, -1))
                    → SELECT COUNT(*) FROM Schedule WHERE ... (week of 5/4)
                    → count = 5 (teacher already has 5 slots that week)
                    → 5 >= 5 ? TRUE ✅
                    → Block EXECUTED
                    Line 217: System.out.println("Warning: Teacher exceeds weekly limit...");
                    Line 218: continue;  → Jump to next iteration (skip 5/4)

          ┌─────────────────────────────────────┐
          │ ITERATION 4: date = 12/4            │
          └─────────────────────────────────────┘
          → All checks pass
          → ps.addBatch() ✅ Queued

          ┌─────────────────────────────────────┐
          │ ITERATION 5: date = 19/4            │
          └─────────────────────────────────────┘
          Line 206: if (!isRoomAvailable(3, 2, 19/4, -1))
                    → SELECT COUNT(*) FROM Schedule WHERE ...
                    → count = 1 (room already booked)
                    → isRoomAvailable() returns false
                    → !false = TRUE ✅
                    → Block EXECUTED
                    Line 207: System.out.println("Warning: Room is not available...");
                    Line 208: continue;  → Jump to next iteration (skip 19/4)

          ┌─────────────────────────────────────┐
          │ ITERATION 6: date = 26/4            │
          └─────────────────────────────────────┘
          → All checks pass
          → ps.addBatch() ✅ Queued

          ┌─────────────────────────────────────┐
          │ ITERATION 7: date = 3/5             │
          └─────────────────────────────────────┘
          → All checks pass
          → ps.addBatch() ✅ Queued

          ┌─────────────────────────────────────┐
          │ ITERATION 8: date = 10/5            │
          └─────────────────────────────────────┘
          → All checks pass
          → ps.addBatch() ✅ Queued

Line 231: int[] results = ps.executeBatch();
          → INSERT 6 records (22/3, 29/3, 12/4, 26/4, 3/5, 10/5)
          → Skipped: 5/4, 19/4

Line 232: conn.commit();  ✅ Transaction committed

Line 235: for (int result : results)
Line 236: if (result > 0)
Line 237: createdCount++;
          
          Results: [1, 1, 1, 1, 1, 1] (6 successful inserts)
          createdCount = 6

Line 239: return 6;

Back to Controller Line 465:
int createdCount = 6;

Line 468: if (6 > 0) {
          → TRUE
          → Line 469: session.setAttribute("message", "Successfully created 6 schedule(s)!");
          → Line 470: session.setAttribute("messageType", "success");

Result: ✅ 6 schedules created successfully
        ⚠️ 2 dates skipped (5/4, 19/4)
        ✅ User sees success message with count
        ✅ Console shows warning messages
```

---

## 4. UPDATE ACTION - Comparison with CREATE

```
┌─────────────────────────────────────────────────────────────────┐
│ Key Difference: excludeScheduleId parameter                     │
└─────────────────────────────────────────────────────────────────┘

CREATE SINGLE (Line 454):
  hasScheduleConflict(classId=5, slotId=2, date, excludeScheduleId=-1)
  
UPDATE (Line 546):
  hasScheduleConflict(classId=5, slotId=2, date, excludeScheduleId=42)
                                                                    ↑ Current schedule ID

Why different?
  - CREATE: No existing record, use -1 (sentinel value)
  - UPDATE: Must exclude current record (ID 42) from the check
            Otherwise, it would conflict with itself!

Example:
  Existing Schedule 42: Class 5, Slot 2, Date 22/3

  UPDATE Case 1 - Changing room (3 → 4):
  Line 546: hasScheduleConflict(5, 2, 22/3, 42)
  SELECT COUNT(*) FROM Schedule 
  WHERE ClassID=5 AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != 42
  
  Result: count = 0 (no OTHER schedules with same class/slot/date)
  Return: false (no conflict) ✅
  → Can update

  UPDATE Case 2 - Another schedule already exists:
  SELECT COUNT(*) FROM Schedule 
  WHERE ClassID=5 AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != 42
  
  Result: count = 1 (Schedule 50 also has class 5, slot 2, date 22/3)
  Return: true (conflict exists) ❌
  → Cannot update
```

---

## 5. DELETE ACTION - Attendance Check

```
┌─────────────────────────────────────────────────────────────────┐
│ ScheduleController.java - doPost() - DELETE ACTION              │
└─────────────────────────────────────────────────────────────────┘

Line 596: case "delete":
Line 599: int scheduleId = Integer.parseInt("42");
          scheduleId = 42

Line 600: String deleteScope = request.getParameter("deleteScope");
          deleteScope = "single" (or "series")

Line 603: Schedule schedule = scheduleDAO.getScheduleById(42);
          → Fetch schedule details

Line 604: if (schedule != null && schedule.isAttendanceStatus())
          → Is attendance already taken?
          
          Scenario A: attendanceStatus = false (not taken)
          → Line 604 condition: FALSE
          → Block NOT executed
          → Continue to delete

          Scenario B: attendanceStatus = true (taken)
          → Line 604 condition: TRUE
          → Block EXECUTED
          → Line 605: session.setAttribute("message", 
                      "Cannot delete schedule with attendance already taken!");
          → Line 606: session.setAttribute("messageType", "error");
          → Line 607: response.sendRedirect("schedule?action=manage");
          → Line 608: return;  ❌ EXIT (do not delete)

Line 611: if ("series".equals(deleteScope))
          
          Case A: deleteScope = "series"
          → TRUE
          → Block EXECUTED
          Line 612: int deletedCount = scheduleDAO.deleteSimilarSchedules(42);
          
          ScheduleDAO.java deleteSimilarSchedules():
            Line 956: List<Schedule> similarSchedules = getSimilarSchedules(42);
            
            getSimilarSchedules() returns:
            [Schedule 42, Schedule 51, Schedule 59] (same class/slot/room)
            
            Line 967: for (Schedule sch : similarSchedules)
            Line 969: if (!schedule.isAttendanceStatus()) (not attended)
                      → Delete this one
            
            Assume all 3 have no attendance:
            → Line 970: ps.setInt(1, 42);
            → Line 971: executeUpdate() → 1 row deleted
            → deletedCount = 1
            
            → Line 970: ps.setInt(1, 51);
            → Line 971: executeUpdate() → 1 row deleted
            → deletedCount = 2
            
            → Line 970: ps.setInt(1, 59);
            → Line 971: executeUpdate() → 1 row deleted
            → deletedCount = 3
            
            return 3;
          
          Line 616: if (3 > 0)
                    → TRUE
                    → Line 617: session.setAttribute("message", 
                                "Successfully deleted 3 schedule(s) in the series!");
          
          Case B: deleteScope = "single"
          → FALSE
          → else Block at Line 619
          Line 621: boolean success = scheduleDAO.deleteSchedule(42);
          
          ScheduleDAO.java deleteSchedule():
            DELETE FROM Schedule WHERE ScheduleID = 42
            → 1 row deleted
            return true;
          
          Line 625: if (true)
                    → TRUE
                    → Line 626: session.setAttribute("message",
                                "Schedule deleted successfully!");

Result: ✅ Schedule(s) deleted successfully
        ✅ User redirected to manage page
        ✅ Success message displayed
```

---

## 📊 PARAMETER FLOW SUMMARY

```
MANAGE:
  Input: classId=0, roomId=0, date=2026-03-22
  ↓ Line 127-134: Set classFilterId=null, roomFilterId=null
  ↓ Line 139: getSchedulesForManagement(date, null, null)
  ↓ SQL WHERE: Only date filter
  → Output: Full week schedule ✅

MANAGE:
  Input: classId=5, roomId=0, date=2026-03-22
  ↓ Line 127-130: Set classFilterId=5
  ↓ Line 133-136: roomFilterId=null (not set)
  ↓ Line 139: getSchedulesForManagement(date, 5, null)
  ↓ SQL WHERE: Date filter + ClassID=5
  → Output: Class 5 only ✅

CREATE SINGLE:
  Input: recurringType=none (default)
  ↓ Line 442: Skip batch block
  ↓ Line 454-475: Execute single validation chain
  ↓ Line 477: createSchedule(classId, roomId, slotId, date, teacherId, false)
  → Output: 1 schedule or error ✅

CREATE BATCH:
  Input: recurringType=weekly, occurrences=8
  ↓ Line 442: Enter batch block
  ↓ Line 450: createMultipleSchedules(..., "weekly", ..., null, 8)
  ↓ Loop each date, skip conflicts, addBatch() pass ones
  ↓ executeBatch(): Insert all queued records
  → Output: N schedules (some may be skipped) ✅

UPDATE:
  Input: scheduleId=42, new values
  ↓ Line 530-535: Fetch existing schedule
  ↓ Line 546-567: Validation chain with excludeScheduleId=42
  ↓ Line 583: editSchedule() with scheduleId=42
  → Output: 1 updated record or error ✅

DELETE:
  Input: scheduleId=42, deleteScope=single
  ↓ Line 603: Check attendance status
  ↓ Line 611-619: Check deleteScope
  ↓ Line 621: deleteSchedule(42)
  → Output: 1 deleted record or error ✅
```

All flows are now **consistent and validated**! 🎯

