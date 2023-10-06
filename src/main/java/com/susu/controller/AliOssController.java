package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.susu.entity.AliOss;
import com.susu.entity.Code;
import com.susu.entity.Paging;
import com.susu.entity.Result;
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

    /**
     * 获取所有oss路径
     */
    @GetMapping
    public Result getDirectory() {
        List<String> directory = aliOSSUtils.getDirectoryAndFilePath();
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
    @SaCheckRole("管理员")
    public Result upload(@RequestPart("files") MultipartFile[] files, String path) throws Exception {
        if(files.length>0){
            List<String> fileurl = new ArrayList<>();
            for (MultipartFile file : files) {
                String url = aliOSSUtils.upload(file, path);
                fileurl.add(url);
            }
            return new Result(fileurl, Code.GET_OK, "上传成功");
        }else {
            return new Result(null,Code.GET_ERR,"请选择上传文件！");
        }
    }

    /**
     * @param path 需要删除的文件路径
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    @SaCheckRole("管理员")
    public Result delete(@RequestParam String path) throws Exception {
        boolean delete = aliOSSUtils.delete(path);
        if(delete){
            return new Result(null, Code.DELETE_OK,"删除成功!");
        }else {
            return new Result(null, Code.DELETE_ERR,"路径错误!");
        }
    }

    /**
     * 默认查询第一页 每页十条数据
     * 指定路径的分页查询
     * @param path
     * @return
     * @throws Exception
     */
    @PostMapping("/paging")
    public Result paging(@RequestParam String path,
                         @RequestParam(defaultValue = "1") Long page,
                         @RequestParam(defaultValue = "10") Integer maxkey) throws Exception {
        Paging<AliOss> aliOssPaging = aliOSSUtils.pagingEnumeration(path,page,maxkey);
        if (aliOssPaging!=null){
            return new Result(aliOssPaging, Code.GET_OK,"查询成功!");
        }else {
            return new Result(null, Code.GET_ERR,"查询失败!");
        }
    }
}
