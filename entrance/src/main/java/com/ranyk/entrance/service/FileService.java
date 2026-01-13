package com.ranyk.entrance.service;

import com.ranyk.common.utils.DateTimeUtils;
import com.ranyk.entrance.config.properties.FileUploadConfigProperties;
import com.ranyk.model.exception.file.FileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CLASS_NAME: FileService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件业务逻辑处理类
 * @date: 2026-01-09
 */
@Slf4j
@Service
public class FileService {
    /**
     * 文件上传配置属性对象
     */
    private final FileUploadConfigProperties fileUploadConfigProperties;

    /**
     * 构造函数
     *
     * @param fileUploadConfigProperties 文件上传配置属性对象
     */
    @Autowired
    public FileService(FileUploadConfigProperties fileUploadConfigProperties) {
        this.fileUploadConfigProperties = fileUploadConfigProperties;
    }

    /**
     * 文件上传处理
     *
     * @param files 文件对象数组
     * @return 返回文件上传后保存的文件路径 List 集合
     */
    public List<String> uploadFile(MultipartFile[] files) {
        if (Objects.isNull(files) || files.length == 0) {
            log.error("上传的文件对象为空!");
            throw new FileException("upload.file.not.found");
        }

        List<String> result = new ArrayList<>(files.length);

        File saveDir = new File(fileUploadConfigProperties.getPath() + DateTimeUtils.getDateStr(LocalDate.now(), DateTimeUtils.DATE_YYYYMMDD_FORMAT));

        if (!saveDir.exists()) {
            if (!saveDir.mkdirs()) {
                log.error("创建文件上传保存文件根目录失败!");
                throw new FileException("create.file.upload.save.dir.fail");
            }
        }

        for (MultipartFile file : files) {
            if (Objects.isNull(file)) {
                log.error("本次上传的文件对象为空!");
                continue;
            }
            try {
                String originalFilename = Objects.isNull(file.getOriginalFilename()) ? "" : file.getOriginalFilename();
                String fileName = DateTimeUtils.getDateTimeStr(LocalDateTime.now(), DateTimeUtils.DATE_TIME_YYYYMMDDHHMMSS_FORMAT) + "-" + originalFilename.substring(0, originalFilename.lastIndexOf(".")).replace(" ", "-") + originalFilename.substring(originalFilename.lastIndexOf("."));
                File destFile = new File(saveDir, fileName);
                file.transferTo(destFile);
                result.add(saveDir + File.separator + fileName);
            } catch (IOException e) {
                log.error("保存文件 {} 发生异常!", file.getOriginalFilename(), e);
                throw new FileException("write.file.exceptions");
            }
        }
        return result;
    }

    public byte[] getLocalImage(String imgPath) {

        File imgFile = new File(imgPath);
        // 2. 校验文件是否存在 + 是否是文件（不是文件夹）
        if (!imgFile.exists() || !imgFile.isFile()) {
            log.error("需要读取的本地图片文件 {} 不存在! ", imgPath);
            return null;
        }
        // 3. 读取图片的字节数组（核心）
        try {
            return Files.readAllBytes(Paths.get(imgPath));
        } catch (Exception e) {
            log.error("读取本地图片文件 {} 发生异常!", imgPath, e);
            return null;
        }
    }

    public byte[] getNetImage(String imgUrl) {
        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;
        HttpURLConnection connection = null;
        try {
            // 2. 构建网络URL连接，读取图片流（核心）
            URL url = new URL(imgUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 连接超时5秒
            connection.setConnectTimeout(5000);
            // 读取超时5秒
            connection.setReadTimeout(5000);
            // 3. 校验网络请求是否成功
            if (connection.getResponseCode() != HttpStatus.OK.value()) {
                return null;
            }
            // 4. 读取网络流为字节数组
            inputStream = connection.getInputStream();
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            log.error("读取网络图片 {} 发生异常!", imgUrl, e);
            return null;
        }
        finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("在读取网络图片 {} 后关闭 InputStream 抛出异常!", imgUrl, e);
                }
            }
            if (bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    log.error("在读取网络图片 {} 后关闭 ByteArrayOutputStream 抛出异常!", imgUrl, e);
                }
            }
            if (connection != null){
                connection.disconnect();
            }
        }
    }
}
