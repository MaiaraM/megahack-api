package br.com.megahack.api.model.persistence;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="files")
@SQLDelete(sql = "UPDATE files SET deleted = true WHERE uuid=?")
@Where(clause = "deleted=false")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = File.class)
@Getter
@Setter
public class File extends Base {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.ManagerView.class)
    private Long idFile;

    @NotNull
    protected String name;

    @NotNull
    @ElementCollection
    protected Set<String> metaTags;

    @NotNull
    protected Long size;

    @NotNull
    protected String path;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
