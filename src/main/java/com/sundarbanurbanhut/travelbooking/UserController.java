package com.sundarbanurbanhut.travelbooking;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173","http://192.168.10.12:5173"}) // if using Vite
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {

    Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

    if (existingUser.isPresent()) {
        return ResponseEntity
                .badRequest()
                .body("Email already registered");
    }

    User savedUser = userRepository.save(user);
    return ResponseEntity.ok(savedUser);
}
    // 🔹 Get All Users
    @GetMapping()
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }
    @DeleteMapping("/{id}")
public ResponseEntity <String> deleteUser(@PathVariable Long id) {

    if (!userRepository.existsById(id)) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    userRepository.deleteById(id);

    return ResponseEntity.ok("User deleted successfully");
}

    //login api
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User loginRequest) {

    Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

    if (optionalUser.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
    }

    User user = optionalUser.get();

    if (!user.getPassword().equals(loginRequest.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
    }

    return ResponseEntity.ok(user);
}
}
