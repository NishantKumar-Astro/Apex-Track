package com.example.ApexTrack.Controller;

import com.example.ApexTrack.Model.Asset;
import com.example.ApexTrack.Service.AssetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/api/assets")
@CrossOrigin
public class AssetController {

    @Autowired
    private AssetService service;

    @GetMapping
    public ResponseEntity<List<Asset>>
    getAllAssets() {
        try {
            return ResponseEntity.ok(service.getAllAsset());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/employ_id/{id}")
    public ResponseEntity<List<Asset>>
    getAssetByEmployid(@PathVariable long id){
        if (service.getAssetsByEmployid(id) != null ){
            return ResponseEntity.ok(service.getAssetsByEmployid(id));
        }
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset>
    getAssetById(@PathVariable long id) {
        Asset asset = service.getAssetById(id);
        return asset != null
                ? ResponseEntity.ok(asset)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Asset>
    addAsset(@Valid @RequestBody Asset asset) {
        try {
            Asset asset1 = service.addAsset(asset);
            if (asset1 != null)
                return new ResponseEntity<>(asset1, HttpStatus.CREATED);
            else return new ResponseEntity<>(asset1,HttpStatus.ALREADY_REPORTED);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAsset(
            @PathVariable long id,
            @RequestBody Asset asset) {
        try {
            Asset updated = service.updateAsset(id, asset);
            return updated != null
                    ? ResponseEntity.ok(updated)
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable long id) {
        String result = service.DeleteAssetById(id);
        return "Success".equals(result)
                ? ResponseEntity.ok(result)
                : ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Asset>> getActiveAssets() {
        return ResponseEntity.ok(service.getActiveAssets());
    }

    @GetMapping("/decommissioned")
    public ResponseEntity<List<Asset>>
    getDecommissionedAssets() {
        return ResponseEntity.ok(service.getDecommissionedAssets());
    }

}























