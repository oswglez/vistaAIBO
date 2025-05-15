package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.*;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.*;
import com.expectra.roombooking.repository.BrandRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.service.HotelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final BrandRepository brandRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository, BrandRepository brandRepository, ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.brandRepository = brandRepository;
        this.modelMapper =  modelMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Hotel> findHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    @Override
    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public void deleteHotelById(Long hotelId) {
        hotelRepository.deleteById(hotelId);
    }
    @Override
    public void logicalDeleteHotel(Long hotelId) {
        hotelRepository.markHotelAsDeleted(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findHotelsByName(String hotelName) {
        return hotelRepository.findByHotelName(hotelName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Amenity> findHotelAmenities(Long hotelId) {
        return hotelRepository.findAllAmenitiesByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Media> findHotelMedias(Long hotelId) {
        return hotelRepository.findAllMediasByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contact> findAllContactsByHotelId(Long hotelId) {
        return hotelRepository.findAllContactsByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> findAllAddressesByHotelId(Long hotelId) {
        return hotelRepository.findAllAddressesByHotelId(hotelId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> findHotelRooms(Long hotelId) {
        return hotelRepository.findAllRoomsByHotelId(hotelId);
    }
    @Override
    @Transactional(readOnly = true)
    public HotelDTO getHotelAndRoomsByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.getHotelAndRoomsByHotelId(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado"));
        HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);

        dto.setRooms(hotel.getRooms().stream()
                .map(room -> {
                    return modelMapper.map(room, RoomDTO.class);
                })
                .collect(Collectors.toSet()));
        return dto;
    }
    @Override
    @Transactional(readOnly = true)
    public HotelDTO findHotelAndRoomsByHotelIdAndRoomType(Long hotelId, String roomType) {
        Hotel hotel = hotelRepository.findHotelAndRoomsByHotelIdAndRoomType(hotelId, roomType)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel no encontrado"));
        HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);

        dto.setRooms(hotel.getRooms().stream()
                .map(room -> {
                    return modelMapper.map(room, RoomDTO.class);
                })
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    @Transactional(readOnly = true) // Es buena práctica marcar como readOnly las consultas
    public Page<HotelListDTO> findConsolidatedHotelData(Pageable pageable) {
        return hotelRepository.findConsolidatedHotelData(pageable);
    }
    @Override
    @Transactional // Asegura que toda la operación sea una única transacción
    public Hotel createHotelWithDetails(HotelCreationRequestDTO requestDTO) {

        // 1. Crear y guardar la entidad Address
        Hotel hotel = new Hotel(); // Crear instancia de Hotel vacía
        // 1. Mapear campos directos de HotelCreationRequestDTO a Hotel manualmente
        hotel.setHotelCode(requestDTO.getHotelCode());
        hotel.setHotelName(requestDTO.getHotelName());
        hotel.setHotelStatus(requestDTO.getHotelStatus());
        hotel.setLocalPhone(requestDTO.getLocalPhone());
        hotel.setCelularPhone(requestDTO.getCelularPhone());
        hotel.setPmsVendor(requestDTO.getPmsVendor());
        hotel.setPmsHotelId(requestDTO.getPmsHotelId());
        hotel.setPmsToken(requestDTO.getPmsToken());
        hotel.setCrsVendor(requestDTO.getCrsVendor());
        hotel.setCrsHotelId(requestDTO.getCrsHotelId());
        hotel.setCrsToken(requestDTO.getCrsToken());
        hotel.setDisclaimer(requestDTO.getDisclaimer());
        hotel.setTotalFloors(requestDTO.getTotalFloors());
        hotel.setTotalRooms(requestDTO.getTotalRooms());
        hotel.setHotelWebsiteUrl(requestDTO.getHotelWebsiteUrl());
        hotel.setHotelDeleted(false); // Valor por defecto al crear

        if (requestDTO.getBrandId() != null) {
            Brand brand = brandRepository.findById(requestDTO.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + requestDTO.getBrandId()));
            hotel.setBrand(brand);
        }
        if (requestDTO.getMainContact() != null) {
            Contact mainContact = modelMapper.map(requestDTO.getMainContact(), Contact.class);
            // Asegurar inicialización de colecciones en Hotel
            if (hotel.getContacts() == null) {
                hotel.setContacts(new HashSet<>());
            }
            hotel.addContact(mainContact); // Usar método helper para la relación bidireccional
            // Esto asume que Hotel tiene CascadeType.PERSIST/ALL para contacts
        }
        if (requestDTO.getMainAddress() != null) {
            Address mainAddress = modelMapper.map(requestDTO.getMainAddress(), Address.class);
            // Asegurar inicialización de colecciones en Hotel
            if (hotel.getAddresses() == null) {
                hotel.setAddresses(new HashSet<>());
            }
            hotel.addAddress(mainAddress); // Usar método helper para la relación bidireccional
            // Esto asume que Hotel tiene CascadeType.PERSIST/ALL para addresses
        }
        // 5. Inicializar otras colecciones si es necesario antes de guardar, aunque
        // si no se añaden elementos, no es estrictamente necesario para la creación.
        if (hotel.getRooms() == null) hotel.setRooms(new HashSet<>());
        if (hotel.getFloorPlans() == null) hotel.setFloorPlans(new java.util.ArrayList<>()); // Si es List
        if (hotel.getMedias() == null) hotel.setMedias(new HashSet<>());
        if (hotel.getAmenities() == null) hotel.setAmenities(new HashSet<>());
        if (hotel.getProviders() == null) hotel.setProviders(new HashSet<>());
        // 5. Guardar la entidad Hotel (esto debería guardar en cascada Contact y Address
        return hotelRepository.save(hotel);
    }
}