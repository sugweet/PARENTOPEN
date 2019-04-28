package com.brigeintelligent.shiro;

import com.brigeintelligent.api.manager.entity.LoginResp;
import com.brigeintelligent.api.manager.entity.Role;
import com.brigeintelligent.api.manager.entity.User;
import com.brigeintelligent.api.manager.service.LoginService;
import com.brigeintelligent.api.shiro.realm.ShiroToken;
import com.brigeintelligent.api.shiro.realm.ShiroUtils;
import com.brigeintelligent.base.BaseCode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description 用户管理
 * @Author Sugweet
 * @Time 2019/4/17 14:27
 */
@RestController
@RequestMapping(value = "/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    //post登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public LoginResp login(String username, String password){
        LoginResp loginResp = new LoginResp();
        try {
            //添加用户认证信息
            ShiroToken shiroToken = new ShiroToken(username, password);
            SecurityUtils.getSubject().login(shiroToken);
            User user = ShiroUtils.currUser();
            loginResp.setUser(user);
            loginResp.setCode(BaseCode.SUCEED);
            loginResp.setMsg("登陆成功");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            loginResp.setCode(BaseCode.FAILED);
            loginResp.setMsg(e.getMessage());
        }
        return loginResp;
    }

    //登出
    @GetMapping(value = "/logout")
    public LoginResp logout(){
        LoginResp loginResp = new LoginResp();
        SecurityUtils.getSubject().logout();
        loginResp.setCode(BaseCode.SUCEED);
        loginResp.setMsg("您已安全退出");
        return loginResp;
    }

    //未登录
    @GetMapping(value = "/noLogin")
    public LoginResp noLogin(){
        LoginResp loginResp = new LoginResp();
        loginResp.setCode(600);
        loginResp.setMsg("未登录");
        return loginResp;
    }

    //未授权
    @GetMapping(value = "/403")
    @ExceptionHandler(value = UnauthorizedException.class) // 如果没有权限就跳转到403界面
    public LoginResp UnauthorizedUrl(){
        LoginResp loginResp = new LoginResp();
        loginResp.setCode(403);
        loginResp.setMsg("没有权限");
        return loginResp;
    }

    //数据初始化
    @PostMapping(value = "/addUser")
    @RequiresPermissions("create") // 用户新增必须有新增权限
    public String addUser(@RequestBody User user){

        User user1 = loginService.addUser(user);
        return "addUser is ok! \n" + user1;
    }

    //角色初始化
    @PostMapping(value = "/addRole")
    @RequiresPermissions("create") // 用户新增必须有新增权限
    public String addRole(@RequestBody Map<String,Object> map){
        Role role = loginService.addRole(map);
        return "addRole is ok! \n" + role;
    }

    // 查询当前用户

    @GetMapping(value = "/getCurrent")
    public LoginResp getCurrent() {
        LoginResp loginResp = new LoginResp();
        User user = loginService.findUserByUserName(ShiroUtils.currUser().getUsername());
        loginResp.setCode(BaseCode.SUCEED);
        loginResp.setMsg("查询成功");
        loginResp.setUser(user);
        return loginResp;
    }

    // 查询所有用户
    @GetMapping(value = "/findAll")
    @RequiresPermissions("select")
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
