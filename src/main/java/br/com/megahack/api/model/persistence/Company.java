package br.com.megahack.api.model.persistence;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="companies")
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE companies SET deleted = true WHERE uuid=?")
@JsonView(JsonViews.CustomerView.class)
@Getter
@Setter
public class Company extends Base {

    @NotNull
    @NotBlank(message = "name is mandatory")
    @JsonView(JsonViews.SummaryView.class)
    protected String name;

    protected String phone;

    protected String mobile;


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
