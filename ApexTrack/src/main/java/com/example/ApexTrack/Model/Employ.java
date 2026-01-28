package com.example.ApexTrack.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Employ {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Employ_ID;
    private String Asset_Serial_No;

    private String Employ_Name;;
    private Date Days_Remaining;
    private String Type;
    private String Department;
    private Integer decommissionYears = 4;

    // Client provides purchase date
    @Column(name = "purchase_date")
    private LocalDate Purchase_Date;

    // System-calculated decommission date (4 years from purchase)
    @Column(name = "decommission_date")
    private LocalDate Decommission_Date;

    // Status (calculated based on current date vs decommission date)
    @Column(name = "status")
    private String Status; // "ACTIVE", "DECOMMISSIONED"


    // Constructor that calculates decommission date
    public
    Employ(String assetSerialNo, String type, String department,
                  String employName, LocalDate purchaseDate) {
        this.Asset_Serial_No = assetSerialNo;
        this.Type = type;
        this.Department = department;
        this.Employ_Name = employName;
        this.Purchase_Date = purchaseDate;

        // Calculate decommission date (4 years from purchase)
        this.Decommission_Date = purchaseDate.plusYears(4);

        // Calculate initial status
        calculateStatus();
    }

    // Method to calculate current status
    public void
    calculateStatus() {
        LocalDate today = LocalDate.now();

        if (Decommission_Date != null) {
            if (today.isAfter(Decommission_Date)) {
                this.Status = "DECOMMISSIONED";
            } else {
                this.Status = "ACTIVE";
            }
        } else {
            this.Status = "ACTIVE";
        }
    }

    // Calculate days remaining (not stored, calculated on demand)
    @Transient
    public Integer
    getDaysRemaining() {
        if (Decommission_Date != null) {
            LocalDate today = LocalDate.now();
            long days = ChronoUnit.DAYS.between(today, Decommission_Date);
            return (int) Math.max(days, 0); // Return 0 if already past
        }
        return null;
    }

    // Calculate equipment age in months
    @Transient
    public Integer
    getEquipmentAgeMonths() {
        if (Purchase_Date != null) {
            LocalDate today = LocalDate.now();
            return (int) ChronoUnit.MONTHS.between(Purchase_Date, today);
        }
        return null;
    }

    // Check if upgrade is needed (within 30 days of decommission)
    @Transient
    public Boolean
    isUpgradeNeeded() {
        Integer daysLeft = getDaysRemaining();
        return daysLeft != null && daysLeft <= 30 && daysLeft > 0;
    }

    // Get upgrade urgency level
    @Transient
    public String
    getUpgradeUrgency() {
        Integer daysLeft = getDaysRemaining();

        if (daysLeft == null || daysLeft < 0) {
            return "PAST_DUE";
        } else if (daysLeft <= 7) {
            return "CRITICAL";
        } else if (daysLeft <= 30) {
            return "URGENT";
        } else if (daysLeft <= 90) {
            return "SOON";
        } else {
            return "NORMAL";
        }
    }

    // Recalculate decommission date (useful if purchase date changes)
    public void
    recalculateDecommissionDate() {
        if (this.Purchase_Date != null) {
            this.Decommission_Date = this.Purchase_Date.plusYears(4);
            calculateStatus();
        }
    }
}
