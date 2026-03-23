    public List<Object[]> getStudentsNotInClass(int classId) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.StudentID, u.FullName, u.Email, s.EnrollmentDate, "
                + "CASE "
                + "    WHEN EXISTS ( "
                + "        SELECT 1 "
                + "        FROM Enrollment e2 "
                + "        WHERE e2.StudentID = s.StudentID "
                + "          AND e2.Status IN ('Paid', 'Active', 'Completed') "
                + "    ) "
                + "    OR EXISTS ( "
                + "        SELECT 1 "
                + "        FROM Payment p "
                + "        JOIN Enrollment e3 ON p.EnrollmentID = e3.EnrollmentID "
                + "        WHERE e3.StudentID = s.StudentID "
                + "          AND p.Status IN ('Approved', 'Paid', 'Complete', 'Completed') "
                + "    ) "
                + "    THEN 'Paid' "
                + "    ELSE 'UnPaid' "
                + "END AS SuggestedStatus "
                + "FROM Student s "
                + "JOIN [User] u ON s.StudentID = u.UserID "
                + "WHERE NOT EXISTS ( "
                + "    SELECT 1 FROM Enrollment e "
                + "    WHERE e.ClassID = ? AND e.StudentID = s.StudentID "
                + ") "
                + "AND NOT EXISTS ( "
                + "    SELECT 1 FROM Enrollment e_closed "
                + "    WHERE e_closed.StudentID = s.StudentID "
                + "      AND e_closed.Status = 'CLOSED' "
                + ") "
                + "ORDER BY u.FullName ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[5];
                    row[0] = rs.getInt("StudentID");
                    row[1] = rs.getString("FullName");
                    row[2] = rs.getString("Email");
                    row[3] = rs.getDate("EnrollmentDate");
                    row[4] = rs.getString("SuggestedStatus");
                    list.add(row);
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to get students not in class: " + e.getMessage());
        }
        return list;
    }
