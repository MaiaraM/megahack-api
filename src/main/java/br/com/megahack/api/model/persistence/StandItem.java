package br.com.megahack.api.model.persistence;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="stand_items")
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE stand_items SET deleted = true WHERE uuid=?")
@JsonView(JsonViews.CustomerView.class)
@Getter
@Setter
public class StandItem extends Base {

    @NotNull
    @NotBlank(message = "name is mandatory")
    @JsonView(JsonViews.SummaryView.class)
    protected String name;

    @NotNull
    @JoinColumn(name = "companyUUid")
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.REMOVE})
    protected Company company;

    protected String description;

    @JsonView(JsonViews.SummaryView.class)
    protected String shortDescription;



    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
