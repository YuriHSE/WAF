package org.pets.demo.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.math3.random.RandomDataGenerator;
import smile.anomaly.IsolationForest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TrafficAnomalyDetectionService {
    private static final Logger logger = LoggerFactory.getLogger(TrafficAnomalyDetectionService.class);
    private IsolationForest model;

    @PostConstruct
    public void trainModel() {
        logger.info("Training Isolation Forest model...");

        double[][] normalTraffic = generateNormalData(1000);
        double[][] anomalyTraffic = generateAnomalies(50);


        double[][] trainingData = new double[normalTraffic.length + anomalyTraffic.length][];
        System.arraycopy(normalTraffic, 0, trainingData, 0, normalTraffic.length);
        System.arraycopy(anomalyTraffic, 0, trainingData, normalTraffic.length, anomalyTraffic.length);

        IsolationForest.Options options = new IsolationForest.Options(100, 10, 0.7, 0);

        model = IsolationForest.fit(trainingData, options);
        logger.info("✅ Model training complete");
    }

    public boolean isAnomalous(String clientIp, String requestPath) {
        int ipHash = Math.abs(clientIp.hashCode() % 10000);
        int pathHash = Math.abs(requestPath.hashCode() % 10000);

        double[] features = { ipHash, pathHash };
        double score = model.score(features);

        boolean anomaly = score < -0.5;
        logger.info("Checked request [{} -> {}]: Score = {}, Anomalous = {}", clientIp, requestPath, score, anomaly);
        return anomaly;
    }

    private double[][] generateNormalData(int count) {
        RandomDataGenerator generator = new RandomDataGenerator();
        double[][] data = new double[count][2];

        for (int i = 0; i < count; i++) {
            data[i][0] = generator.nextUniform(0, 10000);
            data[i][1] = generator.nextUniform(0, 10000);
        }
        return data;
    }

    private double[][] generateAnomalies(int count) {
        Random random = new Random();
        double[][] data = new double[count][2];

        for (int i = 0; i < count; i++) {
            data[i][0] = random.nextDouble() * 50000 - 25000; // Аномальный диапазон
            data[i][1] = random.nextDouble() * 50000 - 25000;
        }
        return data;
    }
}