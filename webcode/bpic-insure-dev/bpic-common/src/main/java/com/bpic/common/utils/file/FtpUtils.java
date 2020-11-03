package com.bpic.common.utils.file;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;



import java.io.*;
import java.util.Properties;

public class FtpUtils {
    //ftp服务器地址
    public static String hostname = "10.1.18.59";
    //ftp服务器端口号默认为21
    public static Integer port = 22;
    //ftp登录账号
    public static String username = "root";
    //ftp登录密码
    public static String password = "password";

    //public FTPClient ftpClient = null;
    /**
     * FTPClient对象
     **/
    private static ChannelSftp ftpClient = null;
    /**
     *
     */
    private static Session sshSession = null;

    /**
     * 初始化ftp服务器
     */
    public static void initFtpClient() {
        try {
            JSch jsch = new JSch();
            // 获取sshSession
            sshSession = jsch.getSession(username, hostname, port);
            // 添加s密码
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            // 开启sshSession链接
            sshSession.connect();
            // 获取sftp通道
            ftpClient = (ChannelSftp) sshSession.openChannel("sftp");
            // 开启
            ftpClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传
     * @param fis 上传文件路径
     * @param ftp_path	服务器保存路径
     * @param newFileName	新文件名
     * @throws Exception
     */
    public static void uploadFile(InputStream fis, String ftp_path, String newFileName) throws Exception {
        try {
            initFtpClient();
            try {
                ftpClient.cd(ftp_path);
            }catch (Exception e){
                ftpClient.mkdir(ftp_path);
                ftpClient.cd(ftp_path);
            }
            ftpClient.put(fis, newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Upload file error.");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Exception("close stream error.");
                }
            }
        }
    }

    /**
     * 关闭
     *
     * @throws Exception
     */
    public static void close() throws Exception {
        try {
            ftpClient.disconnect();
            sshSession.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new Exception("close stream error.");
        }
    }
}
