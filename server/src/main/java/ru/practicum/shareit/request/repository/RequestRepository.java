package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Request save(Request request);

    List<Request> findAllByRequestorIdIsOrderByCreatedDesc(Long requestorId);

    @Query("select r from Request r order by r.created desc")
    Page<Request> findAllIsOrderByCreatedDesc(Pageable pageable);

    List<Request> findAll();

    Optional<Request> findById(Long id);
}
