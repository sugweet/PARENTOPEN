package com.brigeintelligent.shiro;

import com.brigeintelligent.base.basemethod.BaseCode;
import com.brigeintelligent.base.basemethod.BaseResponse;
import com.brigeintelligent.base.baseutils.CodeHintUtils;

import com.brigeintelligent.base.baseutils.FdfsUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private FdfsUtils fdfsUtils;

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

    @ApiOperation(value = "文件上传测试", notes = "字符串")
    @PostMapping(value = "downloadFastDfs", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public String downloadFastDfs(@ApiParam("文件上传测试") MultipartFile file) {

        String filePath = fdfsUtils.uploadByFastDFS(file);

        CodeHintUtils codeHint = CodeHintUtils.getInstance();

        return codeHint.getValues("default.800.fdfsUrl") + filePath;
    }

    @ApiOperation(value = "删除文件测试", notes = "字符串")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "filePath", value = "文件路径", required = false)

    })
    @PostMapping(value = "deleteFastDfs")
    public String deleteFastDfs(String filePath) {
        fdfsUtils.deleteFile(filePath);

        return "删除成功";
    }

}
