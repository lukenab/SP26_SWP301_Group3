USE [LanguageCenterDB];
GO

SET NOCOUNT ON;
GO

BEGIN TRANSACTION;
GO

DECLARE @DefaultPassword VARCHAR(255) = 'e10adc3949ba59abbe56e057f20f883e'; -- 123456
DECLARE @DefaultAvatar VARCHAR(255) = 'https://cdn-icons-png.flaticon.com/512/149/149071.png';

DECLARE @StudentSeed TABLE (
    FullName NVARCHAR(100),
    Email VARCHAR(100),
    Phone VARCHAR(20),
    Address NVARCHAR(255),
    Gender BIT,
    Dob DATE,
    EnrollmentDate DATE
);

INSERT INTO @StudentSeed (FullName, Email, Phone, Address, Gender, Dob, EnrollmentDate)
VALUES
(N'Nguyen Hoang Anh', 'student001@lmcs.com', '0901000001', N'Can Tho', 0, '2004-01-15', '2026-03-01'),
(N'Tran Minh Chau', 'student002@lmcs.com', '0901000002', N'Ho Chi Minh City', 1, '2005-02-18', '2026-03-01'),
(N'Le Gia Bao', 'student003@lmcs.com', '0901000003', N'Da Nang', 0, '2003-03-22', '2026-03-01'),
(N'Pham Ngoc Han', 'student004@lmcs.com', '0901000004', N'An Giang', 1, '2006-04-10', '2026-03-01'),
(N'Vo Thanh Tung', 'student005@lmcs.com', '0901000005', N'Can Tho', 0, '2004-05-27', '2026-03-01'),
(N'Dang Thu Thao', 'student006@lmcs.com', '0901000006', N'Vinh Long', 1, '2005-06-09', '2026-03-02'),
(N'Bui Quoc Dat', 'student007@lmcs.com', '0901000007', N'Soc Trang', 0, '2003-07-14', '2026-03-02'),
(N'Nguyen Khanh Linh', 'student008@lmcs.com', '0901000008', N'Kien Giang', 1, '2006-08-19', '2026-03-02'),
(N'Huynh Tuan Kiet', 'student009@lmcs.com', '0901000009', N'Dong Thap', 0, '2004-09-25', '2026-03-02'),
(N'Phan My Tien', 'student010@lmcs.com', '0901000010', N'Bac Lieu', 1, '2005-10-11', '2026-03-02'),
(N'Nguyen Duc Manh', 'student011@lmcs.com', '0901000011', N'Can Tho', 0, '2004-11-03', '2026-03-03'),
(N'Truong Bao Ngoc', 'student012@lmcs.com', '0901000012', N'Long An', 1, '2006-12-17', '2026-03-03'),
(N'Le Minh Quan', 'student013@lmcs.com', '0901000013', N'Ben Tre', 0, '2003-01-29', '2026-03-03'),
(N'Pham Thu Uyen', 'student014@lmcs.com', '0901000014', N'Tra Vinh', 1, '2005-02-06', '2026-03-03'),
(N'Do Anh Khoa', 'student015@lmcs.com', '0901000015', N'Can Tho', 0, '2004-03-12', '2026-03-03'),
(N'Nguyen Ngoc Tram', 'student016@lmcs.com', '0901000016', N'Hau Giang', 1, '2006-04-28', '2026-03-04'),
(N'Tran Quoc Huy', 'student017@lmcs.com', '0901000017', N'Ca Mau', 0, '2003-05-15', '2026-03-04'),
(N'Le Thao My', 'student018@lmcs.com', '0901000018', N'Can Tho', 1, '2005-06-21', '2026-03-04'),
(N'Pham Hoang Long', 'student019@lmcs.com', '0901000019', N'Da Nang', 0, '2004-07-08', '2026-03-04'),
(N'Vo Kim Ngan', 'student020@lmcs.com', '0901000020', N'Ho Chi Minh City', 1, '2006-08-30', '2026-03-04'),
(N'Nguyen Thanh Nam', 'student021@lmcs.com', '0901000021', N'Can Tho', 0, '2003-09-13', '2026-03-05'),
(N'Tran Bich Phuong', 'student022@lmcs.com', '0901000022', N'An Giang', 1, '2005-10-24', '2026-03-05'),
(N'Le Huu Phuc', 'student023@lmcs.com', '0901000023', N'Can Tho', 0, '2004-11-19', '2026-03-05'),
(N'Pham Khanh Vy', 'student024@lmcs.com', '0901000024', N'Kien Giang', 1, '2006-12-05', '2026-03-05'),
(N'Do Tien Dat', 'student025@lmcs.com', '0901000025', N'Vinh Long', 0, '2003-01-17', '2026-03-05'),
(N'Nguyen Mai Anh', 'student026@lmcs.com', '0901000026', N'Soc Trang', 1, '2005-02-27', '2026-03-06'),
(N'Tran Duc Tai', 'student027@lmcs.com', '0901000027', N'Ben Tre', 0, '2004-03-09', '2026-03-06'),
(N'Le Tuong Vi', 'student028@lmcs.com', '0901000028', N'Can Tho', 1, '2006-04-14', '2026-03-06'),
(N'Pham Gia Huy', 'student029@lmcs.com', '0901000029', N'Dong Thap', 0, '2003-05-26', '2026-03-06'),
(N'Vo Ngoc Diem', 'student030@lmcs.com', '0901000030', N'Long An', 1, '2005-06-18', '2026-03-06'),
(N'Nguyen Quang Hieu', 'student031@lmcs.com', '0901000031', N'Can Tho', 0, '2004-07-02', '2026-03-07'),
(N'Tran Ha My', 'student032@lmcs.com', '0901000032', N'Tra Vinh', 1, '2006-08-16', '2026-03-07'),
(N'Le Minh Tri', 'student033@lmcs.com', '0901000033', N'Bac Lieu', 0, '2003-09-28', '2026-03-07'),
(N'Pham Thanh Truc', 'student034@lmcs.com', '0901000034', N'Can Tho', 1, '2005-10-07', '2026-03-07'),
(N'Doan Gia Khang', 'student035@lmcs.com', '0901000035', N'Ca Mau', 0, '2004-11-22', '2026-03-07'),
(N'Nguyen Yen Nhi', 'student036@lmcs.com', '0901000036', N'Hau Giang', 1, '2006-12-11', '2026-03-08'),
(N'Tran Hoai Nam', 'student037@lmcs.com', '0901000037', N'Can Tho', 0, '2003-01-05', '2026-03-08'),
(N'Le Bao Chau', 'student038@lmcs.com', '0901000038', N'Ho Chi Minh City', 1, '2005-02-13', '2026-03-08'),
(N'Pham Van Kiet', 'student039@lmcs.com', '0901000039', N'Da Nang', 0, '2004-03-20', '2026-03-08'),
(N'Vo Thuy Linh', 'student040@lmcs.com', '0901000040', N'Can Tho', 1, '2006-04-25', '2026-03-08'),
(N'Nguyen Thai Son', 'student041@lmcs.com', '0901000041', N'An Giang', 0, '2003-05-08', '2026-03-09'),
(N'Tran Ngoc Huyen', 'student042@lmcs.com', '0901000042', N'Kien Giang', 1, '2005-06-30', '2026-03-09'),
(N'Le Quoc Bao', 'student043@lmcs.com', '0901000043', N'Can Tho', 0, '2004-07-17', '2026-03-09'),
(N'Pham Minh Thu', 'student044@lmcs.com', '0901000044', N'Ben Tre', 1, '2006-08-09', '2026-03-09'),
(N'Vo Anh Duy', 'student045@lmcs.com', '0901000045', N'Long An', 0, '2003-09-21', '2026-03-09'),
(N'Nguyen Gia Linh', 'student046@lmcs.com', '0901000046', N'Can Tho', 1, '2005-10-12', '2026-03-10'),
(N'Tran Minh Khang', 'student047@lmcs.com', '0901000047', N'Soc Trang', 0, '2004-11-26', '2026-03-10'),
(N'Le Tu Linh', 'student048@lmcs.com', '0901000048', N'Bac Lieu', 1, '2006-12-02', '2026-03-10'),
(N'Pham Huu Tai', 'student049@lmcs.com', '0901000049', N'Can Tho', 0, '2003-01-31', '2026-03-10'),
(N'Vo Thanh Nha', 'student050@lmcs.com', '0901000050', N'Vinh Long', 1, '2005-02-20', '2026-03-10');

INSERT INTO [dbo].[User] ([FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID])
SELECT
    s.FullName,
    s.Email,
    @DefaultPassword,
    s.Phone,
    s.Address,
    s.Gender,
    s.Dob,
    @DefaultAvatar,
    1,
    5
FROM @StudentSeed s
WHERE NOT EXISTS (
    SELECT 1
    FROM [dbo].[User] u
    WHERE u.Email = s.Email
);

INSERT INTO [dbo].[Student] ([StudentID], [EnrollmentDate])
SELECT
    u.UserID,
    s.EnrollmentDate
FROM @StudentSeed s
JOIN [dbo].[User] u ON u.Email = s.Email
WHERE NOT EXISTS (
    SELECT 1
    FROM [dbo].[Student] st
    WHERE st.StudentID = u.UserID
);

SELECT COUNT(*) AS SeedStudentCount
FROM [dbo].[User]
WHERE Email LIKE 'student0%@lmcs.com'
  AND RoleID = 5;

COMMIT TRANSACTION;
GO
