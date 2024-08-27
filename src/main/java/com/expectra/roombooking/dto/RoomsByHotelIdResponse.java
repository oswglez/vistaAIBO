package com.expectra.roombooking.dto;

import com.expectra.roombooking.model.Room;

import java.util.List;

public record RoomsByHotelIdResponse() {
    static String name;
    static Integer roomQty;
    static List<Room> roomsAvailable;
}
