package xyz.qingmumu.util;

import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadUtil {

    /**
     * 单图片上传
     * @param image
     * @param uploadPath
     * @return
     * @throws IOException
     */
    public Result singleImageUpload(MultipartFile image,String uploadPath) throws IOException {
        if (image.isEmpty()){
            return new Result(null, Code.SAVE_ERR,"请选择图片！");
        }
        if (!IOUtil.isImage(image)){
            return new Result(null,Code.SAVE_ERR,"此文件不是图片！");
        }
        AliOSSUtils aliOSSUtils = new AliOSSUtils();
        String url = aliOSSUtils.upload(image,uploadPath);
        return new Result(url,Code.SAVE_OK,"上传成功！");
    }
    public Result multipleImageUpload(MultipartFile[] files,String uploadPath) throws IOException{
        if (files.length == 0) {
            return new Result(null, Code.SAVE_ERR, "请选择图片！");
        }
        int i = 1;
        for (MultipartFile file : files) {
            if (!IOUtil.isImage(file)) {
                return new Result(null, Code.SAVE_ERR, "第" + i + "个文件不是图片,请重新选择图片！");
            }
            i++;
        }
        List<String> fileUrl = new ArrayList<>();
        AliOSSUtils aliOSSUtils = new AliOSSUtils();
        for (MultipartFile file : files) {
            String url = aliOSSUtils.upload(file,uploadPath);
            fileUrl.add(url);
        }
        // 图片资源地址
        String fileUrlString = String.join(",", fileUrl);
        return new Result(fileUrlString, Code.SAVE_OK, "上传成功！");
    }
}