package com.ranyk.model.exception.file;

import com.ranyk.model.exception.base.BaseException;

import java.io.Serial;

/**
 * CLASS_NAME: FileException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件基础异常类
 * @date: 2025-09-25
 */
public class FileException extends BaseException {

    @Serial
    private static final long serialVersionUID = 6815706468546376894L;

    /**
     * 创建一个 FileException 实例对象
     *
     * @param code         错误码
     * @param args         错误码对应的参数
     */
    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }
}
