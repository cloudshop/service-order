package com.eyun.order.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the OwnerRelation entity.
 */
public class OwnerRelationDTO implements Serializable {

    private Long id;

    private String roleName;

    private String description;

    private Instant createdTime;

    private Instant updatedTime;

    private Long userAnnexId;

    private Long mercuryId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUserAnnexId() {
        return userAnnexId;
    }

    public void setUserAnnexId(Long userAnnexId) {
        this.userAnnexId = userAnnexId;
    }

    public Long getMercuryId() {
        return mercuryId;
    }

    public void setMercuryId(Long mercuryId) {
        this.mercuryId = mercuryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OwnerRelationDTO ownerRelationDTO = (OwnerRelationDTO) o;
        if(ownerRelationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ownerRelationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OwnerRelationDTO{" +
            "id=" + getId() +
            ", roleName='" + getRoleName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            "}";
    }
}
