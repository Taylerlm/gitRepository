package com.leyou.lmhitysu.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
    //返回上传的图片的具体地址
    public String uploadFIile(MultipartFile file);
}
