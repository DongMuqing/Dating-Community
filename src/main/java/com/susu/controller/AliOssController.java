package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.util.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Date:2023/8/15 20:38
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/alioss")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AliOssController {
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @GetMapping
    /**
     * 获取所有oss路径
     */
    public Result getDirectory(){
        Map<Integer, List<String>> directoryAndFilePath = aliOSSUtils.getDirectoryAndFilePath();
        List<String> directory = directoryAndFilePath.get(0);
        Integer code = directory != null ? Code.GET_OK : Code.GET_ERR;
        String msg = directory != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(directory, code, msg);
    }

    /**
     * 查询指定路径的所有文件目录
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/filepath")
    public Result getFilePath( @RequestParam String path) throws Exception {
        List<String> filepath = aliOSSUtils.ListRequest(path);
        Integer code = filepath != null ? Code.GET_OK : Code.GET_ERR;
        String msg = filepath != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(filepath, code, msg);
    }
    @PostMapping
    public Result upload(@RequestPart MultipartFile image,String folderPath) throws Exception {
        String url = aliOSSUtils.upload(image, folderPath);
        Integer code = url != null ? Code.GET_OK : Code.GET_ERR;
        String msg = url != null ? "上传成功" : "上传失败，请重试！";
        return new Result(url, code, msg);
    }
}
