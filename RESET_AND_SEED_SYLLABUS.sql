USE LanguageCenterDB;
GO

SET NOCOUNT ON;
GO

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
GO

DECLARE @SyllabusSeed TABLE (
    CourseName NVARCHAR(255),
    OrderIndex INT,
    TopicName NVARCHAR(255),
    Description NVARCHAR(MAX)
);

INSERT INTO @SyllabusSeed (CourseName, OrderIndex, TopicName, Description)
VALUES
(N'IELTS Foundation (Band 4.0 - 5.0)', 1, N'IELTS Overview and Study Plan', N'Understand the IELTS test structure, scoring criteria, and how to build an effective study roadmap.'),
(N'IELTS Foundation (Band 4.0 - 5.0)', 2, N'Core Grammar and Sentence Building', N'Build a strong grammar foundation with sentence patterns commonly used in IELTS tasks.'),
(N'IELTS Foundation (Band 4.0 - 5.0)', 3, N'Listening and Reading Basics', N'Practice essential listening and reading strategies for short conversations, instructions, and simple passages.'),
(N'IELTS Foundation (Band 4.0 - 5.0)', 4, N'Vocabulary for Everyday Topics', N'Expand topic-based vocabulary for family, education, work, travel, and daily communication.'),

(N'IELTS Intensive (Band 6.5+)', 1, N'Advanced Listening Strategies', N'Apply note-taking, distractor analysis, and prediction techniques for higher band listening performance.'),
(N'IELTS Intensive (Band 6.5+)', 2, N'High-Band Reading Techniques', N'Skim, scan, and manage time effectively when handling complex IELTS reading passages.'),
(N'IELTS Intensive (Band 6.5+)', 3, N'Writing Task Achievement and Coherence', N'Develop strong idea organization, paragraphing, and argument development for Writing Task 1 and Task 2.'),
(N'IELTS Intensive (Band 6.5+)', 4, N'Speaking Fluency and Lexical Resource', N'Improve speaking confidence, natural responses, and topic-specific vocabulary for all IELTS speaking parts.'),

(N'Business English Communication', 1, N'Professional Email Writing', N'Learn how to write clear, polite, and effective business emails for workplace communication.'),
(N'Business English Communication', 2, N'Meetings and Workplace Discussions', N'Practice useful language for meetings, expressing opinions, agreeing, disagreeing, and clarifying ideas.'),
(N'Business English Communication', 3, N'Presentations and Reports', N'Develop presentation skills and use formal English to explain charts, plans, and business results.'),
(N'Business English Communication', 4, N'Negotiation and Client Communication', N'Build confidence in negotiating, handling client requests, and maintaining professional communication.'),

(N'TOEIC 500+ Preparation', 1, N'TOEIC Test Format and Time Management', N'Understand the TOEIC structure, question types, and practical time management strategies.'),
(N'TOEIC 500+ Preparation', 2, N'Listening Practice for Parts 1-2', N'Build listening reflexes for photo description and question-response sections.'),
(N'TOEIC 500+ Preparation', 3, N'Reading Skills for Parts 5-6', N'Practice grammar, vocabulary, and sentence completion skills needed for the reading section.'),
(N'TOEIC 500+ Preparation', 4, N'Business Vocabulary and Mini Tests', N'Expand workplace vocabulary and reinforce learning through focused TOEIC practice tests.'),

(N'English for Kids (Starters)', 1, N'Fun with Alphabet and Sounds', N'Introduce letters, phonics, and simple pronunciation through songs and games.'),
(N'English for Kids (Starters)', 2, N'Numbers, Colors, and Everyday Words', N'Help children recognize and use basic vocabulary in a fun and visual way.'),
(N'English for Kids (Starters)', 3, N'Simple Speaking Through Activities', N'Encourage basic speaking patterns using classroom games, pair work, and role play.'),
(N'English for Kids (Starters)', 4, N'Storytelling and Interactive Learning', N'Build listening and speaking confidence through short stories, chants, and interactive activities.'),

