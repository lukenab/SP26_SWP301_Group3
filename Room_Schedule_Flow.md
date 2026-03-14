# Tai lieu luong hoat dong Room va Schedule

## Muc tieu tai lieu
Tai lieu nay giai thich luong xu ly cua 2 module:
- `Room` (`/room`)
- `Schedule` (`/schedule`)

Theo dung code hien tai trong:
- `src/main/java/controller/RoomController.java`
- `src/main/java/dao/RoomDAO.java`
- `src/main/java/controller/ScheduleController.java`
- `src/main/java/dao/ScheduleDAO.java`

---

## 1) Kien truc chung (de hieu truoc khi vao tung module)

### 1.1 Request flow tong quat
1. User bam nut tren sidebar/menu trong `dashboard.jsp`.
2. Browser goi URL servlet (vi du: `/room`, `/schedule?action=manage`).
3. Controller (`doGet`/`doPost`) doc `action`.
4. Controller goi DAO de truy van/cap nhat DB.
5. Controller set du lieu vao `request` va set `home_view`.
6. Forward ve `dashboard.jsp`.
7. `dashboard.jsp` import JSP con theo `home_view`.

### 1.2 2 co che dieu huong quan trong
- `forward("dashboard.jsp")`: giu nguyen `request attribute`, render trang ngay.
- `sendRedirect("...")`: tao request moi, thuong dung sau khi ghi DB (POST-Redirect-GET).

### 1.3 Flash message
Ca 2 module dung session message:
- `message`
- `messageType` (`success`/`error`)

---

## 2) Module Room

## 2.1 Muc dich nghiep vu
Academic staff quan ly phong hoc:
- Xem danh sach
- Xem chi tiet
- Tao
- Cap nhat
- Disable/Enable
- Xoa

## 2.2 URL mapping
Servlet: `@WebServlet("/room")`

## 2.3 Luong GET theo action

### `action=all` (mac dinh)
**Controller:** `RoomController#doGet`
- Goi `RoomDAO.getAllRoom()` de lay danh sach phong.
- Vong lap tung phong, goi `RoomDAO.isRoomInUse(roomId)` de tao `roomUsageMap`.
- Set:
  - `allRooms`
  - `roomUsageMap`
  - `home_view=/academic/room/manageRoom.jsp`
- Forward `dashboard.jsp`.

### `action=detail&id=...`
- Parse `id`.
- Goi `RoomDAO.getRoomByID(id)`.
- Goi `RoomDAO.getClassesUsingRoom(id)` de hien cac lop dang dung phong.
- Set:
  - `roomDetail`
  - `classesUsingRoom`
  - `home_view=/academic/room/roomDetail.jsp`

### `action=create`
- Chi set `home_view=/academic/room/createRoom.jsp`.

### `action=delete&id=...`
- Lay phong can xoa (`roomDel`), mo trang confirm delete.
- `home_view=/academic/room/deleteRoom.jsp`.

### `action=disable&id=...`
- Lay phong (`roomDisable`) + danh sach lop dang dung phong (`classesUsingRoom`).
- `home_view=/academic/room/disableRoom.jsp`.

### `action=enable&id=...`
- Lay phong (`roomEnable`).
- `home_view=/academic/room/enableRoom.jsp`.

### `action=update&id=...`
- Lay phong (`roomUpdate`).
- `home_view=/academic/room/updateRoom.jsp`.

## 2.4 Luong POST theo action

### `action=create`
- Nhan `name`, `capacity`, `type`, `status`.
- Validate:
  - Trung ten: `RoomDAO.checkRoomNameExists(name)` -> bao loi.
  - Ten rong -> bao loi.
- Neu hop le -> `RoomDAO.createRoom(...)`.
- Dat `message/messageType`, redirect ve `/room` hoac `/room?action=create`.

### `action=update`
- Nhan du lieu form.
- Goi `RoomDAO.updateRoom(...)`.
- Dat message, redirect `/room`.

### `action=disable`
- Goi `RoomDAO.disableRoom(id)` (set `Status=0`).
- Dat message, redirect `/room`.

