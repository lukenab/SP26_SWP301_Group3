# 🔄 SCHEDULE MANAGEMENT - DETAILED FLOW DIAGRAM

## 1. MANAGE ACTION - Filter Logic Flow

```
┌─────────────────────────────────────────────────────────┐
│ User clicks: Manage Schedule with filters               │
└─────────────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────────────┐
│ ScheduleController.doGet() - Line 115-153              │
│ action = "manage"                                       │
└─────────────────────────────────────────────────────────┘
                        ↓
    ┌───────────────────┴───────────────────┐
    ↓                                       ↓
Line 122: Get filterClassId            Line 123: Get filterRoomId
from request parameter                 from request parameter
                        ↓
    ┌───────────────────┴───────────────────┐
    ↓                                       ↓
┌──────────────────────┐             ┌──────────────────────┐
│ classId = "0"?       │             │ roomId = "0"?        │
└──────────────────────┘             └──────────────────────┘
    ↓ YES  ↓ NO                          ↓ YES  ↓ NO
    │      │                             │      │
 null   5   ↓                         null   3   ↓
    │      └─→ Integer.parseInt(5)        │      └─→ Integer.parseInt(3)
    │         classFilterId = 5           │         roomFilterId = 3
    └──→ classFilterId = null             └──→ roomFilterId = null

                        ↓
┌─────────────────────────────────────────────────────────┐
│ Line 139: getSchedulesForManagement(date, classId, roomId)
└─────────────────────────────────────────────────────────┘
                        ↓
        ScheduleDAO.java - Line 643


📌 SCENARIO 1: classId=null, roomId=null
───────────────────────────────────────
Line 652-653: if (classId != null && classId > 0) 
             → FALSE (null != null is FALSE)
             → WHERE clause NOT added
             
Line 657-659: if (roomId != null && roomId > 0)
             → FALSE
             → WHERE clause NOT added

SQL Query:
  SELECT s.* FROM Schedule s
  INNER JOIN Class c ON s.ClassID = c.ClassID
  INNER JOIN Course co ON c.CourseID = co.CourseID
  LEFT JOIN [User] u ON s.TeacherID = u.UserID
  WHERE 1=1
  AND s.LearningDate >= DATEADD(day, -(DATEPART(weekday, ?) - 2), CAST(? AS DATE))
  AND s.LearningDate < DATEADD(day, 7 - (DATEPART(weekday, ?) - 2), CAST(? AS DATE))
  -- NO CLASS FILTER
  -- NO ROOM FILTER
  ORDER BY s.LearningDate ASC, s.SlotID ASC

Result: ✅ FULL SCHEDULE OF ENTIRE WEEK (All Classes, All Rooms)


📌 SCENARIO 2: classId=5, roomId=null
─────────────────────────────────────
Line 652-653: if (classId != null && classId > 0)
             → TRUE (5 != null AND 5 > 0)
             → SQL: AND s.ClassID = ?
             → ps.setInt(paramIndex++, 5)

Line 657-659: if (roomId != null && roomId > 0)
             → FALSE
             → WHERE clause NOT added

SQL Query:
  SELECT s.* FROM Schedule s ...
  WHERE 1=1
  AND s.LearningDate >= ... (week check)
  AND s.ClassID = 5  ✅ ADDED
  ORDER BY s.LearningDate ASC, s.SlotID ASC

Result: ✅ CLASS 5 ONLY (all rooms, all slots in that class)


📌 SCENARIO 3: classId=null, roomId=3
──────────────────────────────────────
Line 652-653: if (classId != null && classId > 0)
             → FALSE
             → WHERE clause NOT added

Line 657-659: if (roomId != null && roomId > 0)
             → TRUE (3 != null AND 3 > 0)
             → SQL: AND s.RoomID = ?
             → ps.setInt(paramIndex++, 3)

SQL Query:
  SELECT s.* FROM Schedule s ...
  WHERE 1=1
  AND s.LearningDate >= ... (week check)
  AND s.RoomID = 3  ✅ ADDED
  ORDER BY s.LearningDate ASC, s.SlotID ASC

Result: ✅ ROOM 3 ONLY (all classes using room 3)


📌 SCENARIO 4: classId=5, roomId=3
──────────────────────────────────
Line 652-653: if (classId != null && classId > 0)
             → TRUE
             → SQL: AND s.ClassID = ?
             → ps.setInt(paramIndex++, 5)

Line 657-659: if (roomId != null && roomId > 0)
             → TRUE
             → SQL: AND s.RoomID = ?
             → ps.setInt(paramIndex++, 3)

SQL Query:
  SELECT s.* FROM Schedule s ...
  WHERE 1=1
  AND s.LearningDate >= ... (week check)
  AND s.ClassID = 5  ✅ ADDED
  AND s.RoomID = 3   ✅ ADDED
  ORDER BY s.LearningDate ASC, s.SlotID ASC

Result: ✅ CLASS 5 + ROOM 3 ONLY (intersection)
```

