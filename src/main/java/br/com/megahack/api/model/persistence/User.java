package br.com.megahack.api.model.persistence;

import br.com.megahack.api.model.enums.UserRoles;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE uuid=?")
@Getter
@Setter
public class User extends Base{

    @Column(columnDefinition = "serial", updatable = false)
    @Generated(GenerationTime.INSERT)
    @JsonView(JsonViews.ManagerView.class)
    private Long idUser;

    @NotNull
    @NotBlank(message = "username is mandatory")
    protected String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "password is mandatory")
    protected String password;

    @NotNull
    @NotBlank(message = "email is mandatory")
    @Email
    protected String email;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_authorities",
            joinColumns = {@JoinColumn(name = "userUuid")},
            inverseJoinColumns = {@JoinColumn(name="authorityUuid")})
    protected Set<Authority> authorities = new HashSet<>();

    @Enumerated(EnumType.STRING)
    protected UserRoles role = UserRoles.CUSTOMER;


    public User() {
        this.username = "";
        this.password = "";
        this.email = "";
        this.authorities = new HashSet<>();
    }

    public User(@NotNull String username, @NotNull String password, @NotNull String email) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public void addAuthority(Authority a){
        authorities.add(a);
    }

    public void removeAuthority(Authority a){
        // Set the new authority set to keep only those authorities which
        // don't have the same name as the given one
        authorities = authorities.stream().filter(authority ->
                !authority.getName().equals(a.getName())
        ).collect(Collectors.toSet());
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
