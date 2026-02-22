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
            String token = employService.verify(employ);
            return ResponseEntity.ok().body(token);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<String>
    createEmploy(@RequestBody Employ employ) {
        try {
            String m = employService.createEmploy(employ);
            return ResponseEntity.ok(m);
        } catch (Exception e){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Employ>>
    getAllEmploy(){
        try {
            List<Employ> employs = employService.getAllEmploy();
            return ResponseEntity.ok(employs);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

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
    deleteUser(@PathVariable Long id) {
        return employService.DeleteEmployById(id) ?
                ResponseEntity.ok("User deleted successfully") :
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found");
    }


}
