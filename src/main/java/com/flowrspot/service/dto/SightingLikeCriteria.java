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
 * Criteria class for the SightingLike entity. This class is used in SightingLikeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sighting-likes?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SightingLikeCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter sightingId;

    public SightingLikeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getSightingId() {
        return sightingId;
    }

    public void setSightingId(LongFilter sightingId) {
        this.sightingId = sightingId;
    }

    @Override
    public String toString() {
        return "SightingLikeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sightingId != null ? "sightingId=" + sightingId + ", " : "") +
            "}";
    }

}
