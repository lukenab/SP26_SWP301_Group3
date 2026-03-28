SET NOCOUNT ON;
SET XACT_ABORT ON;

IF OBJECT_ID(N'dbo.Class', N'U') IS NULL
BEGIN
    THROW 50000, 'Table dbo.Class does not exist.', 1;
END;

IF OBJECT_ID('tempdb..#ClassSeed') IS NOT NULL
BEGIN
    DROP TABLE #ClassSeed;
END;

CREATE TABLE #ClassSeed (
    ClassID INT NOT NULL PRIMARY KEY,
    ClassName NVARCHAR(50) NOT NULL,
    CourseID INT NOT NULL,
    TeacherID INT NOT NULL,
    StartDate DATE NOT NULL,
    EndDate DATE NOT NULL,
    Status NVARCHAR(20) NOT NULL,
    RoomID INT NULL
);

INSERT INTO #ClassSeed (
    ClassID,
    ClassName,
    CourseID,
    TeacherID,
    StartDate,
    EndDate,
    Status,
    RoomID
)
VALUES
    (1, N'GE-A1-M01', 1, 21, '2026-04-01', '2026-06-30', N'Pending', 2),
    (2, N'GE-A1-E01', 1, 22, '2026-04-01', '2026-06-30', N'Active', 3),
    (3, N'GE-A2-M01', 2, 23, '2026-04-05', '2026-07-05', N'Pending', 4),
    (4, N'GE-A2-W01', 2, 24, '2026-04-07', '2026-07-07', N'Pending', 5),
    (5, N'GE-B1-E01', 3, 25, '2026-04-10', '2026-07-10', N'Active', 6),
    (6, N'GE-B1-W01', 3, 26, '2026-04-12', '2026-07-12', N'Pending', 7),
    (7, N'GE-B2-M01', 4, 27, '2026-04-15', '2026-07-15', N'Active', 8),
    (8, N'GE-B2-E01', 4, 28, '2026-04-15', '2026-07-15', N'Active', 9),
    (9, N'COM-BEG-M01', 5, 29, '2026-04-02', '2026-06-02', N'Active', 10),
    (10, N'COM-BEG-E01', 5, 30, '2026-04-02', '2026-06-02', N'Pending', 11),
    (11, N'COM-INT-E01', 6, 31, '2026-04-08', '2026-06-08', N'Pending', 12),
    (12, N'COM-INT-W01', 6, 32, '2026-04-10', '2026-06-10', N'Active', 1),
    (13, N'IELTS-PREP-M01', 7, 33, '2026-04-05', '2026-08-05', N'Pending', 2),
    (14, N'IELTS-PREP-E01', 7, 34, '2026-04-05', '2026-08-05', N'Pending', 3),
    (15, N'IELTS-INT-M01', 8, 35, '2026-04-12', '2026-08-12', N'Active', 4),
    (16, N'IELTS-INT-E01', 8, 36, '2026-04-12', '2026-08-12', N'Active', 5),
    (17, N'IELTS-SW-E01', 9, 37, '2026-04-18', '2026-06-18', N'Active', 6),
    (18, N'IELTS-SW-W01', 9, 38, '2026-04-20', '2026-06-20', N'Active', 7),
    (19, N'TOEIC-450-M01', 10, 39, '2026-04-01', '2026-06-01', N'Active', 8),
    (20, N'TOEIC-450-E01', 10, 40, '2026-04-01', '2026-06-01', N'Pending', 9),
    (21, N'TOEIC-650-M01', 11, 41, '2026-04-05', '2026-06-05', N'Pending', 10),
    (22, N'TOEIC-650-E01', 11, 42, '2026-04-05', '2026-06-05', N'Active', 11),
    (23, N'TOEIC-800-E01', 12, 43, '2026-04-10', '2026-06-10', N'Pending', 12),
    (24, N'TOEIC-800-W01', 12, 44, '2026-04-12', '2026-06-12', N'Active', 1),
    (25, N'BIZ-COM-E01', 13, 45, '2026-04-15', '2026-06-15', N'Active', 2),
    (26, N'BIZ-COM-W01', 13, 46, '2026-04-18', '2026-06-18', N'Active', 3),
    (27, N'JOB-INT-E01', 14, 47, '2026-04-20', '2026-05-20', N'Active', 4),
    (28, N'JOB-INT-W01', 14, 48, '2026-04-22', '2026-05-22', N'Active', 5),
    (29, N'PUB-SPK-E01', 15, 49, '2026-04-25', '2026-06-25', N'Active', 6),
    (30, N'PUB-SPK-W01', 15, 50, '2026-04-27', '2026-06-27', N'Active', 7),
    (31, N'TRV-ENG-E01', 16, 21, '2026-05-01', '2026-06-01', N'Active', 8),
    (32, N'TRV-ENG-W01', 16, 22, '2026-05-03', '2026-06-03', N'Active', 9),
    (33, N'KID-W01', 17, 23, '2026-05-05', '2026-08-05', N'Active', 10),
    (34, N'KID-W02', 17, 24, '2026-05-05', '2026-08-05', N'Active', 11),
    (35, N'TEEN-W01', 18, 25, '2026-05-10', '2026-08-10', N'Active', 12),
    (36, N'TEEN-W02', 18, 26, '2026-05-10', '2026-08-10', N'Active', 1),
    (37, N'PRON-E01', 19, 27, '2026-05-15', '2026-07-15', N'Active', 2),
    (38, N'PRON-W01', 19, 28, '2026-05-17', '2026-07-17', N'Active', 3),
    (39, N'GRAM-M01', 20, 29, '2026-05-20', '2026-07-20', N'Active', 4),
    (40, N'GRAM-E01', 20, 44, '2026-05-20', '2026-07-20', N'Active', 5);

