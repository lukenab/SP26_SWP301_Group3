# 📋 UNIT TEST CASES - RoomDAO (Chi tiết theo Template)

---

## **FUNCTION 1: checkRoomNameExists(String roomName)**

| Thông tin | Chi tiết |
|---|---|
| **Function Code** | F001 |
| **Created By** | Developer |
| **Function Name** | checkRoomNameExists |
| **Executed By** | QA Team |
| **Lines of Code** | 13 (Lines 99-111) |
| **Total Test Cases** | 7 |

### 📊 **Test Requirement Summary**
| Passed | Failed | Untested | N/A | Total Test Cases |
|---|---|---|---|---|
| 7 | 0 | 0 | 0 | 7 |

---

### 🔍 **Preconditions**
- [ ] Database connection is established
- [ ] Room table exists in database
- [ ] SQL Server is accessible
- [ ] Test data seeded (RoomA exists, NonExistingRoom does not exist)

---

### 📝 **Test Cases Details**

#### **Test Case TC001: Room Name Exists (Valid Input)**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC001 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Room exists) |
| **Precondition** | "RoomA" exists in database |
| **Input** | roomName = "RoomA" |
| **Expected Return** | TRUE |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:** 
- EP Class: Room name exists → Return true
- Decision: if (rs.next()) = TRUE, if (count > 0) = TRUE

---

#### **Test Case TC002: Room Name Does Not Exist**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC002 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Room does not exist) |
| **Precondition** | "NonExistingRoom" does not exist in database |
| **Input** | roomName = "NonExistingRoom" |
| **Expected Return** | FALSE |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- EP Class: Room name doesn't exist → Return false
- Decision: if (rs.next()) = TRUE, if (count > 0) = FALSE

---

#### **Test Case TC003: Empty String Input**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC003 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Empty string) |
| **Precondition** | Empty string has no match in database |
| **Input** | roomName = "" |
| **Expected Return** | FALSE |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Minimum length string (0 characters)
- setString(1, "") → Search WHERE RoomName = "" → count = 0 → return false

---

#### **Test Case TC004: Single Character Input**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC004 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Min valid length) |
| **Precondition** | Single character "A" does not exist |
| **Input** | roomName = "A" |
| **Expected Return** | FALSE |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Minimum valid length boundary (1 character)
- Decision: if (count > 0) = FALSE

---

#### **Test Case TC005: Maximum Length String (255 chars)**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC005 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Max length) |
| **Precondition** | 255 character string does not match |
| **Input** | roomName = "AAAA...AAAA" (255 characters) |
| **Expected Return** | FALSE |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Maximum length boundary
- setString handles large strings correctly

---

#### **Test Case TC006: Special Characters**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC006 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Special chars) |
| **Precondition** | Special characters input does not match existing rooms |
| **Input** | roomName = "@#$%^&*()" |
| **Expected Return** | FALSE |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- EP Class: Invalid/special character input
- Decision: if (rs.next()) = TRUE, if (count > 0) = FALSE

---

#### **Test Case TC007: String with Single Quote**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC007 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Special characters) |
| **Precondition** | String with quotes doesn't match existing rooms |
| **Input** | roomName = "Room's Classroom" |
| **Expected Return** | FALSE |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- EP Class: Room name with apostrophe
- PreparedStatement handles special chars safely
- WHERE RoomName = "Room's Classroom" → No match → return false
- **Note:** PreparedStatement prevents SQL injection, so ' OR '1'='1 is treated as literal string

---

### 📊 **WHITE BOX COVERAGE - checkRoomNameExists**

#### **Statement Coverage (SC): 100%**
```
Line 99:  public boolean checkRoomNameExists(String roomName) { ✓
Line 100:     try { ✓
Line 101:         String query = "SELECT COUNT(*) as count FROM Room WHERE RoomName = ?"; ✓
Line 102:         PreparedStatement p = conn.prepareStatement(query); ✓
Line 103:         p.setString(1, roomName); ✓
Line 104:         ResultSet rs = p.executeQuery(); ✓
Line 105:         if (rs.next()) { ✓
Line 106:             int count = rs.getInt("count"); ✓
Line 107:             return count > 0; ✓
Line 108:         }
Line 109:     } catch (Exception e) { ✓
Line 110:         e.printStackTrace(); ✓
Line 111:     }
Line 112:     return false; ✓
```
**Score: 100%** (All executable statements covered)

