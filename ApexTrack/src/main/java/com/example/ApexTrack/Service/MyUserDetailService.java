package com.example.ApexTrack.Service;

import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.Model.UserPrincipal;
import com.example.ApexTrack.repository.EmployRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    EmployRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employ employ = repo.findByUsername(username);
        if (employ == null){
            System.out.println("User not Found of Usename:" + username);
            throw new RuntimeException("User not found");
        }
        System.out.println("User found details:" +" "+username +" "+"Password"+" "+ employ.getDepartment());
        return new UserPrincipal(employ);
    }
}
