package com.olifarhaan.response;

import java.time.LocalDate;

import com.olifarhaan.model.Room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author M. Ali Farhan
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private String id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestName;

    private String guestEmail;

    private int numOfAdults;

    private int numOfChildren;

    private int totalNumOfGuests;

    private String bookingConfirmationCode;

    private Room room;

    public BookingResponse(String id, LocalDate checkInDate, LocalDate checkOutDate,
            String bookingConfirmationCode) {
        this.id = id;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