### `action=enable`
- Goi `RoomDAO.enableRoom(id)` (set `Status=1`).
- Dat message, redirect `/room`.

### `action=delete`
- Goi `RoomDAO.deleteRoombyID(id)`.
- Dat message, redirect `/room`.

## 2.5 DAO Room tom tat
`RoomDAO` thao tac truc tiep bang `PreparedStatement`:
- `getAllRoom`, `getRoomByID`
- `checkRoomNameExists`
- `isRoomInUse`
- `getClassesUsingRoom`
- `createRoom`, `updateRoom`, `disableRoom`, `enableRoom`, `deleteRoombyID`

## 2.6 Diem can luu y Room
1. O `action=all`, hien tai goi `isRoomInUse` theo tung phong -> co the tao N+1 query khi data lon.
2. Controller co comment "Delete room (only for rooms not in use)", nhung hien tai khong check lai `isRoomInUse` truoc khi xoa; viec chan chu yeu phu thuoc DB constraint.

---

## 3) Module Schedule

## 3.1 Muc dich nghiep vu
Quan ly lich hoc theo role:
- Teacher (role 4): xem lich day
- Academic (role 2): quan ly lich tong
- Student (role 5): xem lich hoc ca nhan

## 3.2 URL mapping va role gate
Servlet: `@WebServlet("/schedule")`

Trong `doGet`:
- Neu khong co user/role -> redirect login.
- Chi cho role `2`, `4`, `5` vao.

## 3.3 Bien thoi gian tuan (dung lai nhieu action)
`selectedDate` duoc dung de tinh:
- `monday`
- `prevWeek`, `nextWeek`
- `dateOfWeek[7]`
- `weekdays`, `slots`

Muc dich: render giao dien lich theo tuan.

## 3.4 Luong GET theo action

### A. Nhom teacher

#### `action=view` (mac dinh)
- Neu co `classId`: lay lich theo lop cua teacher qua `TeacherDAO.getScheduleByClassId(...)`.
  - `home_view=teacher/view_class_schedule.jsp`
- Neu khong co `classId`: lay lich day tong qua `TeacherDAO.getTeachingSchedule(...)`.
  - `home_view=teacher/teacher_schedule.jsp`

#### `action=viewByClass`
- Tuong tu `view` theo class, explicit hon.
- `home_view=teacher/view_class_schedule.jsp`

### B. Nhom academic

#### `action=manage`
- Lay filter `classId`, `roomId`, `date`.
- Save vao session:
  - `selectedClassId`
  - `selectedRoomId`
  - `selectedDate`
- Goi `ScheduleDAO.getSchedulesForManagement(selectedDate, classFilterId, roomFilterId)`.
- Nap option:
  - `allClasses` (tu `ClassDAO`)
  - `allRooms`, `allTeachers` (tu `ScheduleDAO`)
- `home_view=/academic/schedule/manageSchedule.jsp`

#### `action=create`
- Nap du lieu dropdown: class, room, slot.
- `home_view=/academic/schedule/createSchedule.jsp`

#### `action=edit&scheduleId=...`
- Lay schedule can sua: `ScheduleDAO.getScheduleById(...)`.
- Neu null -> message loi, redirect manage.
- Neu co -> nap class/room/slot options.
- `home_view=/academic/schedule/editSchedule.jsp`

#### `action=viewDetail&scheduleId=...`
- Lay chi tiet schedule.
- Luu `selectedClassId` vao session de nut Back ve dung bo loc.
- `home_view=/academic/schedule/viewSchedule.jsp`

#### `action=delete&scheduleId=...`
- Lay schedule can xoa.
- Luu `selectedClassId`, `selectedDate` vao session.
- Goi `ScheduleDAO.getSimilarSchedules(scheduleId)` de hien so lich lien quan (cho xoa theo series).
- `home_view=/academic/schedule/deleteSchedule.jsp`

#### `action=get&scheduleId=...`
- Tra JSON chi tiet schedule (de mo modal/detail nhanh).

### C. Nhom student

