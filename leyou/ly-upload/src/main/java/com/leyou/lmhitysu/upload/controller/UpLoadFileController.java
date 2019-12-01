package com.leyou.lmhitysu.upload.controller;

import com.leyou.lmhitysu.upload.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "upload")
public class UpLoadFileController {

    @Autowired
    private IUploadFileService uploadFileService;

    @RequestMapping(value = "image")
    public ResponseEntity<String> image(@RequestParam("file") MultipartFile file){
        String filePath = this.uploadFileService.uploadFIile(file);
        Map result = new HashMap();
        if(null == filePath){

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }else{
            // 返回200，并且携带url路径
            return ResponseEntity.ok(filePath);
        }
    }
}
