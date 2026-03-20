IF COL_LENGTH('dbo.Class', 'RoomID') IS NULL
BEGIN
    ALTER TABLE [dbo].[Class]
    ADD [RoomID] INT NULL;

    ALTER TABLE [dbo].[Class] WITH CHECK
    ADD CONSTRAINT [FK_Class_Room]
    FOREIGN KEY ([RoomID]) REFERENCES [dbo].[Room] ([RoomID]);
END
GO