IF EXISTS (
    SELECT 1
    FROM #ClassSeed s
    LEFT JOIN [dbo].[Course] c ON c.CourseID = s.CourseID
    WHERE c.CourseID IS NULL
)
BEGIN
    THROW 50001, 'Missing Course rows required by SEED_CLASS_DATA.sql.', 1;
END;

IF EXISTS (
    SELECT 1
    FROM #ClassSeed s
    LEFT JOIN [dbo].[Employee] e ON e.EmployeeID = s.TeacherID
    WHERE e.EmployeeID IS NULL
)
BEGIN
    THROW 50002, 'Missing Employee rows required by SEED_CLASS_DATA.sql.', 1;
END;

IF COL_LENGTH('dbo.Class', 'RoomID') IS NOT NULL
   AND EXISTS (
       SELECT 1
       FROM #ClassSeed s
       LEFT JOIN [dbo].[Room] r ON r.RoomID = s.RoomID
       WHERE s.RoomID IS NOT NULL
         AND r.RoomID IS NULL
   )
BEGIN
    THROW 50003, 'Missing Room rows required by SEED_CLASS_DATA.sql.', 1;
END;

BEGIN TRY
    BEGIN TRAN;

    IF COL_LENGTH('dbo.Class', 'MaxCapacity') IS NOT NULL
       AND COL_LENGTH('dbo.Class', 'RoomID') IS NOT NULL
    BEGIN
        UPDATE c
        SET
            c.ClassName = s.ClassName,
            c.CourseID = s.CourseID,
            c.TeacherID = s.TeacherID,
            c.StartDate = s.StartDate,
            c.EndDate = s.EndDate,
            c.Status = s.Status,
            c.MaxCapacity = co.TotalSlots,
            c.RoomID = s.RoomID
        FROM [dbo].[Class] c
        INNER JOIN #ClassSeed s ON s.ClassID = c.ClassID
        INNER JOIN [dbo].[Course] co ON co.CourseID = s.CourseID;

        SET IDENTITY_INSERT [dbo].[Class] ON;

        INSERT INTO [dbo].[Class] (
            ClassID,
            ClassName,
            CourseID,
            TeacherID,
            StartDate,
            EndDate,
            Status,
            MaxCapacity,
            RoomID
        )
        SELECT
            s.ClassID,
            s.ClassName,
            s.CourseID,
            s.TeacherID,
            s.StartDate,
            s.EndDate,
            s.Status,
            co.TotalSlots,
            s.RoomID
        FROM #ClassSeed s
        INNER JOIN [dbo].[Course] co ON co.CourseID = s.CourseID
        WHERE NOT EXISTS (
            SELECT 1
            FROM [dbo].[Class] c
            WHERE c.ClassID = s.ClassID
        );

        SET IDENTITY_INSERT [dbo].[Class] OFF;
    END;
    ELSE IF COL_LENGTH('dbo.Class', 'MaxCapacity') IS NOT NULL
    BEGIN
        UPDATE c
        SET
            c.ClassName = s.ClassName,
            c.CourseID = s.CourseID,
            c.TeacherID = s.TeacherID,
            c.StartDate = s.StartDate,
            c.EndDate = s.EndDate,
            c.Status = s.Status,
            c.MaxCapacity = co.TotalSlots
        FROM [dbo].[Class] c
        INNER JOIN #ClassSeed s ON s.ClassID = c.ClassID
        INNER JOIN [dbo].[Course] co ON co.CourseID = s.CourseID;

        SET IDENTITY_INSERT [dbo].[Class] ON;

        INSERT INTO [dbo].[Class] (
            ClassID,
            ClassName,
            CourseID,
            TeacherID,
            StartDate,
            EndDate,
            Status,
            MaxCapacity
        )
        SELECT
            s.ClassID,
            s.ClassName,
            s.CourseID,
            s.TeacherID,
            s.StartDate,
            s.EndDate,
            s.Status,
            co.TotalSlots
        FROM #ClassSeed s
        INNER JOIN [dbo].[Course] co ON co.CourseID = s.CourseID
        WHERE NOT EXISTS (
            SELECT 1
            FROM [dbo].[Class] c
            WHERE c.ClassID = s.ClassID
        );

        SET IDENTITY_INSERT [dbo].[Class] OFF;
    END;
    ELSE IF COL_LENGTH('dbo.Class', 'RoomID') IS NOT NULL
    BEGIN
        UPDATE c
        SET
            c.ClassName = s.ClassName,
            c.CourseID = s.CourseID,
            c.TeacherID = s.TeacherID,
            c.StartDate = s.StartDate,
            c.EndDate = s.EndDate,
            c.Status = s.Status,
            c.RoomID = s.RoomID
        FROM [dbo].[Class] c
        INNER JOIN #ClassSeed s ON s.ClassID = c.ClassID;

        SET IDENTITY_INSERT [dbo].[Class] ON;

        INSERT INTO [dbo].[Class] (
            ClassID,
            ClassName,
            CourseID,
            TeacherID,
            StartDate,
            EndDate,
            Status,
            RoomID
        )
        SELECT
            s.ClassID,
            s.ClassName,
            s.CourseID,
            s.TeacherID,
            s.StartDate,
            s.EndDate,
            s.Status,
            s.RoomID
        FROM #ClassSeed s
        WHERE NOT EXISTS (
            SELECT 1
            FROM [dbo].[Class] c
            WHERE c.ClassID = s.ClassID
        );

        SET IDENTITY_INSERT [dbo].[Class] OFF;
    END;
    ELSE
    BEGIN
        UPDATE c
        SET
            c.ClassName = s.ClassName,
            c.CourseID = s.CourseID,
            c.TeacherID = s.TeacherID,
            c.StartDate = s.StartDate,
            c.EndDate = s.EndDate,
            c.Status = s.Status
        FROM [dbo].[Class] c
        INNER JOIN #ClassSeed s ON s.ClassID = c.ClassID;

        SET IDENTITY_INSERT [dbo].[Class] ON;

        INSERT INTO [dbo].[Class] (
            ClassID,
            ClassName,
            CourseID,
            TeacherID,
            StartDate,
            EndDate,
            Status
        )
        SELECT
            s.ClassID,
            s.ClassName,
            s.CourseID,
            s.TeacherID,
            s.StartDate,
            s.EndDate,
            s.Status
        FROM #ClassSeed s
        WHERE NOT EXISTS (
            SELECT 1
            FROM [dbo].[Class] c
            WHERE c.ClassID = s.ClassID
        );

        SET IDENTITY_INSERT [dbo].[Class] OFF;
    END;

    DECLARE @MaxClassID INT;
    SET @MaxClassID = (SELECT MAX(ClassID) FROM #ClassSeed);

    DBCC CHECKIDENT ('dbo.Class', RESEED, @MaxClassID) WITH NO_INFOMSGS;

    COMMIT TRAN;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
    BEGIN
        ROLLBACK TRAN;
    END;

    BEGIN TRY
        SET IDENTITY_INSERT [dbo].[Class] OFF;
    END TRY
    BEGIN CATCH
    END CATCH;

    THROW;
END CATCH;

DROP TABLE #ClassSeed;
