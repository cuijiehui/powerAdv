package com.coresun.powerbank.utils;

import android.os.Message;

import com.coresun.powerbank.common.Constants;
import com.coresun.powerbank.common.MyApplication;
import com.coresun.powerbank.entity.AdvTxt;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import okhttp3.ResponseBody;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author Android
 * @date 2018/7/13
 * @details
 */

public class FileUtils {
//    caChePath = MyApplication.getInstance.getExternalCacheDir() + File.separator + "adv";
    public static void dowFile(String url,String cachePath,String fileName){
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(cachePath, fileName) {
                    @Override
                    public void inProgress(float progress) {
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        Logger.e("接口测试 dowAdvFile下载失败！\n" + e.toString());

                    }

                    @Override
                    public void onResponse(File response) {
                        Logger.d("接口测试 dowAdvFile下载完成！" + response.getAbsolutePath());

                    }
                });
    }
    public static String getVideoPath(){
        return MyApplication.getContext().getExternalCacheDir() + File.separator + "adv" + File.separator;
    }
    public static boolean weiteFile(ResponseBody body,String path){
        Logger.e( "WriteFileManager.startToWrite.path:" + path);
        File futureFile = new File(path);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (futureFile.exists()) {
            futureFile.delete();
        }
        try {
            long fileSize = body.contentLength();
            Logger.d("WriteFileManager.writeResponseBodyToDisk.fileSize:"+fileSize);
            try {
                byte[] fileReader = new byte[1024 * 1024];
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
//                    LogUtil.i(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                Logger.d("file download: " + fileSizeDownloaded + " of " + fileSize);
                outputStream.flush();
                return true;
            } catch (IOException e) {
                Logger.e( "WriteFileManager.startToWrite.path:" + path);

                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Logger.e( "WriteFileManager.startToWrite.path:" + path);

            return false;
        }
    }
    public static AdvTxt writeResponseBodyToDisk(ResponseBody body, AdvTxt advTxt) {
        String path =getVideoPath() + advTxt.getFilename();
        Logger.i( "WriteFileManager.startToWrite.path:" + path);
        File futureFile = new File(path);
        if(futureFile.exists()){
            advTxt.setPath(path);
            return advTxt;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            long fileSize = body.contentLength();
            Logger.d("WriteFileManager.writeResponseBodyToDisk.fileSize:"+fileSize);
            try {
                byte[] fileReader = new byte[1024 * 1024];
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
//                    LogUtil.i(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                Logger.d("file download: " + fileSizeDownloaded + " of " + fileSize);
                outputStream.flush();
                advTxt.setPath(path);
                return advTxt;
            } catch (IOException e) {
                e.printStackTrace();
                return advTxt;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return advTxt;
        }

    }
    public static void deleteAdvFile(String oldPath, String newPath) {
        if (!oldPath.equals(newPath)) {
            File oldFile = new File( oldPath);
            File newFile = new File(  newPath);
            if (!oldFile.exists()) {
                return;
            }
            ArrayList<AdvTxt> oldAdvTxts = FileUtils.refreshData(oldFile);
            ArrayList<AdvTxt> newAdvTxts = FileUtils.refreshData(newFile);
            for (AdvTxt oldAdvTxt : oldAdvTxts) {
                for (AdvTxt newAdvTxt : newAdvTxts) {
                    if (!oldAdvTxt.getFilename().equals(newAdvTxt.getFilename())) {
                        deleteFile(oldAdvTxt.getFilename());
                    }
                }
            }
        }
    }
    /**
     * 删除文件
     *
     * @param path 文件根目录
     */
    public static void deleteFile(String path) {
        Logger.i("删除文件" + path);
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                deleteAllFiles(file);
            } else {
                file.delete();
            }
        }
    }
    /**
     * 删除文件夹里面的全部文件
     *
     * @param root 文件夹
     */
    public static void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
    /**
     * 获取文件名字
     *
     * @param pathandname 路径
     * @return
     */
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, pathandname.length());
        } else {
            return null;
        }
    }
    /**
     * 将文件里面的json解析成bean
     *
     * @param file 需要解析的文件
     * @return
     */
    public static ArrayList<AdvTxt> refreshData(File file) {
        ArrayList<AdvTxt> advTxts = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);

            String res = EncodingUtils.getString(bytes, "UTF-8");
            Gson gson = new Gson();
            AdvTxt[] ress = gson.fromJson(res, AdvTxt[].class);
            for (AdvTxt re : ress) {
                advTxts.add(re);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return advTxts;

    }

    /**
     * 文件大小是否相等
     * @param file
     * @param size
     * @return
     */
    public static boolean fileIsEquals(File file, float size) {
        String fileSize = "";
        fileSize = formatFileSize(file.length());
        float fSize = new Float(fileSize);
        float qSize = size - fSize;
        return qSize < 1;
    }
    /**
     * 将文件大小转换成字节
     */

    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS);
        } else {
            fileSizeString = df.format((double) fileS / 1024);
        }
        return fileSizeString;

    }
}
