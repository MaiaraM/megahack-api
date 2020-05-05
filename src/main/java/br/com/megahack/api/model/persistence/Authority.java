package br.com.megahack.api.model.persistence;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="authorities")
@Where(clause = "deleted=false")
@Getter
@Setter
@SQLDelete(sql = "UPDATE authorities SET deleted = true WHERE uuid=?")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = Authority.class)
public class Authority extends Base {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.ManagerView.class)
    private Long idAuthority;

    @NotNull
    protected String name;


    public Authority() {
    }

    public Authority(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