---

## 2. CREATE ACTION - Single Schedule Validation Chain

```
┌─────────────────────────────────────────────────────┐
│ POST /schedule?action=create                        │
│ classId=5, roomId=3, slotId=2, learningDate=22/3/26
│ recurringType=none (default single)                 │
└─────────────────────────────────────────────────────┘
                        ↓
Line 429-434: Parse parameters
              ↓
Line 438: recurringType == "none" ?
              ↓ YES
┌─────────────────────────────────────────────────────┐
│ Line 459-475: Single Schedule Block                 │
└─────────────────────────────────────────────────────┘
                        ↓
                   ✅ CHECK 1
        ┌──────────────────────────────────┐
        │ Line 454: hasScheduleConflict()   │
        │ (5, 2, 22/3, excludeId=-1)       │
        └──────────────────────────────────┘
                   ↓
    ScheduleDAO.java Line 421-432:
    SELECT COUNT(*) FROM Schedule 
    WHERE ClassID=5 AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != -1
    
    IF count > 0:
        ❌ ERROR: "Schedule conflict..."
        ❌ REDIRECT: schedule?action=manage
        ❌ STOP HERE (do not continue to checks 2,3,4)
    
    IF count = 0:
        ✅ PASS → Continue to CHECK 2


                   ✅ CHECK 2
        ┌──────────────────────────────────┐
        │ Line 461: isRoomAvailable()       │
        │ (3, 2, 22/3, excludeId=-1)       │
        └──────────────────────────────────┘
                   ↓
    ScheduleDAO.java Line 373-386:
    SELECT COUNT(*) FROM Schedule 
    WHERE RoomID=3 AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != -1
    
    IF count > 0:
        ❌ ERROR: "Room is not available..."
        ❌ REDIRECT
        ❌ STOP HERE
    
    IF count = 0:
        ✅ PASS → Continue to CHECK 3


                   ✅ CHECK 3
        ┌──────────────────────────────────┐
        │ Line 467: isTeacherAvailable()    │
        │ (teacherId, 2, 22/3, -1)         │
        └──────────────────────────────────┘
                   ↓
    ScheduleDAO.java Line 396-409:
    SELECT COUNT(*) FROM Schedule 
    WHERE TeacherID=X AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != -1
    
    IF count > 0:
        ❌ ERROR: "Teacher is not available..."
        ❌ REDIRECT
        ❌ STOP HERE
    
    IF count = 0:
        ✅ PASS → Continue to CHECK 4


                   ✅ CHECK 4
        ┌──────────────────────────────────┐
        │ Line 473: teacherExceedsWeeklyLimit()
        │ (teacherId, 22/3, -1)            │
        └──────────────────────────────────┘
                   ↓
    ScheduleDAO.java Line 468-482:
    SELECT COUNT(*) FROM Schedule 
    WHERE TeacherID=X 
    AND LearningDate BETWEEN (Monday) AND (Sunday)
    AND ScheduleID != -1
    
    IF count >= 5:
        ❌ ERROR: "Teacher has reached weekly limit! Current: 5/5"
        ❌ REDIRECT
        ❌ STOP HERE
    
    IF count < 5:
        ✅ PASS → Continue to CREATE


                   ✅ CREATE
        ┌──────────────────────────────────┐
        │ Line 477: createSchedule()        │
        │ (5, 3, 2, 22/3, teacherId, false)│
        └──────────────────────────────────┘
                   ↓
    ScheduleDAO.java Line 114-131:
    → Line 117: hasClassSlotConflict() (double-check!)
       SELECT COUNT(*) FROM Schedule 
       WHERE ClassID=5 AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != -1
       
       IF count > 0:
           ❌ Return false (fail to create)
       
       IF count = 0:
           ✅ Line 122: INSERT INTO Schedule VALUES(...)
           ✅ Return true
    
    Back to Controller Line 480:
    if (success) {
        ✅ "Schedule created successfully!"
    }
                   ↓
        ┌──────────────────────────────────┐
        │ ✅ SUCCESS: 1 schedule created    │
        │ REDIRECT: manage?classId=5...    │
        └──────────────────────────────────┘
```

