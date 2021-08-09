package com.provider.controller;

import com.provider.dto.TariffDto;
import com.provider.entity.Tariff;
import com.provider.mapper.TariffMapper;
import com.provider.security.JwtProvider;
import com.provider.service.TariffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.List;

@Log4j2
@CrossOrigin
@RestController
@Validated
@RequestMapping("api/v1/tariffs")
public class TariffController {

    private final TariffService tariffService;

    private final JwtProvider jwtProvider;

    @Autowired
    public TariffController(TariffService tariffService, JwtProvider jwtProvider) {
        this.tariffService = tariffService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping
    public ResponseEntity<Page<TariffDto>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String order
    ) {
        log.trace("Getting paginated tariff list");
        return new ResponseEntity<>(tariffService.getAll(page, size, sort, order), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<TariffDto> getTariff(@PathVariable("id") Long id) {
        log.trace("Getting tariff by id");
        return new ResponseEntity<>(tariffService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/service/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<List<TariffDto>> getTariffListByServiceId(@PathVariable("id") Long id) {
        log.trace("Getting tariff list by service id");
        return new ResponseEntity<>(tariffService.getTariffListByServiceId(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<List<TariffDto>> getTariffListByUserId(@RequestHeader(name = "Authorization") String token) {
        log.trace("Getting tariff list by user id");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffService.getTariffListByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403")})
    public ResponseEntity<TariffDto> createTariff(@Valid @RequestBody TariffDto tariffDto) {
        log.trace("Creating tariff");
        return new ResponseEntity<>(tariffService.create(tariffDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<TariffDto> updateTariff(@Valid @PathVariable("id") Long id, @RequestBody TariffDto tariffDto) {
        log.trace("Updating tariff");
        return new ResponseEntity<>(tariffService.update(tariffDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<Tariff> deleteTariff(@PathVariable("id") Long id) {
        log.trace("Deleting tariff");
        tariffService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/order")
    @Operation(summary = "Create an user's order from the tariff list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "402"),
            @ApiResponse(responseCode = "403")})
    public ResponseEntity<BigDecimal> makeOrder(@NotEmpty @RequestBody List<TariffDto> tariffDtoList, @RequestHeader(name = "Authorization") String token) {
        log.trace("Order making");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffService.makeOrder(id, tariffDtoList), HttpStatus.CREATED);
    }
}
