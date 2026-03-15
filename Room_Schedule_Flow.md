# Tai lieu chi tiet luong hoat dong Room va Schedule

## 0) Muc tieu cua ban tai lieu nay
Tai lieu nay giai thich **chi tiet** module `Room` va `Schedule` theo code hien tai, theo goc nhin:
- Request vao tu JSP nhu the nao
- Controller nhan tham so gi, xu ly gi
- DAO truy van SQL gi, tra ve kieu du lieu nao
- JSP can nhung attribute nao de render
- Nhung diem de gay bug neu doi ten field / doi path / doi kieu

Doc xong tai lieu nay, ban co the tu lan theo code va sua logic ma khong bi "mu".

---

## 1) Kien truc tong the (phai nam truoc)

### 1.1. Mo hinh MVC trong project nay
- `JSP` = View (hien thi + form input).
- `Controller` (Servlet) = dieu phoi (doc param, validate, goi DAO, set attribute, forward/redirect).
- `DAO` = tang truy cap DB (`PreparedStatement`, `ResultSet`).
- `Model` = object Java (`Room`, `Schedule`, `Slot`, `Classes`, ...).

### 1.2. 2 kieu dieu huong quan trong
1. `forward("dashboard.jsp")`
   - Cung request.
   - `request.setAttribute(...)` con nguyen.
   - Dung cho trang GET (show list/form/detail).

2. `sendRedirect("...")`
   - Tao request moi.
   - `request attribute` mat, chi `session attribute` con.
   - Dung sau POST de tranh submit lai (PRG pattern).

### 1.3. Co che render giao dien trung tam
`dashboard.jsp` co:
- menu theo role
- `<c:import url="${home_view}" />`

Nghia la Controller can set:
- `request.setAttribute("home_view", "...jsp")`

Neu quen set `home_view` -> dashboard trong.
Neu path sai -> 500 do import khong tim thay file.

---

## 2) Module Room (`/room`)

## 2.1. URL mapping
- Servlet: `@WebServlet(name = "RoomController", urlPatterns = {"/room"})`
- File: `src/main/java/controller/RoomController.java`

### 2.2. doGet: action map chi tiet
Controller doc:
- `String action = request.getParameter("action")`
- neu `null` -> mac dinh `"all"`

#### a) `action=all`
Muc dich: load trang quan ly phong.

- DAO goi: `List<Room> allRoom = rdao.getAllRoom()`
- Vong lap moi room, goi them `rdao.isRoomInUse(roomId)` de tao map:
  - `Map<Integer, Boolean> roomUsageMap`
  - key = `RoomID`, value = co dang duoc schedule su dung hay khong
- Set attribute:
  - `allRooms` (kieu `List<Room>`)
  - `roomUsageMap` (kieu `Map<Integer, Boolean>`)
  - `home_view = /academic/room/manageRoom.jsp`
- Forward `dashboard.jsp`

Output UI:
- `manageRoom.jsp` render bang phong.
- Nut `Delete` hay `Disable` phu thuoc `roomUsageMap[r.roomId]`.

#### b) `action=detail&id=...`
Muc dich: xem chi tiet 1 phong.

Input:
- Query param `id` (string), parse sang `int`.

Xu ly:
- `Room roomDetail = rdao.getRoomByID(id)`
- `List<String[]> classesUsingRoom = rdao.getClassesUsingRoom(id)`

Output:
- `roomDetail`
- `classesUsingRoom`
- `home_view=/academic/room/roomDetail.jsp`

#### c) `action=create`
- Khong query DB.
- Chi set `home_view=/academic/room/createRoom.jsp`

#### d) `action=delete&id=...`
- Lay room can xoa bang `getRoomByID`
- Set `roomDel`
- `home_view=/academic/room/deleteRoom.jsp`

#### e) `action=disable&id=...`
- Lay `roomDisable`
- Lay `classesUsingRoom` (de canh bao dang co lop dung phong)
- `home_view=/academic/room/disableRoom.jsp`

#### f) `action=enable&id=...`
- Lay `roomEnable`
- `home_view=/academic/room/enableRoom.jsp`

#### g) `action=update&id=...`
- Lay `roomUpdate`
- `home_view=/academic/room/updateRoom.jsp`

---

