-- Test script to verify batch schedule creation
-- This shows what should happen when batch creating schedules

-- Test Case 1: Weekly pattern (same weekday)
-- Start: 2026-03-23 (Monday)
-- Pattern: Weekly (repeat every Monday)
-- End: Never (max 100)
-- Expected: Mondays: 03-23, 03-30, 04-06, 04-13, 04-20, 04-27, 05-04, 05-11, 05-18, 05-25

-- Test Case 2: Daily pattern
-- Start: 2026-03-23 (Monday)
-- Pattern: Daily
-- End: After 5 occurrences
-- Expected: 03-23, 03-24, 03-25, 03-26, 03-27

-- Test Case 3: Weekdays
-- Start: 2026-03-23 (Monday)
-- Pattern: Weekdays (Mon-Fri)
-- End: After 10 occurrences
-- Expected: Mon 03-23, Tue 03-24, Wed 03-25, Thu 03-26, Fri 03-27, Mon 03-30, Tue 03-31, Wed 04-01, Thu 04-02, Fri 04-03

-- Test Case 4: Custom (Mon, Wed, Fri)
-- Start: 2026-03-23 (Monday)
-- Pattern: Custom - Days 1,3,5 (Mon, Wed, Fri)
-- End: After 6 occurrences
-- Expected: Mon 03-23, Wed 03-25, Fri 03-27, Mon 03-30, Wed 04-01, Fri 04-03

-- View results with:
-- SELECT COUNT(*) FROM Schedule WHERE ClassID = ? AND RoomID = ? AND SlotID = ?
--     ORDER BY LearningDate ASC

