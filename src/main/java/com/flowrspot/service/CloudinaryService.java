package com.flowrspot.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.flowrspot.config.ApplicationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private Cloudinary cloudinary;

    private final ApplicationProperties applicationProperties;

    public CloudinaryService(ApplicationProperties applicationProperties){
        this.applicationProperties = applicationProperties;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", applicationProperties.getCloudinary().getName(),
            "api_key", applicationProperties.getCloudinary().getApiKey(),
            "api_secret", applicationProperties.getCloudinary().getApiSecret()));
    }

    public String upload(File file, String fileName){
        Map params = ObjectUtils.asMap("public_id", fileName);
        try {
            Map uploadResult = cloudinary.uploader().upload(file, params);
            return uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String uploadFlowerPicture(File file, String fileName) {
        return upload(file, applicationProperties.getCloudinary().getFlowerFolder() + fileName);
    }

    public String uploadSightingPicture(File file, String fileName) {
        return upload(file, applicationProperties.getCloudinary().getSightingFolder() + fileName);
    }
}