(N'Advanced Speaking Workshop', 1, N'Pronunciation and Intonation Correction', N'Focus on stress, rhythm, connected speech, and natural English intonation.'),
(N'Advanced Speaking Workshop', 2, N'Fluency Through Discussion Tasks', N'Improve fluency by participating in guided conversations and topic-based discussions.'),
(N'Advanced Speaking Workshop', 3, N'Extended Responses and Storytelling', N'Practice building longer, more natural speaking responses with clear structure.'),
(N'Advanced Speaking Workshop', 4, N'Confidence in Real-Life Communication', N'Apply speaking skills in practical situations such as interviews, networking, and presentations.'),

(N'Basic English Grammar', 1, N'Parts of Speech and Sentence Patterns', N'Review nouns, verbs, adjectives, adverbs, and common sentence structures.'),
(N'Basic English Grammar', 2, N'Tenses and Daily Use', N'Practice present, past, and future tenses in real-life contexts.'),
(N'Basic English Grammar', 3, N'Questions, Negatives, and Modals', N'Build accuracy in asking questions, making negatives, and using modal verbs.'),
(N'Basic English Grammar', 4, N'Grammar Review and Error Correction', N'Consolidate grammar knowledge through exercises and correction of common mistakes.'),

(N'English for Tourism & Hospitality', 1, N'Welcoming Guests and Giving Information', N'Practice front-desk English for greeting guests and providing basic travel information.'),
(N'English for Tourism & Hospitality', 2, N'Handling Reservations and Requests', N'Use practical English for booking rooms, checking availability, and managing customer requests.'),
(N'English for Tourism & Hospitality', 3, N'Restaurant and Service Communication', N'Learn useful expressions for restaurant service, customer support, and complaint handling.'),
(N'English for Tourism & Hospitality', 4, N'Travel Assistance and Emergency Situations', N'Prepare for common travel issues, directions, lost items, and service recovery situations.'),

(N'Academic Writing Masterclass', 1, N'Essay Structure and Academic Style', N'Understand thesis statements, introductions, body paragraphs, and formal academic tone.'),
(N'Academic Writing Masterclass', 2, N'Coherence, Cohesion, and Referencing', N'Improve logical flow, linking devices, and proper use of evidence and references.'),
(N'Academic Writing Masterclass', 3, N'Argument Development and Critical Thinking', N'Build strong arguments with supporting evidence and analytical thinking.'),
(N'Academic Writing Masterclass', 4, N'Editing, Proofreading, and Final Drafting', N'Refine grammar, vocabulary, and organization to produce polished academic writing.'),

(N'se1911', 1, N'Introduction to English Communication', N'Build a simple foundation in vocabulary, greetings, and basic self-introduction.'),
(N'se1911', 2, N'Basic Listening and Speaking Practice', N'Practice common classroom and everyday communication patterns.'),
(N'se1911', 3, N'Grammar and Useful Expressions', N'Learn simple grammar structures and useful expressions for daily interaction.'),
(N'se1911', 4, N'Consolidation and Mini Practice Tasks', N'Review key knowledge through pair work, speaking tasks, and short exercises.'),

(N'General English Beginner (A1)', 1, N'Greetings and Personal Information', N'Learn how to greet others, introduce yourself, and share basic personal information.'),
(N'General English Beginner (A1)', 2, N'Basic Vocabulary for Daily Life', N'Build vocabulary related to family, school, food, numbers, and common objects.'),
(N'General English Beginner (A1)', 3, N'Simple Grammar for Communication', N'Practice be verb, simple present tense, and basic sentence formation.'),
(N'General English Beginner (A1)', 4, N'Listening and Speaking in Daily Situations', N'Use English in everyday conversations such as shopping, asking for help, and talking about routines.'),

(N'General English Elementary (A2)', 1, N'Everyday Communication Skills', N'Practice describing routines, preferences, and familiar situations with confidence.'),
(N'General English Elementary (A2)', 2, N'Essential Grammar Expansion', N'Strengthen control of past tense, future plans, comparatives, and countable or uncountable nouns.'),
(N'General English Elementary (A2)', 3, N'Listening for Main Ideas', N'Improve listening comprehension through practical conversations and short recordings.'),
(N'General English Elementary (A2)', 4, N'Speaking and Functional Language', N'Use English to ask for directions, make plans, invite others, and solve everyday problems.'),

