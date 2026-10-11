package com.fpt.sba301.slot18.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "orchid")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Orchid implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orchid_id")
    private Long orchidId;

    @Column(name = "orchid_name", nullable = false)
    private String orchidName;

    @Column(name = "is_natural")
    private Boolean natural;

    @Column(name = "orchid_description")
    private String orchidDescription;

    @Column(name = "is_attractive")
    private Boolean attractive;

    @Column(name = "orchid_url")
    private String orchidUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private OrchidCategory category;

    public Orchid() {
    }

    public Orchid(Long orchidId, String orchidName, Boolean natural, String orchidDescription,
                  Boolean attractive, String orchidUrl, OrchidCategory category) {
        this.orchidId = orchidId;
        this.orchidName = orchidName;
        this.natural = natural;
        this.orchidDescription = orchidDescription;
        this.attractive = attractive;
        this.orchidUrl = orchidUrl;
        this.category = category;
    }

    public Long getOrchidId() {
        return orchidId;
    }

    public void setOrchidId(Long orchidId) {
        this.orchidId = orchidId;
    }

    public String getOrchidName() {
        return orchidName;
    }

    public void setOrchidName(String orchidName) {
        this.orchidName = orchidName;
    }

    public Boolean getNatural() {
        return natural;
    }

    public void setNatural(Boolean natural) {
        this.natural = natural;
    }

    public String getOrchidDescription() {
        return orchidDescription;
    }

    public void setOrchidDescription(String orchidDescription) {
        this.orchidDescription = orchidDescription;
    }

    public Boolean getAttractive() {
        return attractive;
    }

    public void setAttractive(Boolean attractive) {
        this.attractive = attractive;
    }

    public String getOrchidUrl() {
        return orchidUrl;
    }

    public void setOrchidUrl(String orchidUrl) {
        this.orchidUrl = orchidUrl;
    }

    public OrchidCategory getCategory() {
        return category;
    }

    public void setCategory(OrchidCategory category) {
        this.category = category;
    }
}