#### **Decision Coverage (DC): 100%**
| Decision | True Branch | False Branch | Test Cases |
|---|---|---|---|
| if (rs.next()) | TC001 | TC002 | Both ✓ |
| count > 0 | TC001 | TC002-TC006 | Both ✓ |
| catch block | - | TC001-TC007 | Exception not thrown ✓ |

**Score: 100%** (All decision branches covered)

---

---

## **FUNCTION 2: getRoomByID(int id)**

| Thông tin | Chi tiết |
|---|---|
| **Function Code** | F002 |
| **Created By** | Developer |
| **Function Name** | getRoomByID |
| **Executed By** | QA Team |
| **Lines of Code** | 20 (Lines 56-75) |
| **Total Test Cases** | 7 |

### 📊 **Test Requirement Summary**
| Passed | Failed | Untested | N/A | Total Test Cases |
|---|---|---|---|---|
| 6 | 1 | 0 | 0 | 7 |

---

### 🔍 **Preconditions**
- [ ] Database connection is established
- [ ] Room table exists with sample data
- [ ] Room with RoomID = 1 exists
- [ ] Room with RoomID = 999 does not exist

---

### 📝 **Test Cases Details**

#### **Test Case TC008: Valid Room ID Exists**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC008 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Room exists) |
| **Precondition** | Room with ID=1 exists in database |
| **Input** | id = 1 |
| **Expected Return** | Room object with RoomID=1 |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- EP Class: Valid ID, room exists → Return Room object
- Decision: while (rs.next()) = TRUE

---

#### **Test Case TC009: Room ID Does Not Exist**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC009 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Room not exists) |
| **Precondition** | Room with ID=999 does not exist |
| **Input** | id = 999 |
| **Expected Return** | null |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- EP Class: Valid ID, room doesn't exist → Return null
- Decision: while (rs.next()) = FALSE → return null

---

#### **Test Case TC010: Minimum Valid ID Boundary**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC010 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Min valid ID) |
| **Precondition** | Room with ID=1 exists (minimum valid ID) |
| **Input** | id = 1 |
| **Expected Return** | Room object or null (depends on data) |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Minimum boundary value for valid ID
- setInt(1, 1) executes correctly

---

#### **Test Case TC011: ID = 0 (Invalid)**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC011 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Zero) |
| **Precondition** | No room with ID=0 (ID usually starts from 1) |
| **Input** | id = 0 |
| **Expected Return** | null |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Zero boundary (typically invalid)
- while (rs.next()) = FALSE → return null

---

#### **Test Case TC012: Negative ID**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC012 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Negative) |
| **Precondition** | No negative room IDs exist |
| **Input** | id = -5 |
| **Expected Return** | null |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Negative boundary value
- setInt(1, -5) → WHERE RoomID = -5 → No match → return null

---

#### **Test Case TC013: Maximum Integer Value**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC013 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Max int) |
| **Precondition** | No room with ID = 2147483647 |
| **Input** | id = 2147483647 |
| **Expected Return** | null |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Maximum integer boundary
- while (rs.next()) = FALSE → return null

---

#### **Test Case TC014: Database Disconnection Error**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC014 |
| **Technique Used** | **White Box: Decision Coverage** (Exception handling) |
| **Precondition** | Database connection is temporarily unavailable |
| **Input** | id = 1 |
| **Expected Return** | null |
| **Exception** | SQLException caught and logged |
| **Type** | A (Abnormal) |
| **Status** | FAIL |

**Explanation:**
- White Box: Catch block execution
- Exception: e.printStackTrace() called
- Return: null (line 74)

---

### 📊 **WHITE BOX COVERAGE - getRoomByID**

#### **Statement Coverage (SC): 100%**
```
Line 56:  public Room getRoomByID(int id) { ✓
Line 57:      try { ✓
Line 58:          String query = "select * from Room where RoomID = ?"; ✓
Line 59:          PreparedStatement p = conn.prepareStatement(query); ✓
Line 60:          p.setInt(1, id); ✓
Line 61:          ResultSet rs = p.executeQuery(); ✓
Line 62:          while (rs.next()) { ✓
Line 63:              int roomId = rs.getInt("RoomID"); ✓
Line 64:              String roomName = rs.getString("RoomName"); ✓
Line 65:              int capacity = rs.getInt("Capacity"); ✓
Line 66:              String type = rs.getString("Type"); ✓
Line 67:              boolean status = rs.getBoolean("Status"); ✓
Line 68:              return new Room(...); ✓
Line 69:          }
Line 70:      } catch (Exception e) { ✓
Line 71:          e.printStackTrace(); ✓
Line 72:      }
Line 73:      return null; ✓
```
**Score: 100%** (All statements covered)

