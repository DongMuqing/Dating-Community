package xyz.qingmumu.controller.user;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;
import xyz.qingmumu.util.UploadUtil;

import java.io.IOException;

@RestController
@RequestMapping("api/{version}/user/upload")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class UploadController {
    private static final String AVATAR_PATH = "Userpics/"; //头像oss目录
    private static final String POST_PATH = "Post/";  //动态oss目录
    private static final String ARTICLE_PATH = "Article/"; //文章oss目录
    @Autowired
    private UploadUtil uploadUtil;

    /**
     * 头像上传
     *
     * @param avatar
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadAvatar")
    @SaCheckRole("用户")
    public Result upload(@RequestPart("files") MultipartFile avatar) throws IOException {
        return uploadUtil.singleImageUpload(avatar, AVATAR_PATH);
    }

    /**
     * 动态图片上传
     *
     * @param files 最多9张图片
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadPostImage")
    @SaCheckRole("用户")
    public Result uploadPostImage(@RequestPart("files") MultipartFile[] files) throws IOException {
        if (files.length > 9) {
            return new Result(null, Code.SAVE_ERR, "图片不能超过九张！");
        }
        return uploadUtil.multipleImageUpload(files, POST_PATH);
    }

    /**
     * 文章的图片上传
     *
     * @param cover 封面
     * @return
     */
    @PostMapping("/uploadArticleImage")
    @SaCheckRole("用户")
    public Result uploadArticleImage(@RequestPart("files") MultipartFile cover) throws IOException {
        return uploadUtil.singleImageUpload(cover, ARTICLE_PATH);
    }
}
