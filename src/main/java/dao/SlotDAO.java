package dao;

import model.Slot;
import utils.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class SlotDAO extends DBContext {

    public List<Slot> getAllSlots() {
        List<Slot> slots = new ArrayList<>();
        String sql = "SELECT SlotID, StartTime, EndTime FROM Slot";


        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotID(rs.getInt("SlotID"));
                slot.setStartTime(rs.getTime("StartTime").toLocalTime());
                slot.setEndTime(rs.getTime("EndTime").toLocalTime());
                slots.add(slot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public Slot getSlotByID(int slotID) {
        String sql = "SELECT SlotID, StartTime, EndTime FROM Slot WHERE SlotID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Slot slot = new Slot();
                slot.setSlotID(rs.getInt("SlotID"));
                slot.setStartTime(rs.getTime("StartTime").toLocalTime());
                slot.setEndTime(rs.getTime("EndTime").toLocalTime());
                return slot;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addSlot(Slot slot) {
        String sql = "INSERT INTO Slot (StartTime, EndTime) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTime(1, Time.valueOf(slot.getStartTime()));
            ps.setTime(2, Time.valueOf(slot.getEndTime()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateSlot(Slot slot) {
        String sql = "UPDATE Slot SET StartTime = ?, EndTime = ? WHERE SlotID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTime(1, Time.valueOf(slot.getStartTime()));
            ps.setTime(2, Time.valueOf(slot.getEndTime()));
            ps.setInt(3, slot.getSlotID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteSlot(int slotID) {
        String sql = "DELETE FROM Slot WHERE SlotID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}