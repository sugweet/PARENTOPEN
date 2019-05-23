package com.brigeintelligent.shiro;

import com.brigeintelligent.api.manager.entity.User;
import com.brigeintelligent.api.shiro.ShiroToken;
import com.brigeintelligent.api.shiro.ShiroUtils;
import com.brigeintelligent.base.basemethod.BaseCode;
import com.brigeintelligent.base.basemethod.BaseResponse;
import com.brigeintelligent.service.loginService.LoginService;
import io.swagger.annotations.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 用户管理
 * @Author Sugweet
 * @Time 2019/4/17 14:27
 */
@Api(tags = "系统登录接口")
@RestController("C_LoginController")
@RequestMapping(value = "/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    //post登录
    @ApiOperation(value = "登录接口", notes = "code:0为成功，否则失败")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", dataType = "String", required = true),
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public BaseResponse login(String username,String password) {


        BaseResponse loginResp = new BaseResponse();
        try {
            //添加用户认证信息
            ShiroToken shiroToken = new ShiroToken(username, password);
            SecurityUtils.getSubject().login(shiroToken);
            User user = ShiroUtils.currUser();
            loginResp.setFlag(true);
            loginResp.setCode(BaseCode.SUCEED);
            loginResp.setMsg(user);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            loginResp.setCode(BaseCode.FAILED);
            loginResp.setMsg(e.getMessage());
        }
        return loginResp;
    }

    //登出
    @ApiOperation(value = "退出接口", notes = "code：0表示成功，否则失败")
    @GetMapping(value = "/logout")
    public BaseResponse logout() {
        SecurityUtils.getSubject().logout();
        return new BaseResponse(BaseCode.SUCEED, "您已安全退出");
    }

    //未登录
    @ApiOperation(value = "未登录接口", notes = "code：0表示成功，否则失败")
    @GetMapping(value = "/noLogin")
    public BaseResponse noLogin() {

        return new BaseResponse(600, "您未登录");
    }

    //未授权
    @ApiOperation(value = "未授权接口", notes = "code：0表示成功，否则失败")
    @GetMapping(value = "/403")
    @ExceptionHandler(value = UnauthorizedException.class) // 如果没有权限就跳转到403界面
    public BaseResponse UnauthorizedUrl() {

        return new BaseResponse(403, "没有权限");
    }

    //数据初始化
    @ApiOperation(value = "新增/更新用户接口", notes = "code:0为成功，否则失败")
    @PostMapping(value = "/addUser")
    @RequiresPermissions("create") // 用户新增必须有新增权限
    public BaseResponse addUser(@RequestBody @ApiParam(name = "用户实体", value = "json格式", required = true) User user) {

        User user1 = loginService.addUser(user);
        if (user1 != null) {

            return new BaseResponse(BaseCode.SUCEED, "新增/更新成功");
        }
        return new BaseResponse(BaseCode.FAILED, "新增/更新失败");
    }

    // 查询当前用户
    @ApiOperation(value = "查询当前用户接口", notes = "code:0为成功，否则失败")
    @GetMapping(value = "/getCurrent")
    public BaseResponse getCurrent() {
        BaseResponse loginResp = new BaseResponse();
        User user = loginService.findUserByUserName(ShiroUtils.currUser().getUsername());
        loginResp.setFlag(true);
        loginResp.setCode(BaseCode.SUCEED);
        loginResp.setMsg(user);
        return loginResp;
    }

    // 用户名校验
    @ApiOperation(value = "用户名校验", notes = "code:0为用户名可用，否则用户名重复")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", name = "id", value = "用户id", required = false),
    })
    @GetMapping(value = "/usernameExist")
    public BaseResponse usernameExist(String username, String id) {
        BaseResponse loginResp = new BaseResponse();
        Boolean usernameExist = loginService.usernameExist(username, id);
        if (usernameExist) {
            loginResp.setFlag(false);
            loginResp.setCode(BaseCode.FAILED);
            loginResp.setMsg("用户名重复");
        } else {
            loginResp.setFlag(true);
            loginResp.setCode(BaseCode.SUCEED);
            loginResp.setMsg("用户名可用");
        }
        return loginResp;
    }

    // 查询所有用户
    @ApiOperation(value = "查询所有用户接口", notes = "返回结果集合")
    @GetMapping(value = "/findAll")
    public BaseResponse findAll() {
        BaseResponse loginResp = new BaseResponse();
        List<User> users = loginService.findAll();
        loginResp.setFlag(true);
        loginResp.setCode(BaseCode.SUCEED);
        loginResp.setMsg(users);
        return loginResp;

    }

    /*//注解的使用
    @RequiresRoles("admin")
    @RequiresPermissions("create")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public String create(){
        return "Create success!";
    }*/

    public static void main(String[] args) {
        String s = "varchar(1233)";
        String[] substring = s.split("\\(");
        String length = substring[1];
        length = length.substring(0, length.length() - 1);
        System.out.println(length);
    }
}
