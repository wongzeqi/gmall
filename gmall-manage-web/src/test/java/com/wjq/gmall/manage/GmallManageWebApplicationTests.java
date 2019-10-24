package com.wjq.gmall.manage;

import com.wjq.gmall.util.Const;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {



    //图片上传操作
    @Test
    public void contextLoads() throws IOException, MyException {
        //获取配置文件路径
        //配置fdfs的全局连接地址
        String tracker = GmallManageWebApplicationTests.class.getResource("/tracker.conf").getPath();
        ClientGlobal.init(tracker);
        TrackerClient trackerClient = new TrackerClient();
        //获得一个Tracker的一个连接实例
        TrackerServer trackerServer =trackerClient.getConnection();
        //通过tracker获取一个storage 连接的客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);
        String[] infos = storageClient.upload_file("d:/a.png","png",null);
        String address = Const.TRICKER_IP;
        for(String s:infos){
            address+=("/"+s);
        }
        System.out.println(address);

    }

}
