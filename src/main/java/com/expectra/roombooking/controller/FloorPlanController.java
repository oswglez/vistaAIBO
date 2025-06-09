package com.expectra.roombooking.controller;

import com.expectra.roombooking.exception.ResourceNotFoundException;
import com.expectra.roombooking.model.FloorPlan;
import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.service.FloorPlanService;
import com.expectra.roombooking.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/floorplan")
@Tag(name = "FloorPlan Management", description = "Endpoints para gestión de planos de piso de hoteles")
@CrossOrigin(origins = "*")
public class FloorPlanController {

    private final FloorPlanService floorPlanService;
    private final HotelService hotelService;


    @Autowired
    public FloorPlanController(FloorPlanService floorPlanService, HotelService hotelService) {
        this.floorPlanService = floorPlanService;
        this.hotelService = hotelService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los planos", description = "Retorna todos los planos de piso disponibles")
    public ResponseEntity<List<FloorPlan>> getAllFloorPlans() {
        List<FloorPlan> floorPlans = floorPlanService.getAllFloorPlans();
        return new ResponseEntity<>(floorPlans, HttpStatus.OK);
    }

    @GetMapping("/{floorPlanid}")
    @Operation(summary = "Obtener plano por ID", description = "Busca y retorna un plano de piso por su ID")
    public ResponseEntity<FloorPlan> getFloorPlanById(@PathVariable Long floorPlanid) {
        FloorPlan floorPlan = floorPlanService.getFloorPlanById(floorPlanid);
        return new ResponseEntity<>(floorPlan, HttpStatus.OK);
    }

    @GetMapping("/hotel/{hotelId}")
    @Operation(summary = "Obtener planos por hotel", description = "Retorna todos los planos asociados a un hotel específico")
    public ResponseEntity<List<FloorPlan>> getFloorPlansByHotelId(@PathVariable Long hotelId) {
        List<FloorPlan> floorPlans = floorPlanService.getFloorPlansByHotelId(hotelId);
        return new ResponseEntity<>(floorPlans, HttpStatus.OK);
    }

    @PostMapping("/hotels/{hotelId}")
    @Operation(summary = "Crea un FloorPlan para un Hotel específico")
    public ResponseEntity<FloorPlan> createFloorPlanForHotel(
            @PathVariable Long hotelId,          // <-- Extrae el ID del path
            @RequestBody FloorPlan floorPlan   // <-- Datos del floorplan del cuerpo
    ) {
        FloorPlan createdFloorPlan = floorPlanService.createFloorPlanForHotel(hotelId, floorPlan);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFloorPlan);
    }

    @PutMapping("/{floorPlanid}")
    @Operation(summary = "Actualizar plano de piso", description = "Actualiza un plano de piso existente por su ID")
    public ResponseEntity<FloorPlan> updateFloorPlan(@PathVariable Long floorPlanid, @RequestBody FloorPlan floorPlan) {
        // Asegurarse de que el ID del path coincida con el ID del objeto
        floorPlan.setId(floorPlanid);
        FloorPlan updatedFloorPlan = floorPlanService.saveFloorPlan(floorPlan);
        return new ResponseEntity<>(updatedFloorPlan, HttpStatus.OK);
    }

    @DeleteMapping("/{floorPlanid}")
    @Operation(summary = "Eliminar plano de piso", description = "Elimina un plano de piso por su ID")
    public ResponseEntity<Void> deleteFloorPlan(@PathVariable Long floorPlanid) {
        floorPlanService.deleteFloorPlanById(floorPlanid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/batch/hotel/{hotelId}")
    @Operation(summary = "Crear múltiples planos para un hotel",
            description = "Recibe y guarda una lista de planos asociados a un hotel específico")
    public ResponseEntity<List<FloorPlan>> createMultipleFloorPlans(
            @PathVariable Long hotelId,
            @RequestBody List<FloorPlan> floorPlans) {

        // Asegurar que todos los planos estén asociados al hotel correcto
        Hotel hotel = hotelService.findHotelById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));


        floorPlans.forEach(floorPlan -> floorPlan.setHotel(hotel));

        // Guardar todos los planos
        List<FloorPlan> savedFloorPlans = floorPlanService.saveAllFloorPlans(floorPlans);

        return new ResponseEntity<>(savedFloorPlans, HttpStatus.CREATED);
    }
}