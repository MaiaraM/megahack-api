package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.CustomerType;
import br.com.megahack.api.model.persistence.Price;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractPriceRepository<P extends Price> extends UuidRepository<P, String>{


    @Query(value = "select pr.* from prices pr inner join event_category cp on cp.category_uuid in " +
            "(select uuid from categories c where c.slug=?1) " +
            "inner join  events p on p.uuid = pr.event_uuid " +
            "where pr.customer_type_uuid = ?2 " +
            "and p.deleted = false and cp.event_uuid = p.uuid " +
            "and p.active=true " , nativeQuery = true)
    Page<P> findByCustomerTypeCategory(String slug, CustomerType customerType, Pageable pageable);

    P findByEventUuidAndCustomerType(String uuid, CustomerType customerType);
}
