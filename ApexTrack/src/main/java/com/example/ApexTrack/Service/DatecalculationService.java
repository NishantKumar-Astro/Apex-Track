package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.Asset;
import com.example.ApexTrack.repository.AssetRepo;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DatecalculationService {

    @Autowired
    private AssetRepo repo;

    // Calculate days remaining (not stored, calculated on demand)
    @Transient
    public Integer
    getDaysRemaining(Asset asset) {
        if (asset.getDecommission_date() != null) {
            LocalDate today = LocalDate.now();
            long days = ChronoUnit.DAYS.between(today, asset.getDecommission_date());
            return (int) Math.max(days, 0); // Return 0 if already past
        }
        return null;
    }

    // Calculate equipment age in months
    @Transient
    public Integer
    getEquipmentAgeMonths(Asset asset) {
        if (asset != null) {
            LocalDate today = LocalDate.now();
            return (int) ChronoUnit.MONTHS.between(asset.getAssigned_date(), today);
        }
        return null;
    }

    // Get active assets
    public List<Asset>
    getActiveAssets() {
        return repo.findAssetsWithStatusActive();
    }

    // Get assets that are decommissioned
    public List<Asset>
    getDecommissionedAssets() {
        return repo.findAssetsWithStatusDecommissioned();
    }
}