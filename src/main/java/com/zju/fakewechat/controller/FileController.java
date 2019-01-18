package com.zju.fakewechat.controller;

import com.zju.fakewechat.model.response.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;

/**
 * @author: takumiCX
 * @create: 2019-01-18
 * @email: takumicx@zju.edu.cn
 **/
@Controller
@Slf4j
@Validated
@Api("图片上传相关接口")
public class FileController {

    @GetMapping("/image")
    @ResponseBody
    @ApiOperation("根据文件名下载图片")
    public Response<Boolean> downloadImage(@RequestParam("image") @NotBlank(message = "Imagepath cant be empty!") String image, HttpServletResponse response) {

        Response<Boolean> res;
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + image);// 设置文件名
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        String absImagePath = System.getProperty("user.dir") + File.separator + "images" + File.separator+image;
        log.info("Image path:{}", absImagePath);
        try {
            fis = new FileInputStream(absImagePath);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }

            res = Response.success(true);

        } catch (Exception e) {

            log.error("Download image error!", e);

            res = Response.failed(e);

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("BufferedInputStream close error!", e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("FileInputStream close error!", e);

                }
            }
        }
        return res;
    }

}
