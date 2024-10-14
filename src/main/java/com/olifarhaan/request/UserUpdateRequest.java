package com.olifarhaan.request;

import java.time.LocalDate;

import com.olifarhaan.model.Address;
import com.olifarhaan.model.User.Gender;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String fullName;
    private String email;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private Address address;
}
