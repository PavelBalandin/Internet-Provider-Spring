package com.provider.controller;

import com.provider.dto.TariffDTO;
import com.provider.entity.Tariff;
import com.provider.mapper.TariffMapper;
import com.provider.security.JwtProvider;
import com.provider.service.TariffService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final TariffMapper tariffMapper;

    @Autowired
    public TariffController(TariffService tariffService, JwtProvider jwtProvider, TariffMapper tariffMapper) {
        this.tariffService = tariffService;
        this.jwtProvider = jwtProvider;
        this.tariffMapper = tariffMapper;
    }

    @GetMapping
    public ResponseEntity<Page<Tariff>> getAll(
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
    public ResponseEntity<TariffDTO> getTariff(@PathVariable("id") Long id) {
        log.trace("Getting tariff by id");
        return new ResponseEntity<>(tariffMapper.entityToDTO(tariffService.findById(id)), HttpStatus.OK);
    }

    @GetMapping("/service/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<List<TariffDTO>> getTariffListByServiceId(@PathVariable("id") Long id) {
        log.trace("Getting tariff list by service id");
        return new ResponseEntity<>(tariffMapper.listEntityToDTOList(tariffService.getTariffListByServiceId(id)), HttpStatus.OK);
    }

    @GetMapping("/user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<List<TariffDTO>> getTariffListByUserId(@RequestHeader(name = "Authorization") String token) {
        log.trace("Getting tariff list by user id");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffMapper.listEntityToDTOList(tariffService.getTariffListByUserId(id)), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "403")})
    public ResponseEntity<TariffDTO> createTariff(@Valid @RequestBody TariffDTO tariffDTO) {
        log.trace("Creating tariff");
        Tariff tariff = tariffMapper.DTOtoEntity(tariffDTO);
        return new ResponseEntity<>(tariffMapper.entityToDTO(tariffService.create(tariff)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403"),
            @ApiResponse(responseCode = "404")})
    public ResponseEntity<TariffDTO> updateTariff(@Valid @PathVariable("id") Long id, @RequestBody TariffDTO tariffDTO) {
        log.trace("Updating tariff");
        Tariff tariff = tariffMapper.DTOtoEntity(tariffDTO);
        return new ResponseEntity<>(tariffMapper.entityToDTO(tariffService.update(tariff, id)), HttpStatus.OK);
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "402"),
            @ApiResponse(responseCode = "403")})
    public ResponseEntity<BigDecimal> makeOrder(@NotEmpty @RequestBody List<TariffDTO> tariffDTOList, @RequestHeader(name = "Authorization") String token) {
        log.trace("Order making");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffService.makeOrder(id, tariffMapper.listDTOtoEntityList(tariffDTOList)), HttpStatus.CREATED);
    }
}
