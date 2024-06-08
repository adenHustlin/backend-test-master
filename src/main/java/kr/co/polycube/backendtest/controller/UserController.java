package kr.co.polycube.backendtest.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.polycube.backendtest.model.User;
import kr.co.polycube.backendtest.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable(name = "id") Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable(name = "id") Long id, @RequestBody User user) {
        return userService.updateUser(id, user.getName());
    }
}
