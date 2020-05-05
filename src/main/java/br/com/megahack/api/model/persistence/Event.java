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
@Table(name="events")
@Where(clause = "deleted=false")
@SQLDelete(sql = "UPDATE events SET deleted = true WHERE uuid=?")
@JsonView(JsonViews.CustomerView.class)
@Getter
@Setter
@DiscriminatorColumn
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = Event.class)
public class Event extends Base {

    @NotNull
    @NotBlank(message = "skuCode is mandatory")
    @JsonView(JsonViews.SummaryView.class)
    protected String skuCode;

    @NotNull
    @NotBlank(message = "name is mandatory")
    @JsonView(JsonViews.SummaryView.class)
    protected String name;

    @NotNull
    @NotBlank(message = "shortName is mandatory")
    @JsonView(JsonViews.SummaryView.class)
    protected String shortName;

    protected String description;

    @JsonView(JsonViews.SummaryView.class)
    protected String shortDescription;

    protected String metaTitle;

    protected String metaKeywords;

    protected String metaDescription;

    @Column(unique = true)
    @JsonView(JsonViews.SummaryView.class)
    protected String slug;

    @JsonView(JsonViews.SummaryView.class)
    protected Double fakePrice = 0.0;

    @OneToMany(mappedBy = "event")
    @Cascade({ALL})
    @JsonView(JsonViews.ManagerView.class)
    @JsonIgnoreProperties("event")
    @JsonManagedReference
    protected List<Price> prices;

    // There should be only one of absolute or percentual discount set at a given time
    protected Double absoluteDiscount = 0.0;

    protected Double percentualDiscount = 0.0;

    protected Date discountBeginDate;

    protected Date discountEndDate;

    protected Date releaseDate;


    @ManyToMany
    @JoinTable(name = "user_event", joinColumns = {@JoinColumn(name = "eventUUid", insertable = false, updatable = false)}, inverseJoinColumns = {@JoinColumn(name = "userUUid")})
    protected List<User> users;

    @OneToMany
    @Cascade({ALL})
    @JsonView(JsonViews.CustomerView.class)
    protected List<Stand> stands;


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
