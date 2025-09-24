package com.ranyk.model.exception.file;

import java.io.Serial;

/**
 * CLASS_NAME: FileSizeLimitExceededException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件大小限制异常类
 * @date: 2025-09-25
 */
public class FileSizeLimitExceededException extends FileException{

    @Serial
    private static final long serialVersionUID = 4238952733789873965L;

    /**
     * 创建一个 FileSizeLimitExceededException 实例对象
     *
     * @param defaultMaxSize 默认最大文件大小
     */
    public FileSizeLimitExceededException(long defaultMaxSize) {
        super("upload.exceed.maxSize", new Object[]{defaultMaxSize});
    }
}
