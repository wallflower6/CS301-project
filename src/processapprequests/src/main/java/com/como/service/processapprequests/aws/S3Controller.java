package com.como.service.processapprequests.aws;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.s3.model.S3Object;
import com.como.service.processapprequests.JSONService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class S3Controller {
    @Autowired
    private S3Service s3Service;

    @Autowired
    private JSONService jsonService;
    
    @PostMapping("/cache")
    public void saveStringToCache(@RequestBody Map<String, String> data) {
        s3Service.saveString("data", data.toString());
    }

    @RequestMapping("/get-cache")
    public List<String> getCache() {
        return s3Service.getAllStoredKeys();
    }

    // This works, now modify to return values that are needed
    @PostMapping("/key-cache")
    public String getCacheByKey(@RequestBody String key) {
        Map<String, Object> map = jsonService.convertStringToMap(s3Service.findData(key));
        return map.get("key1").toString();
    }
}
