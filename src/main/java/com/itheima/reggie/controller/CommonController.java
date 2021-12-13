package com.itheima.reggie.controller;

import com.itheima .reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/3 0003 20:52
 */

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    //读取配置文件中的reggie图片路径
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 上传菜品图片
     * @param file
     * @return
     */

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //获取文件原始名
        String originalFilename = file.getOriginalFilename();
        //根据文件名切割后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用uuid防止图片文件名相同
        String fileName = UUID.randomUUID().toString() + suffix;
        //判断文件夹是否存在，如果不存在，进行创建
        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);


    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
    //输入流，通过输入流读取文件内容
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            //输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream=response.getOutputStream();

            response.setContentType("/image/jpeg");

            //利用common-IO的工具，直接可以进行拷贝
            IOUtils.copy(fileInputStream,outputStream);
//            int len=0;
//            byte[] bytes = new byte[1024];
//            while ((len=fileInputStream.read(bytes))!=-1){
//                outputStream.write(bytes,0,len);
//                outputStream.flush();
//            }
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