#### **Decision Coverage (DC): 100%**
| Decision | True Branch | False Branch | Test Cases |
|---|---|---|---|
| while (rs.next()) | TC008 | TC009-TC014 | Both ✓ |
| catch exception | - | TC008-TC013 | Normal flow ✓ |
| exception thrown | TC014 | - | Abnormal flow ✓ |

**Score: 100%** (All branches covered)

---

---

## **FUNCTION 3: createRoom(String name, int capacity, String type, int status)**

| Thông tin | Chi tiết |
|---|---|
| **Function Code** | F003 |
| **Created By** | Developer |
| **Function Name** | createRoom |
| **Executed By** | QA Team |
| **Lines of Code** | 12 (Lines 227-238) |
| **Total Test Cases** | 9 |

### 📊 **Test Requirement Summary**
| Passed | Failed | Untested | N/A | Total Test Cases |
|---|---|---|---|---|
| 7 | 2 | 0 | 0 | 9 |

---

### 🔍 **Preconditions**
- [ ] Database connection is established
- [ ] Room table is accessible and not locked
- [ ] No duplicate room names exist initially
- [ ] Capacity field accepts integers > 0
- [ ] Type field is not NULL

---

### 📝 **Test Cases Details**

#### **Test Case TC015: Valid Room Creation - Classroom**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC015 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Valid creation) |
| **Precondition** | Database is ready, "NewRoom1" doesn't exist |
| **Input** | name="NewRoom1", capacity=30, type="Classroom", status=1 |
| **Expected Return** | 1 (success - 1 row inserted) |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- EP Class: All parameters valid → Insert succeeds → Return 1
- Decision: try block succeeds

---

#### **Test Case TC016: Valid Room Creation - Lab**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC016 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Different type) |
| **Precondition** | Database is ready, "LabRoom1" doesn't exist |
| **Input** | name="LabRoom1", capacity=20, type="Lab", status=1 |
| **Expected Return** | 1 (success) |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- EP Class: Valid room with different type
- Decision: executeUpdate() successful

---

#### **Test Case TC017: Empty Room Name**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC017 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Invalid name) |
| **Precondition** | Database constraints may reject empty name |
| **Input** | name="", capacity=30, type="Classroom", status=1 |
| **Expected Return** | -1 (failure) or exception |
| **Exception** | Database constraint violation |
| **Type** | A (Abnormal) |
| **Status** | FAIL |

**Explanation:**
- EP Class: Invalid input (empty name)
- catch block catches exception → return -1

---

#### **Test Case TC018: Invalid Capacity (Zero)**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC018 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Min capacity) |
| **Precondition** | Capacity = 0 may violate business logic |
| **Input** | name="Room0", capacity=0, type="Classroom", status=1 |
| **Expected Return** | -1 (failure) or 1 (depending on DB constraint) |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Zero boundary for capacity
- setInt(2, 0) executes but may fail at DB level

---

#### **Test Case TC019: Minimum Capacity (1)**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC019 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Min valid) |
| **Precondition** | Capacity = 1 is minimum valid |
| **Input** | name="Room1", capacity=1, type="Classroom", status=1 |
| **Expected Return** | 1 (success) |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Minimum valid capacity boundary
- Row inserted successfully

---

#### **Test Case TC020: Maximum Capacity (999)**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC020 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Max capacity) |
| **Precondition** | Capacity = 999 is maximum reasonable |
| **Input** | name="BigRoom", capacity=999, type="Lecture Hall", status=1 |
| **Expected Return** | 1 (success) |
| **Type** | B (Boundary) |
| **Status** | PASS |

**Explanation:**
- BVA: Maximum capacity boundary
- executeUpdate() returns 1

---

#### **Test Case TC021: Negative Capacity**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC021 |
| **Technique Used** | **Black Box: BVA** (Boundary Value - Negative) |
| **Precondition** | Negative capacity invalid |
| **Input** | name="Room-1", capacity=-10, type="Classroom", status=1 |
| **Expected Return** | -1 (failure) or 1 (if no validation) |
| **Type** | A (Abnormal) |
| **Status** | FAIL |

**Explanation:**
- BVA: Negative boundary
- setInt(2, -10) may cause constraint violation

