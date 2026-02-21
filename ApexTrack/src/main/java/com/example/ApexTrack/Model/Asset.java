package com.example.ApexTrack.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "assets")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true,nullable = false)
    @NotNull
    private String serial_no;

    @Column(nullable = false)
    @NotNull
    private String type;

    @Column(nullable = false)
    @NotNull
    private LocalDate assigned_date;
    @Column(nullable = false)
    @NotNull
    private LocalDate decommission_date; // System-calculated decommission date (4 years from purchase)

    @Column(nullable = false)
    private String status; // "ACTIVE", "DECOMMISSIONED"

    @ManyToOne
    @JoinColumn(name = "employ_id")
    @JsonIgnore
    private Employ employ;
}
