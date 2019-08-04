package com.brigeintelligent.base.baseutils;

import com.brigeintelligent.base.basemethod.BaseCode;
import com.brigeintelligent.base.basemethod.BaseException;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

/**
 * @ClassName FdfsUtils
 * @Description FastDFS工具类
 * @Author Sugweet Chen
 * @Date 2019/8/3 10:41
 * @Version 1.0
 **/
@Component
@Slf4j
public class FdfsUtils {
    @Resource
    private FastFileStorageClient storageClient;

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws BaseException
     */
    public synchronized String uploadByFastDFS(MultipartFile file) throws BaseException {
        try {
            byte[] bytes = getBytes(file);
            InputStream stream = new ByteArrayInputStream(bytes);
            String fileType = getFileType(file);
            StorePath storePath = storageClient.uploadFile(stream, file.getSize(), fileType, null);
            return storePath.getFullPath();
        } catch (Exception e) {
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws BaseException
     */
    public synchronized String uploadByFastDFS(File file) throws BaseException {
        try {
            byte[] bytes = getBytes(file);
            InputStream stream = new ByteArrayInputStream(bytes);
            String fileType = getFileType(file);
            StorePath storePath = storageClient.uploadFile(stream, file.length(), fileType, null);
            return storePath.getPath();
        } catch (Exception e) {
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param filePath
     * @return
     * @throws BaseException
     */
    public synchronized String uploadByFastDFS(String filePath) throws BaseException {
        try {
            File file = new File(filePath);
            byte[] bytes = getBytes(file);
            InputStream stream = new ByteArrayInputStream(bytes);
            String fileType = getFileType(file);
            StorePath storePath = storageClient.uploadFile(stream, file.length(), fileType, null);
            return storePath.getFullPath();
        } catch (Exception e) {
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
    }

    /**
     * 从fastDFS上下载文件
     *
     * @param filePath
     * @return
     * @throws BaseException
     */
    public synchronized byte[] downloadByFastDFS(String filePath) throws BaseException {
        try {

            StorePath storePath = StorePath.parseFromUrl(filePath);
            return storageClient.downloadFile(storePath.getGroup(),
                    storePath.getPath(), new DownloadByteArray());

        } catch (Exception e) {
            //e.printStackTrace();
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    public synchronized void deleteFile(String filePath) {
        storageClient.deleteFile(filePath);
    }

    /**
     * 解析文件路径
     *
     * @param filePath
     * @return
     */
   /* public PathInfo parseFromUrl(String filePath) {
        int pos = filePath.indexOf("opengroup");
        String groupAndPath = filePath.substring(pos);
        pos = groupAndPath.indexOf("/");
        String group = groupAndPath.substring(0, pos);
        String path = groupAndPath.substring(pos + 1);
        return new PathInfo(group, path);
    }
*/
    /**
     * 根据路径获取文件类型
     *
     * @param filePath
     * @return
     */
    public static String getFileType(String filePath) {
        int index = filePath.lastIndexOf(".");
        return filePath.substring(index + 1);
    }

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    public static String getFileType(File file) {
        return getFileType(file.getAbsolutePath());
    }

    /**
     * 获取文件类型
     *
     * @param file
     * @return
     */
    public static String getFileType(MultipartFile file) {
        //获取源文件名称
        String originalFileName = file.getOriginalFilename();
        //获取文件后缀--.doc
        if (originalFileName != null)
            return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        return null;
    }

    /**
     * multipart转byte[]
     *
     * @param file
     * @return
     */
    private static byte[] getBytes(MultipartFile file) {
        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            log.error("=========文件转换成byte数组失败", e);
        }
        return bytes;
    }

    /**
     * IO file转byte[]
     *
     * @param file
     * @return
     */
    private static byte[] getBytes(File file) {
        byte[] buffer = null;
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
        } catch (Exception e) {
            log.error("=========文件转换成byte数组失败", e);
        }
        return buffer;
    }

    /**
     * 封装FastDFS文件信息
     */
    /*public class PathInfo {
        private String groupName;
        private String path;

        private PathInfo(String groupName, String path) {
            this.groupName = groupName;
            this.path = path;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }*/

}
