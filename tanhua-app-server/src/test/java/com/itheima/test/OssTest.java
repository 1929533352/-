package com.itheima.test;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.tanhua.autoconfig.template.OSSTemplate;
import com.tanhua.server.AppServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppServerApplication.class)
public class OssTest {
    @Autowired
    private OSSTemplate ossTemplate;
    @Test
    public void test() throws FileNotFoundException{
        String path = "C:\\Users\\霄\\Pictures\\Camera Roll\\5.png";
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        String url = ossTemplate.upload(path, fileInputStream);
        System.out.println(url);
    }




    @Test
    public void testOss() throws FileNotFoundException {
        //1.创建图片路径
        String path = "C:\\Users\\霄\\Pictures\\Camera Roll\\1.png";
        //2.拼写图片路径
        String filena = new SimpleDateFormat("yyyy/MM/dd").format(new Date())
                + "/" + UUID.randomUUID().toString() + path.substring(path.lastIndexOf("."));
        //3.构造FileInputStream
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tQBjJNWAZDYcmde8uGj";
        String accessKeySecret = "oXwN6V8v7xPUqhCEa1V6uk193BkArv";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "tanhua193";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "exampledir/exampleobject.txt";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 填写字符串。
            //  String content = "Hello OSS";

            // 创建PutObjectRequest对象。
            //  PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));

            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传字符串。
            ossClient.putObject(bucketName, filena, fileInputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        String url="https://"+bucketName+"."+filena;
        System.out.println(url);
        System.out.println(filena);
    }
}
