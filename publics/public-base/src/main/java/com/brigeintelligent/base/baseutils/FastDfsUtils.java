package com.brigeintelligent.base.baseutils;

import com.brigeintelligent.base.basemethod.BaseCode;
import com.brigeintelligent.base.basemethod.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Description：FastDfs工具类
 * @Author：Sugweet
 * @Time：2019/7/19 13:55
 */
@Slf4j
public class FastDfsUtils {

    private final static String FASTDFS_CONFIG = "fastdfs_client.conf";
    private TrackerClient trackerClient = null;
    private TrackerServer trackerServer = null;
    private StorageClient storageClient = null;

    public FastDfsUtils() throws BaseException {
        try {
            ClientGlobal.init(FASTDFS_CONFIG);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
        log.info("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
    }

    /**
     * 避免文件冲突，每次实例化一个StorageClient
     */
    private StorageClient getStorageClient() throws BaseException {
        try {
            if (trackerClient == null)
                trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            if (trackerServer == null)
                trackerServer = trackerClient.getConnection();
           // StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            StorageServer storageServer = null;
            if (storageClient == null)
                storageClient = new StorageClient(trackerServer, null);
            return storageClient;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
    }

    /**
     * 向FASTDFS上传文件
     */
    public synchronized String uploadByFastDFS(MultipartFile file) throws BaseException {
        try {
            StorageClient storageClient = this.getStorageClient();
            String extName = getFileType(Objects.requireNonNull(file.getOriginalFilename()));
            String[] r = storageClient.upload_file(file.getBytes(), extName, null);
            if (r != null)
                return r[0] + "/" + r[1];
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
    }

    /**
     * 从FASTDFS上下载文件，得到byte数组
     */
    public synchronized byte[] downloadByFastDFS(String fullPath) throws BaseException {
        try {
            StorageClient storageClient = this.getStorageClient();
            PathInfo storePath = parseFromUrl(fullPath);
            return storageClient.download_file(storePath.getGroupName(), storePath.getPath());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(BaseCode.FAILED, e.getMessage());
        }
    }

    /**
     * 解析文件路径
     */
    public PathInfo parseFromUrl(String filePath) {
        int pos = filePath.indexOf("group");
        String groupAndPath = filePath.substring(pos);
        pos = groupAndPath.indexOf("/");
        String group = groupAndPath.substring(0, pos);
        String path = groupAndPath.substring(pos + 1);
        return new PathInfo(group, path);
    }

    public static String getFileType(String filePath) {
        int index = filePath.lastIndexOf(".");
        return filePath.substring(index + 1);
    }

    public static String getFileType(File file) {
        return getFileType(file.getAbsolutePath());
    }

    public boolean isFileType(MultipartFile file) {
        boolean flag = false;
        String fileType = FastDfsUtils.getFileType(Objects.requireNonNull(file.getOriginalFilename()));
        flag = "jpg".equals(fileType) || "jpeg".equals(fileType) || "png".equals(fileType);
        return flag;
    }

    public Map<String, MultipartFile> getMapIsNull(Map<String, MultipartFile> map) {
        Map<String, MultipartFile> remap = new HashMap<String, MultipartFile>();
        for (String item : map.keySet()) {
            if (!map.get(item).isEmpty()) {
                remap.put(item, map.get(item));
            }
        }
        return remap;
    }

    /**
     * 封裝fastDfs文件路徑信息
     */
    class PathInfo {
        private String groupName;
        private String path;

        private PathInfo(String groupName, String path) {
            this.groupName = groupName;
            this.path = path;
        }

        private String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        private String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
