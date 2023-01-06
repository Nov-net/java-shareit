package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
// @Data
@Builder
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable=false)
    private User requestor;
    /*@Column
    private LocalDateTime created;*/

}