package com.example.ApexTrack.repository;

import com.example.ApexTrack.Model.Employ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployRepo extends JpaRepository<Employ, Long> {
    Employ findById(long id);
    Employ findByEmail(String email);
    Employ findByUsername(String username);

}
