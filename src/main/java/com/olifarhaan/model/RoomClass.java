package com.olifarhaan.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olifarhaan.domains.BedType;
import com.olifarhaan.domains.Feature;
import com.olifarhaan.request.RoomClassRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "room_classes")
public class RoomClass extends BaseEntity {

    @Column(nullable = false)
    private double basePrice;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int maxGuestCount;

    @JsonIgnore
    @OneToMany(mappedBy = "roomClass", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_class_feature", joinColumns = @JoinColumn(name = "room_class_id"))
    @Column(name = "feature")
    private List<Feature> features;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_class_bed_type", joinColumns = @JoinColumn(name = "room_class_id"))
    @Column(name = "bed_type")
    private List<BedType> bedTypes;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_class_image", joinColumns = @JoinColumn(name = "room_class_id"))
    @Column(name = "image_url")
    private List<String> images;

    public RoomClass(RoomClassRequest roomClassRequest) {
        this.title = roomClassRequest.getTitle();
        this.description = roomClassRequest.getDescription();
        this.maxGuestCount = roomClassRequest.getMaxGuestCount();
        this.basePrice = roomClassRequest.getBasePrice();
        this.images = roomClassRequest.getImages();
        this.features = roomClassRequest.getFeatures();
        this.bedTypes = roomClassRequest.getBedTypes();
    }
}
