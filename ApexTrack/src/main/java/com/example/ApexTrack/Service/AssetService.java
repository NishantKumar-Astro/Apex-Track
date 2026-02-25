package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.Asset;
import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.repository.AssetRepo;
import com.example.ApexTrack.repository.EmployRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetRepo repo;

    @Autowired
    private EmployRepo erepo;

    @Autowired
    private StatusCalculationService status;

    // Default decommission period in years
    private static final int DEFAULT_DECOMMISSION_YEARS = 4;

    // Add new asset with automatic decommission date calculation
    public Asset
    addAsset(Asset asset) {
        if (repo.findBySerial_no(asset.getSerial_no()) != null) {
            return null;
        } else {
            Long employId = asset.getEmploy().getId(); // assuming employ is not null
            Employ managedEmploy = erepo.findById(employId)
                    .orElseThrow(() -> new RuntimeException("Employ not found with id: " + employId));
            asset.setEmploy(managedEmploy);
            if(asset.getAssigned_date() == null){
                LocalDate current = LocalDate.now();
                asset.setAssigned_date(current);
            }
            asset.setDecommission_date(asset.getAssigned_date().plusYears(DEFAULT_DECOMMISSION_YEARS));
            asset.setStatus("ACTIVE");
            return repo.save(asset);
        }
    }

    public List<Asset>
    getAssetsByEmployid(long id){
        return repo.findAssetsByEmploy_id(id);
    }

    public Asset
    getAssetById(long id) {
        Asset asset = repo.findById(id).orElse(null);
        if (asset != null){
            status.calculateStatus(asset);
            repo.save(asset);
            return asset;
        } else return asset;

    }

    public List<Asset>
    getAllAsset() {
        List<Asset> assets = repo.findAll();
        assets.forEach(status::calculateStatus);
        return assets;

    }

    public String
    DeleteAssetById(long id) {
        Asset asset = getAssetById(id);
        if (asset != null){
            repo.deleteById(asset.getId());
            return "Success";
        }
        return "Asset not Found or please enter correct details";
    }

    @Transactional
    public Asset
    updateAsset(long id, Asset asset) {
        Asset asset1 = repo.findById(id).orElse(null);
        if (asset1 != null) {
            if (asset.getAssigned_date() != null){
                asset1.setAssigned_date(asset.getAssigned_date());
                asset1.setDecommission_date(asset.getAssigned_date().plusYears(4));
            }
            if (asset.getSerial_no() != null)
                asset1.setSerial_no(asset.getSerial_no());
            if (asset.getType() != null)
                asset1.setType(asset.getType());
            status.calculateStatus(asset1);
            return repo.save(asset1);
        }
        else return null;
    }

    public List<Asset> getActiveAssets() {
        return repo.findAssetsWithStatusActive();
    }

    public List<Asset> getDecommissionedAssets() {
        return repo.findAssetsWithStatusDecommissioned();
    }


}