### 2.3. doPost: action map chi tiet

#### a) `action=create`
Input form (tu `createRoom.jsp`):
- `name` (String)
- `capacity` (String -> int)
- `type` (String)
- `status` (String -> int; 1/0)

Validate hien tai:
1. `checkRoomNameExists(name)` -> trung ten thi bao loi.
2. `name` rong/null -> bao loi.

Neu pass:
- `int changes = rdao.createRoom(...)`
- `changes != -1` -> success.

Return flow:
- Loi -> `sendRedirect("room?action=create")`
- Thanh cong -> `sendRedirect("room")`

Thong bao:
- dung `sessionScope.message`, `sessionScope.messageType`

#### b) `action=update`
Input:
- `id`, `name`, `capacity`, `type`, `status`

Xu ly:
- `int updated = rdao.updateRoom(...)`

Output:
- set message success/error
- redirect `room`

#### c) `action=delete`
Input:
- `id`

Xu ly:
- `int deleted = rdao.deleteRoombyID(id)`

Output:
- message + redirect `room`

#### d) `action=disable`
- Goi `rdao.disableRoom(id)` -> SQL set `Status = 0`
- redirect `room`

#### e) `action=enable`
- Goi `rdao.enableRoom(id)` -> SQL set `Status = 1`
- redirect `room`

---

## 2.4. Hop dong ham trong RoomDAO (input/output/return type)
File: `src/main/java/dao/RoomDAO.java`

### `List<Room> getAllRoom()`
- SQL: `select * from Room`
- Mapping moi dong DB -> `new Room(roomId, roomName, capacity, type, status)`
- Tra ve `List<Room>`
- Loi: in stacktrace, tra `null`

### `Room getRoomByID(int id)`
- SQL: `select * from Room where RoomID = ?`
- Tra 1 `Room` hoac `null` neu khong co

### `boolean checkRoomNameExists(String roomName)`
- SQL count theo `RoomName`
- `true` neu count > 0
- `false` neu khong ton tai hoac loi

### `boolean isRoomInUse(int roomId)`
- SQL count trong bang `Schedule`
- `true` neu co lich dung phong

### `List<String[]> getClassesUsingRoom(int roomId)`
- SQL join `Schedule`, `Class`, `Course`, `Employee`, `[User]`
- Moi dong du lieu nhat vao `String[6]`:
  - `[0]=ClassID`
  - `[1]=ClassName`
  - `[2]=Status` (status lop)
  - `[3]=CourseName`
  - `[4]=TeacherName`
  - `[5]=TotalSchedules`
- Tra `List<String[]>`

### `int createRoom(...)`, `int updateRoom(...)`, `int disableRoom(...)`, `int enableRoom(...)`, `int deleteRoombyID(...)`
- Tra so dong bi anh huong (`executeUpdate()`)
- Loi -> `-1`

---

## 2.5. JSP Room can gi tu Controller

### `manageRoom.jsp`
Can:
- `allRooms: List<Room>`
- `roomUsageMap: Map<Integer, Boolean>`

Nut action dung query string:
- `?action=detail&id=...`
- `?action=update&id=...`
- `?action=disable&id=...`
- `?action=delete&id=...`
- `?action=enable&id=...`

### `createRoom.jsp`
Form POST:
- hidden `action=create`
- `name`, `capacity`, `type`, `status`

### `updateRoom.jsp`
Can:
- `roomUpdate: Room`
Form POST:
- hidden `action=update`, `id`
- `name`, `capacity`, `type`, `status`

### `deleteRoom.jsp`
Can:
- `roomDel: Room`
Form POST:
- `action=delete`, `id`

### `disableRoom.jsp`
Can:
- `roomDisable: Room`
- `classesUsingRoom: List<String[]>`
Form POST:
- `action=disable`, `id`

### `roomDetail.jsp`
Can:
- `roomDetail: Room`
- `classesUsingRoom: List<String[]>`

---

## 3) Module Schedule (`/schedule`)

## 3.1. URL mapping va role gate
File: `src/main/java/controller/ScheduleController.java`

- Servlet: `@WebServlet("/schedule")`
- Trong `doGet` va `doPost`:
  - lay `user` tu session
  - neu khong co user/role -> redirect `login.jsp`
