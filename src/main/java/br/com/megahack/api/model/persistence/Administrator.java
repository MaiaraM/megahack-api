package br.com.megahack.api.model.persistence;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="administrators")
@Where(clause = "deleted=false")
@Setter
@Getter
@SQLDelete(sql = "UPDATE administrators SET deleted = true WHERE uuid=?")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = Administrator.class)
public class Administrator extends Base {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.ManagerView.class)
    private Long idAdministrator;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userUuid")
    @NotNull
    @Valid
    protected User user;

    @NotNull
    @NotBlank(message = "name is mandatory")
    protected String name;

    public Administrator(@NotNull User user, @NotNull String name) {
        this.user = user;
        this.name = name;
    }

    public Administrator() {

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
