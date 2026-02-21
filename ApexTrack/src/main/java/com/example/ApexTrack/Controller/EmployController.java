package com.example.ApexTrack.Controller;

import com.example.ApexTrack.Model.Employ;
import com.example.ApexTrack.Service.EmployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employ")
@CrossOrigin
public class EmployController {

    @Autowired
    private EmployService employService;

    @PostMapping("/login")
    public ResponseEntity<String>
    login(@RequestBody Employ employ) {
        try {
            employService.createEmploy(employ);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<Employ>
    createEmploy(@RequestBody Employ employ) {
        return ResponseEntity.ok(employService.createEmploy(employ));
    }

    @GetMapping
    public ResponseEntity<List<Employ>>
    getAllEmploy(){
        return ResponseEntity.ok(employService.getAllEmploy());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employ>
    getEmployById(@PathVariable long id){
        Employ employ = employService.getEmployById(id);
         return employ != null ?
                 ResponseEntity.ok(employService.getEmployById(id))
                 :ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteUser(@RequestBody Long id , String email, String password) {
        return employService.DeleteEmployById(id,email,password) ?
                ResponseEntity.ok("User deleted successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
    }


}