- Role duoc phep trong `doGet`: `2` (academic), `4` (teacher), `5` (student)

### 3.2. Khoi context "tuan"
Truoc khi switch action, controller tinh san:
- `selectedDate` (String `yyyy-MM-dd`, mac dinh ngay hien tai)
- `mondayDate`
- `dateOfWeek[7]` (format `dd/MM`)
- `prevWeek`, `nextWeek`
- `weekdays` (Monday..Sunday)
- `slots` (`List<Slot>` tu `SlotDAO.getAllSlots()`)

Muc dich:
- cac JSP lich tuan dung lai ngay lap tuc, khong can tinh lai.

---

## 3.3. doGet action theo role

### A) Teacher

#### `action=view` (mac dinh)
- Neu co `classId`:
  - `teacherDAO.getScheduleByClassId(classId, teacherId, selectedDate)`
  - set `className`, `classId`, `scheduleList`
  - `home_view = teacher/view_class_schedule.jsp`
- Neu khong co `classId`:
  - `teacherDAO.getTeachingSchedule(teacherId, selectedDate)`
  - `home_view = teacher/teacher_schedule.jsp`

#### `action=viewByClass`
- Giong nhanh tren, explicit theo class
- `home_view = teacher/view_class_schedule.jsp`

### B) Academic

#### `action=manage`
Input filter GET:
- `classId` (String)
- `roomId` (String)
- `date` (String)

Controller parse:
- `classFilterId: Integer` (null/so)
- `roomFilterId: Integer` (null/so)

Luu context vao session:
- `selectedClassId`
- `selectedRoomId`
- `selectedDate`

DAO calls:
- `scheduleDAO.getSchedulesForManagement(selectedDate, classFilterId, roomFilterId)` -> `List<Schedule>`
- `classDAO.getClassManagementList()` -> `List<Object[]>` cho dropdown class
- `scheduleDAO.getAllRooms()` -> `List<Object[]>` cho dropdown room
- `scheduleDAO.getAllTeachers()` -> `List<Object[]>` (hien tai co set nhung JSP manage chua dung)

Set request:
- `classId`, `roomId`, `selectedDate`
- `weekdays`, `slots`
- `scheduleList`, `allClasses`, `allRooms`, `allTeachers`
- `home_view=/academic/schedule/manageSchedule.jsp`

##### Giai phau logic bo loc tren web (chi tiet)

1) Tang JSP (nguoi dung gui bo loc nhu the nao)
- Form trong `manageSchedule.jsp` la `method="GET"`.
- Input name co dinh:
  - `action=manage` (hidden)
  - `classId` (dropdown)
  - `roomId` (dropdown)
  - `date` (input date)
- Gia tri mac dinh "tat ca" cua class/room la chuoi `"0"`.

2) Tang Controller (chuyen gia tri form thanh filter thuc su)
- `filterClassId` / `filterRoomId` duoc doc tu query string.
- Rule parse:
  - `null`, rong, hoac `"0"` -> **khong filter** (gan `null`).
  - so hop le > 0 -> filter theo id do.
- Date:
  - Neu `date` rong/null -> controller tu gan `LocalDate.now().toString()`.
- Context filter duoc luu vao session (`selectedClassId`, `selectedRoomId`, `selectedDate`) de:
  - sau khi Create/Update/Delete redirect ve list van giu filter cu.

3) Tang DAO (SQL loc du lieu theo tuan + class + room)
Ham: `getSchedulesForManagement(String selectedDate, Integer classId, Integer roomId)`

- SQL duoc build dong:
  - Luon co `WHERE 1=1`
  - Co `selectedDate` -> them 2 dieu kien theo TUAN:
    - `LearningDate >= MondayOfWeek(selectedDate)`
    - `LearningDate < NextMonday(selectedDate)`
  - Co `classId` -> them `AND s.ClassID = ?`
  - Co `roomId` -> them `AND s.RoomID = ?`
- Thu tu bind param:
  - 4 param date truoc (do SQL week-range dung 4 dau `?`)
  - roi toi `classId` (neu co)
  - roi toi `roomId` (neu co)

=> Nghia la bo loc date khong loc 1 ngay, ma loc **ca tuan chua ngay do**.

