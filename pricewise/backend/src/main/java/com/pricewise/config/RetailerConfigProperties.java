package com.pricewise.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "pricewise")
public class RetailerConfigProperties {

    private Retailers retailers = new Retailers();
    private Alerts alerts = new Alerts();
    private Mail mail = new Mail();

    public static class Retailers {
        private Connector amazon = new Connector();
        private Connector flipkart = new Connector();
        private Sync sync = new Sync();

        public Connector getAmazon() {
            return amazon;
        }

        public void setAmazon(Connector amazon) {
            this.amazon = amazon;
        }

        public Connector getFlipkart() {
            return flipkart;
        }

        public void setFlipkart(Connector flipkart) {
            this.flipkart = flipkart;
        }

        public Sync getSync() {
            return sync;
        }

        public void setSync(Sync sync) {
            this.sync = sync;
        }
    }

    public static class Connector {
        private boolean enabled = false;
        private String apiKey = "";
        private String apiSecret = "";
        private String associateTag = "";
        private String affiliateId = "";
        private String affiliateToken = "";
        private String endpoint = "";
        private int timeoutMs = 5000;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

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

        public String getAssociateTag() {
            return associateTag;
        }

        public void setAssociateTag(String associateTag) {
            this.associateTag = associateTag;
        }

        public String getAffiliateId() {
            return affiliateId;
        }

        public void setAffiliateId(String affiliateId) {
            this.affiliateId = affiliateId;
        }

        public String getAffiliateToken() {
            return affiliateToken;
        }

        public void setAffiliateToken(String affiliateToken) {
            this.affiliateToken = affiliateToken;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public int getTimeoutMs() {
            return timeoutMs;
        }

        public void setTimeoutMs(int timeoutMs) {
            this.timeoutMs = timeoutMs;
        }
    }

    public static class Sync {
        private long scheduledRateMs = 3600000;
        private int freshnessThresholdHours = 12;

        public long getScheduledRateMs() {
            return scheduledRateMs;
        }

        public void setScheduledRateMs(long scheduledRateMs) {
            this.scheduledRateMs = scheduledRateMs;
        }

        public int getFreshnessThresholdHours() {
            return freshnessThresholdHours;
        }

        public void setFreshnessThresholdHours(int freshnessThresholdHours) {
            this.freshnessThresholdHours = freshnessThresholdHours;
        }
    }

    public static class Alerts {
        private long evaluationRateMs = 1800000;

        public long getEvaluationRateMs() {
            return evaluationRateMs;
        }

        public void setEvaluationRateMs(long evaluationRateMs) {
            this.evaluationRateMs = evaluationRateMs;
        }
    }

    public static class Mail {
        private boolean configured = false;
        private String from = "alerts@pricewise.local";

        public boolean isConfigured() {
            return configured;
        }

        public void setConfigured(boolean configured) {
            this.configured = configured;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }

    public Retailers getRetailers() {
        return retailers;
    }

    public void setRetailers(Retailers retailers) {
        this.retailers = retailers;
    }

    public Alerts getAlerts() {
        return alerts;
    }

    public void setAlerts(Alerts alerts) {
        this.alerts = alerts;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail = mail;
    }
}

