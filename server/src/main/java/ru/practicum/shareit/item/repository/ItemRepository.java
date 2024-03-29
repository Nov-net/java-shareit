package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item save(Item item);

    @Query("select i from Item i where i.owner.id =?1 order by i.id asc")
    Page<Item> findAllByOwnerIdIsOrderById(long userId, Pageable pageable);

    Item getById(long id);

    Optional<Item> findById(Long id);

    void delete(Item item);

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))")
    Page<Item> search(String text, Pageable pageable);

}
