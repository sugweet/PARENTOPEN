package com.brigeintelligent.shiro;

import com.brigeintelligent.api.manager.entity.Role;
import com.brigeintelligent.api.manager.entity.User;
import com.brigeintelligent.api.manager.service.LoginService;
import com.brigeintelligent.api.shiro.realm.ShiroToken;
import com.brigeintelligent.api.shiro.realm.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Sugweet
 * @Time 2019/4/17 14:27
 */
@RestController
@RequestMapping(value = "api/shiro")
public class LoginController {

    @Autowired
    private LoginService loginService;

    //退出的时候是get请求，主要是用于退出
    @GetMapping(value = "/login")
    public String login() {
        System.out.println("跳进登陆啦");
        return "index";
    }

    //post登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody Map<String,String> map){
        //添加用户认证信息
        ShiroToken shiroToken = new ShiroToken(map.get("username"), map.get("password"));
        SecurityUtils.getSubject().login(shiroToken);
        User user = ShiroUtils.currUser();
        System.out.println(user.getUsername());
        return "login";
    }

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }

    //登出
    @RequestMapping(value = "/logout")
    public String logout(){
        return "logout";
    }

    //错误页面展示
    @RequestMapping(value = "/error",method = RequestMethod.POST)
    public String error(){
        return "error ok!";
    }

    //数据初始化
    @PostMapping(value = "/addUser")
    public String addUser(@RequestBody User user){

        User user1 = loginService.addUser(user);
        return "addUser is ok! \n" + user1;
    }

    //角色初始化
    @PostMapping(value = "/addRole")
    public String addRole(@RequestBody Map<String,Object> map){
        Role role = loginService.addRole(map);
        return "addRole is ok! \n" + role;
    }

    // 查询所有用户
    @GetMapping(value = "/findAll")
    public List<User> findAll() {
        return loginService.findAll();
    }

    //注解的使用
    @RequiresRoles("admin")
    @RequiresPermissions("create")
    @RequestMapping(value = "/create")
    public String create(){
        return "Create success!";
    }


}
