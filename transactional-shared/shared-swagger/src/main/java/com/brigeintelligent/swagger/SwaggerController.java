package com.brigeintelligent.swagger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description：swagger入口
 * @Author：Sugweet
 * @Time：2019/4/28 17:18
 */
@RestController
public class SwaggerController {
    @RequestMapping("/swagger")
    public void swagger(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

}
