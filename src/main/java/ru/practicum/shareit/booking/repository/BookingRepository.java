package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking save(Booking booking);

    Optional<Booking> findBookingById(Long id);

    List<Booking> findAll();

    @Query(value = " select b.* from bookings b " +
            " where b.booker_id = ?1 " +
            " order by b.start_date desc ", nativeQuery = true)
    List<Booking> findByBooker_IdAndOrderByStart_DateDesc(Long bookerId);

    @Query(value = " select b.* from bookings b " +
            " left join items i on b.item_id = i.id " +
            " where i.owner_id = ?1 " +
            " order by b.end_date desc ", nativeQuery = true)
    List<Booking> findByOwner_Id(Long ownerId);

    @Query(value = " select b.* from bookings b " +
            " where b.item_id = ?1 and b.end_date < ?2 " +
            " order by b.end_date desc " +
            " limit 1 ", nativeQuery = true)
    Optional<Booking> findLastBookingByItem_IdOrderByEnd_DateDescLimitOne(Long itemId, LocalDateTime dateTime);

    @Query(value = " select b.* from bookings b " +
            " where b.item_id = ?1 and b.start_date > ?2 " +
            " order by b.start_date asc " +
            " limit 1 ", nativeQuery = true)
    Optional<Booking> findNextBookingByItem_IdOrderByStart_DateDescLimitOne(Long itemId, LocalDateTime dateTime);

}
