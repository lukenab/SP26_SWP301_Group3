-- Tạo bảng Slot
CREATE TABLE [dbo].[Slot](
                             [SlotID] [int] IDENTITY(1,1) NOT NULL,
                             [StartTime] [time](7) NOT NULL,
                             [EndTime] [time](7) NOT NULL,
                             PRIMARY KEY CLUSTERED
                                 (
                                  [SlotID] ASC
                                     )WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

-- Insert dữ liệu mẫu cho Slot (có thể tùy chỉnh)
INSERT INTO [dbo].[Slot] ([StartTime], [EndTime]) VALUES
                                                      ('07:00:00', '09:00:00'),
                                                      ('09:15:00', '11:15:00'),
                                                      ('12:30:00', '14:30:00'),
                                                      ('14:45:00', '16:45:00'),
                                                      ('17:00:00', '19:00:00')
GO

-- Xóa ràng buộc foreign key cũ của Schedule (nếu có)
ALTER TABLE [dbo].[Schedule] DROP CONSTRAINT IF EXISTS [FK__Schedule__ClassI__*]
GO

-- Xóa cột Slot cũ trong Schedule
ALTER TABLE [dbo].[Schedule] DROP COLUMN [Slot]
GO

-- Thêm cột SlotID vào Schedule
ALTER TABLE [dbo].[Schedule] ADD [SlotID] [int] NULL
GO

-- Tạo foreign key từ Schedule đến Slot
ALTER TABLE [dbo].[Schedule] WITH CHECK ADD FOREIGN KEY([SlotID])
    REFERENCES [dbo].[Slot] ([SlotID])
GO

-- Cập nhật dữ liệu hiện có (map Slot cũ sang SlotID mới)
UPDATE [dbo].[Schedule] SET [SlotID] = 1 WHERE [ScheduleID] IN (1, 3)
UPDATE [dbo].[Schedule] SET [SlotID] = 2 WHERE [ScheduleID] IN (2, 4, 5)
GO