4) Tang JSP render ket qua (tai sao co luc thay bang, co luc thay empty)
- `manageSchedule.jsp` co dieu kien:
  - Neu **ca classId va roomId deu rong/0** -> hien thong bao "Please select a class or room".
  - Neu co it nhat 1 filter (class hoac room) -> hien bang lich.
- Trong bang:
  - Cot = 7 thu trong tuan (`weekdays`)
  - Hang = tung `slot`
  - Moi o quet `scheduleList`, record nao trung `day + slot` thi hien.

5) Vi du URL thuc te de tu kiem chung
- Loc theo class 7, tat ca phong, tuan chua 2026-03-09:
  - `schedule?action=manage&classId=7&roomId=0&date=2026-03-09`
- Loc theo phong 3, tat ca class:
  - `schedule?action=manage&classId=0&roomId=3&date=2026-03-09`
- Loc dong thoi class + room:
  - `schedule?action=manage&classId=7&roomId=3&date=2026-03-09`
- Khong chon class/room:
  - `schedule?action=manage&classId=0&roomId=0&date=2026-03-09`
  - Trang se hien empty-state (khong render bang lich).

6) Diem can biet de tranh hieu nham
- `date` la "ngay neo" cua tuan, khong phai ngay can loc duy nhat.
- Dropdown "All" gui gia tri `0`, khong phai null.
- Neu xoa bo hidden `action=manage`, request co the roi ve action mac dinh khac.

7) Truong hop tham so filter = null (ban vua hoi)

- `classId = null`:
  - Controller KHONG parse int.
  - `classFilterId` giu `null`.
  - Session gan `selectedClassId = 0`.
  - DAO KHONG them dieu kien `AND s.ClassID = ?`.

- `roomId = null`:
  - Controller KHONG parse int.
  - `roomFilterId` giu `null`.
  - Session gan `selectedRoomId = 0`.
  - DAO KHONG them dieu kien `AND s.RoomID = ?`.

- `date = null`:
  - O dau `doGet`, controller tu set `selectedDate = LocalDate.now().toString()`.
  - Vi vay xuong DAO, date khong con null nua.
  - DAO VAN ap filter theo tuan cua ngay hien tai.

- Neu ca `classId` va `roomId` deu null/0:
  - Ve DB: co the van lay du lieu theo tuan.
  - Ve UI `manageSchedule.jsp`: vao nhanh empty-state (khong ve grid), do rule view bat buoc phai chon class hoac room.

=> Tom lai: `null` cua class/room duoc hieu la "khong loc theo tieu chi do"; `null` cua date duoc chuyen thanh "hom nay".

##### Bang chan tri bo loc (action=manage)

| classId | roomId | date | Controller parse | SQL filter thuc te | UI ket qua |
|---|---|---|---|---|---|
| `0` | `0` | co/khong | class=`null`, room=`null` | chi filter theo tuan (neu co date) | Hien empty-state (JSP chan render bang) |
| `7` | `0` | `2026-03-09` | class=7, room=`null` | tuan + `AND s.ClassID=7` | Hien bang, chi lich cua class 7 |
| `0` | `3` | `2026-03-09` | class=`null`, room=3 | tuan + `AND s.RoomID=3` | Hien bang, chi lich tai room 3 |
| `7` | `3` | `2026-03-09` | class=7, room=3 | tuan + `AND s.ClassID=7 AND s.RoomID=3` | Hien bang, giao cua class 7 va room 3 |
| rong/null | rong/null | rong/null | class=`null`, room=`null`, date=hom nay | filter tuan hien tai | Van empty-state vi class/room deu khong chon |

Ghi chu quan trong:
- DAO van co the tra du lieu khi class/room deu null, nhung JSP chu dong khong cho hien bang neu khong chon class hoac room.
- Day la "luat UI" (view-level rule), khong phai "luat DB".

##### Vi du day du: tu URL den SQL

Vi du request:
- `schedule?action=manage&classId=7&roomId=3&date=2026-03-09`

Buoc 1 - Controller parse:
- `classFilterId = 7`
- `roomFilterId = 3`
- `selectedDate = "2026-03-09"`

Buoc 2 - DAO build SQL:
- Them filter tuan quanh `selectedDate`
- Them `AND s.ClassID = ?`
- Them `AND s.RoomID = ?`

