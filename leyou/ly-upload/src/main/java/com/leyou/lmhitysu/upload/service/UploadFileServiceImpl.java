package com.leyou.lmhitysu.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.lmhitysu.common.Exception.LyException;
import com.leyou.lmhitysu.upload.config.UploadFieConfiguration;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service("uploadFileService")
public class UploadFileServiceImpl implements IUploadFileService {


    private static final Logger logger = LoggerFactory.getLogger(UploadFileServiceImpl.class);
    @Autowired
    FastFileStorageClient storageClient;
    @Autowired
    private UploadFieConfiguration uploadFieConfiguration;
    private static List<String> fileTypes = new ArrayList<String>();
    static {
        fileTypes.add("image/png");
        fileTypes.add("image/jpeg");
        fileTypes.add("image/jpg");
    }
    @Override
    public String uploadFIile(MultipartFile file) {
        try{
            //校验文件格式　文件格式可以放到一个配置类中
            if(!fileTypes.contains(file.getContentType())){
                logger.error("不支持的文件类型");
                throw new LyException("UP_FILE_ERROR不支持的文件类型，请检查文件类型后重试");
            }
            //校验文件内容
            String fileContent = converFileContent(file.getInputStream());

            if(fileContent.length()<0){
                logger.error("文件内容不能为空");
                throw new LyException("UP_FILE_ERROR文件内容为空");
            }
            //保存图片
            File dir = new File("/home/liming/Pictures");
            if(dir == null){
                dir.mkdirs();
            }
            //保存图片
            //file.transferTo(new File(dir,file.getOriginalFilename()));
            //获取图片后缀名
            String extension = file.getOriginalFilename().split("\\.")[1];
            //将图片保存到ｆａｓｔＤＦＳ服务器
            StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(),extension,null);
            //返回文件地址
            String url ="http://"+ uploadFieConfiguration.getFastdfsUrl()+"/" + storePath.getFullPath();
            return url;
        }catch (IOException e){
            logger.error(e.getMessage());
            return null;
        }

    }
    private String converFileContent(InputStream stream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = null;
        try{
            while((line = bufferedReader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
        }catch (IOException e){
            throw new LyException("IO流异常");
        }finally {
            try{
                stream.close();
            }catch (IOException e) {
                throw  new LyException("IO流关闭异常");
            }
        }
        return builder.toString();

    }
}
