package com.ranyk.entrance.api;

import com.ranyk.entrance.service.FileService;
import com.ranyk.model.response.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * CLASS_NAME: FileApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件处理接口
 * @date: 2026-01-09
 */
@RestController
@RequestMapping("/api/file")
public class FileApi {
    /**
     * 文件处理服务类对象
     */
    private final FileService fileService;

    /**
     * 构造函数
     *
     * @param fileService 文件处理服务类对象
     */
    @Autowired
    public FileApi(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 文件上传处理
     *
     * @param files 文件对象数组
     * @return 文件上传后保存的文件路径 List 集合
     */
    @PostMapping
    public R<List<String>> uploadFile(@RequestParam("file") MultipartFile[] files) {
        return R.ok(fileService.uploadFile(files));
    }

    /**
     * 接口1：根据【本地磁盘路径】读取图片并返回给前端
     *
     * @param imgPath 前端传入的本地图片绝对路径 如：D:/upload/1.jpg 、/usr/local/upload/2.png
     * @return 图片字节流，前端可直接用img标签展示
     */
    @GetMapping("/get/local/image")
    public ResponseEntity<byte[]> getLocalImage(@RequestParam("imgPath") String imgPath) {
        // 1. 校验参数非空
        if (imgPath == null || imgPath.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("图片路径不能为空".getBytes());
        }
        byte[] imageBytes = fileService.getLocalImage(imgPath);
        if (imageBytes == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("读取图片失败!".getBytes());
        }
        // 4. 设置响应头：返回图片类型，让前端识别为图片而非普通字符串
        HttpHeaders headers = new HttpHeaders();
        // 根据文件后缀自动设置响应的媒体类型
        if (imgPath.endsWith(".jpg") || imgPath.endsWith(".jpeg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (imgPath.endsWith(".png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else if (imgPath.endsWith(".gif")) {
            headers.setContentType(MediaType.IMAGE_GIF);
        }
        // 5. 返回：响应头 + 图片字节流 + 200状态码
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

    }

    /**
     * 接口2：根据【网络图片URL】读取图片并返回给前端
     *
     * @param imgUrl 前端传入的网络图片地址 如：<a href="https://xxx.com/logo.png">...</a>、<a href="http://img.baidu.com/1.jpg">...</a>
     * @return 图片字节流
     */
    @GetMapping("/get/net/image")
    public ResponseEntity<byte[]> getNetImage(@RequestParam("imgUrl") String imgUrl) {
        // 1. 校验参数非空
        if (imgUrl == null || imgUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("图片 URL 不能为空".getBytes());
        }

        byte[] imageBytes = fileService.getNetImage(imgUrl);

        if (imageBytes == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("读取图片失败!".getBytes());
        }
        // 6. 设置响应头并返回
        HttpHeaders headers = new HttpHeaders();
        if (imgUrl.endsWith(".jpg") || imgUrl.endsWith(".jpeg")) {
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (imgUrl.endsWith(".png")) {
            headers.setContentType(MediaType.IMAGE_PNG);
        } else if (imgUrl.endsWith(".gif")) {
            headers.setContentType(MediaType.IMAGE_GIF);
        }
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