Buoc 3 - Thu tu bind param thuc te:
1. date #1 (`selectedDate`)
2. date #2 (`selectedDate`)
3. date #3 (`selectedDate`)
4. date #4 (`selectedDate`)
5. `classId = 7`
6. `roomId = 3`

Buoc 4 - ResultSet -> `List<Schedule>`:
- moi row map thanh 1 `Schedule` co `learningDate`, `slot`, `room`, `classes`

Buoc 5 - JSP dat vao tung o lich:
- JSP lap `slot` (hang) x `weekdays` (cot)
- moi o quet `scheduleList`
- neu `s.slot.slotID == slot.slotID` va `dayInSql == day` thi in card schedule

##### Tai sao "co du lieu" nhung 1 so o van trong?

1. Khong trung thu trong tuan
- JSP so sanh bang ten thu (`Monday`, `Tuesday`, ...).
- Record nao khong map dung ten thu do thi khong vao o.

2. Khong trung slot
- Record o Slot 2 se khong hien o dong Slot 1.

3. Filter giao nhau qua hep
- Loc dong thoi class + room co the loai het du lieu.

4. Rule empty-state cua JSP
- classId=0 va roomId=0 thi khong hien grid du DAO co data.

##### Debug bo loc trong 5 phut (khong can sua code)

1. In URL dang chay
- Xac nhan query string dung `action`, `classId`, `roomId`, `date`.

2. Kiem tra session context
- Sau khi create/update/delete, URL redirect co giu `classId`, `roomId`, `date` khong.

3. Kiem tra dieu kien JSP
- Neu ca `classId` va `roomId` deu 0/null -> se vao nhanh empty-state.

4. Kiem tra map vao o lich
- Day/slot phai trung ca hai moi hien.

5. Kiem tra du lieu goc DB
- Xac nhan `LearningDate` nam trong tuan cua `date` da chon.

##### Logic bo loc trong Room (hien tai)

- `manageRoom.jsp` hien tai chua co form filter backend.
- Danh sach phong la full list tu `RoomDAO.getAllRoom()`.
- "Loc" hien tai cua Room chu yeu la theo luat hien nut action:
  - `roomUsageMap[roomId] = true` -> hien `Disable`
  - `roomUsageMap[roomId] = false` -> hien `Delete`

---

## 3.4. doPost (academic CRUD)

### `action=create`
Input form:
- `classId`, `roomId`, `slotId`, `learningDate`

Buoc xu ly:
1. Lay `teacherId` tu class:
   - `teacherId = classDAO.getTeacherIdByClassId(classId)`
2. Validate conflict theo thu tu:
   - `hasScheduleConflict(classId, slotId, learningDate, 0)`
   - `isRoomAvailable(roomId, slotId, learningDate, 0)`
   - `isTeacherAvailable(teacherId, slotId, learningDate, 0)`
   - `teacherExceedsWeeklyLimit(teacherId, learningDate, 0)`
3. Neu pass -> `createSchedule(..., attendanceStatus=false)`
4. Set flash message
5. Redirect lai `schedule?action=manage` kem context session (`classId/roomId/date`)

### `action=update`
Input:
- `scheduleId`, `classId`, `roomId`, `slotId`, `learningDate`

Xu ly:
1. Lay schedule cu: `existingSchedule = getScheduleById(scheduleId)`
2. Co gang lay `teacherId` tu `existingSchedule.getEmployee()`
3. Neu khong co employee -> fallback lay teacher tu class
4. Validate conflict giong create, nhung truyen `excludeScheduleId = scheduleId`
5. Goi `editSchedule(...)`
6. Redirect manage kem context

### `action=delete`
Input:
- `scheduleId`
- `deleteScope` (`single` hoac `series`)

Xu ly:
1. Lay schedule hien tai
2. Neu `attendanceStatus = true` -> chan xoa
3. Neu `deleteScope=series`:
   - `deleteSimilarSchedules(scheduleId)`
4. Neu single:
   - `deleteSchedule(scheduleId)`
5. Redirect manage kem context

---

## 3.5. Hop dong ham quan trong trong ScheduleDAO
File: `src/main/java/dao/ScheduleDAO.java`

## Nhom CRUD co ban

### `List<Schedule> getAll()`
- Lay toan bo schedule
- Mapping room va slot qua DAO rieng
- Tra `List<Schedule>`

