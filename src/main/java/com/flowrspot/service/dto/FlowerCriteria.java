package com.flowrspot.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Flower entity. This class is used in FlowerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /flowers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FlowerCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter image;

    private LongFilter sightingsId;

    public FlowerCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getImage() {
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public LongFilter getSightingsId() {
        return sightingsId;
    }

    public void setSightingsId(LongFilter sightingsId) {
        this.sightingsId = sightingsId;
    }

    @Override
    public String toString() {
        return "FlowerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (image != null ? "image=" + image + ", " : "") +
                (sightingsId != null ? "sightingsId=" + sightingsId + ", " : "") +
            "}";
    }

}
