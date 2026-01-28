package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.AssetRequestDTO;
import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.repository.AssetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepo repo;

    // Default decommission period in years
    private static final int DEFAULT_DECOMMISSION_YEARS = 4;

    public List<Employ>
    getAllEmploy() {
        List<Employ> assets = repo.findAll();
        assets.forEach(Employ::calculateStatus);
        return assets;
    }

    public Employ
    getEmployById(int id) {
        Employ employ = repo.findById(id).orElse(null);
        if (employ != null) {
            employ.calculateStatus();
        }
        return employ;
    }

    // Add new asset with automatic decommission date calculation
    public Employ
    addAsset(AssetRequestDTO request) {
        // Create new asset (constructor calculates decommission date)
        Employ employ = new Employ(
                request.getAssetSerialNo(),
                request.getType(),
                request.getDepartment(),
                request.getEmployName(),
                request.getPurchaseDate()
        );

        // If client specified custom decommission years
        if (request.getDecommissionYears() != null &&
                request.getDecommissionYears() != DEFAULT_DECOMMISSION_YEARS) {
            employ.setDecommission_Date(
                    request.getPurchaseDate().plusYears(request.getDecommissionYears())
            );
        }

        return repo.save(employ);
    }

    // Update only purchase date (triggers decommission date recalculation)
    @Transactional
    public Employ
    updatePurchaseDate(int id, LocalDate newPurchaseDate) {
        Employ existingAsset = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        // Update purchase date
        existingAsset.setPurchase_Date(newPurchaseDate);

        // Recalculate decommission date (4 years from new purchase date)
        existingAsset.setDecommission_Date(newPurchaseDate.plusYears(DEFAULT_DECOMMISSION_YEARS));

        // Recalculate status
        existingAsset.calculateStatus();

        return repo.save(existingAsset);
    }

    // Manually update decommission date (override automatic calculation)
    @Transactional
    public Employ
    updateDecommissionDate(int id, LocalDate newDecommissionDate) {
        Employ existingAsset = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        existingAsset.setDecommission_Date(newDecommissionDate);
        existingAsset.calculateStatus();

        return repo.save(existingAsset);
    }

    // Mark asset as decommissioned immediately
    @Transactional
    public Employ
    decommissionNow(int id) {
        Employ existingAsset = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        existingAsset.setDecommission_Date(LocalDate.now());
        existingAsset.setStatus("DECOMMISSIONED");

        return repo.save(existingAsset);
    }

    // Update full asset details
    @Transactional
    public Employ
    updateAsset(int id, AssetRequestDTO request) {
        return repo.findById(id).map(existingAsset -> {
            existingAsset.setAsset_Serial_No(request.getAssetSerialNo());
            existingAsset.setType(request.getType());
            existingAsset.setDepartment(request.getDepartment());
            existingAsset.setEmploy_Name(request.getEmployName());

            // If purchase date changed, update it and recalculate decommission
            if (request.getPurchaseDate() != null) {
                existingAsset.setPurchase_Date(request.getPurchaseDate());

                // Calculate decommission date
                int years = request.getDecommissionYears() != null ?
                        request.getDecommissionYears() : DEFAULT_DECOMMISSION_YEARS;
                existingAsset.setDecommission_Date(
                        request.getPurchaseDate().plusYears(years)
                );
            }

            existingAsset.calculateStatus();

            return repo.save(existingAsset);
        }).orElse(null);
    }

    // Get assets that are decommissioned
    public List<Employ>
    getDecommissionedAssets() {
        return repo.findAll().stream()
                .filter(asset -> {
                    asset.calculateStatus();
                    return "DECOMMISSIONED".equals(asset.getStatus());
                })
                .collect(Collectors.toList());
    }

    // Get active assets
    public List<Employ>
    getActiveAssets() {
        return repo.findAll().stream()
                .filter(asset -> {
                    asset.calculateStatus();
                    return "ACTIVE".equals(asset.getStatus());
                })
                .collect(Collectors.toList());
    }

    // Get assets that need upgrade (within 30 days of decommission)
    public List<Employ>
    getAssetsNeedingUpgrade() {
        return repo.findAll().stream()
                .filter(asset -> {
                    asset.calculateStatus();
                    return "ACTIVE".equals(asset.getStatus()) &&
                            asset.isUpgradeNeeded();
                })
                .collect(Collectors.toList());
    }

    // Get assets by upgrade urgency
    public List<Employ>
    getAssetsByUrgency(String urgency) {
        return repo.findAll().stream()
                .filter(asset -> {
                    asset.calculateStatus();
                    return urgency.equals(asset.getUpgradeUrgency());
                })
                .collect(Collectors.toList());
    }

    // Get assets expiring within specific days
    public List<Employ>
    getAssetsExpiringWithinDays(int days) {
        LocalDate today = LocalDate.now();
        LocalDate thresholdDate = today.plusDays(days);

        return repo.findAll().stream()
                .filter(asset -> {
                    asset.calculateStatus();
                    return asset.getDecommission_Date() != null &&
                            !asset.getDecommission_Date().isAfter(thresholdDate) &&
                            asset.getDecommission_Date().isAfter(today);
                })
                .collect(Collectors.toList());
    }

    public String
    DeleteEmploy(int id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "Deleted";
        } else {
            return "Employ does not exist";
        }
    }
}