(N'General English Pre-Intermediate (B1)', 1, N'Confident Social Communication', N'Build confidence in discussing experiences, opinions, and daily issues in English.'),
(N'General English Pre-Intermediate (B1)', 2, N'Grammar for More Accurate Expression', N'Practice present perfect, conditionals, and more complex sentence structures.'),
(N'General English Pre-Intermediate (B1)', 3, N'Reading and Vocabulary Development', N'Expand topic-based vocabulary and reading strategies for general texts.'),
(N'General English Pre-Intermediate (B1)', 4, N'Speaking for Real-Life Situations', N'Participate in role plays and discussions related to study, work, and travel.'),

(N'General English Intermediate (B2)', 1, N'Advanced Communication Strategies', N'Develop fluency in conversations, discussions, and expressing nuanced opinions.'),
(N'General English Intermediate (B2)', 2, N'Complex Grammar and Accuracy', N'Improve control of passive voice, reported speech, and advanced clause structures.'),
(N'General English Intermediate (B2)', 3, N'Academic and Workplace Reading', N'Practice reading longer texts and identifying key arguments, evidence, and tone.'),
(N'General English Intermediate (B2)', 4, N'Presentation and Discussion Skills', N'Build confidence in giving presentations and responding to questions in English.'),

(N'English Communication for Beginners', 1, N'Basic Greetings and Social English', N'Learn how to start conversations and respond in simple everyday situations.'),
(N'English Communication for Beginners', 2, N'Listening and Repetition Practice', N'Improve pronunciation and listening through guided repetition and short dialogues.'),
(N'English Communication for Beginners', 3, N'Role Plays in Real-Life Contexts', N'Practice ordering food, shopping, asking for help, and introducing yourself.'),
(N'English Communication for Beginners', 4, N'Confidence Building Through Interaction', N'Use learned expressions in pair work, games, and simple speaking activities.'),

(N'English Communication Intermediate', 1, N'Fluency in Everyday Discussions', N'Improve fluency through discussion of familiar and social topics.'),
(N'English Communication Intermediate', 2, N'Pronunciation and Natural Speaking', N'Focus on stress, intonation, linking sounds, and clearer spoken communication.'),
(N'English Communication Intermediate', 3, N'Group Presentations and Interaction', N'Practice presenting ideas and participating actively in group communication tasks.'),
(N'English Communication Intermediate', 4, N'Speaking with Confidence and Precision', N'Develop more accurate and natural responses in extended conversations.'),

(N'IELTS Preparation (Band 4.0 - 5.5)', 1, N'IELTS Format and Foundation Skills', N'Learn the IELTS structure, scoring system, and foundation strategies for all four skills.'),
(N'IELTS Preparation (Band 4.0 - 5.5)', 2, N'Listening and Reading Practice', N'Build basic test-taking techniques for listening sections and reading passages.'),
(N'IELTS Preparation (Band 4.0 - 5.5)', 3, N'Writing Basics for Task 1 and Task 2', N'Practice organizing ideas, sentence patterns, and common task responses.'),
(N'IELTS Preparation (Band 4.0 - 5.5)', 4, N'Speaking Practice and Common Topics', N'Prepare for speaking interviews with familiar topics and simple fluency-building techniques.'),

(N'IELTS Intensive (Target Band 6.5)', 1, N'Listening and Reading for Higher Bands', N'Apply advanced techniques to increase accuracy and speed in IELTS receptive skills.'),
(N'IELTS Intensive (Target Band 6.5)', 2, N'Writing Task 1 Data and Report Skills', N'Produce well-organized reports with strong vocabulary and precise data description.'),
(N'IELTS Intensive (Target Band 6.5)', 3, N'Writing Task 2 Argument Development', N'Build high-quality essays with clear arguments, examples, and logical progression.'),
(N'IELTS Intensive (Target Band 6.5)', 4, N'Speaking Performance and Band Improvement', N'Improve pronunciation, fluency, lexical resource, and coherence in speaking responses.'),

