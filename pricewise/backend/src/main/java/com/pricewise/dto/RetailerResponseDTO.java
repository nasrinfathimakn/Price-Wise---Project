package com.pricewise.dto;

import com.pricewise.enums.IntegrationType;

public class RetailerResponseDTO {
    private Long id;
    private String name;
    private String code;
    private String websiteUrl;
    private String logoUrl;
    private Boolean active;
    private IntegrationType integrationType;

    public RetailerResponseDTO() {
    }

    public RetailerResponseDTO(Long id, String name, String code, String websiteUrl, String logoUrl, Boolean active, IntegrationType integrationType) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.websiteUrl = websiteUrl;
        this.logoUrl = logoUrl;
        this.active = active;
        this.integrationType = integrationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public IntegrationType getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(IntegrationType integrationType) {
        this.integrationType = integrationType;
    }
}

