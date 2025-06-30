package com.expectra.roombooking.service.impl;

import com.expectra.roombooking.dto.*;
import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.*;
import com.expectra.roombooking.repository.AddressRepository;
import com.expectra.roombooking.repository.BrandRepository;
import com.expectra.roombooking.repository.ContactRepository;
import com.expectra.roombooking.repository.HotelRepository;
import com.expectra.roombooking.service.BrandValidationService;
import com.expectra.roombooking.service.HotelService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {
    private static final Logger log = LoggerFactory.getLogger(HotelServiceImpl.class);

    private final HotelRepository hotelRepository;
    private final BrandRepository brandRepository;
    private final ContactRepository contactRepository;
    private final AddressRepository addressRepository;
    private final BrandValidationService brandValidationService;

    private final ModelMapper modelMapper;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository,
                            BrandRepository brandRepository,
                            ContactRepository contactRepository,
                            AddressRepository addressRepository,
                            BrandValidationService brandValidationService,
                            ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.brandRepository = brandRepository;
        this.contactRepository = contactRepository;
        this.addressRepository = addressRepository;
        this.brandValidationService = brandValidationService;
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
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);

        dto.setRooms(hotel.getRooms().stream()
                .map(room -> modelMapper.map(room, RoomOnlyDTO.class))
                .collect(Collectors.toSet()));
        return dto;
    }
    @Override
    @Transactional(readOnly = true)
    public HotelDTO findHotelAndRoomsByHotelIdAndRoomType(Long hotelId, String roomType) {
        Hotel hotel = hotelRepository.findHotelAndRoomsByHotelIdAndRoomType(hotelId, roomType)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));
        HotelDTO dto = modelMapper.map(hotel, HotelDTO.class);

        dto.setRooms(hotel.getRooms().stream()
                .map(room -> modelMapper.map(room, RoomOnlyDTO.class))
                .collect(Collectors.toSet()));
        return dto;
    }

    @Override
    @Transactional(readOnly = true) // Es buena práctica marcar como readOnly las consultas
    public Page<HotelListDTO> findConsolidatedHotelData(Long userId, Pageable pageable) {
        return hotelRepository.findConsolidatedHotelData(userId, pageable);
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
        hotel.setDisclaimer(requestDTO.getDisclaimer());
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
    // En HotelServiceImpl.java
    @Override
    @Transactional(readOnly = true)
    public HotelCreationRequestDTO findHotelByIdWithFullRelations(Long id) { // Nuevo nombre de método
        Hotel hotel = hotelRepository.findHotelByIdWithFullRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        HotelCreationRequestDTO dto = new HotelCreationRequestDTO();

        // Poblar campos
        dto.setHotelId(hotel.getHotelId());
        dto.setHotelCode(hotel.getHotelCode());
        dto.setHotelName(hotel.getHotelName());
        dto.setHotelStatus(hotel.getHotelStatus());
        dto.setLocalPhone(hotel.getLocalPhone());
        dto.setHotelWebsiteUrl(hotel.getHotelWebsiteUrl());
        dto.setDisclaimer(hotel.getDisclaimer());

        if (hotel.getBrand() != null) {
            dto.setBrandId(hotel.getBrand().getBrandId());
            dto.setBrandName(hotel.getBrand().getBrandName()); // Campo añadido al DTO
            if (hotel.getBrand().getChain() != null) {
                dto.setChainId(hotel.getBrand().getChain().getChainId()); // Campo añadido al DTO
                dto.setChainName(hotel.getBrand().getChain().getChainName()); // Campo añadido al DTO
            }
        }

        Contact mainContactEntity = hotel.getContacts().stream()
                .filter(c -> "MAIN".equalsIgnoreCase(c.getContactType()))
                .findFirst().orElse(null);
        if (mainContactEntity != null) {
            dto.setMainContact(modelMapper.map(mainContactEntity, ContactInfoDTO.class));
        } else {
            dto.setMainContact(new ContactInfoDTO()); // O manejar de otra forma si es estrictamente requerido
        }

        Address mainAddressEntity = hotel.getAddresses().stream()
                .filter(a -> "MAIN".equalsIgnoreCase(a.getAddressType()))
                .findFirst().orElse(null);
        if (mainAddressEntity != null) {
            dto.setMainAddress(modelMapper.map(mainAddressEntity, AddressInfoDTO.class));
        } else {
            dto.setMainAddress(new AddressInfoDTO()); // O manejar de otra forma
        }

        // Los campos de HotelCreationRequestDTO que no están en la entidad Hotel
        // (como pmsVendor, etc., que eliminamos del form) quedarán null si no se setean aquí.
        // Esto está bien si el frontend los ignora.

        return dto;
    }

    @Override
    @Transactional
    public HotelCreationRequestDTO updateHotelWithDetails(Long hotelId, HotelCreationRequestDTO hotelDetailsDTO) {
        // Buscar el hotel existente con todas sus relaciones
        Hotel hotel = hotelRepository.findHotelByIdWithFullRelations(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        // 1. Actualizar campos básicos del hotel
        hotel.setHotelCode(hotelDetailsDTO.getHotelCode());
        hotel.setHotelName(hotelDetailsDTO.getHotelName());
        hotel.setHotelStatus(hotelDetailsDTO.getHotelStatus());
        hotel.setLocalPhone(hotelDetailsDTO.getLocalPhone());
        hotel.setHotelWebsiteUrl(hotelDetailsDTO.getHotelWebsiteUrl());
        hotel.setDisclaimer(hotelDetailsDTO.getDisclaimer());

        // 2. Actualizar Brand
        Long requestedBrandId = hotelDetailsDTO.getBrandId();
        if (requestedBrandId != null) {
            if (hotel.getBrand() == null || !hotel.getBrand().getBrandId().equals(requestedBrandId)) {
                Brand brand = brandRepository.findById(requestedBrandId)
                        .orElseThrow(() -> new ResourceNotFoundException("Brand not found with ID: " + requestedBrandId));
                hotel.setBrand(brand);
            }
        } else {
            if (hotel.getBrand() == null) {
                log.error("CRITICAL: Hotel (ID: {}) must have an associated brand.", hotelId);
                throw new IllegalStateException("Hotel must have an associated brand.");
            }
        }

        // 3. Actualizar Contacto Principal
        if (hotel.getContacts() == null) hotel.setContacts(new HashSet<>());

        Set<Contact> nonMainContacts = hotel.getContacts().stream()
                .filter(c -> !"MAIN".equalsIgnoreCase(c.getContactType()))
                .collect(Collectors.toSet());

        // Remover contactos principales existentes usando el método helper
        hotel.getContacts().stream()
                .filter(c -> "MAIN".equalsIgnoreCase(c.getContactType()))
                .forEach(mainContact -> {
                    mainContact.getHotels().remove(hotel); // Remover hotel del contacto
                });

        hotel.setContacts(new HashSet<>(nonMainContacts));

        // Agregar nuevo contacto principal si se proporciona
        if (hotelDetailsDTO.getMainContact() != null &&
                StringUtils.hasText(hotelDetailsDTO.getMainContact().getContactEmail())) {

            ContactInfoDTO contactRequestDTO = hotelDetailsDTO.getMainContact();
            Contact contactToAssociate;

            Optional<Contact> existingContactOpt = contactRepository.findByContactEmailIgnoreCase(contactRequestDTO.getContactEmail());
            if (existingContactOpt.isPresent()) {
                contactToAssociate = existingContactOpt.get();
                log.info("Updating existing contact (ID: {}) for email: {}",
                        contactToAssociate.getContactId(), contactRequestDTO.getContactEmail());

                // Actualizar campos del contacto existente
                contactToAssociate.setFirstName(contactRequestDTO.getFirstName());
                contactToAssociate.setLastName(contactRequestDTO.getLastName());
                contactToAssociate.setContactTitle(contactRequestDTO.getContactTitle());
                contactToAssociate.setContactMobileNumber(contactRequestDTO.getContactMobileNumber());
            } else {
                log.info("Creating new contact for email: {}", contactRequestDTO.getContactEmail());
                contactToAssociate = modelMapper.map(contactRequestDTO, Contact.class);
                contactToAssociate.setContactId(null);
            }

            contactToAssociate.setContactType("MAIN");
            final Long contactIdToCheck = contactToAssociate.getContactId();

            boolean alreadyExists = hotel.getContacts().stream()
                    .anyMatch(c -> contactIdToCheck != null &&
                            contactIdToCheck.equals(c.getContactId()));

            if (!alreadyExists) {
                hotel.addContact(contactToAssociate);
                log.info("Contact added to hotel. Contact ID: {}, Type: {}",
                        contactToAssociate.getContactId(), contactToAssociate.getContactType());
            } else {
                log.warn("Contact already exists in hotel collection, skipping addition");
            }
        }

        // 4. Actualizar Dirección Principal
        if (hotel.getAddresses() != null) {
            // Remover direcciones principales existentes
            new HashSet<>(hotel.getAddresses()).stream()
                    .filter(a -> "MAIN".equalsIgnoreCase(a.getAddressType()))
                    .forEach(addressToRemove -> hotel.removeAddress(addressToRemove));
        } else {
            hotel.setAddresses(new HashSet<>());
        }

        // Agregar nueva dirección principal si se proporciona
        if (hotelDetailsDTO.getMainAddress() != null) {
            AddressInfoDTO addressRequestDTO = hotelDetailsDTO.getMainAddress();
            Address newMainAddress = modelMapper.map(addressRequestDTO, Address.class);
            newMainAddress.setAddressId(null);
            newMainAddress.setAddressType("MAIN");

            hotel.addAddress(newMainAddress);
            log.info("Nueva dirección MAIN preparada para Hotel ID {}: {}", hotelId, newMainAddress.getStreet());
        }

        // Logs para depuración
        log.info("Antes de hotelRepository.save(hotel). Hotel ID: {}. Addresses: {}, Contacts: {}",
                hotelId,
                hotel.getAddresses().stream().map(a -> "ID:" + a.getAddressId() + " T:" + a.getAddressType()).collect(Collectors.toList()),
                hotel.getContacts().stream().map(c -> "ID:" + c.getContactId() + " T:" + c.getContactType()).collect(Collectors.toList()));

        // 5. Guardar el hotel actualizado (esto guardará todas las relaciones en cascada)
        Hotel updatedHotelEntity = hotelRepository.save(hotel);

        log.info("Después de hotelRepository.save(hotel). Hotel ID: {}. Addresses: {}, Contacts: {}",
                updatedHotelEntity.getHotelId(),
                updatedHotelEntity.getAddresses().stream().map(a -> "ID:" + a.getAddressId() + " T:" + a.getAddressType()).collect(Collectors.toList()),
                updatedHotelEntity.getContacts().stream().map(c -> "ID:" + c.getContactId() + " T:" + c.getContactType()).collect(Collectors.toList()));

        // Devolver los detalles actualizados del hotel
        return getHotelDetailsForEditForm(updatedHotelEntity.getHotelId());
    }



    @Transactional(readOnly = true)
    public HotelCreationRequestDTO getHotelDetailsForEditForm(Long hotelId) {
        log.info("Fetching hotel details for edit form, ID: {}", hotelId);
        Hotel hotel = hotelRepository.findHotelByIdWithFullRelations(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));
        log.debug("Hotel entity fetched for edit: {}", hotel);

        HotelCreationRequestDTO dto = new HotelCreationRequestDTO();
        // Mapear campos directos
        dto.setHotelId(hotel.getHotelId());
        dto.setHotelCode(hotel.getHotelCode());
        dto.setHotelName(hotel.getHotelName());
        dto.setHotelStatus(hotel.getHotelStatus()); // Asegurar que este campo esté en Hotel y se mapee
        dto.setLocalPhone(hotel.getLocalPhone());
        dto.setHotelWebsiteUrl(hotel.getHotelWebsiteUrl());
        dto.setDisclaimer(hotel.getDisclaimer());

        // Mapear Brand y Chain
        if (hotel.getBrand() != null) {
            dto.setBrandId(hotel.getBrand().getBrandId());
            dto.setBrandName(hotel.getBrand().getBrandName());
            if (hotel.getBrand().getChain() != null) {
                dto.setChainId(hotel.getBrand().getChain().getChainId());
                dto.setChainName(hotel.getBrand().getChain().getChainName());
            }
        }

        // Mapear Contacto Principal
        hotel.getContacts().stream()
                .filter(c -> "MAIN".equalsIgnoreCase(c.getContactType()))
                .findFirst()
                .ifPresent(contactEntity -> dto.setMainContact(modelMapper.map(contactEntity, ContactInfoDTO.class)));

        // Mapear Dirección Principal
        hotel.getAddresses().stream()
                .filter(a -> "MAIN".equalsIgnoreCase(a.getAddressType()))
                .findFirst()
                .ifPresent(addressEntity -> dto.setMainAddress(modelMapper.map(addressEntity, AddressInfoDTO.class)));

        return dto;
    }

}