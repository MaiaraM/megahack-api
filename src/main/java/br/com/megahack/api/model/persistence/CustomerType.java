package br.com.megahack.api.model.persistence;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="customer_types")
@Where(clause = "deleted=false")
@Getter
@Setter
@JsonView(JsonViews.SummaryView.class)
@SQLDelete(sql = "UPDATE customer_types SET deleted = true WHERE uuid=?")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class", defaultImpl = CustomerType.class)
public class CustomerType extends Base {

    protected String name;

}
