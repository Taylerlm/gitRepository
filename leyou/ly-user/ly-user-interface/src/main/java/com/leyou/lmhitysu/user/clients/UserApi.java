package com.leyou.lmhitysu.user.clients;

import com.leyou.lmhitysu.user.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {
    @GetMapping(value = "query")
    public ResponseEntity<User> query(@RequestParam(value = "username") String username,
                                      @RequestParam(value = "password") String password);
}
