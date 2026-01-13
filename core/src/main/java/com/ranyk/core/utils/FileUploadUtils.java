package com.ranyk.core.utils;

import com.ranyk.common.constant.CompressedSuffixEnum;
import com.ranyk.model.exception.file.FileException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * CLASS_NAME: FileUploadUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 文件上传处理工具类
 * @date: 2026-01-09
 */
@Slf4j
@SuppressWarnings("unused")
public class FileUploadUtils {
    /**
     * 定义的字节数组大小
     */
    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 获取 File 文件对象内容的 byte[] 数组
     *
     * @param file File 文件对象
     * @return 获取到的文件类容 byte[] 数组
     */
    public static byte[] getContent(File file) {
        try {
            return getContent(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            log.error("获取文件的字节数组 byte[] 时,未获取到文件 {} ", file.getAbsolutePath());
            log.error("异常详情为: ", e);
            throw new FileException("upload.file.not.found");
        }
    }

    /**
     * 获取对应的文件输入流的类容
     *
     * @param is InputStream 输入流对象
     * @return 获取到的输入流对象的 byte[] 数组
     */
    public static byte[] getContent(InputStream is) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            var buffer = new byte[1024];
            while (true) {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("通过输入流获取文件字节数组时发生异常, 异常为: ", e);
            throw new FileException("get.file.flow.exceptions");
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将对应 ByteArrayInputStream 对象,写入指定的文件中
     *
     * @param bais 文件内容 ByteArrayInputStream 对象
     * @param fos  需要将内容保存到的文件输出流 FileOutputStream 对象
     */
    public static void writeFile(ByteArrayInputStream bais, FileOutputStream fos) {
        try {
            int temp;
            byte[] bt = new byte[1024 * 10];
            while ((temp = bais.read(bt)) != -1) {
                fos.write(bt, 0, temp);
            }
        } catch (Exception e) {
            log.error("在写入文件时发生异常,异常信息为: ", e);
            throw new FileException("write.file.exceptions");
        } finally {
            try {
                bais.close();
            } catch (Exception e) {
                log.error("在写入文件后关闭 ByteArrayInputStream 对象时,发生异常,异常信息为:  ", e);
            }
            try {
                fos.close();
            } catch (Exception e) {
                log.error("在写入文件后关闭 FileOutputStream 对象时,发生异常,异常信息为:  ", e);
            }

        }
    }

    /**
     * 通过 byte[] 数组写入指定路径的文件中
     *
     * @param source 需要写入的文件内容  byte[] 数组
     * @param path   需要写入的文件路径
     */
    public static void writeFile(byte[] source, String path) {
        try {
            var bais = new ByteArrayInputStream(source);
            var fos = new FileOutputStream(new File(path));
            writeFile(bais, fos);
        } catch (Exception e) {
            log.error("将对应的文件写入指定位置的文件中发生异常,异常信息为: ", e);
            throw new FileException("write.file.exceptions");
        }
    }

    /**
     * 通过指定路径对其路径下的文件进行压缩成 ZIP 包
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流对象
     * @param keepDirStructure 是否需要保持原有的文件结构
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure) {
        try (var zos = new ZipOutputStream(out)) {
            var sourceFile = new File(srcDir);
            if (!sourceFile.exists() && sourceFile.createNewFile()) {
                log.error("文件不存在,并进行创建成功!");
            }
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
        } catch (Exception e) {
            log.error("在对文件进行 ZIP 压缩时发生异常, 异常为: ", e);
            throw new FileException("compressed.file.exceptions");
        }
    }

    /**
     * 同时压缩多个文件
     *
     * @param srcFile 需要压缩的文件对象 List 集合
     * @param out     压缩文件输出流对象
     */
    public static void toZip(List<File> srcFile, OutputStream out) {
        try (var zos = new ZipOutputStream(out)) {
            for (File file : srcFile) {
                writeZip(file, zos);
            }
        } catch (Exception e) {
            log.error("在对多个文件进行压缩时发生异常,异常信息为: ", e);
            throw new FileException("compressed.file.exceptions");
        }
    }

    /**
     * 向指定的 zip 文件中写入内容
     *
     * @param file 需向 zip 文件中写入的文件对象
     * @param zos  zip 文件对象
     */
    public static void writeZip(File file, ZipOutputStream zos) {
        var buf = new byte[BUFFER_SIZE];
        int len;
        try (var fis = new FileInputStream(file)) {
            zos.putNextEntry(new ZipEntry(file.getName()));
            while ((len = fis.read()) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
        } catch (Exception e) {
            log.error("在向 ZIP 文件中写入文件内容时发生异常, 异常为: ", e);
            throw new FileException("compressed.file.exceptions");
        }
    }

    /**
     *
     * 压缩文件方法
     *
     * @param file             需要压缩的文件对象,可以是一个文件目录,也可以是单个具体的文件
     * @param zos              压缩输出流对象
     * @param name             压缩的实体对象名
     * @param keepDirStructure 当存在多级文件目录时,是否需要保留文件结构
     */
    public static void compress(File file, ZipOutputStream zos, String name, boolean keepDirStructure) {
        // 1. 判断传入的文件对象是文件,是文件则直接进行压缩
        if (file.isFile()) {
            compressFile(file, zos, name);
        }
        // 2. 当传入的文件对象不是文件,则需进行遍历传入的文件夹,对其进行文件夹压缩
        else {
            compressDir(file, zos, name, keepDirStructure);
        }
    }

    /**
     * 压缩文件
     *
     * @param file 需要压缩的文件,只能是文件,不能是文件夹
     * @param zos  压缩文件输出流对象
     * @param name 压缩实体的名称
     */
    private static void compressFile(File file, ZipOutputStream zos, String name) {
        // 1. 判断传入的File对象是否是文件
        if (!file.isFile()) {
            log.error("在调用 compressFile 处判断压缩的是否是文件时发现当前压缩对象不是文件对象!");
            throw new FileException("not.is.file");
        }
        // 2. 创建一个byte数组,用于后面的循环中每次获取文件中内容的大小
        var buf = new byte[BUFFER_SIZE];
        // 3. 获取文件的输入流对象
        try (var fis = new FileInputStream(file)) {
            // 4. 向文件压缩输出流中添加一个压缩实体对象
            var zipEntry = new ZipEntry(name);
            zos.putNextEntry(zipEntry);
            // 5. 声明单次读取文件内容的长度变量
            int len;
            // 6. 复制文件到压缩输出流中
            while ((len = fis.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // 7. 关闭压缩实体对象
            zos.closeEntry();
        } catch (Exception e) {
            log.error("在调用 compressFile 处发生异常,异常信息为: ", e);
            throw new FileException("compressed.file.exceptions");
        }
    }

    /**
     * 压缩目录
     *
     * @param file             需要压缩的文件目录路径,必须是文件目录
     * @param zos              压缩文件输出流对象
     * @param name             压缩的实体的名称,如果是目录则是路径
     * @param keepDirStructure 是否保留原文件结构
     */
    private static void compressDir(File file, ZipOutputStream zos, String name, boolean keepDirStructure) {
        // 1. 判断传入的File对象是否是文件夹
        if (!file.isDirectory()) {
            log.error("在调用 compressDir 方法处判断当前压缩对象是否是一个文件夹时发现当前压缩对象不是一个文件夹!");
            throw new FileException("compressed.file.exceptions");
        }
        // 2. 获取传入的文件目录下的文件对象数组
        File[] files = file.listFiles();
        // 3. 进行递归调用,以压缩文件目录下的所有文件
        try {
            // 4. 判断获取的文件对象数组是否存在,不存在则进行空文件目录的压缩
            if (Objects.isNull(files)) {
                // 5. 判断是否需要进行原文件结构保留
                if (keepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }
            }
            // 6. 存在文件进行压缩
            else {
                // 7. 进行遍历文件对象数组,递归调用压缩方法
                for (File entity : files) {
                    // 7.1。判断是否需要进行原文件结构的压缩,
                    if (keepDirStructure) {
                        compress(entity, zos, name + File.pathSeparator + entity.getName(), true);
                    }
                    // 7.2. 不需要进行原文件结构的压缩,不保留原文件结构就是将所有文件直接压缩至根目录下
                    else {
                        compress(entity, zos, entity.getName(), false);
                    }
                }
            }
        } catch (Exception e) {
            log.error("在调用 compressDir 方法压缩文件目录失败,发生的异常信息为: ", e);
            throw new FileException("compressed.file.exceptions");
        }
    }

    /**
     * 压缩单个文件
     *
     * @param file    需要压缩的文件对象
     * @param zipFile 压缩后保存的文件对象
     */
    public static void compressSingleFile(File file, File zipFile) {
        // 1. 判断入参的压缩文件是否是指定的压缩文件后缀
        if (!CompressedSuffixEnum.judgeZipFileFormat(zipFile.getName())) {
            log.error("传入的压缩文件格式不正确!");
            throw new FileException("compressed.file.exceptions");
        }
        // 2. 获取原文件的输入流对象 和 获取压缩文件的输出流对象
        try (var fis = new FileInputStream(file);
             var bis = new BufferedInputStream(fis);
             var dis = new DataInputStream(bis);
             var fos = new FileOutputStream(zipFile);
             var bos = new BufferedOutputStream(fos);
             var zos = new ZipOutputStream(bos);) {
            // 4. 创建压缩实体对象
            var zipEntry = new ZipEntry(file.getName());
            // 5. 将压缩实体添加进压缩输出流中
            zos.putNextEntry(zipEntry);
            int len;
            var buf = new byte[BUFFER_SIZE];
            while ((len = dis.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
        } catch (Exception e) {
            log.error("在对单个文件进行压缩时发生异常,异常信息为: ", e);
            throw new FileException("compressed.file.exceptions");
        }
    }
}
