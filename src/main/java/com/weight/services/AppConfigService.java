// src/main/java/com/weight/services/AppConfigService.java
package com.weight.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weight.model.AppConfig;
import com.weight.repository.AppConfigRepository;

@Service
public class AppConfigService {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private AppConfigRepository appConfigRepository;
    
    // Valeurs par défaut au cas où les configurations ne sont pas en base de données
    private static final Map<String, String> DEFAULT_CONFIGS = new HashMap<>();
    static {
        DEFAULT_CONFIGS.put("serviceFeesPercentage", "10");
        DEFAULT_CONFIGS.put("maxPackageWeight", "30");
        DEFAULT_CONFIGS.put("maxPackageDimensions", "150");
    }
    
    /**
     * Récupère toutes les configurations
     * @return Map avec les clés et valeurs de configuration
     */
    public Map<String, Object> getAllConfigs() {
        logger.info("Récupération de toutes les configurations");
        List<AppConfig> configs = appConfigRepository.findAll();
        
        // Si aucune config n'existe, initialiser les valeurs par défaut
        if (configs.isEmpty()) {
            logger.info("Aucune configuration trouvée, initialisation des valeurs par défaut");
            initializeDefaultConfigs();
            configs = appConfigRepository.findAll();
        }
        
        Map<String, Object> configMap = new HashMap<>();
        
        for (AppConfig config : configs) {
            // Convertir en int ou double si applicable
            String value = config.getParamValue();
            try {
                if (value.contains(".")) {
                    configMap.put(config.getParamKey(), Double.parseDouble(value));
                } else {
                    configMap.put(config.getParamKey(), Integer.parseInt(value));
                }
            } catch (NumberFormatException e) {
                configMap.put(config.getParamKey(), value);
            }
        }
        
        return configMap;
    }
    
    /**
     * Récupère une configuration spécifique
     * @param key Clé de la configuration
     * @return Valeur de la configuration (string, peut être convertie par l'appelant)
     */
    public String getConfig(String key) {
        logger.info("Récupération de la configuration: {}", key);
        
        Optional<AppConfig> config = appConfigRepository.findByParamKey(key);
        
        if (config.isPresent()) {
            return config.get().getParamValue();
        } else if (DEFAULT_CONFIGS.containsKey(key)) {
            // Si la config n'existe pas mais qu'une valeur par défaut est disponible
            AppConfig newConfig = createConfig(key, DEFAULT_CONFIGS.get(key), 
                    "Configuration par défaut pour " + key);
            return newConfig.getParamValue();
        }
        
        return null;
    }
    
    /**
     * Récupère la valeur de configuration en tant qu'entier
     * @param key Clé de la configuration
     * @param defaultValue Valeur par défaut si la config n'existe pas
     * @return Valeur entière
     */
    public int getConfigAsInt(String key, int defaultValue) {
        String value = getConfig(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("Erreur lors de la conversion de {} en entier", key);
            }
        }
        return defaultValue;
    }
    
    /**
     * Récupère la valeur de configuration en tant que double
     * @param key Clé de la configuration
     * @param defaultValue Valeur par défaut si la config n'existe pas
     * @return Valeur double
     */
    public double getConfigAsDouble(String key, double defaultValue) {
        String value = getConfig(key);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                logger.error("Erreur lors de la conversion de {} en double", key);
            }
        }
        return defaultValue;
    }
    
    /**
     * Met à jour une configuration
     * @param key Clé de la configuration
     * @param value Nouvelle valeur
     * @return AppConfig mise à jour
     */
    public AppConfig updateConfig(String key, String value) {
        logger.info("Mise à jour de la configuration: {} = {}", key, value);
        
        Optional<AppConfig> existingConfig = appConfigRepository.findByParamKey(key);
        
        if (existingConfig.isPresent()) {
            AppConfig config = existingConfig.get();
            config.setParamValue(value);
            config.setUpdatedAt(new Date());
            return appConfigRepository.save(config);
        } else {
            return createConfig(key, value, "Configuration ajoutée le " + new Date());
        }
    }
    
    /**
     * Crée une nouvelle configuration
     * @param key Clé de la configuration
     * @param value Valeur
     * @param description Description
     * @return Nouvelle AppConfig
     */
    public AppConfig createConfig(String key, String value, String description) {
        logger.info("Création d'une nouvelle configuration: {} = {}", key, value);
        
        AppConfig config = new AppConfig();
        config.setParamKey(key);
        config.setParamValue(value);
        config.setDescription(description);
        config.setCreatedAt(new Date());
        config.setUpdatedAt(new Date());
        
        return appConfigRepository.save(config);
    }
    
    /**
     * Initialise les configurations par défaut
     */
    public void initializeDefaultConfigs() {
        logger.info("Initialisation des configurations par défaut");
        
        for (Map.Entry<String, String> entry : DEFAULT_CONFIGS.entrySet()) {
            createConfig(entry.getKey(), entry.getValue(), 
                    "Configuration par défaut pour " + entry.getKey());
        }
    }
    
    /**
     * Calcule les frais de service basés sur un montant
     * @param amount Montant de base
     * @return Montant des frais de service
     */
    public double calculateServiceFees(double amount) {
        double percentage = getConfigAsDouble("serviceFeesPercentage", 10.0);
        return (amount * percentage) / 100.0;
    }
    
    /**
     * Calcule le montant total incluant les frais de service
     * @param amount Montant de base
     * @return Montant total avec frais
     */
    public double calculateTotalAmount(double amount) {
        return amount + calculateServiceFees(amount);
    }
}
