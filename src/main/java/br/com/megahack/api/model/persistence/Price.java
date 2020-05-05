package br.com.megahack.api.model.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "prices")
@Where(clause = "deleted=false")
@JsonView(JsonViews.CustomerView.class)
@Getter
@Setter
@DiscriminatorColumn
@SQLDelete(sql = "UPDATE prices SET deleted = true WHERE uuid=?")
public class Price extends Base {

    @NotNull
    protected Double price = 0.0;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "customerTypeUuid")
    @JsonView(JsonViews.ManagerView.class)
    protected CustomerType customerType ;

    @ManyToOne
    @JoinColumn(name = "eventUuid")
    @JsonBackReference
    protected Event event;


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
