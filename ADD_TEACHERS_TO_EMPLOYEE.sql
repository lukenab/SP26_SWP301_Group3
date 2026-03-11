-- Script to add missing teachers to Employee table
-- Date: March 2, 2026

USE [LanguageCenterDB]
GO

-- Add Nguyen Thi Thu Lan (UserID = 4) to Employee table
-- She is already a teacher (RoleID = 4) but missing from Employee
INSERT INTO [dbo].[Employee] ([EmployeeID], [HireDate], [Education], [Experience])
VALUES (4, CAST(N'2026-01-15' AS Date), N'IELTS 8.0', N'Experienced English teacher with 5+ years teaching TOEIC and IELTS preparation courses')
GO

-- Update Le Nhut Huy (UserID = 9) from Admin (RoleID = 1) to Teacher (RoleID = 4)
UPDATE [dbo].[User]
SET [RoleID] = 4, [Status] = 1
WHERE [UserID] = 9
GO

-- Add Le Nhut Huy (UserID = 9) to Employee table
INSERT INTO [dbo].[Employee] ([EmployeeID], [HireDate], [Education], [Experience])
VALUES (9, CAST(N'2026-03-01' AS Date), N'IELTS 6.5', N'Passionate English teacher with expertise in communicative language teaching methods')
GO

-- Verify the changes
SELECT u.UserID, u.FullName, u.Email, u.RoleID, r.RoleName, e.HireDate, e.Education
FROM [User] u
LEFT JOIN Employee e ON u.UserID = e.EmployeeID
LEFT JOIN Role r ON u.RoleID = r.RoleID
WHERE u.UserID IN (4, 9)
GO

PRINT 'Successfully added teachers to Employee table!'
PRINT 'UserID 4: Nguyen Thi Thu Lan - Already a teacher, now in Employee'
PRINT 'UserID 9: Le Nhut Huy - Changed from Admin to Teacher, added to Employee'
GO
