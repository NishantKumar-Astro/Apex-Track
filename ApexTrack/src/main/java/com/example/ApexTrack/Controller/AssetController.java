package com.example.ApexTrack.Controller;

import com.example.ApexTrack.Model.AssetRequestDTO;
import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.Service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
@CrossOrigin
public class AssetController {

    @Autowired
    private AssetService service;

    @GetMapping
    public ResponseEntity<List<Employ>>
    getAllAssets() {
        return ResponseEntity.ok(service.getAllEmploy());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employ>
    getAssetById(@PathVariable int id) {
        Employ employ = service.getEmployById(id);
        return employ != null
                ? ResponseEntity.ok(employ)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Employ>
    addAsset(@RequestBody AssetRequestDTO request) {
        try {
            Employ employ = service.addAsset(request);
            return new ResponseEntity<>(employ, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}/purchase-date")
    public ResponseEntity<Employ> updatePurchaseDate(
            @PathVariable int id,
            @RequestBody Map<String, LocalDate> request) {

        LocalDate newPurchaseDate = request.get("purchaseDate");
        if (newPurchaseDate == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Employ updated = service.updatePurchaseDate(id, newPurchaseDate);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/decommission-now")
    public ResponseEntity<Employ> decommissionNow(@PathVariable int id) {
        try {
            Employ decommissioned = service.decommissionNow(id);
            return ResponseEntity.ok(decommissioned);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/decommission-date")
    public ResponseEntity<Employ> updateDecommissionDate(
            @PathVariable int id,
            @RequestBody Map<String, LocalDate> request) {

        LocalDate newDecommissionDate = request.get("decommissionDate");
        if (newDecommissionDate == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Employ updated = service.updateDecommissionDate(id, newDecommissionDate);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<Employ>> getActiveAssets() {
        return ResponseEntity.ok(service.getActiveAssets());
    }

    @GetMapping("/decommissioned")
    public ResponseEntity<List<Employ>> getDecommissionedAssets() {
        return ResponseEntity.ok(service.getDecommissionedAssets());
    }

    @GetMapping("/upgrade-needed")
    public ResponseEntity<List<Employ>> getAssetsNeedingUpgrade() {
        return ResponseEntity.ok(service.getAssetsNeedingUpgrade());
    }

    @GetMapping("/urgency/{urgency}")
    public ResponseEntity<List<Employ>> getAssetsByUrgency(@PathVariable String urgency) {
        return ResponseEntity.ok(service.getAssetsByUrgency(urgency));
    }

    @GetMapping("/expiring/{days}")
    public ResponseEntity<List<Employ>> getAssetsExpiringWithinDays(@PathVariable int days) {
        return ResponseEntity.ok(service.getAssetsExpiringWithinDays(days));
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Map<String, Object>> getAssetDetails(@PathVariable int id) {
        Employ asset = service.getEmployById(id);
        if (asset == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> details = new HashMap<>();
        details.put("asset", asset);
        details.put("daysRemaining", asset.getDaysRemaining());
        details.put("equipmentAgeMonths", asset.getEquipmentAgeMonths());
        details.put("upgradeNeeded", asset.isUpgradeNeeded());
        details.put("upgradeUrgency", asset.getUpgradeUrgency());
        details.put("currentDate", LocalDate.now());

        return ResponseEntity.ok(details);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(
            @PathVariable int id,
            @RequestBody AssetRequestDTO request) {
        try {
            Employ updated = service.updateAsset(id, request);
            return updated != null
                    ? ResponseEntity.ok(updated)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable int id) {
        String result = service.DeleteEmploy(id);
        return "Deleted".equals(result)
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }
}




















