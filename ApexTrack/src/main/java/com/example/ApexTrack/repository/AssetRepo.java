package com.example.ApexTrack.repository;

import com.example.ApexTrack.Model.Employ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepo extends JpaRepository<Employ,Integer> {
}
