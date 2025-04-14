package com.demo.departments.demoDepartments.controller;

import com.demo.departments.demoDepartments.service.PersonService;
import com.demo.departments.demoDepartments.service.dto.PersonDTO;
import com.demo.departments.demoDepartments.service.dto.mapper.MappingLevel;
import com.demo.departments.demoDepartments.controller.swagger.api.PersonControllerEndpoint;
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
 * REST controller for managing Person entities
 */
@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
@Validated
public class PersonController implements PersonControllerEndpoint {

    private final PersonService personService;

    @Override
    public ResponseEntity<List<PersonDTO>> getAllPersons(String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        List<PersonDTO> persons = personService.findAll(mappingLevel);
        return ResponseEntity.ok(persons);
    }

    /**
     * GET /api/persons/minimal : Get all persons with MINIMAL mapping level
     * 
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with minimal fields
     */
    @GetMapping("/minimal")
    public ResponseEntity<List<PersonDTO>> getAllPersonsMinimal() {
        List<PersonDTO> persons = personService.findAll(MappingLevel.MINIMAL);
        return ResponseEntity.ok(persons);
    }

    /**
     * GET /api/persons/basic : Get all persons with BASIC mapping level
     * 
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with basic fields
     */
    @GetMapping("/basic")
    public ResponseEntity<List<PersonDTO>> getAllPersonsBasic() {
        List<PersonDTO> persons = personService.findAll(MappingLevel.BASIC);
        return ResponseEntity.ok(persons);
    }

    /**
     * GET /api/persons/summary : Get all persons with SUMMARY mapping level
     * 
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with summary fields
     */
    @GetMapping("/summary")
    public ResponseEntity<List<PersonDTO>> getAllPersonsSummary() {
        List<PersonDTO> persons = personService.findAll(MappingLevel.SUMMARY);
        return ResponseEntity.ok(persons);
    }

    /**
     * GET /api/persons/complete : Get all persons with COMPLETE mapping level
     * 
     * @return the ResponseEntity with status 200 (OK) and the list of persons in body with all fields
     */
    @GetMapping("/complete")
    public ResponseEntity<List<PersonDTO>> getAllPersonsComplete() {
        List<PersonDTO> persons = personService.findAll(MappingLevel.COMPLETE);
        return ResponseEntity.ok(persons);
    }

    @Override
    public ResponseEntity<PersonDTO> getPerson(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "level", defaultValue = "SUMMARY") 
            @Pattern(regexp = "^(MINIMAL|BASIC|SUMMARY|COMPLETE)$", message = "Mapping level must be one of: MINIMAL, BASIC, SUMMARY, COMPLETE") 
            String level) {
        MappingLevel mappingLevel = MappingLevel.valueOf(level.toUpperCase());
        PersonDTO person = personService.findById(id, mappingLevel);
        return ResponseEntity.ok(person);
    }

    /**
     * GET /api/persons/:id/minimal : Get the person with MINIMAL mapping level
     * 
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with minimal fields
     */
    @Override
    @GetMapping("/{id}/minimal")
    public ResponseEntity<PersonDTO> getPersonMinimal(@PathVariable @NotNull @Min(1) Long id) {
        PersonDTO person = personService.findById(id, MappingLevel.MINIMAL);
        return ResponseEntity.ok(person);
    }

    /**
     * GET /api/persons/:id/basic : Get the person with BASIC mapping level
     * 
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with basic fields
     */
    @Override
    @GetMapping("/{id}/basic")
    public ResponseEntity<PersonDTO> getPersonBasic(@PathVariable @NotNull @Min(1) Long id) {
        PersonDTO person = personService.findById(id, MappingLevel.BASIC);
        return ResponseEntity.ok(person);
    }

    /**
     * GET /api/persons/:id/summary : Get the person with SUMMARY mapping level
     * 
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with summary fields
     */
    @Override
    @GetMapping("/{id}/summary")
    public ResponseEntity<PersonDTO> getPersonSummary(@PathVariable @NotNull @Min(1) Long id) {
        PersonDTO person = personService.findById(id, MappingLevel.SUMMARY);
        return ResponseEntity.ok(person);
    }

    /**
     * GET /api/persons/:id/complete : Get the person with COMPLETE mapping level
     * 
     * @param id the id of the person to retrieve
     * @return the ResponseEntity with status 200 (OK) and the person with all fields
     */
    @Override
    @GetMapping("/{id}/complete")
    public ResponseEntity<PersonDTO> getPersonComplete(@PathVariable @NotNull @Min(1) Long id) {
        PersonDTO person = personService.findById(id, MappingLevel.COMPLETE);
        return ResponseEntity.ok(person);
    }

    /**
     * GET /api/persons/:id/full : Get the person with specified attributes
     * 
     * @param id the id of the person to retrieve
     * @param attributes the attributes to include (comma-separated)
     * @return the ResponseEntity with status 200 (OK) and the person in body, 
     * or with status 404 (Not Found)
     */
    @Override
    @GetMapping("/{id}/full")
    public ResponseEntity<PersonDTO> getPersonWithAttributes(
            @PathVariable @NotNull @Min(1) Long id,
            @RequestParam(name = "attributes", required = false) String attributes) {
        List<String> attributeList = attributes != null 
                ? Arrays.asList(attributes.split(",")) 
                : List.of();
        PersonDTO person = personService.findByIdFull(id, attributeList);
        return ResponseEntity.ok(person);
    }

    @Override
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        PersonDTO result = personService.save(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<PersonDTO> updatePerson(
            @PathVariable @NotNull @Min(1) Long id, 
            @Valid @RequestBody PersonDTO personDTO) {
        PersonDTO result = personService.update(id, personDTO);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deletePerson(@PathVariable @NotNull @Min(1) Long id) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}