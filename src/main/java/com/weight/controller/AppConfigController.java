// src/main/java/com/weight/controller/AppConfigController.java
package com.weight.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weight.services.AppConfigService;

@RestController
@RequestMapping("/config")
//@CrossOrigin(origins = "*")
public class AppConfigController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private AppConfigService appConfigService;
    
    /**
     * Récupère toutes les configurations
     * @return Map des configurations
     */
    @GetMapping("/params")
    public ResponseEntity<?> getParams() {
        logger.info("Récupération de la configuration actuelle");
        
        try {
            Map<String, Object> configs = appConfigService.getAllConfigs();
            return new ResponseEntity<>(configs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des configurations: {}", e.getMessage());
            return new ResponseEntity<>("Erreur lors de la récupération des configurations", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Met à jour les configurations
     * @param configs Map des configurations à mettre à jour
     * @return Map des configurations mises à jour
     */
    @PutMapping("/params")
    public ResponseEntity<?> updateParams(@RequestBody Map<String, Object> configs) {
        logger.info("Mise à jour de la configuration: {}", configs);
        
        try {
            // Mettre à jour chaque configuration
            for (Map.Entry<String, Object> entry : configs.entrySet()) {
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                appConfigService.updateConfig(key, value);
            }
            
            // Récupérer toutes les configurations mises à jour
            Map<String, Object> updatedConfigs = appConfigService.getAllConfigs();
            return new ResponseEntity<>(updatedConfigs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour des configurations: {}", e.getMessage());
            return new ResponseEntity<>("Erreur lors de la mise à jour des configurations", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}