(N'IELTS Speaking & Writing Skills', 1, N'Speaking Part 1 and Fluency Building', N'Practice natural responses, confidence, and topic expansion for short interview answers.'),
(N'IELTS Speaking & Writing Skills', 2, N'Speaking Part 2 and Long Turn Responses', N'Develop structured long answers with examples, linking words, and smooth delivery.'),
(N'IELTS Speaking & Writing Skills', 3, N'Writing Task 1 Academic Reporting', N'Learn how to describe trends, comparisons, and visual data in an academic style.'),
(N'IELTS Speaking & Writing Skills', 4, N'Writing Task 2 Essay Development', N'Practice writing balanced, coherent essays with strong vocabulary and clear arguments.'),

(N'TOEIC Preparation 450+', 1, N'TOEIC Basics and Test Familiarization', N'Get familiar with the TOEIC format, core question types, and beginner-level strategies.'),
(N'TOEIC Preparation 450+', 2, N'Listening Practice for Daily Workplace Contexts', N'Improve comprehension of common workplace conversations and short talks.'),
(N'TOEIC Preparation 450+', 3, N'Grammar and Vocabulary for Reading', N'Build reading skills with essential grammar and vocabulary for sentence completion tasks.'),
(N'TOEIC Preparation 450+', 4, N'Practice Sets and Review', N'Consolidate knowledge through mini tests and guided review of frequent TOEIC patterns.'),

(N'TOEIC Preparation 650+', 1, N'Intermediate TOEIC Strategy Training', N'Apply more efficient techniques for handling listening and reading questions under time pressure.'),
(N'TOEIC Preparation 650+', 2, N'Listening Comprehension and Note Focus', N'Practice understanding details, implied meaning, and workplace situations in audio recordings.'),
(N'TOEIC Preparation 650+', 3, N'Reading for Accuracy and Speed', N'Improve performance in incomplete sentences, text completion, and short reading passages.'),
(N'TOEIC Preparation 650+', 4, N'Full Practice and Error Analysis', N'Use mock practice and review recurring mistakes to move beyond the 650 score target.'),

(N'TOEIC Intensive 800+', 1, N'High-Level TOEIC Test Strategies', N'Focus on advanced exam strategies to maximize speed, concentration, and answer accuracy.'),
(N'TOEIC Intensive 800+', 2, N'Listening for Inference and Detail', N'Train to catch nuanced meaning, intention, and complex information in longer conversations.'),
(N'TOEIC Intensive 800+', 3, N'Advanced Reading and Time Control', N'Handle difficult reading sets, multi-passage questions, and detailed information efficiently.'),
(N'TOEIC Intensive 800+', 4, N'Mock Tests and Score Optimization', N'Practice under exam conditions and fine-tune strategy to reach a score above 800.'),

(N'English for Job Interviews', 1, N'Common Interview Questions', N'Learn how to answer typical job interview questions clearly and confidently in English.'),
(N'English for Job Interviews', 2, N'Professional Self-Introduction', N'Build a strong self-introduction highlighting skills, education, and experience.'),
(N'English for Job Interviews', 3, N'Behavioral and Situational Answers', N'Practice structured responses for strengths, weaknesses, teamwork, and problem-solving questions.'),
(N'English for Job Interviews', 4, N'Mock Interviews and Feedback', N'Join interview simulations and receive feedback on language, confidence, and delivery.'),

(N'Public Speaking in English', 1, N'Presentation Structure and Opening Techniques', N'Learn how to organize speeches and capture audience attention effectively.'),
(N'Public Speaking in English', 2, N'Voice, Body Language, and Delivery', N'Improve pronunciation, tone, eye contact, and posture for impactful speaking.'),
(N'Public Speaking in English', 3, N'Visual Support and Audience Engagement', N'Use slides, transitions, and audience interaction techniques to strengthen presentations.'),
(N'Public Speaking in English', 4, N'Speech Practice and Performance Feedback', N'Build confidence through repeated speaking practice and constructive feedback.'),

