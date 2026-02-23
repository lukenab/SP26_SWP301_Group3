-- ========================================
-- SUPER SIMPLE FIX - Chạy từng dòng một
-- ========================================
-- Nếu script trên vẫn lỗi, hãy chạy TỪNG DÒNG này theo thứ tự:

-- Bước 1: Chọn database
USE LanguageCenterDB;

-- Bước 2: Thêm cột Status (Chạy dòng này trước, đợi kết quả)
ALTER TABLE Room ADD Status BIT NULL;

-- Bước 3: Set giá trị mặc định cho cột Status (Chạy sau khi Bước 2 thành công)
UPDATE Room SET Status = 1;

-- Bước 4: Đổi cột Status thành NOT NULL (Optional - chạy sau khi Bước 3 thành công)
ALTER TABLE Room ALTER COLUMN Status BIT NOT NULL;

-- Bước 5: Set default constraint (Optional)
ALTER TABLE Room ADD CONSTRAINT DF_Room_Status DEFAULT 1 FOR Status;

-- Bước 6: Kiểm tra kết quả
SELECT * FROM Room;

-- DONE! Restart application.

