package com.flowrspot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sighting.
 */
@Entity
@Table(name = "sighting")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sighting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "image")
    private String image;

    @ManyToOne
    private Flower flower;
	
	@ManyToOne
    private User user;

    @Lob
    @Column(name = "quote")
    private String quote;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Sighting longitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Sighting latitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public Sighting image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Flower getFlower() {
        return flower;
    }

    public Sighting flower(Flower flower) {
        this.flower = flower;
        return this;
    }

    public void setFlower(Flower flower) {
        this.flower = flower;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sighting sighting = (Sighting) o;
        if (sighting.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sighting.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sighting{" +
            "id=" + getId() +
            ", longitude=" + getLongitude() +
            ", latitude=" + getLatitude() +
            ", image='" + getImage() + "'" +
            "}";
    }


}