---

## 3. CREATE ACTION - Batch/Recurring Schedule

```
┌─────────────────────────────────────────────────────┐
│ POST /schedule?action=create                        │
│ classId=5, roomId=3, slotId=2, learningDate=22/3/26
│ recurringType=weekly                                │
│ endCondition=after, occurrences=8                   │
└─────────────────────────────────────────────────────┘
                        ↓
Line 438: recurringType != "none" ?
              ↓ YES
┌─────────────────────────────────────────────────────┐
│ Line 443-457: Batch Schedule Block                  │
└─────────────────────────────────────────────────────┘
                        ↓
Line 450: createMultipleSchedules(
           5, 3, 2, 22/3, teacherId,
           "weekly", "", "after", null, 8)
                        ↓
    ScheduleDAO.java Line 175-230:
    
    ┌─────────────────────────────────────┐
    │ Line 179-184: generateScheduleDates()│
    │ "weekly", startDate=22/3, occurrences=8
    └─────────────────────────────────────┘
                        ↓
    Result: [22/3, 29/3, 5/4, 12/4, 19/4, 26/4, 3/5, 10/5]
                        ↓
    ┌─────────────────────────────────────┐
    │ Line 195-225: for (Date date : scheduleDates)
    │ Loop through each date              │
    └─────────────────────────────────────┘
                        ↓


    📌 ITERATION 1: date = 22/3
    ─────────────────────────────
    Line 198: hasClassSlotConflict(5, 2, 22/3, -1)
        SELECT COUNT(*) FROM Schedule 
        WHERE ClassID=5 AND SlotID=2 AND LearningDate=22/3
        
        IF count > 0:
            ❌ SKIP this date
            continue; → Jump to next iteration
        
        IF count = 0:
            ✅ PASS → Continue

    Line 203: isRoomAvailable(3, 2, 22/3, -1)
        SELECT COUNT(*) FROM Schedule 
        WHERE RoomID=3 AND SlotID=2 AND LearningDate=22/3
        
        IF count > 0:
            ❌ SKIP
            continue;
        
        IF count = 0:
            ✅ PASS → Continue

    Line 208: isTeacherAvailable(teacherId, 2, 22/3, -1)
        SELECT COUNT(*) FROM Schedule 
        WHERE TeacherID=X AND SlotID=2 AND LearningDate=22/3
        
        IF count > 0:
            ❌ SKIP
            continue;
        
        IF count = 0:
            ✅ PASS → Continue

    Line 213: teacherExceedsWeeklyLimit(teacherId, 22/3, -1)
        SELECT COUNT(*) FROM Schedule 
        WHERE TeacherID=X AND LearningDate BETWEEN (week of 22/3)
        
        IF count >= 5:
            ❌ SKIP
            continue;
        
        IF count < 5:
            ✅ PASS → addBatch

    Line 218-223: addBatch()
        ps.setInt(1, 5);
        ps.setInt(2, 3);
        ps.setInt(3, 2);
        ps.setDate(4, 22/3);
        ps.setInt(5, teacherId);
        ps.setBoolean(6, false);
        ps.addBatch(); ✅ Queued for insert


    📌 ITERATION 2: date = 29/3
    ─────────────────────────────
    → All checks pass → addBatch() ✅


    📌 ITERATION 3: date = 5/4
    ─────────────────────────────
    Line 213: teacherExceedsWeeklyLimit(teacherId, 5/4, -1)
        SELECT COUNT(*) FROM Schedule 
        WHERE TeacherID=X AND LearningDate BETWEEN (Monday 3/4) AND (Sunday 9/4)
        
        Result: 5 schedules already in this week
        
        IF count >= 5:
            ❌ true (5 >= 5)
            ❌ SKIP
            continue;
            print "Warning: Teacher exceeds weekly limit..."


    📌 ITERATION 4: date = 12/4
    ─────────────────────────────
    → All checks pass → addBatch() ✅


    📌 ITERATION 5: date = 19/4
    ─────────────────────────────
    Line 203: isRoomAvailable(3, 2, 19/4, -1)
        SELECT COUNT(*) FROM Schedule 
        WHERE RoomID=3 AND SlotID=2 AND LearningDate=19/4
        
        Result: 1 (room already booked!)
        
        IF count > 0:
            ❌ SKIP
            continue;
            print "Warning: Room is not available..."


    📌 ITERATION 6: date = 26/4
    ─────────────────────────────
    → All checks pass → addBatch() ✅


    📌 ITERATION 7: date = 3/5
    ─────────────────────────────
    → All checks pass → addBatch() ✅


    📌 ITERATION 8: date = 10/5
    ─────────────────────────────
    → All checks pass → addBatch() ✅


    After all iterations:
    ├─ 22/3 ✅ queued
    ├─ 29/3 ✅ queued
    ├─ 5/4  ❌ skipped (teacher weekly limit)
    ├─ 12/4 ✅ queued
    ├─ 19/4 ❌ skipped (room occupied)
    ├─ 26/4 ✅ queued
    ├─ 3/5  ✅ queued
    └─ 10/5 ✅ queued

                        ↓
    Line 224: ps.executeBatch()
        INSERT INTO Schedule (ClassID, RoomID, SlotID, LearningDate, ...)
        VALUES (5, 3, 2, 22/3, ...), (5, 3, 2, 29/3, ...), ...
        
        Result: 6 rows inserted
                        ↓
    Line 225: conn.commit()
        ✅ Transaction committed
                        ↓
    createdCount = 6
                        ↓
    Back to Controller Line 453:
    if (createdCount > 0) {
        ✅ "Successfully created 6 schedule(s)!"
        ✅ REDIRECT: manage
    }
```

