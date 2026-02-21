package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.Asset;
import com.example.ApexTrack.repository.AssetRepo;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class StatusCalculationService {

    @Autowired
    AssetRepo repo;

    public Asset
    calculateStatus(Asset asset) {
        LocalDate today = LocalDate.now();
        LocalDate date = asset.getDecommission_date();
        if (date != null) {
            if (today.isAfter(date)) {
                asset.setStatus("DECOMMISSIONED");
            } else {
                asset.setStatus("ACTIVE");
            }
        } else {
            asset.setStatus("ACTIVE");
        }
        return asset;
    }

    // Get assets that need upgrade (within 30 days of decommission)
    public List<Asset>
    getAssetsNeedingUpgrade() {
        List<Asset> asset = repo.findAssetsNeedingUpgradeNative();
        if ( asset != null)
            return asset;
        else
            return null;
    }

    // Check if upgrade is needed (within 30 days of decommission) BY ID
    @Transient
    public Boolean
    isUpgradeNeeded(long id) {
        if (repo.findAssetsNeedingUpgradeById(id) != null)
            return true;
        return false;
    }
}
