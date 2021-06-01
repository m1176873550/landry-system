package com.ming.ls.modules.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ImageUtil {
    /**
     * 获取图片
     *
     * @param path
     * @param response
     * @throws IOException
     */
    public void image(String path, HttpServletResponse response) throws IOException {
        if (StringUtils.isNotEmpty(path)){
            //读取路径下面的文件
            File file = new File(path);
            if (file.exists()) {
                String ext = file.getName().substring(file.getName().indexOf("."));
                //判断图片格式,设置相应的输出文件格式
                if (ext.equals("jpg")) {
                    response.setContentType("image/jpeg");
                } else if (ext.equals("JPG")) {
                    response.setContentType("image/jpeg");
                } else if (ext.equals("png")) {
                    response.setContentType("image/png");
                } else if (ext.equals("PNG")) {
                    response.setContentType("image/png");
                }
            } else {
                return;
            }
            //读取指定路径下面的文件
            InputStream in = new FileInputStream(file);
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            //创建存放文件内容的数组
            byte[] buff = new byte[1024];
            //所读取的内容使用n来接收
            int n;
            //当没有读取完时,继续读取,循环
            while ((n = in.read(buff)) != -1) {
                //将字节数组的数据全部写入到输出流中
                outputStream.write(buff, 0, n);
            }
            //强制将缓存区的数据进行输出
            outputStream.flush();
            //关流
            outputStream.close();
            in.close();
        }
    }
}
