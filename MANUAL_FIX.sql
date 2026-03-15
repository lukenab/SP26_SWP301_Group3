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

-- ========================================
-- Voucher Usage log table (for apply voucher flow)
-- ========================================
IF OBJECT_ID('dbo.VoucherUsage', 'U') IS NULL
BEGIN
    CREATE TABLE [dbo].[VoucherUsage](
        [UsageID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
        [VoucherID] [int] NOT NULL,
        [UsedByUserID] [int] NULL,
        [BaseAmount] [decimal](18, 2) NOT NULL,
        [DiscountAmount] [decimal](18, 2) NOT NULL,
        [FinalAmount] [decimal](18, 2) NOT NULL,
        [UsedAt] [datetime] NOT NULL DEFAULT (GETDATE()),
        [Status] [nvarchar](30) NULL
    );

    ALTER TABLE [dbo].[VoucherUsage] WITH CHECK
    ADD CONSTRAINT [FK_VoucherUsage_Voucher]
    FOREIGN KEY([VoucherID]) REFERENCES [dbo].[Voucher] ([VoucherID]);

    ALTER TABLE [dbo].[VoucherUsage] WITH CHECK
    ADD CONSTRAINT [FK_VoucherUsage_User]
    FOREIGN KEY([UsedByUserID]) REFERENCES [dbo].[User] ([UserID]);
END

-- ========================================
-- Syllabus table (for Manage Syllabus / Learning Path)
-- ========================================
IF OBJECT_ID('dbo.Syllabus', 'U') IS NULL
BEGIN
    CREATE TABLE [dbo].[Syllabus](
        [SyllabusID] [int] IDENTITY(1,1) NOT NULL PRIMARY KEY,
        [CourseID] [int] NOT NULL,
        [OrderIndex] [int] NOT NULL,
        [TopicName] [nvarchar](255) NOT NULL,
        [Description] [nvarchar](max) NULL
    );

    ALTER TABLE [dbo].[Syllabus] WITH CHECK
    ADD CONSTRAINT [FK_Syllabus_Course]
    FOREIGN KEY([CourseID]) REFERENCES [dbo].[Course] ([CourseID]);
END

IF NOT EXISTS (SELECT 1 FROM [dbo].[Syllabus])
BEGIN
    INSERT INTO [dbo].[Syllabus] ([CourseID], [OrderIndex], [TopicName], [Description]) VALUES
    (12, 1, N'Introduction to IELTS Writing Task 1', N'Overview of Task 1 format, scoring criteria, and common mistakes.'),
    (12, 2, N'Grammar and Sentence Structures', N'Practice complex sentence patterns and coherence in writing.'),
    (13, 1, N'Advanced Listening Strategies', N'Apply note-taking and distractor-handling for higher band targets.'),
    (15, 1, N'TOEIC Part 1-2 Foundation', N'Build listening reflexes with photo and question-response practice.');
END

-- ========================================
-- Class.MaxCapacity (allow per-class capacity changes)
-- ========================================
IF COL_LENGTH('dbo.Class', 'MaxCapacity') IS NULL
BEGIN
    ALTER TABLE [dbo].[Class] ADD [MaxCapacity] [int] NULL;

    UPDATE c
    SET c.MaxCapacity = co.TotalSlots
    FROM [dbo].[Class] c
    LEFT JOIN [dbo].[Course] co ON c.CourseID = co.CourseID
    WHERE c.MaxCapacity IS NULL;

    ALTER TABLE [dbo].[Class] ALTER COLUMN [MaxCapacity] [int] NOT NULL;
END