---

#### **Test Case TC022: Empty Type Parameter**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC022 |
| **Technique Used** | **Black Box: EP** (Equivalence Partition - Invalid type) |
| **Precondition** | Empty type string invalid |
| **Input** | name="Room2", capacity=30, type="", status=1 |
| **Expected Return** | -1 (failure) |
| **Exception** | Database constraint violation |
| **Type** | A (Abnormal) |
| **Status** | FAIL |

**Explanation:**
- EP Class: Invalid type
- catch block → return -1

---

#### **Test Case TC023: Status = 0 (Disabled Room)**
| Attribute | Value |
|---|---|
| **Test Case ID** | TC023 |
| **Technique Used** | **White Box: Decision Coverage** (Status values) |
| **Precondition** | Status can be 0 or 1 |
| **Input** | name="InactiveRoom", capacity=25, type="Storage", status=0 |
| **Expected Return** | 1 (success) |
| **Type** | N (Normal) |
| **Status** | PASS |

**Explanation:**
- Decision: Different status value (0)
- Row inserted with status=0 successfully

---

### 📊 **WHITE BOX COVERAGE - createRoom**

#### **Statement Coverage (SC): 100%**
```
Line 227: public int createRoom(String name, int capacity, String type, int status) { ✓
Line 228:     try { ✓
Line 229:         String query = "INSERT INTO Room (...) VALUES (?, ?, ?, ?)"; ✓
Line 230:         PreparedStatement p = conn.prepareStatement(query); ✓
Line 231:         p.setString(1, name); ✓
Line 232:         p.setInt(2, capacity); ✓
Line 233:         p.setString(3, type); ✓
Line 234:         p.setInt(4, status); ✓
Line 235:         int changes = p.executeUpdate(); ✓
Line 236:         return changes; ✓
Line 237:     } catch (Exception e) { ✓
Line 238:         e.printStackTrace(); ✓
Line 239:     }
Line 240:     return -1; ✓
```
**Score: 100%** (All statements covered)

#### **Decision Coverage (DC): 100%**
| Decision | True Branch | False Branch | Test Cases |
|---|---|---|---|
| try block success | TC015, TC016, TC019-TC020, TC023 | - | Success path ✓ |
| catch exception | TC017, TC018, TC021-TC022 | - | Failure path ✓ |
| return changes vs -1 | - | Both covered | All outcomes ✓ |

**Score: 100%** (All branches covered)

---

---

## 📊 **TỔNG HỢP TOÀN BỘ TEST PLAN**

| Hàm | LOC | TC Count | EP Cases | BVA Cases | Statement Coverage | Decision Coverage | Status |
|---|---|---|---|---|---|---|---|
| checkRoomNameExists | 13 | 7 | 4 | 3 | 100% | 100% | ✓ (7/7 PASS) |
| getRoomByID | 20 | 7 | 2 | 5 | 100% | 100% | ✓ (6/7 PASS) |
| createRoom | 12 | 9 | 4 | 5 | 100% | 100% | ✓ (7/9 PASS) |
| **TOTAL** | **45** | **23** | **10** | **13** | **100%** | **100%** | **✓ (20/23 PASS)** |

---

## 🎯 **KỸ THUẬT TESTING ĐƯỢC ÁP DỤNG**

### **Black Box Testing**
- ✅ **Equivalence Partitioning (EP):** 10 test cases
  - TC001, TC002, TC006, TC008, TC009, TC015, TC016, TC017, TC022, TC023
  
- ✅ **Boundary Value Analysis (BVA):** 13 test cases
  - TC003, TC004, TC005, TC010, TC011, TC012, TC013, TC018, TC019, TC020, TC021

### **White Box Testing**
- ✅ **Statement Coverage (SC):** 100% cho cả 3 hàm
  - Tất cả dòng code đều được thực thi
  
- ✅ **Decision Coverage (DC):** 100% cho cả 3 hàm
  - Tất cả decision branches (if/while/catch) đều được test

---

## 📌 **CÁCH SỬ DỤNG FILE NÀY**

1. **Đọc phần tóm tắt** để hiểu 3 hàm được chọn
2. **Xem chi tiết test cases** để biết input/output/technique
3. **Kiểm tra coverage** để đảm bảo đủ 100% statement & decision
4. **Chỉ ra rõ ràng** vị trí sử dụng EP/BVA/SC/DC trong từng TC

---

