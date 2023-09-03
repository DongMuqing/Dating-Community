package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.susu.damian.AliOss;
import com.susu.damian.Code;
import com.susu.damian.Paging;
import com.susu.damian.Result;
import com.susu.util.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public Result getDirectory() {
        Map<Integer, List<String>> directoryAndFilePath = aliOSSUtils.getDirectoryAndFilePath();
        List<String> directory = directoryAndFilePath.get(0);
        Integer code = directory != null ? Code.GET_OK : Code.GET_ERR;
        String msg = directory != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(directory, code, msg);
    }

    /**
     * 查询指定路径的所有文件目录
     *
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/filepath")
    public Result getFilePath(@RequestParam String path) throws Exception {
        path.substring(path.length() - 1);
        List<AliOss> filepath = aliOSSUtils.ListRequest(path);
        Integer code = filepath != null ? Code.GET_OK : Code.GET_ERR;
        String msg = filepath != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(filepath, code, msg);
    }

    @PostMapping("/upload")
    public Result upload(@RequestPart("files") MultipartFile[] files, String path) throws Exception {
        List<String> fileurl = new ArrayList<>();
        for (MultipartFile file : files) {
            String url = aliOSSUtils.upload(file, path);
            fileurl.add(url);
        }
        Integer code = fileurl != null ? Code.GET_OK : Code.GET_ERR;
        String msg = fileurl != null ? "上传成功" : "上传失败，请重试！";
        return new Result(fileurl, code, msg);
    }

    /**
     * @param path 需要删除的文件路径
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    public Result delete(@RequestParam String path) throws Exception {
        boolean delete = aliOSSUtils.delete(path);
        if(delete){
            return new Result(null, Code.DELETE_OK,"删除成功!");
        }else {
            return new Result(null, Code.DELETE_ERR,"路径错误!");
        }
    }

    /**
     * 指定路径的分页查询
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/paging")
    public Result paging(@RequestParam String path,
                         @RequestParam Integer page,
                         @RequestParam Integer maxkey) throws Exception {
        Paging<AliOss> aliOssPaging = aliOSSUtils.pagingEnumeration(path,page,maxkey);
        if (aliOssPaging!=null){
            return new Result(aliOssPaging, Code.GET_OK,"查询成功!");
        }else {
            return new Result(null, Code.GET_ERR,"查询失败!");
        }
    }
}
