package com.expectra.roombooking.service.impl;

        import com.expectra.roombooking.model.Amenity;
        import com.expectra.roombooking.model.Media;
        import com.expectra.roombooking.model.Room;
        import com.expectra.roombooking.model.RoomType;
        import com.expectra.roombooking.repository.RoomRepository;
        import com.expectra.roombooking.service.RoomService;
        import jakarta.transaction.Transactional;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.Optional;
// RoomServiceImpl.java
@Service
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;


    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoomById(Long roomId) {
        roomRepository.deleteById(roomId);
    }

 //   @Override
//   public List<Room> getRoomsByHotelId(Long hotelId) {
//        return roomRepository.getAllRoomsByHotelId(hotelId);
//    }
 @Override
 public List<Media> getAllMediasByHotelIdAndRoomId(Long hotelId, Long roomId) {
     return roomRepository.getAllMediasByHotelIdAndRoomId(hotelId, roomId);
 }
    @Override
    public List<Amenity> getAllAmenitiesByHotelIdAndRoomId(Long hotelId, Long roomId) {
        return roomRepository.getAllAmenitiesByHotelIdAndRoomId(hotelId, roomId);
    }
//    @Override
//    public List<Amenity> getRoomAmenitiesByHotelAndRoom(Long hotelId, Long roomId) {
//        return roomRepository.getAllAmenitiesByHotelIdAndRoomId(hotelId, roomId);
//    }

    @Override
    public List<Media> getRoomMediaByHotelAndRoomType(Long hotelId, RoomType roomType) {
        return roomRepository.getAllMediasByHotelIdAndRoomType(hotelId, roomType);
    }

    @Override
    public List<Amenity> getRoomAmenitiesByHotelAndRoomType(Long hotelId, RoomType roomType) {
        return roomRepository.getAllAmenitiesByHotelIdAndRoomType(hotelId, roomType);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }
}