USE [master]
GO
/****** Object:  Database [LanguageCenterDB]    Script Date: 24-Feb-26 7:59:22 PM ******/
CREATE DATABASE [LanguageCenterDB]
GO
Use LanguageCenterDB
ALTER DATABASE [LanguageCenterDB] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [LanguageCenterDB].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [LanguageCenterDB] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET ARITHABORT OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [LanguageCenterDB] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [LanguageCenterDB] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET  ENABLE_BROKER 
GO
ALTER DATABASE [LanguageCenterDB] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [LanguageCenterDB] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET RECOVERY FULL 
GO
ALTER DATABASE [LanguageCenterDB] SET  MULTI_USER 
GO
ALTER DATABASE [LanguageCenterDB] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [LanguageCenterDB] SET DB_CHAINING OFF 
GO
ALTER DATABASE [LanguageCenterDB] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [LanguageCenterDB] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [LanguageCenterDB] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [LanguageCenterDB] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'LanguageCenterDB', N'ON'
GO
ALTER DATABASE [LanguageCenterDB] SET QUERY_STORE = ON
GO
ALTER DATABASE [LanguageCenterDB] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [LanguageCenterDB]
GO
/****** Object:  Table [dbo].[Assessment]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Assessment](
	[AssessmentID] [int] IDENTITY(1,1) NOT NULL,
	[CourseID] [int] NULL,
	[AssessmentName] [nvarchar](50) NOT NULL,
	[Weight] [float] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[AssessmentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Attendance]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Attendance](
	[AttendanceID] [int] IDENTITY(1,1) NOT NULL,
	[ScheduleID] [int] NULL,
	[EnrollmentID] [int] NULL,
	[Status] [nvarchar](20) NULL,
	[Note] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[AttendanceID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Class]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Class](
	[ClassID] [int] IDENTITY(1,1) NOT NULL,
	[ClassName] [nvarchar](50) NOT NULL,
	[CourseID] [int] NULL,
	[TeacherID] [int] NULL,
	[StartDate] [date] NULL,
	[EndDate] [date] NULL,
	[Status] [nvarchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[ClassID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Consultation]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Consultation](
	[ConsultationID] [int] IDENTITY(1,1) NOT NULL,
	[LeadID] [int] NULL,
	[SaleID] [int] NULL,
	[Note] [nvarchar](max) NULL,
	[ConsultDate] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ConsultationID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Course]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Course](
	[CourseID] [int] IDENTITY(1,1) NOT NULL,
	[CourseName] [nvarchar](100) NOT NULL,
	[Description] [nvarchar](max) NULL,
	[TotalSlots] [int] NOT NULL,
	[TuitionFee] [decimal](18, 2) NOT NULL,
	[Status] [bit] NULL,
	[image] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[CourseID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Employee]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Employee](
	[EmployeeID] [int] NOT NULL,
	[HireDate] [date] NULL,
	[Education] [nvarchar](255) NULL,
	[Experience] [nvarchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[EmployeeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Enrollment]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Enrollment](
	[EnrollmentID] [int] IDENTITY(1,1) NOT NULL,
	[StudentID] [int] NULL,
	[ClassID] [int] NULL,
	[EnrollDate] [datetime] NULL,
	[Status] [nvarchar](20) NULL,
	[FinalGrade] [float] NULL,
PRIMARY KEY CLUSTERED 
(
	[EnrollmentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Feedback]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Feedback](
	[FeedbackID] [int] IDENTITY(1,1) NOT NULL,
	[EnrollmentID] [int] NULL,
	[Rating] [int] NULL,
	[Comment] [nvarchar](max) NULL,
	[SentDate] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[FeedbackID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Grade]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Grade](
	[GradeID] [int] IDENTITY(1,1) NOT NULL,
	[EnrollmentID] [int] NULL,
	[AssessmentID] [int] NULL,
	[Score] [float] NULL,
PRIMARY KEY CLUSTERED 
(
	[GradeID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Lead]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Lead](
	[LeadID] [int] IDENTITY(1,1) NOT NULL,
	[FullName] [nvarchar](100) NULL,
	[Email] [varchar](100) NULL,
	[Phone] [varchar](20) NULL,
	[InterestedCourseID] [int] NULL,
	[Status] [nvarchar](20) NULL,
	[CreateDate] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[LeadID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Payment]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Payment](
	[PaymentID] [int] IDENTITY(1,1) NOT NULL,
	[EnrollmentID] [int] NULL,
	[Amount] [decimal](18, 2) NOT NULL,
	[PaymentDate] [datetime] NULL,
	[PaymentMethod] [nvarchar](50) NULL,
	[EvidenceImage] [varchar](255) NULL,
	[Status] [nvarchar](20) NULL,
	[VoucherID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[PaymentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Role]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Role](
	[RoleID] [int] IDENTITY(1,1) NOT NULL,
	[RoleName] [nvarchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[RoleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Room]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Room](
	[RoomID] [int] IDENTITY(1,1) NOT NULL,
	[RoomName] [nvarchar](50) NOT NULL,
	[Capacity] [int] NULL,
	[Type] [nvarchar](50) NULL,
	[Status] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[RoomID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Schedule]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Schedule](
	[ScheduleID] [int] IDENTITY(1,1) NOT NULL,
	[ClassID] [int] NULL,
	[RoomID] [int] NULL,
	[Slot] [int] NOT NULL,
	[LearningDate] [date] NOT NULL,
	[TeacherID] [int] NULL,
	[AttendanceStatus] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[ScheduleID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Student]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Student](
	[StudentID] [int] NOT NULL,
	[EnrollmentDate] [date] NULL,
PRIMARY KEY CLUSTERED 
(
	[StudentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SystemLog]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SystemLog](
	[LogID] [int] IDENTITY(1,1) NOT NULL,
	[ActorName] [nvarchar](100) NULL,
	[ActionType] [nvarchar](50) NULL,
	[Description] [nvarchar](max) NULL,
	[LogDate] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[LogID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[FullName] [nvarchar](100) NOT NULL,
	[Email] [varchar](100) NOT NULL,
	[Password] [varchar](255) NOT NULL,
	[Phone] [varchar](20) NULL,
	[Address] [nvarchar](255) NULL,
	[Gender] [bit] NULL,
	[Dob] [date] NULL,
	[Avatar] [varchar](255) NULL,
	[Status] [bit] NULL,
	[RoleID] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Voucher]    Script Date: 24-Feb-26 7:59:23 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Voucher](
	[VoucherID] [int] IDENTITY(1,1) NOT NULL,
	[Code] [varchar](20) NOT NULL,
	[DiscountAmount] [decimal](18, 2) NULL,
	[DiscountPercent] [float] NULL,
	[ValidUntil] [date] NULL,
	[Status] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[VoucherID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Class] ON 
GO
INSERT [dbo].[Class] ([ClassID], [ClassName], [CourseID], [TeacherID], [StartDate], [EndDate], [Status]) VALUES (2, N'IELTS_K18', 13, 3, CAST(N'2026-01-01' AS Date), CAST(N'2026-06-01' AS Date), N'Pending')
GO
INSERT [dbo].[Class] ([ClassID], [ClassName], [CourseID], [TeacherID], [StartDate], [EndDate], [Status]) VALUES (3, N'TOEIC_A01', 12, 3, CAST(N'2026-01-15' AS Date), CAST(N'2026-05-15' AS Date), N'Pending')
GO
SET IDENTITY_INSERT [dbo].[Class] OFF
GO
SET IDENTITY_INSERT [dbo].[Course] ON 
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (12, N'IELTS Foundation (Band 4.0 - 5.0)', N'A comprehensive foundation course for beginners. Focuses on essential grammar, vocabulary building, and basic listening & reading skills required for the IELTS test.', 24, CAST(4500000.00 AS Decimal(18, 2)), 1, N'ielts-foundation.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (13, N'IELTS Intensive (Band 6.5+)', N'Advanced training program targeting all 4 skills: Listening, Speaking, Reading, and Writing. Designed for students aiming for a high band score in a short period.', 30, CAST(8500000.00 AS Decimal(18, 2)), 1, N'ielts-intensive.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (14, N'Business English Communication', N'Professional English course for working environment. Learn how to write professional emails, deliver presentations, and negotiate effectively in international business contexts.', 20, CAST(5000000.00 AS Decimal(18, 2)), 1, N'business-english.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (15, N'TOEIC 500+ Preparation', N'Fast-track TOEIC preparation course. Focuses on test-taking strategies, mock tests, and business vocabulary to ensure a score of 500+.', 25, CAST(3200000.00 AS Decimal(18, 2)), 1, N'toeic-prep.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (16, N'English for Kids (Starters)', N'Fun and engaging English course for children aged 6-8. Learning through songs, interactive games, and colorful videos to build natural reflexes.', 36, CAST(6000000.00 AS Decimal(18, 2)), 1, N'english-kids.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (17, N'Advanced Speaking Workshop', N'Specialized speaking workshop with 100% Native Speakers. Focuses on correcting pronunciation, intonation, and developing natural fluency.', 15, CAST(4000000.00 AS Decimal(18, 2)), 1, N'speaking.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (18, N'Basic English Grammar', N'Grammar retrieval course for beginners. Systematizes all important grammar rules and sentence structures to build a solid foundation.', 20, CAST(2500000.00 AS Decimal(18, 2)), 0, N'grammar.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (19, N'English for Tourism & Hospitality', N'Essential English skills for professionals in hotels, travel agencies, and customer service. Focus on real-world communication.', 24, CAST(4800000.00 AS Decimal(18, 2)), 1, N'tourism-english.png')
GO
INSERT [dbo].[Course] ([CourseID], [CourseName], [Description], [TotalSlots], [TuitionFee], [Status], [image]) VALUES (20, N'Academic Writing Masterclass', N'Develop advanced writing techniques for essays, research papers, and professional reports. Ideal for university students.', 20, CAST(5500000.00 AS Decimal(18, 2)), 1, N'academic-writing.png')
GO
SET IDENTITY_INSERT [dbo].[Course] OFF
GO
INSERT [dbo].[Employee] ([EmployeeID], [HireDate], [Education], [Experience]) VALUES (3, CAST(N'2026-01-19' AS Date), N'IELTS 8.5', N'Senior Product Designer with 8+ years of experience in creating user-centered designs for web and mobile applications')
GO
INSERT [dbo].[Employee] ([EmployeeID], [HireDate], [Education], [Experience]) VALUES (15, CAST(N'2026-02-20' AS Date), N'Ielts 8.5', N'Plan and contribute to extracurricular activities such as clubs and academic contests.')
GO
INSERT [dbo].[Employee] ([EmployeeID], [HireDate], [Education], [Experience]) VALUES (16, CAST(N'2026-02-23' AS Date), N'IELTS 8.0', N'Plan and contribute to extracurricular activities such as clubs and academic contests.')
GO
SET IDENTITY_INSERT [dbo].[Lead] ON 
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (1, N'John Smith', N'john.smith@email.com', N'0901112233', 12, N'New', CAST(N'2023-10-01T00:00:00.000' AS DateTime))
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (2, N'Emily Watson', N'emily.w@email.com', N'0902223344', 13, N'Contacted', CAST(N'2023-10-02T00:00:00.000' AS DateTime))
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (3, N'Michael Chen', N'mike.chen@email.com', N'0903334455', 14, N'Consulting', CAST(N'2023-10-05T00:00:00.000' AS DateTime))
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (4, N'Sarah Johnson', N'sarah.j@email.com', N'0904445566', 15, N'Converted', CAST(N'2023-09-20T00:00:00.000' AS DateTime))
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (5, N'David Kim', N'david.kim@email.com', N'0905556677', 16, N'Lost', CAST(N'2023-09-15T00:00:00.000' AS DateTime))
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (6, N'Jessica Nguyen', N'jessica.n@email.com', N'0906667788', 17, N'New', CAST(N'2023-10-10T00:00:00.000' AS DateTime))
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (7, N'Robert Brown', N'rob.brown@email.com', N'0907778899', 18, N'Consulting', CAST(N'2023-10-08T00:00:00.000' AS DateTime))
GO
INSERT [dbo].[Lead] ([LeadID], [FullName], [Email], [Phone], [InterestedCourseID], [Status], [CreateDate]) VALUES (8, N'Amanda Taylor', N'amanda.t@email.com', N'0908889900', 19, N'New', CAST(N'2023-10-11T00:00:00.000' AS DateTime))
GO
SET IDENTITY_INSERT [dbo].[Lead] OFF
GO
SET IDENTITY_INSERT [dbo].[Role] ON 
GO
INSERT [dbo].[Role] ([RoleID], [RoleName]) VALUES (2, N'Academic Staff')
GO
INSERT [dbo].[Role] ([RoleID], [RoleName]) VALUES (1, N'Admin')
GO
INSERT [dbo].[Role] ([RoleID], [RoleName]) VALUES (3, N'Sale Staff')
GO
INSERT [dbo].[Role] ([RoleID], [RoleName]) VALUES (5, N'Student')
GO
INSERT [dbo].[Role] ([RoleID], [RoleName]) VALUES (4, N'Teacher')
GO
SET IDENTITY_INSERT [dbo].[Role] OFF
GO
SET IDENTITY_INSERT [dbo].[Room] ON 
GO
INSERT [dbo].[Room] ([RoomID], [RoomName], [Capacity], [Type], [Status]) VALUES (1, N'R101', 30, NULL, 1)
GO
INSERT [dbo].[Room] ([RoomID], [RoomName], [Capacity], [Type], [Status]) VALUES (2, N'R205', 40, NULL, 1)
GO
INSERT [dbo].[Room] ([RoomID], [RoomName], [Capacity], [Type], [Status]) VALUES (3, N'Lab-A', 25, NULL, 1)
GO
SET IDENTITY_INSERT [dbo].[Room] OFF
GO
SET IDENTITY_INSERT [dbo].[Schedule] ON 
GO
INSERT [dbo].[Schedule] ([ScheduleID], [ClassID], [RoomID], [Slot], [LearningDate], [TeacherID], [AttendanceStatus]) VALUES (1, 2, 1, 1, CAST(N'2026-02-02' AS Date), 3, 1)
GO
INSERT [dbo].[Schedule] ([ScheduleID], [ClassID], [RoomID], [Slot], [LearningDate], [TeacherID], [AttendanceStatus]) VALUES (2, 2, 1, 2, CAST(N'2026-02-03' AS Date), 3, 0)
GO
INSERT [dbo].[Schedule] ([ScheduleID], [ClassID], [RoomID], [Slot], [LearningDate], [TeacherID], [AttendanceStatus]) VALUES (3, 2, 1, 1, CAST(N'2026-02-04' AS Date), 3, 0)
GO
INSERT [dbo].[Schedule] ([ScheduleID], [ClassID], [RoomID], [Slot], [LearningDate], [TeacherID], [AttendanceStatus]) VALUES (4, 2, 1, 2, CAST(N'2026-02-05' AS Date), 3, 0)
GO
INSERT [dbo].[Schedule] ([ScheduleID], [ClassID], [RoomID], [Slot], [LearningDate], [TeacherID], [AttendanceStatus]) VALUES (5, 3, 2, 1, CAST(N'2026-02-06' AS Date), 3, 1)
GO
SET IDENTITY_INSERT [dbo].[Schedule] OFF
GO
INSERT [dbo].[Student] ([StudentID], [EnrollmentDate]) VALUES (14, CAST(N'2026-02-15' AS Date))
GO
SET IDENTITY_INSERT [dbo].[User] ON 
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (1, N'Admin', N'admin@fpt.edu.vn', N'e10adc3949ba59abbe56e057f20f883e', N'0812154005', N'Can Tho', 0, CAST(N'2026-02-13' AS Date), N'https://cdn.chuuniotaku.com/upload/chuuniotaku_com/post/images/2024/01/06/1101/akai-shuichi-3.jpg', 1, 1)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (3, N'Luu Huu Luan', N'teacher@fpt.edu.vn', N'e10adc3949ba59abbe56e057f20f883e', N'0971692124', N'Can Tho', 0, CAST(N'2003-06-28' AS Date), N'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSx7Xsp04dHfEpd10iB3hW3VsyJLESrQGw_IA&s', 0, 4)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (4, N'Nguyễn Thị Thu Lan', N'teacher@lmcs.com', N'202cb962ac59075b964b07152d234b70', N'0909123456', N'Da Nang', 1, CAST(N'1980-05-15' AS Date), N'https://image.lag.vn/upload/news/25/07/29/cgv1-15939230426701527304222_XVLZ.png', 1, 4)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (5, N'Nguyễn An Bình', N'student@lmcs.com', N'202cb962ac59075b964b07152d234b70', N'0912345678', N'Nghệ An', 0, CAST(N'2006-06-11' AS Date), N'https://genk.mediacdn.vn/2019/10/2/photo-1-15700177064031654572509.jpg', 1, 5)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (9, N'Lê Nhựt Huy', N'huyce200064@gmail.com', N'123456', N'02838445678', N'234 Võ Văn Kiệt', 0, CAST(N'2026-02-27' AS Date), N'https://www.detectiveconanworld.com/wiki/images/0/05/Genta_Kojima_Profile.jpg', 0, 1)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (12, N'Nguyen Thi Kim Lien', N'khanhvh@university.edu.vn', N'e10adc3949ba59abbe56e057f20f883e', N'0812154005', N'234 Võ Văn Kiệt', 0, CAST(N'2026-02-26' AS Date), N'https://iweather.edu.vn/upload/2025/04/ran-7-001.webp', 1, 3)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (13, N'Trần Gia Tường', N'tuongtran@gmail.com', N'e10adc3949ba59abbe56e057f20f883e', N'0812154005', N'Can Tho', 1, CAST(N'2026-02-11' AS Date), N'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZAMblRE2JgKH6BYlVIqOyEAipCHS99TwrKg&s', 1, 5)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (14, N'Nguyễn Minh Quân', N'quannmcs180364@gmail.com', N'202cb962ac59075b964b07152d234b70', N'0905111222', N'Nghe An', 1, CAST(N'2026-02-12' AS Date), N'https://cdn-icons-png.flaticon.com/512/149/149071.png', 1, 5)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (15, N'Nguyen Thi Kim Lien', N'lienlmcs@gmail.com', N'e10adc3949ba59abbe56e057f20f883e', N'0905111222', N'Long Xuyen', 1, CAST(N'2005-02-08' AS Date), N'https://i.pinimg.com/originals/8d/60/1f/8d601f2a5c7412f252f67ce10b88817e.jpg', 1, 3)
GO
INSERT [dbo].[User] ([UserID], [FullName], [Email], [Password], [Phone], [Address], [Gender], [Dob], [Avatar], [Status], [RoleID]) VALUES (16, N'Trương Hoàng Phúc', N'phucnmcs@gmail.com', N'e10adc3949ba59abbe56e057f20f883e', N'02838445678', N'234 Võ Văn Kiệt', 1, CAST(N'2005-02-23' AS Date), N'https://cdn.chuuniotaku.com/upload/chuuniotaku_com/post/images/2024/01/06/1101/akai-shuichi-3.jpg', 1, 2)
GO
SET IDENTITY_INSERT [dbo].[User] OFF
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Class__F8BF561BCF8105CE]    Script Date: 24-Feb-26 7:59:23 PM ******/
ALTER TABLE [dbo].[Class] ADD UNIQUE NONCLUSTERED 
(
	[ClassName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Role__8A2B61606C231D11]    Script Date: 24-Feb-26 7:59:23 PM ******/
ALTER TABLE [dbo].[Role] ADD UNIQUE NONCLUSTERED 
(
	[RoleName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__User__A9D1053463A51F45]    Script Date: 24-Feb-26 7:59:23 PM ******/
ALTER TABLE [dbo].[User] ADD UNIQUE NONCLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__Voucher__A25C5AA715BEC57D]    Script Date: 24-Feb-26 7:59:23 PM ******/
ALTER TABLE [dbo].[Voucher] ADD UNIQUE NONCLUSTERED 
(
	[Code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Class] ADD  DEFAULT ('Pending') FOR [Status]
GO
ALTER TABLE [dbo].[Consultation] ADD  DEFAULT (getdate()) FOR [ConsultDate]
GO
ALTER TABLE [dbo].[Course] ADD  DEFAULT ((1)) FOR [Status]
GO
ALTER TABLE [dbo].[Employee] ADD  DEFAULT (getdate()) FOR [HireDate]
GO
ALTER TABLE [dbo].[Enrollment] ADD  DEFAULT (getdate()) FOR [EnrollDate]
GO
ALTER TABLE [dbo].[Enrollment] ADD  DEFAULT ('Unpaid') FOR [Status]
GO
ALTER TABLE [dbo].[Feedback] ADD  DEFAULT (getdate()) FOR [SentDate]
GO
ALTER TABLE [dbo].[Lead] ADD  DEFAULT ('New') FOR [Status]
GO
ALTER TABLE [dbo].[Lead] ADD  DEFAULT (getdate()) FOR [CreateDate]
GO
ALTER TABLE [dbo].[Payment] ADD  DEFAULT (getdate()) FOR [PaymentDate]
GO
ALTER TABLE [dbo].[Payment] ADD  DEFAULT ('Pending') FOR [Status]
GO
ALTER TABLE [dbo].[Room] ADD  CONSTRAINT [DF_Room_Status]  DEFAULT ((1)) FOR [Status]
GO
ALTER TABLE [dbo].[Schedule] ADD  DEFAULT ((0)) FOR [AttendanceStatus]
GO
ALTER TABLE [dbo].[Student] ADD  DEFAULT (getdate()) FOR [EnrollmentDate]
GO
ALTER TABLE [dbo].[SystemLog] ADD  DEFAULT (getdate()) FOR [LogDate]
GO
ALTER TABLE [dbo].[User] ADD  DEFAULT ((1)) FOR [Status]
GO
ALTER TABLE [dbo].[Voucher] ADD  DEFAULT ((1)) FOR [Status]
GO
ALTER TABLE [dbo].[Assessment]  WITH CHECK ADD FOREIGN KEY([CourseID])
REFERENCES [dbo].[Course] ([CourseID])
GO
ALTER TABLE [dbo].[Attendance]  WITH CHECK ADD FOREIGN KEY([EnrollmentID])
REFERENCES [dbo].[Enrollment] ([EnrollmentID])
GO
ALTER TABLE [dbo].[Attendance]  WITH CHECK ADD FOREIGN KEY([ScheduleID])
REFERENCES [dbo].[Schedule] ([ScheduleID])
GO
ALTER TABLE [dbo].[Class]  WITH CHECK ADD FOREIGN KEY([CourseID])
REFERENCES [dbo].[Course] ([CourseID])
GO
ALTER TABLE [dbo].[Class]  WITH CHECK ADD FOREIGN KEY([TeacherID])
REFERENCES [dbo].[Employee] ([EmployeeID])
GO
ALTER TABLE [dbo].[Consultation]  WITH CHECK ADD FOREIGN KEY([LeadID])
REFERENCES [dbo].[Lead] ([LeadID])
GO
ALTER TABLE [dbo].[Consultation]  WITH CHECK ADD FOREIGN KEY([SaleID])
REFERENCES [dbo].[Employee] ([EmployeeID])
GO
ALTER TABLE [dbo].[Employee]  WITH CHECK ADD FOREIGN KEY([EmployeeID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[Enrollment]  WITH CHECK ADD FOREIGN KEY([ClassID])
REFERENCES [dbo].[Class] ([ClassID])
GO
ALTER TABLE [dbo].[Enrollment]  WITH CHECK ADD FOREIGN KEY([StudentID])
REFERENCES [dbo].[Student] ([StudentID])
GO
ALTER TABLE [dbo].[Feedback]  WITH CHECK ADD FOREIGN KEY([EnrollmentID])
REFERENCES [dbo].[Enrollment] ([EnrollmentID])
GO
ALTER TABLE [dbo].[Grade]  WITH CHECK ADD FOREIGN KEY([AssessmentID])
REFERENCES [dbo].[Assessment] ([AssessmentID])
GO
ALTER TABLE [dbo].[Grade]  WITH CHECK ADD FOREIGN KEY([EnrollmentID])
REFERENCES [dbo].[Enrollment] ([EnrollmentID])
GO
ALTER TABLE [dbo].[Lead]  WITH CHECK ADD FOREIGN KEY([InterestedCourseID])
REFERENCES [dbo].[Course] ([CourseID])
GO
ALTER TABLE [dbo].[Payment]  WITH CHECK ADD FOREIGN KEY([EnrollmentID])
REFERENCES [dbo].[Enrollment] ([EnrollmentID])
GO
ALTER TABLE [dbo].[Payment]  WITH CHECK ADD FOREIGN KEY([VoucherID])
REFERENCES [dbo].[Voucher] ([VoucherID])
GO
ALTER TABLE [dbo].[Schedule]  WITH CHECK ADD FOREIGN KEY([ClassID])
REFERENCES [dbo].[Class] ([ClassID])
GO
ALTER TABLE [dbo].[Schedule]  WITH CHECK ADD FOREIGN KEY([RoomID])
REFERENCES [dbo].[Room] ([RoomID])
GO
ALTER TABLE [dbo].[Schedule]  WITH CHECK ADD FOREIGN KEY([TeacherID])
REFERENCES [dbo].[Employee] ([EmployeeID])
GO
ALTER TABLE [dbo].[Student]  WITH CHECK ADD FOREIGN KEY([StudentID])
REFERENCES [dbo].[User] ([UserID])
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD FOREIGN KEY([RoleID])
REFERENCES [dbo].[Role] ([RoleID])
GO
ALTER TABLE [dbo].[Feedback]  WITH CHECK ADD CHECK  (([Rating]>=(1) AND [Rating]<=(5)))
GO
ALTER TABLE [dbo].[Grade]  WITH CHECK ADD CHECK  (([Score]>=(0) AND [Score]<=(10)))
GO
USE [master]
GO
ALTER DATABASE [LanguageCenterDB] SET  READ_WRITE 
GO
