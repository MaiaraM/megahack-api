package br.com.megahack.api.model.persistence;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Base {

    @NotNull
    @JsonView(JsonViews.SummaryView.class)
    @Id
    protected String uuid = UUID.randomUUID().toString();

    //ERP or external manager identifier
    @JsonView(JsonViews.ManagerView.class)
    protected String externalId;

    @CreatedDate
    @Column(updatable = false)
    @JsonView(JsonViews.ManagerView.class)
    protected Date created;

    @LastModifiedDate
    @JsonView(JsonViews.ManagerView.class)
    protected Date modified;

    @NotNull
    @JsonView(JsonViews.ManagerView.class)
    protected Boolean active = true;

    @NotNull
    @JsonView(JsonViews.ManagerView.class)
    protected Boolean deleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Base base = (Base) o;
        return Objects.equals(uuid, base.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
