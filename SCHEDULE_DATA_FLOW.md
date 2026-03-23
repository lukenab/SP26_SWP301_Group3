# 📊 SCHEDULE MANAGEMENT - DATA FLOW ANALYSIS

## 🎯 Tổng Quan
System này quản lý lịch học với validation phức tạp. Luồng dữ liệu phụ thuộc vào:
- **Action**: manage, create, update, delete
- **ClassId & RoomId**: Giá trị filter (0 = all, >0 = specific)
- **Recurring**: none (single) hoặc recurring types (daily, weekly, etc.)

---

## 1️⃣ **MANAGE ACTION (Xem Danh Sách Lịch)**

### 📍 Controller: `ScheduleController.java` (dòng 115-153)

```
GET /schedule?action=manage&classId=X&roomId=Y&date=YYYY-MM-DD
        ↓
```

### ✅ **Scenario 1: classId=0 & roomId=0 (All Classes, All Rooms)**

**Dòng thực thi:**
```
Line 127: filterClassId = null  ❌ (Không filter class)
Line 134: roomFilterId = null   ❌ (Không filter room)
Line 139: getSchedulesForManagement(selectedDate, null, null)
          ↓
ScheduleDAO.java (dòng 650):
    Line 652-653: if (classId != null && classId > 0) → SKIP ❌ (null không thỏa)
    Line 657-659: if (roomId != null && roomId > 0) → SKIP ❌ (null không thỏa)
    Line 661: ORDER BY s.LearningDate ASC, s.SlotID ASC
    ↓
SELECT từ DB: **TOÀN BỘ SCHEDULE CỦA TUẦN** ✅
```

**Kết quả:** Hiển thị **full schedule** cả tuần, tất cả lớp, tất cả phòng

---

### ✅ **Scenario 2: classId=5 & roomId=0 (Specific Class, All Rooms)**

**Dòng thực thi:**
```
Line 125: filterClassId = 5 ✅ (Có giá trị class)
Line 133: roomFilterId = null ❌ (Không filter room)
Line 139: getSchedulesForManagement(selectedDate, 5, null)
          ↓
ScheduleDAO.java (dòng 650):
    Line 656: ps.setInt(paramIndex++, 5)  ✅
    Line 657-659: if (roomId != null && roomId > 0) → SKIP ❌
    ↓
SELECT ... WHERE 1=1 AND s.LearningDate >= ... AND s.ClassID = 5
    ↓
**Lịch của CLASS 5 toàn bộ phòng trong tuần**
```

**Kết quả:** Chỉ show Class 5, mọi phòng

---

### ✅ **Scenario 3: classId=0 & roomId=3 (All Classes, Specific Room)**

**Dòng thực thi:**
```
Line 127: filterClassId = null ❌
Line 131: roomFilterId = 3 ✅
Line 139: getSchedulesForManagement(selectedDate, null, 3)
          ↓
ScheduleDAO.java (dòng 650):
    Line 652-653: if (classId != null) → SKIP ❌
    Line 658: ps.setInt(paramIndex++, 3) ✅
    ↓
SELECT ... WHERE 1=1 AND s.LearningDate >= ... AND s.RoomID = 3
    ↓
**Tất cả lớp, CHỈ ROOM 3**
```

**Kết quả:** Hiển thị Schedule của Room 3, mọi class

---

### ✅ **Scenario 4: classId=5 & roomId=3 (Both Specific)**

**Dòng thực thi:**
```
Line 125: filterClassId = 5 ✅
Line 131: roomFilterId = 3 ✅
Line 139: getSchedulesForManagement(selectedDate, 5, 3)
          ↓
ScheduleDAO.java:
    Line 656: ps.setInt(paramIndex++, 5) ✅
    Line 658: ps.setInt(paramIndex++, 3) ✅
    ↓
SELECT ... WHERE 1=1 AND s.ClassID = 5 AND s.RoomID = 3
    ↓
**CHỈ Schedule của Class 5 trong Room 3**
```

**Kết quả:** Chỉ show Class 5 + Room 3 combination

---

## 2️⃣ **CREATE ACTION - SINGLE SCHEDULE**

### 📍 Controller: `ScheduleController.java` (dòng 423-475)

```
POST /schedule?action=create
    classId=5, roomId=3, slotId=2, learningDate=2026-03-22
    recurringType=none (default)
        ↓
Line 441: recurringType = "none" (không recurring)
Line 459-475: Vào block SINGLE schedule
```

### ✅ **Validation Chain (Thứ tự kiểm tra)**

