package com.wjq.util;


import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;



public class PmsUploadUtil {

    public static String uploadImage(MultipartFile multipartFile) {

        String imgUrl = Const.TRICKER_IP;
        String[] infos = null;
        try {
            //文件上传
            //获取配置文件路径
            //配置fdfs的全局连接地址
            String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
            ClientGlobal.init(tracker);
            TrackerClient trackerClient = new TrackerClient();
            //获得一个Tracker的一个连接实例
            TrackerServer trackerServer = trackerClient.getConnection();
            //通过tracker获取一个storage 连接的客户端
            StorageClient storageClient = new StorageClient(trackerServer, null);
            //获取文件的扩展名
            String filename = multipartFile.getOriginalFilename();//获取文件的全名 eg: a.jpg
            int i = filename.lastIndexOf(".");
            String extName = filename.substring(i+1);

            infos = storageClient.upload_file("d:/a.png",extName, null);
        }catch (Exception e){
            e.printStackTrace();
        }


        for (String s : infos) {
            imgUrl += ("/" + s);
        }


        return imgUrl;
    }
}
