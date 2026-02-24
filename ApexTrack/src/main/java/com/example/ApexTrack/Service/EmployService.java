package com.example.ApexTrack.Service;
import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.repository.EmployRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployService {

    @Autowired
    JWTService jwtService;

    @Autowired
    private AuthenticationManager authmanager;

    @Autowired
    EmployRepo repo;

    @Autowired
    StatusCalculationService status;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<Employ>
    getAllEmploy() {
            return repo.findAll();
    }

    public boolean
    DeleteEmployById(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    public String
    createEmploy(Employ employ) {
        employ.setRole("Role_USER");
        employ.setPassword(encoder.encode(employ.getPassword()));
        if (repo.findByEmail(employ.getEmail()) != null ){
            return "Already exist";
        }
        repo.save(employ);
        return "Created";
    }

    public Employ
    getEmployById(long id) {
        return repo.findById(id);
    }

    public String
    verify(Employ employ) {
        try{
            Authentication authentication = authmanager.authenticate(
                    new UsernamePasswordAuthenticationToken(employ.getUsername(),employ.getPassword())
            );

            if (authentication.isAuthenticated()){
                return jwtService.generateToken(employ.getUsername());
            }
        } catch (Exception e){
            return "Fail" + e.getMessage();
        }

        return "Fail: Not Authenticated";
    }

}
