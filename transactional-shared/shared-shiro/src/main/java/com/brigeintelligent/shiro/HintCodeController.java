package com.brigeintelligent.shiro;

import com.brigeintelligent.base.basemethod.BaseCode;
import com.brigeintelligent.base.basemethod.BaseResponse;
import com.brigeintelligent.base.baseutils.CodeHintUtils;
import com.brigeintelligent.base.baseutils.FastDfsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description：代码提示刷新接口
 * @Author：Sugweet
 * @Time：2019/7/18 10:56
 */
@Api(tags = "代码提示刷新接口")
@RestController("C_HintCodeController")
@RequestMapping(value = "/api/")
public class HintCodeController {

    //更新
    @ApiOperation(value = "代码提示刷新接口", notes = "code：0表示成功，否则失败")
    @GetMapping(value = "onLoad")
    public BaseResponse onLoad() {
        CodeHintUtils instance = CodeHintUtils.getInstance();
        boolean b = instance.onLoad();
        if (b) {
            return new BaseResponse(BaseCode.SUCEED, "配置信息更新成功");
        }
        return null;
    }

    @ApiOperation(value = "代码提示测试接口", notes = "字符串")
    @GetMapping(value = "test")
    public String test() {
        String values = CodeHintUtils.getInstance().getValues("signin.800.nolist");
        System.out.println(values);
        return values;
    }

    @ApiOperation(value = "fastdfsTest", notes = "字符串")
    @PostMapping(value = "fastdfs", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public String fastdfs(@ApiParam("文件上传测试") MultipartFile file) {
        FastDfsUtils fastDfsUtils = new FastDfsUtils();
        String uploadByFastDFS = fastDfsUtils.uploadByFastDFS(file);
        return uploadByFastDFS;
    }

}
