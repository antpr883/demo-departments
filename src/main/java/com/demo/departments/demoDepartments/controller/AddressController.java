package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.service.AddressService;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;
import com.demo.departments.demoDepartments.controller.swagger.api.AddressControllerEndpoint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * REST controller for managing Address entities
 */
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Validated
public class AddressController implements AddressControllerEndpoint {

    private final AddressService addressService;

    @Override
    public ResponseEntity<List<AddressDTO>> getAllAddresses(
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<AddressDTO> addresses = addressService.findAll(mappingLevel);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/minimal : Get all addresses with MINIMAL mapping level
     *
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body with minimal fields
     */
    @GetMapping("/minimal")
    public ResponseEntity<List<AddressDTO>> getAllAddressesMinimal() {
        List<AddressDTO> addresses = addressService.findAll(MappingLevel.MINIMAL);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/basic : Get all addresses with BASIC mapping level
     *
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body with basic fields
     */
    @GetMapping("/basic")
    public ResponseEntity<List<AddressDTO>> getAllAddressesBasic() {
        List<AddressDTO> addresses = addressService.findAll(MappingLevel.BASIC);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/summary : Get all addresses with SUMMARY mapping level
     *
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body with summary fields
     */
    @GetMapping("/summary")
    public ResponseEntity<List<AddressDTO>> getAllAddressesSummary() {
        List<AddressDTO> addresses = addressService.findAll(MappingLevel.SUMMARY);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/complete : Get all addresses with COMPLETE mapping level
     *
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body with all fields
     */
    @GetMapping("/complete")
    public ResponseEntity<List<AddressDTO>> getAllAddressesComplete() {
        List<AddressDTO> addresses = addressService.findAll(MappingLevel.COMPLETE);
        return ResponseEntity.ok(addresses);
    }

    @Override
    public ResponseEntity<AddressDTO> getAddress(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        AddressDTO address = addressService.findById(id, mappingLevel);
        return ResponseEntity.ok(address);
    }

    /**
     * GET /api/addresses/:id/minimal : Get the address with MINIMAL mapping level
     *
     * @param id the id of the address to retrieve
     * @return the ResponseEntity with status 200 (OK) and the address with minimal fields
     */
    @GetMapping("/{id}/minimal")
    public ResponseEntity<AddressDTO> getAddressMinimal(
            @PathVariable @NotNull @Min(1) Long id) {
        AddressDTO address = addressService.findById(id, MappingLevel.MINIMAL);
        return ResponseEntity.ok(address);
    }

    /**
     * GET /api/addresses/:id/basic : Get the address with BASIC mapping level
     *
     * @param id the id of the address to retrieve
     * @return the ResponseEntity with status 200 (OK) and the address with basic fields
     */
    @GetMapping("/{id}/basic")
    public ResponseEntity<AddressDTO> getAddressBasic(
            @PathVariable @NotNull @Min(1) Long id) {
        AddressDTO address = addressService.findById(id, MappingLevel.BASIC);
        return ResponseEntity.ok(address);
    }

    /**
     * GET /api/addresses/:id/summary : Get the address with SUMMARY mapping level
     *
     * @param id the id of the address to retrieve
     * @return the ResponseEntity with status 200 (OK) and the address with summary fields
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<AddressDTO> getAddressSummary(
            @PathVariable @NotNull @Min(1) Long id) {
        AddressDTO address = addressService.findById(id, MappingLevel.SUMMARY);
        return ResponseEntity.ok(address);
    }

    /**
     * GET /api/addresses/:id/complete : Get the address with COMPLETE mapping level
     *
     * @param id the id of the address to retrieve
     * @return the ResponseEntity with status 200 (OK) and the address with all fields
     */
    @GetMapping("/{id}/complete")
    public ResponseEntity<AddressDTO> getAddressComplete(
            @PathVariable @NotNull @Min(1) Long id) {
        AddressDTO address = addressService.findById(id, MappingLevel.COMPLETE);
        return ResponseEntity.ok(address);
    }

    /**
     * GET /api/addresses/:id/full : Get the address with specified attributes
     *
     * @param id the id of the address to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the address in body,
     * or with status 404 (Not Found)
     */
    @GetMapping("/{id}/full")
    public ResponseEntity<AddressDTO> getAddressWithAttributes(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "attributes", required = false) String attributes) {
        List<String> attributeList = attributes != null
                ? Arrays.asList(attributes.split(","))
                : List.of();
        AddressDTO address = addressService.findByIdFull(id, attributeList);
        return ResponseEntity.ok(address);
    }

    /**
     * GET /api/addresses/person/:personId : Get all addresses for a person
     *
     * @param personId the id of the person
     * @param level the mapping level (MINIMAL, BASIC, SUMMARY, COMPLETE)
     * @return the ResponseEntity with status 200 (OK) and the list of addresses in body
     */
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<AddressDTO>> getAddressesByPersonId(
            @PathVariable @NotNull @Min(1) Long personId,
            @RequestParam(name = "level", defaultValue = "SUMMARY")
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE")
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<AddressDTO> addresses = addressService.findByPersonId(personId, mappingLevel);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/person/:personId/minimal : Get all addresses for a person with MINIMAL mapping level
     *
     * @param personId the id of the person
     * @return the ResponseEntity with status 200 (OK) and the addresses with minimal fields
     */
    @GetMapping("/person/{personId}/minimal")
    public ResponseEntity<List<AddressDTO>> getAddressesByPersonIdMinimal(
            @PathVariable @NotNull @Min(1) Long personId) {
        List<AddressDTO> addresses = addressService.findByPersonId(personId, MappingLevel.MINIMAL);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/person/:personId/basic : Get all addresses for a person with BASIC mapping level
     *
     * @param personId the id of the person
     * @return the ResponseEntity with status 200 (OK) and the addresses with basic fields
     */
    @GetMapping("/person/{personId}/basic")
    public ResponseEntity<List<AddressDTO>> getAddressesByPersonIdBasic(
            @PathVariable @NotNull @Min(1) Long personId) {
        List<AddressDTO> addresses = addressService.findByPersonId(personId, MappingLevel.BASIC);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/person/:personId/summary : Get all addresses for a person with SUMMARY mapping level
     *
     * @param personId the id of the person
     * @return the ResponseEntity with status 200 (OK) and the addresses with summary fields
     */
    @GetMapping("/person/{personId}/summary")
    public ResponseEntity<List<AddressDTO>> getAddressesByPersonIdSummary(
            @PathVariable @NotNull @Min(1) Long personId) {
        List<AddressDTO> addresses = addressService.findByPersonId(personId, MappingLevel.SUMMARY);
        return ResponseEntity.ok(addresses);
    }

    /**
     * GET /api/addresses/person/:personId/complete : Get all addresses for a person with COMPLETE mapping level
     *
     * @param personId the id of the person
     * @return the ResponseEntity with status 200 (OK) and the addresses with all fields
     */
    @GetMapping("/person/{personId}/complete")
    public ResponseEntity<List<AddressDTO>> getAddressesByPersonIdComplete(
            @PathVariable @NotNull @Min(1) Long personId) {
        List<AddressDTO> addresses = addressService.findByPersonId(personId, MappingLevel.COMPLETE);
        return ResponseEntity.ok(addresses);
    }

    @Override
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO result = addressService.save(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<AddressDTO> updateAddress(
            @PathVariable @NotNull @Min(1) Long id,
            @Valid @RequestBody AddressDTO addressDTO) {
        AddressDTO result = addressService.update(id, addressDTO);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteAddress(
            @PathVariable @NotNull @Min(1) Long id) {
        addressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}