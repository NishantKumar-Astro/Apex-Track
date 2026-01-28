package com.example.ApexTrack.Model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AssetRequestDTO {
    private String assetSerialNo;
    private String type;  // Laptop, Server, Monitor
    private String department;
    private String employName;
    private LocalDate purchaseDate;  // Client provides this

    // Optional: Client can override default 4-year decommission rule
    private Integer decommissionYears = 4; // Default 4 years
}