#### `action=studentView`
- Xac dinh `weekStart` (mac dinh Monday hien tai).
- Goi `ScheduleDAO.getScheduleByStudentWeek(...)`.
- Build map lich theo ngay + slot de render bang tuan.
- `home_view=student/studentSchedule.jsp`

## 3.5 Luong POST theo action (academic CRUD)

### `action=create`
1. Parse `classId`, `roomId`, `slotId`, `learningDate`.
2. Lay `teacherId` tu `ClassDAO.getTeacherIdByClassId(classId)`.
3. Validate theo thu tu:
   - `hasScheduleConflict(...)`: lop da co lich cung slot/cung ngay?
   - `isRoomAvailable(...)`: phong co bi trung?
   - `isTeacherAvailable(...)`: giao vien co bi trung?
   - `teacherExceedsWeeklyLimit(...)`: giao vien vuot gioi han 5 slots/tuan?
4. Neu pass -> `createSchedule(...)`.
5. Dat flash message.
6. Redirect ve `schedule?action=manage` kem bo loc da luu trong session.

### `action=update`
- Tuong tu create, nhung:
  - Lay schedule cu qua `getScheduleById`.
  - Validate co `excludeScheduleId` de bo qua ban ghi hien tai.
  - Goi `editSchedule(...)`.
- Redirect lai manage kem bo loc.

### `action=delete`
- Kiem tra `AttendanceStatus`:
  - Neu da diem danh -> khong cho xoa.
- Neu `deleteScope=series`:
  - Goi `deleteSimilarSchedules(scheduleId)` (chi xoa nhung lich chua diem danh).
- Nguoc lai:
  - Goi `deleteSchedule(scheduleId)`.
- Redirect lai manage kem bo loc.

## 3.6 DAO Schedule tom tat (phan dung cho academic)
- Doc du lieu:
  - `getScheduleById`
  - `getSchedulesForManagement`
  - `getAllRooms`, `getAllTeachers`
  - `getSimilarSchedules`
- Validate conflict:
  - `hasScheduleConflict`
  - `isRoomAvailable`
  - `isTeacherAvailable`
  - `teacherExceedsWeeklyLimit`
  - `getTeacherWeeklySlotCount`
- Ghi du lieu:
  - `createSchedule`
  - `editSchedule`
  - `deleteSchedule`
  - `deleteSimilarSchedules`

---

## 4) Session attributes quan trong

### Room
- `message`, `messageType`

### Schedule
- `message`, `messageType`
- `selectedClassId`
- `selectedRoomId`
- `selectedDate`

`selectedClassId/selectedRoomId/selectedDate` giup sau khi create/update/delete quay lai dung context bo loc tren trang manage.

---

## 5) Luong nhanh theo goc nhin nguoi dung

## 5.1 Academic quan ly phong
1. Vao `room` -> xem list.
2. Bam tao/sua/disable/enable/xoa.
3. Submit form -> POST `room?action=...`.
4. Controller goi DAO cap nhat DB.
5. Redirect lai `room`, hien message.

## 5.2 Academic quan ly lich
1. Vao `schedule?action=manage`.
2. Chon filter class/room/date.
3. Bam create/edit/delete.
4. POST `schedule?action=create|update|delete`.
5. Controller validate trung lich -> cap nhat DB.
6. Redirect lai manage voi bo loc truoc do.

---

## 6) Goi y doc code de de nho logic
- B1: Doc `RoomController`/`ScheduleController` truoc (hieu action map).
- B2: Tu tung action, lan theo ham DAO tuong ung.
- B3: Cuoi cung mo JSP de hieu data can truyen (`request attributes`).

Neu can, co the ve sequence diagram cho 3 use case hay dung nhat:
- Tao phong
- Tao lich
- Xoa lich theo series

---

## 7) Glossary nhanh
- `home_view`: duong dan JSP con duoc import ben trong `dashboard.jsp`.
- `AttendanceStatus` trong Schedule: da/chua diem danh.
- `deleteScope=series`: xoa lo lich cung pattern (class + room + slot).

---

Tai lieu nay la anh xa theo code hien tai. Neu ban tiep tuc doi folder JSP hoac split controller theo service layer, nho cap nhat lai muc URL/path va action table.
