package com.brigeintelligent.shiro;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @Author Sugweet
 * @Time 2019/4/17 14:27
 */
@Controller
@RequestMapping(value = "api/shiro")
public class LoginController {

    @GetMapping(value = "/login")
    public String login() {
        return null;
    }

}
