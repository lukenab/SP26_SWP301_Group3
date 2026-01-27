/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;

/**
 *
 * @author phuct
 */
public class Consultation {
    private int consultationId;
    private Lead lead;
    private Employee employee;
    private String note;
    private LocalDateTime consultation;

    public Consultation() {
    }

    public Consultation(int consultationId, Lead lead, Employee employee, String note, LocalDateTime consultation) {
        this.consultationId = consultationId;
        this.lead = lead;
        this.employee = employee;
        this.note = note;
        this.consultation = consultation;
    }

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getConsultation() {
        return consultation;
    }

    public void setConsultation(LocalDateTime consultation) {
        this.consultation = consultation;
    }

    @Override
    public String toString() {
        return "Consultation{" + "consultationId=" + consultationId + ", lead=" + lead + ", employee=" + employee + ", note=" + note + ", consultation=" + consultation + '}';
    }

    
    
}
