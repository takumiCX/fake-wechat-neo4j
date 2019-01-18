package com.zju.fakewechat.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author: takumiCX
 * @create: 2019-01-18
 * @email: takumicx@zju.edu.cn
 **/

@Service
@Slf4j
public class ImageService {


    public String handle(MultipartFile file) {

        //校验文件是否为空
        checkFile(file);

        File tempDir = new File("images");

        if(!tempDir.exists()){
            tempDir.mkdirs();
        }

        String tempFileName=UUID.randomUUID().toString()+file.getOriginalFilename();
        String filePath = tempDir.getPath() + File.separator + tempFileName;
        try {
            file.transferTo(new File(System.getProperty("user.dir"),filePath));
            log.info("{} transfer to {}",file.getOriginalFilename(),filePath);

        } catch (IOException e) {
            log.error("Transfer file to {} error!",filePath,e);
            throw new RuntimeException(e);
        }

        return tempFileName;

    }


    private void checkFile(MultipartFile file){

        if (file == null || file.getSize() == 0L) {

            throw new RuntimeException("Image is Null Or Empty!");

        }

    }

}