---

## 4. UPDATE ACTION - Validation with Exclusion

```
┌─────────────────────────────────────────────────────┐
│ POST /schedule?action=update                        │
│ scheduleId=42 (Existing record to update)           │
│ classId=5, roomId=4, slotId=2, learningDate=22/3/26 │
│ (Changing from: room 3 → room 4)                    │
└─────────────────────────────────────────────────────┘
                        ↓
Line 530-535: Parse & fetch existing schedule
                        ↓
┌─────────────────────────────────────────────────────┐
│ IMPORTANT DIFFERENCE: excludeScheduleId = 42        │
│ (Not -1 like single create!)                        │
└─────────────────────────────────────────────────────┘
                        ↓

                   ✅ CHECK 1
    Line 546: hasScheduleConflict(5, 2, 22/3, scheduleId=42)
    
    ScheduleDAO.java Line 421:
    SELECT COUNT(*) FROM Schedule 
    WHERE ClassID=5 AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != 42
                                                                       ↑ KEY!
    
    This query EXCLUDES schedule ID 42 from the check!
    
    Scenario A: No other conflicts
        count = 0 → ✅ PASS (can update)
    
    Scenario B: Another schedule exists (ID != 42)
        e.g., Schedule ID 50 also has Class 5, Slot 2, Date 22/3
        count = 1 → ❌ FAIL (true conflict exists)
                   → REDIRECT


                   ✅ CHECK 2
    Line 553: isRoomAvailable(4, 2, 22/3, scheduleId=42)
    
    ScheduleDAO.java Line 373:
    SELECT COUNT(*) FROM Schedule 
    WHERE RoomID=4 AND SlotID=2 AND LearningDate=22/3 AND ScheduleID != 42
                                                                       ↑ KEY!
    
    This query excludes the current schedule 42!
    So it checks if ANY OTHER schedule is using room 4
    
    Scenario: Schedule 42 itself uses room 4 → NOT counted!
    
    Scenario: Schedule 50 uses room 4 → count = 1 → ❌ FAIL


                   ✅ CHECK 3 & 4
    Same pattern: all checks use excludeScheduleId = 42


    ✅ ALL CHECKS PASS
    Line 583: editSchedule(42, 5, 4, 2, 22/3, ...)
    
    ScheduleDAO.java Line 127:
    UPDATE Schedule 
    SET ClassID=5, RoomID=4, SlotID=2, LearningDate=22/3, ...
    WHERE ScheduleID = 42
    
    ✅ "Schedule updated successfully!"
    ✅ REDIRECT: manage
```