### `Schedule getScheduleById(int scheduleId)`
- Join `Schedule` + `Class`
- Set:
  - `scheduleId`
  - `learningDate` (`java.sql.Date`)
  - `attendanceStatus` (`boolean`)
  - `room` (`Room`)
  - `slot` (`Slot`)
  - `classes` (`Classes` co `classid`, `className`)
- Tra `Schedule` hoac `null`

### `boolean createSchedule(...)`
Signature:
- `(int classId, int roomId, int slotId, Date learningDate, int teacherId, boolean attendanceStatus)`
- SQL `INSERT`
- Tra `true` neu insert > 0

### `boolean editSchedule(...)`
- SQL `UPDATE ... WHERE ScheduleID = ?`
- Tra `true` neu update > 0

### `boolean deleteSchedule(int scheduleId)`
- SQL `DELETE`
- Tra `true` neu delete > 0

## Nhom validation conflict

### `boolean hasScheduleConflict(int classId, int slotId, Date learningDate, int excludeScheduleId)`
- Dem ban ghi trung class + slot + date, bo qua `excludeScheduleId`
- `true` = co xung dot

### `boolean isRoomAvailable(int roomId, int slotId, Date learningDate, int excludeScheduleId)`
- Dem ban ghi trung room + slot + date
- `true` = room ranh (count = 0)

### `boolean isTeacherAvailable(int teacherId, int slotId, Date learningDate, int excludeScheduleId)`
- Dem ban ghi trung teacher + slot + date
- `true` = teacher ranh

### `boolean teacherExceedsWeeklyLimit(int teacherId, Date learningDate, int excludeScheduleId)`
- Dem so slot trong tuan cua teacher
- `true` neu da >= 5

### `int getTeacherWeeklySlotCount(...)`
- Tra so slot hien tai trong tuan

## Nhom danh sach option/filter

### `List<Object[]> getAllRooms()`
Moi phan tu `Object[5]`:
- `[0]=RoomID (Integer)`
- `[1]=RoomName (String)`
- `[2]=Capacity (Integer)`
- `[3]=Type (String)`
- `[4]=Status (Boolean)`

### `List<Object[]> getAllTeachers()`
Moi phan tu `Object[3]`:
- `[0]=UserID`
- `[1]=FullName`
- `[2]=Email`

### `List<Schedule> getSchedulesForManagement(String selectedDate, Integer classId, Integer roomId)`
- SQL build dong theo filter
- Filter theo tuan chua `selectedDate`
- Co filter class/room neu >0
- Tra `List<Schedule>` (co room, slot, classes.className)

## Nhom student

### `List<Schedule> getScheduleByStudentWeek(int userId, String startDate, String endDate)`
- Join `Enrollment`, `Class`, `Course`, `Room`, `Slot`, `User`, `Employee`, `Attendance`
- Build object long nhau:
  - `Schedule`
    - `Classes` + `Course`
    - `Room`
    - `Slot`
    - `Employee`
- `attendanceStatus` trong model `Schedule` duoc map tu chuoi `Attendance.Status == "Present"`

## Nhom series

### `List<Schedule> getSimilarSchedules(int scheduleId)`
- Tim cac lich cung `ClassID + SlotID + RoomID`
- Tra list tang dan theo ngay

### `int deleteSimilarSchedules(int scheduleId)`
- Lay list similar
- Xoa tung lich neu `AttendanceStatus = false`
- Tra so luong da xoa

---

## 4) Request/Session contract (rat quan trong)

## 4.1 Request attributes (song trong 1 request)

### Room
- `allRooms`
- `roomUsageMap`
- `roomDetail`
- `roomDel`
- `roomDisable`
- `roomEnable`
- `roomUpdate`
- `classesUsingRoom`
- `home_view`

### Schedule
- `selectedDate`, `monday`, `dateOfWeek`, `prevWeek`, `nextWeek`
- `weekdays`, `slots`
- `scheduleList`
- `allClasses`, `allRooms`, `allTeachers`
- `schedule`
- `relatedCount`, `similarSchedules`
- `weeklySchedule`, `employeeUsers`, `weekStart`
- `home_view`

