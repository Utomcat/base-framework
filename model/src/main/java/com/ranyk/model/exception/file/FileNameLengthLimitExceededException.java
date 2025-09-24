package com.ranyk.model.exception.file;

import java.io.Serial;

/**
 * CLASS_NAME: FileNameLengthLimitExceededException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件名称长度超长异常类
 * @date: 2025-09-25
 */
public class FileNameLengthLimitExceededException extends FileException{

    @Serial
    private static final long serialVersionUID = -4316418016574396006L;

    /**
     * 创建一个 FileNameLengthLimitExceededException 实例对象
     *
     * @param defaultFileNameLength 默认文件名长度
     */
    public FileNameLengthLimitExceededException(int defaultFileNameLength) {
        super("upload.filename.exceed.length", new Object[]{defaultFileNameLength});
    }
}