```
1️⃣ Line 454: hasScheduleConflict(classId=5, slotId=2, date=2026-03-22, excludeScheduleId=-1)
   ScheduleDAO.java (dòng 421):
   SELECT COUNT(*) FROM Schedule 
   WHERE ClassID=5 AND SlotID=2 AND LearningDate=2026-03-22 AND ScheduleID != -1
   
   ❌ Nếu count > 0:
      → "Schedule conflict: This class already has a schedule..."
      → redirect to manage ❌ CREATE FAIL
   
   ✅ Nếu count = 0: → Tiếp tục check 2

2️⃣ Line 461: isRoomAvailable(roomId=3, slotId=2, date=2026-03-22, excludeScheduleId=-1)
   ScheduleDAO.java (dòng 373):
   SELECT COUNT(*) FROM Schedule 
   WHERE RoomID=3 AND SlotID=2 AND LearningDate=2026-03-22 AND ScheduleID != -1
   
   ❌ Nếu count > 0 (room booked):
      → "Room is not available for this time slot!"
      → redirect ❌ CREATE FAIL
   
   ✅ Nếu count = 0: → Tiếp tục check 3

3️⃣ Line 467: isTeacherAvailable(teacherId, slotId=2, date=2026-03-22, excludeScheduleId=-1)
   ScheduleDAO.java (dòng 396):
   SELECT COUNT(*) FROM Schedule 
   WHERE TeacherID=teacher AND SlotID=2 AND LearningDate=2026-03-22 AND ScheduleID != -1
   
   ❌ Nếu count > 0 (teacher busy):
      → "Teacher is not available at this time slot on this date!"
      → redirect ❌ CREATE FAIL
   
   ✅ Nếu count = 0: → Tiếp tục check 4

4️⃣ Line 473: teacherExceedsWeeklyLimit(teacherId, date=2026-03-22, excludeScheduleId=-1)
   ScheduleDAO.java (dòng 468):
   SELECT COUNT(*) FROM Schedule 
   WHERE TeacherID=teacher AND LearningDate BETWEEN (Monday of week) AND (Sunday of week)
   
   ❌ Nếu count >= 5:
      → "Teacher has reached the weekly limit! Current slots: 5/5"
      → redirect ❌ CREATE FAIL
   
   ✅ Nếu count < 5: → Tiếp tục tạo

5️⃣ Line 477: createSchedule(classId=5, roomId=3, slotId=2, date, teacherId, false)
   ScheduleDAO.java (dòng 114):
   → Lại gọi hasClassSlotConflict() (double check)
   ✅ Nếu pass → INSERT vào DB
   → "Schedule created successfully!" ✅ CREATE SUCCESS
```

---

## 3️⃣ **CREATE ACTION - BATCH/RECURRING SCHEDULE**

### 📍 Controller: `ScheduleController.java` (dòng 434-457)

```
POST /schedule?action=create
    classId=5, roomId=3, slotId=2, learningDate=2026-03-22
    recurringType=weekly
    endCondition=after
    occurrences=8
        ↓
Line 441: recurringType = "weekly" (recurring)
Line 443-456: Vào block BATCH schedule
```

### ✅ **Batch Processing (Xử lý nhiều ngày)**

```
Line 450: createMultipleSchedules(classId=5, roomId=3, slotId=2, 
          learningDate=2026-03-22, teacherId, "weekly", "", "after", null, 8)
          ↓
ScheduleDAO.java (dòng 175):
    Line 179-184: generateScheduleDates(...)
                  → Tạo list 8 ngày theo pattern "weekly"
                  → Result: [2026-03-22, 2026-03-29, 2026-04-05, 2026-04-12, ...]
    
    Line 195: for each date in scheduleDates:
        ✅ Scenario: date=2026-03-22
            Line 198: hasClassSlotConflict(5, 2, 2026-03-22, -1)
            ❌ Nếu conflict → skip date này + print warning
            ✅ Nếu ok → check room
            
            Line 203: isRoomAvailable(3, 2, 2026-03-22, -1)
            ❌ Nếu room occupied → skip + warning
            ✅ Nếu ok → check teacher
            
            Line 208: isTeacherAvailable(teacherId, 2, 2026-03-22, -1)
            ❌ Nếu teacher busy → skip + warning
            ✅ Nếu ok → check weekly limit
            
            Line 213: teacherExceedsWeeklyLimit(teacherId, 2026-03-22, -1)
            ❌ Nếu exceeded (>= 5/week) → skip + warning
            ✅ Nếu ok → addBatch()
            
        ✅ Scenario: date=2026-03-29
            → Lặp lại checks tương tự...
            
        Sau vòng lặp: executeBatch() tất cả dates đã pass checks
        → INSERT multiple records
```

**Kết quả:** Có thể tạo được 3-5 ngày thay vì 8 (tùy validation)
- ✅ Các ngày không conflict/occupied → INSERT thành công
- ❌ Các ngày conflict/occupied → SKIP

---

## 4️⃣ **UPDATE ACTION**

### 📍 Controller: `ScheduleController.java` (dòng 528-595)

```
POST /schedule?action=update
    scheduleId=42 (cái cần chỉnh)
    classId=5, roomId=3, slotId=2, learningDate=2026-03-22
        ↓
Line 530: scheduleId = 42
Line 535: getScheduleById(42) → lấy info schedule cũ
Line 545-595: Các checks y hệt CREATE, nhưng...
```

### ✅ **Khác biệt so với CREATE:**

