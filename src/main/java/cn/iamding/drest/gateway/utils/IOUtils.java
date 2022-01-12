package cn.iamding.drest.gateway.utils;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * IO相关工具类
 * Created on 2022-01-12
 */
public class IOUtils {

    public static String readStringBodyFromRequest(HttpServletRequest request) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } finally {
            if (null != br) {
                br.close();
            }
        }
        return sb.toString();
    }
}
