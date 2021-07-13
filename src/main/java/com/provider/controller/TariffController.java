package com.provider.controller;

import com.provider.entity.Tariff;
import com.provider.security.JwtProvider;
import com.provider.service.TariffService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Log4j2
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
        log.trace("Getting paginated tariff list");
        return new ResponseEntity<>(tariffService.getAll(page, size, sort, order), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tariff> getTariff(@PathVariable("id") Long id) {
        log.trace("Getting tariff by id");
        return new ResponseEntity<>(tariffService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/service/{id}")
    public ResponseEntity<List<Tariff>> getTariffListByServiceId(@PathVariable("id") Long id) {
        log.trace("Getting tariff list by service id");
        return new ResponseEntity<>(tariffService.getTariffListByServiceId(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Tariff>> getTariffListByUserId(@RequestHeader(name = "Authorization") String token) {
        log.trace("Getting tariff list by user id");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffService.getTariffListByUserId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tariff> createTariff(@RequestBody Tariff tariff) {
        log.trace("Creating tariff");
        return new ResponseEntity<>(tariffService.create(tariff), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tariff> updateTariff(@PathVariable("id") Long id, @RequestBody Tariff tariff) {
        log.trace("Updating tariff");
        return new ResponseEntity<>(tariffService.update(tariff, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tariff> deleteTariff(@PathVariable("id") Long id) {
        log.trace("Deleting tariff");
        tariffService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/order")
    public ResponseEntity<BigDecimal> makeOrder(@RequestBody List<Tariff> tariffList, @RequestHeader(name = "Authorization") String token) {
        log.trace("Order making");
        Long id = jwtProvider.getUserIdFromToken(token);
        return new ResponseEntity<>(tariffService.makeOrder(id, tariffList), HttpStatus.CREATED);
    }
}
