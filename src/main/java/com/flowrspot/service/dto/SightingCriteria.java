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
 * Criteria class for the Sighting entity. This class is used in SightingResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sightings?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SightingCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private DoubleFilter longitude;

    private DoubleFilter latitude;

    private StringFilter image;

    private LongFilter likesId;

    private LongFilter flowerId;

    public SightingCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public StringFilter getImage() {
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public LongFilter getLikesId() {
        return likesId;
    }

    public void setLikesId(LongFilter likesId) {
        this.likesId = likesId;
    }

    public LongFilter getFlowerId() {
        return flowerId;
    }

    public void setFlowerId(LongFilter flowerId) {
        this.flowerId = flowerId;
    }

    @Override
    public String toString() {
        return "SightingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (longitude != null ? "longitude=" + longitude + ", " : "") +
                (latitude != null ? "latitude=" + latitude + ", " : "") +
                (image != null ? "image=" + image + ", " : "") +
                (likesId != null ? "likesId=" + likesId + ", " : "") +
                (flowerId != null ? "flowerId=" + flowerId + ", " : "") +
            "}";
    }

}
