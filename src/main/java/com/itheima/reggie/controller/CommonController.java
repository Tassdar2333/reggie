package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件的上传和下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        log.info("文件上传");

        String originFileName = file.getOriginalFilename();
        //后缀
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));
        //构建新文件名
        String fileName = UUID.randomUUID().toString() + suffix;
        //使用uuid重新生成文件名
        File dir = new File(basePath);

        //判断当前目录是否存在
        if(!dir.exists()){
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }

    /**
     * 为文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void dowload(String name, HttpServletResponse response) throws FileNotFoundException {
        //输入流 读取文件
        try(FileInputStream fileInputStream = new FileInputStream(new File(basePath + name))){

            //输出流
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int len = 0;

            byte[] bytes = new byte[1024];

            while((len = fileInputStream.read(bytes)) != -1){

                outputStream.write(bytes,0,len);

                outputStream.flush();

                outputStream.close();

            }

        } catch (IOException e) {

            throw new RuntimeException(e);

        }


    }

}