---

## 5. DELETE ACTION - Attendance Check

```
┌─────────────────────────────────────────────────────┐
│ POST /schedule?action=delete                        │
│ scheduleId=42                                       │
│ deleteScope=single (or "series")                    │
└─────────────────────────────────────────────────────┘
                        ↓
Line 602-603: Check if attendance taken
    if (schedule.isAttendanceStatus() == true)
        ❌ ERROR: "Cannot delete schedule with attendance already taken!"
        ❌ REDIRECT
        ❌ STOP HERE


                   ✅ IF NO ATTENDANCE
    Line 609: if ("series".equals(deleteScope))
        
        ✅ SERIES DELETE
        Line 610: deleteSimilarSchedules(42)
        
        ScheduleDAO.java Line 956:
        → getSimilarSchedules(42)
            SELECT s2.* FROM Schedule s1
            INNER JOIN Schedule s2 ON
                s1.ClassID = s2.ClassID AND
                s1.RoomID = s2.RoomID AND
                s1.SlotID = s2.SlotID
            WHERE s1.ScheduleID = 42
            
            Result: All schedules with same Class/Room/Slot
            e.g., [ID 42, ID 51, ID 59, ...]
        
        → Loop through each similar schedule
            IF AttendanceStatus = false:
                DELETE FROM Schedule WHERE ScheduleID = ...
                deletedCount++
        
        Result: 3 schedules deleted
        ✅ "Successfully deleted 3 schedule(s) in the series!"
    
    
    ✅ SINGLE DELETE
    Line 619: else
        Line 621: deleteSchedule(42)
        
        ScheduleDAO.java Line 142:
        DELETE FROM Schedule WHERE ScheduleID = 42
        
        Result: 1 schedule deleted
        ✅ "Schedule deleted successfully!"
```

---

## 📊 Decision Tree Summary

```
START (User Action)
│
├─ MANAGE?
│  ├─ classId=0, roomId=0 → Show ALL ✅
│  ├─ classId=X, roomId=0 → Show Class X only
│  ├─ classId=0, roomId=Y → Show Room Y only
│  └─ classId=X, roomId=Y → Show Class X + Room Y
│
├─ CREATE?
│  ├─ recurringType=none?
│  │  └─ Single Schedule
│  │     ├─ hasConflict? → FAIL ❌
│  │     ├─ roomOccupied? → FAIL ❌
│  │     ├─ teacherBusy? → FAIL ❌
│  │     ├─ weeklyLimitExceeded? → FAIL ❌
│  │     └─ All pass? → INSERT 1 record ✅
│  │
│  └─ recurringType=weekly/daily/etc?
│     └─ Batch Schedule
│        └─ For each date:
│           ├─ hasConflict? → SKIP date
│           ├─ roomOccupied? → SKIP date
│           ├─ teacherBusy? → SKIP date
│           ├─ weeklyLimitExceeded? → SKIP date
│           └─ All pass? → addBatch
│        └─ executeBatch() → INSERT multiple ✅
│
├─ UPDATE?
│  ├─ Same checks as CREATE
│  │  (but with excludeScheduleId = scheduleId)
│  └─ If all pass → UPDATE record ✅
│
└─ DELETE?
   ├─ Has attendance? → FAIL ❌
   ├─ deleteScope=series? → DELETE all similar
   └─ deleteScope=single? → DELETE 1 record ✅
```

---

## 🎯 KEY EXCLUSION LOGIC

| Operation | excludeScheduleId | Why |
|-----------|-------------------|-----|
| CREATE single | -1 | No existing record to exclude |
| CREATE batch | -1 | Creating new dates |
| UPDATE | scheduleId | Exclude current record being edited |
| DELETE | (not used) | Just delete the record |

This ensures the system doesn't report conflicts with itself! 🎯

