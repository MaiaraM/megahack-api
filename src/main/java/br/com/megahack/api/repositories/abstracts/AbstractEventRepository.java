package br.com.megahack.api.repositories.abstracts;

import br.com.megahack.api.model.persistence.Event;
import br.com.megahack.api.repositories.UuidRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface AbstractEventRepository<P extends Event> extends UuidRepository<P, String> {
    P findBySkuCode(String skuCode);

    P findBySlugAndActiveTrue(String slug);

    P findBySlug(String slug);

    // Using native query to optimize the join without having to create intermediate table entity
    // We have to manually filter deleted events
    @Query(value = "select p.* from events p inner join event_category cp on cp.category_uuid in" +
            "(select uuid from categories c where c.slug=?1) and p.deleted = false and cp.event_uuid = p.uuid " +
            //"and (?2=-1 or p.price>=?2) and (?3=-1 or p.price<=?3) " +
            "and p.active=true " , nativeQuery = true)
    Page<P> findPublicEventsByCategorySlug(String slug, Pageable pageable);

    Page<P> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Event> findByActiveTrue(Pageable page);
}
