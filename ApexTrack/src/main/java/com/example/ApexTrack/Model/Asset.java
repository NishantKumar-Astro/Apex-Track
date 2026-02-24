package com.example.ApexTrack.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate assigned_date;     // Client provides purchase date
    @Column(nullable = false)
    private LocalDate decommission_date; // System-calculated decommission date (4 years from purchase)
    @Column(nullable = false)
    private String status; // "ACTIVE", "DECOMMISSIONED"

    @ManyToOne
    @JoinColumn(name = "employ_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Employ employ;
}
