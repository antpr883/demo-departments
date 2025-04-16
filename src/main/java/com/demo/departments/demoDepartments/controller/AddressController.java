package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.service.AddressService;
import com.demo.departments.demoDepartments.service.dto.AddressDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<AddressDTO> addresses = addressService.findAll(withAudit, attributeSet);
        return ResponseEntity.ok(addresses);
    }
    
    /**
     * Helper method to parse comma-separated attributes into a Set
     */
    private Set<String> parseAttributesParam(String attributes) {
        if (attributes == null || attributes.trim().isEmpty()) {
            return null;
        }
        return new HashSet<>(Arrays.asList(attributes.split(",")));
    }


    @Override
    public ResponseEntity<AddressDTO> getAddress(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        AddressDTO address = addressService.findById(id, withAudit, attributeSet);
        return ResponseEntity.ok(address);
    }



    @Override
    public ResponseEntity<List<AddressDTO>> getAddressesByPersonId(
            @PathVariable @NotNull @Min(1) Long personId,
            @RequestParam(name = "withAudit", defaultValue = "false") boolean withAudit,
            @RequestParam(name = "attributes", required = false) String attributes) {
        
        Set<String> attributeSet = parseAttributesParam(attributes);
        List<AddressDTO> addresses = addressService.findByPersonId(personId, withAudit, attributeSet);
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