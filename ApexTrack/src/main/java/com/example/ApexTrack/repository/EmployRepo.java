package com.example.ApexTrack.repository;

import com.example.ApexTrack.Model.Employ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployRepo extends JpaRepository<Employ, Long> {
    Employ findById(long id);
    Employ findByEmail(String email);
    Employ findByUsername(String username);

}