```
1️⃣ Line 546: hasScheduleConflict(classId=5, slotId=2, date, scheduleId=42)
             ↑ KHÁC: excludeScheduleId=42 (không loại trừ ID -1)
   SQL: ... AND ScheduleID != 42 ✅ (bỏ qua schedule này)
   
2️⃣ Line 553: isRoomAvailable(roomId=3, slotId=2, date, scheduleId=42)
             ↑ excludeScheduleId=42
   SQL: ... AND ScheduleID != 42 ✅
   
3️⃣ Line 561: isTeacherAvailable(teacherId, slotId=2, date, scheduleId=42)
             ↑ excludeScheduleId=42
   SQL: ... AND ScheduleID != 42 ✅
   
4️⃣ Line 568: teacherExceedsWeeklyLimit(teacherId, date, scheduleId=42)
             ↑ excludeScheduleId=42
   SQL: ... AND ScheduleID != 42 ✅

✅ Khi tất cả pass → editSchedule() → UPDATE record
```

**Lợi ích:** Cho phép update schedule sang phòng/slot khác mà không conflict với chính nó

---

## 5️⃣ **DELETE ACTION**

### 📍 Controller: `ScheduleController.java` (dòng 596-661)

```
POST /schedule?action=delete
    scheduleId=42
    deleteScope=single (hoặc "series")
        ↓
Line 602: Check attendance status
```

### ✅ **Validation:**

```
Line 604: if (schedule.isAttendanceStatus() == true)
   ❌ Nếu đã có attendance record:
      → "Cannot delete schedule with attendance already taken!"
      → redirect ❌ DELETE FAIL
   
✅ Nếu chưa có attendance:
   Line 609: if ("series".equals(deleteScope))
      → deleteSimilarSchedules(42)
        SQL: SELECT s2.* FROM Schedule s1
             INNER JOIN Schedule s2 ON s1.ClassID = s2.ClassID
             AND s1.RoomID = s2.RoomID AND s1.SlotID = s2.SlotID
             WHERE s1.ScheduleID = 42
        → Delete ALL non-attended schedules cùng class/slot/room
        → "Successfully deleted X schedule(s) in the series!" ✅
   
   Line 619: else (deleteScope = "single")
      → deleteSchedule(42)
        SQL: DELETE FROM Schedule WHERE ScheduleID = 42
        → Delete CHỈ 1 record
        → "Schedule deleted successfully!" ✅
```

---

## 📋 SUMMARY TABLE

| Action | ClassId | RoomId | Outcome |
|--------|---------|--------|---------|
| **MANAGE** | 0 | 0 | Full week schedule ✅ |
| **MANAGE** | 5 | 0 | Class 5 only |
| **MANAGE** | 0 | 3 | Room 3 only |
| **MANAGE** | 5 | 3 | Class 5 + Room 3 |
| **CREATE-SINGLE** | - | - | Pass 4 checks → INSERT 1 record |
| **CREATE-BATCH** | - | - | Loop each date, skip conflicts, INSERT rest |
| **UPDATE** | - | - | Same as CREATE but exclude current record (scheduleId) |
| **DELETE-SINGLE** | - | - | If no attendance → DELETE 1 record |
| **DELETE-SERIES** | - | - | If no attendance → DELETE all similar |

---

## 🔍 **KEY POINTS**

1. **excludeScheduleId = -1** → Dùng khi CREATE (tạo mới, không có record nào để exclude)
2. **excludeScheduleId = scheduleId** → Dùng khi UPDATE/DELETE (phải exclude record hiện tại)
3. **null filter** → Không filter, show ALL
4. **Batch validation** → Skip ngày bị conflict, không throw error
5. **Single validation** → Throw error, không create nếu có conflict

---

## 🎬 **EXAMPLE FLOW: Tạo batch recurring weekly schedule**

```
User: Tạo recurring schedule
      Class 5, Room 3, Slot 2
      Weekly, 8 occurrences, từ 22/3/2026

Controller:
  Line 450: createMultipleSchedules(5, 3, 2, 2026-03-22, teacher, "weekly", "", "after", null, 8)

ScheduleDAO:
  generateScheduleDates() → [22/3, 29/3, 5/4, 12/4, 19/4, 26/4, 3/5, 10/5]
  
  Loop through dates:
    22/3: ✅ pass all → addBatch
    29/3: ✅ pass all → addBatch
    5/4:  ❌ teacher already 5/week → skip
    12/4: ✅ pass all → addBatch
    19/4: ❌ room occupied → skip
    26/4: ✅ pass all → addBatch
    3/5:  ✅ pass all → addBatch
    10/5: ✅ pass all → addBatch
  
  executeBatch() → INSERT 6 records (8 - 2 skipped)
  
Result: "Successfully created 6 schedule(s)!" ✅
```

---

## ⚠️ **FIXES APPLIED**

1. ✅ **Duplicate imports** → Cleaned up
2. ✅ **excludeScheduleId=0 → -1** → Fixed
3. ✅ **Batch validation** → Added room/teacher/weekly checks
4. ✅ **Class conflict check** → Present in both single and batch

All validation is now **consistent** across all operations! 🚀

