package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.susu.damian.AliOss;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.util.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
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
    @SaIgnore
    public Result getFilePath( @RequestParam String path) throws Exception {
        path.substring(path.length()-1);
        List<AliOss> filepath = aliOSSUtils.ListRequest(path);
        log.info(filepath.toString());
        Integer code = filepath != null ? Code.GET_OK : Code.GET_ERR;
        String msg = filepath != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(filepath, code, msg);
    }
    @PostMapping("/upload")
    public Result upload(@RequestPart("files") MultipartFile[] files,String path) throws Exception {
        System.out.println(files.toString());
        List<String> fileurl=new ArrayList<>();
        for (MultipartFile file : files) {
            String url = aliOSSUtils.upload(file, path);
            fileurl.add(url);
        }
        Integer code = fileurl!= null ? Code.GET_OK : Code.GET_ERR;
        String msg = fileurl != null ? "上传成功" : "上传失败，请重试！";
        return new Result(fileurl, code, msg);
    }
}
