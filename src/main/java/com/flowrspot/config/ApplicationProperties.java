package com.flowrspot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Flowr Spot.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Cloudinary cloudinary = new Cloudinary();

    public Cloudinary getCloudinary() {
        return cloudinary;
    }

    public static class Cloudinary {
        private String apiKey;

        private String apiSecret;

        private String name;

        private String flowerFolder;

        private String sightingFolder;

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiSecret() {
            return apiSecret;
        }

        public void setApiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getFlowerFolder() {
            return flowerFolder;
        }

        public void setFlowerFolder(String flowerFolder) {
            this.flowerFolder = flowerFolder;
        }

        public String getSightingFolder() {
            return sightingFolder;
        }

        public void setSightingFolder(String sightingFolder) {
            this.sightingFolder = sightingFolder;
        }
    }
}
