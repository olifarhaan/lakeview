package com.olifarhaan.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olifarhaan.domains.RoomStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author M. Ali Farhan
 */
@Entity
@Table(name = "rooms")
@Getter
@Setter
@AllArgsConstructor
public class Room extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @ManyToOne
    @JoinColumn(name = "room_class_id", nullable = false)
    private RoomClass roomClass;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public Room() {
        this.bookings = new ArrayList<>();
    }

    public Room(Floor floor, RoomClass roomClass, String roomNumber, RoomStatus roomStatus) {
        this.floor = floor;
        this.roomClass = roomClass;
        this.roomNumber = roomNumber;
        this.roomStatus = roomStatus;
    }
}
