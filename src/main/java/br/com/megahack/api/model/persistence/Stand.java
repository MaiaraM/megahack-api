package br.com.megahack.api.model.persistence;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static org.hibernate.annotations.CascadeType.ALL;

@Entity
@Table(name="stands")
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE stands SET deleted = true WHERE uuid=?")
@JsonView(JsonViews.CustomerView.class)
@Getter
@Setter
public class Stand extends Base {

    @NotNull
    @NotBlank(message = "name is mandatory")
    @JsonView(JsonViews.SummaryView.class)
    protected String name;

    @NotNull
    @JoinColumn(name = "companyUUid")
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST , CascadeType.REMOVE})
    protected StandItem standItem;


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
