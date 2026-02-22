package com.example.ApexTrack.repository;

import com.example.ApexTrack.Model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssetRepo extends JpaRepository<Asset, Long> {

    @Query(value = "SELECT * " +
            "FROM assets " +
            "WHERE employ_id = :id",
            nativeQuery = true)
    List<Asset> findAssetsByEmploy_id(@Param("id") Long id);

    @Query("SELECT a FROM Asset a WHERE a.serial_no = :serial_no")
    Asset findBySerial_no(@Param("serial_no") String serial_no);

    @Query("SELECT a FROM Asset a WHERE a.status = 'ACTIVE'")
    List<Asset> findAssetsWithStatusActive();

    @Query("SELECT a FROM Asset a WHERE a.status = 'DECOMMISSIONED'")
    List<Asset> findAssetsWithStatusDecommissioned();

    @Query(value = "SELECT * " +
            "FROM Asset " +
            "WHERE decommission_date " +
            "IS NOT NULL " +
            "AND " +
            "decommission_date > CURRENT_DATE " +
            "AND " +
            "decommission_date " +
            "BETWEEN CURRENT_DATE " +
            "AND CURRENT_DATE + INTERVAL '30 days'",
            nativeQuery = true)
    List<Asset> findAssetsNeedingUpgradeNative();

    @Query(value = "SELECT a " +
            "FROM Asset a " +
            "WHERE a.Decommission_Date IS NOT NULL " +
            "AND a.Decommission_Date > CURRENT_DATE " +
            "AND a.Decommission_Date <= CURRENT_DATE + INTERVAL '30 days' " +
            "AND a.id = :id",nativeQuery = true)
    List<Asset> findAssetsNeedingUpgradeById(@Param("id") Long id);
}

