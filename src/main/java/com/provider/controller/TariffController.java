package com.provider.controller;

import com.provider.entity.Tariff;
import com.provider.security.JwtProvider;
import com.provider.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin
@RestController
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
    public ResponseEntity<Page<Tariff>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String order
    ) {
        return new ResponseEntity<>(tariffService.getAll(page, size, sort, order), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tariff> getTariff(@PathVariable("id") Long id) {
        return new ResponseEntity<>(tariffService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/service/{id}")
    public ResponseEntity<List<Tariff>> getTariffListByServiceId(@PathVariable("id") Long id) {
        return new ResponseEntity<>(tariffService.getTariffListByServiceId(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Tariff>> getTariffListByUserId(@RequestHeader(name = "Authorization") String token) {
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffService.getTariffListByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tariff> createTariff(@RequestBody Tariff tariff) {
        return new ResponseEntity<>(tariffService.create(tariff), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tariff> updateTariff(@PathVariable("id") Long id, @RequestBody Tariff tariff) {
        return new ResponseEntity<>(tariffService.update(tariff, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tariff> deleteTariff(@PathVariable("id") Long id) {
        tariffService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/order")
    public ResponseEntity<BigDecimal> makeOrder(@RequestBody List<Tariff> tariffList, @RequestHeader(name = "Authorization") String token) {
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffService.makeOrder(id, tariffList), HttpStatus.CREATED);
    }
}