## 4.2 Session attributes (di qua redirect)
- `message`, `messageType`
- `selectedClassId`, `selectedRoomId`, `selectedDate`
- `user`

---

## 5) Trace thuc te (sequence de nho)

## 5.1 Tao 1 lich hoc (academic)
1. User mo `GET /schedule?action=create`.
2. Controller load dropdown + forward `createSchedule.jsp`.
3. User submit form `POST /schedule` voi `action=create`.
4. Controller parse input -> lay `teacherId` tu class.
5. Validate 4 lop conflict (class, room, teacher, weekly limit).
6. Neu pass -> DAO insert.
7. Controller set flash message vao session.
8. Controller redirect `schedule?action=manage&classId=...&roomId=...&date=...`.
9. `manage` action load lai list theo context cu.

## 5.2 Xoa series lich
1. User mo `GET /schedule?action=delete&scheduleId=...`.
2. Controller lay schedule + similarSchedules + relatedCount.
3. JSP cho chon `deleteScope=single|series`.
4. Submit `POST /schedule action=delete`.
5. Neu series -> DAO xoa cac lich cung pattern, bo qua lich da diem danh.
6. Redirect manage + hien message tong ket so dong xoa.

## 5.3 Disable room
1. User bam disable trong list room.
2. `GET /room?action=disable&id=...` load thong tin room + lop dang dung.
3. Submit `POST /room action=disable`.
4. DAO update `Room.Status = 0`.
5. Redirect `room` va hien toast.

---

## 6) Diem de nham va gay bug (theo code hien tai)

1. `RoomDAO.getClassesUsingRoom()` tra `String[6]` voi index theo class/course/teacher,
   nhung `roomDetail.jsp` va `disableRoom.jsp` dang doc nhu `[className, date, slot]`.
   => mismatch data shape, de hien sai cot.

2. `createSchedule.jsp` co UI recurring (`recurringType`, `endCondition`, ...),
   nhung `ScheduleController.doPost(action=create)` hien tai KHONG doc cac field recurring.
   => UI cho phep chon, backend van tao 1 record duy nhat.

3. `editSchedule.jsp` co radio `editScope=single|series`,
   nhung `doPost(action=update)` chua xu ly `editScope`.
   => luon update single.

4. `getScheduleById()` khong set `employee` trong `Schedule`.
   Trong update controller co doan lay teacher tu `existingSchedule.getEmployee()`
   nen thuong roi vao fallback lay teacher tu class.

5. Nhieu link trong JSP dang de `href=""` hoac `href="#"`.
   Neu doi path folder JSP, cac link nay de dan den route khong mong muon.

---

## 7) Cach doc code nhanh nhu debug

1. Bat dau tu URL user bam (vd `schedule?action=manage`).
2. Vao controller action tuong ung.
3. Ghi ra giay:
   - param dau vao
   - DAO nao duoc goi
   - attribute nao duoc set
   - forward/redirect di dau
4. Mo JSP, doi chieu tung `${...}` voi attribute o buoc 3.
5. Neu value null/sai type -> truy nguoc lai DAO mapping.

---

## 8) Pseudo-code de nho logic

```text
Room list:
GET /room?action=all
  -> RoomDAO.getAllRoom()
  -> for each room: RoomDAO.isRoomInUse(roomId)
  -> set allRooms + roomUsageMap + home_view
  -> forward dashboard.jsp

Schedule create:
POST /schedule action=create
  -> parse classId roomId slotId learningDate
  -> teacherId = ClassDAO.getTeacherIdByClassId(classId)
  -> if class conflict => error redirect manage
  -> if room busy => error redirect manage
  -> if teacher busy => error redirect manage
  -> if weekly limit reached => error redirect manage
  -> ScheduleDAO.createSchedule(...)
  -> success/error message
  -> redirect manage with saved filters
```

---

## 9) Ket luan
- Room va Schedule dang theo mo hinh MVC ro rang: JSP -> Controller -> DAO -> DB -> JSP.
- Thu de vo nhat khong phai SQL, ma la "hop dong du lieu" giua DAO, Controller, JSP.
- Chi can sai 1 ten attribute, sai 1 index Object[]/String[], hoac sai kieu Date la vo trang.
- Neu ban giu dung quy trinh doc flow o muc 7, ban se de debug va mo rong module hon rat nhieu.