(N'English for Travel', 1, N'Airport and Transportation English', N'Learn useful language for flights, tickets, directions, and public transportation.'),
(N'English for Travel', 2, N'Hotel Booking and Check-In Situations', N'Practice checking in, asking for services, and handling hotel-related issues.'),
(N'English for Travel', 3, N'Restaurant and Shopping Communication', N'Use practical English for ordering food, shopping, and asking about prices or services.'),
(N'English for Travel', 4, N'Emergencies and Travel Problem Solving', N'Prepare for common travel issues such as lost items, medical needs, and asking for help.'),

(N'English for Kids (Age 6-8)', 1, N'Phonics and Sound Recognition', N'Introduce young learners to English sounds and basic pronunciation patterns.'),
(N'English for Kids (Age 6-8)', 2, N'Vocabulary Through Games and Songs', N'Build vocabulary using music, games, movement, and colorful activities.'),
(N'English for Kids (Age 6-8)', 3, N'Simple Speaking and Classroom Language', N'Encourage children to use short sentences in familiar classroom and home situations.'),
(N'English for Kids (Age 6-8)', 4, N'Story-Based Learning and Review', N'Reinforce language through simple stories, repetition, and interactive review activities.'),

(N'English for Teens (Age 9-15)', 1, N'Communication for School and Social Life', N'Build confidence using English in school topics, hobbies, and peer conversations.'),
(N'English for Teens (Age 9-15)', 2, N'Grammar and Vocabulary Expansion', N'Improve range and accuracy with useful grammar and topic vocabulary for teenagers.'),
(N'English for Teens (Age 9-15)', 3, N'Listening and Reading for Young Learners', N'Develop comprehension skills through age-appropriate audio and reading materials.'),
(N'English for Teens (Age 9-15)', 4, N'Projects, Discussion, and Speaking Practice', N'Use English actively in presentations, teamwork, and guided discussion tasks.'),

(N'Pronunciation & Accent Training', 1, N'English Sounds and Phonetic Awareness', N'Identify and practice key vowel and consonant sounds that commonly challenge Vietnamese learners.'),
(N'Pronunciation & Accent Training', 2, N'Stress, Rhythm, and Intonation', N'Improve natural flow through sentence stress, rhythm, and rising or falling intonation patterns.'),
(N'Pronunciation & Accent Training', 3, N'Connected Speech and Clarity', N'Practice linking, reductions, and connected speech for more natural pronunciation.'),
(N'Pronunciation & Accent Training', 4, N'Accent Improvement Through Speaking Tasks', N'Apply pronunciation techniques in guided speaking and personalized correction activities.'),

(N'English Grammar Foundation', 1, N'Grammar Fundamentals and Sentence Structure', N'Review the building blocks of English sentences including word order and basic sentence types.'),
(N'English Grammar Foundation', 2, N'Tenses and Common Usage Patterns', N'Practice the most important tenses and understand when to use each one accurately.'),
(N'English Grammar Foundation', 3, N'Clauses, Modals, and Functional Grammar', N'Expand grammar range with modals, relative clauses, and useful communication structures.'),
(N'English Grammar Foundation', 4, N'Comprehensive Grammar Review', N'Consolidate grammar knowledge through mixed exercises, correction tasks, and practical application.');

DELETE s
FROM dbo.Syllabus s
JOIN dbo.Course c ON c.CourseID = s.CourseID
WHERE c.CourseName IN (
    SELECT DISTINCT CourseName
    FROM @SyllabusSeed
);

INSERT INTO dbo.Syllabus (CourseID, OrderIndex, TopicName, Description)
SELECT c.CourseID, s.OrderIndex, s.TopicName, s.Description
FROM @SyllabusSeed s
JOIN dbo.Course c ON c.CourseName = s.CourseName;

PRINT 'Syllabus reset and reseed completed successfully.';
GO
