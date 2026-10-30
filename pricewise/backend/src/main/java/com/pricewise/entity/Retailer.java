package com.pricewise.entity;

import com.pricewise.enums.IntegrationType;
import jakarta.persistence.*;

@Entity
@Table(name = "retailers")
public class Retailer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "website_url", nullable = false, length = 255)
    private String websiteUrl;

    @Column(name = "logo_url", length = 255)
    private String logoUrl;

    @Column(nullable = false)
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "integration_type", nullable = false, length = 30)
    private IntegrationType integrationType = IntegrationType.MOCK;

    public Retailer() {
    }

    public Retailer(Long id, String name, String code, String websiteUrl, String logoUrl, Boolean active, IntegrationType integrationType) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.websiteUrl = websiteUrl;
        this.logoUrl = logoUrl;
        this.active = active != null ? active : true;
        this.integrationType = integrationType != null ? integrationType : IntegrationType.MOCK;
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

