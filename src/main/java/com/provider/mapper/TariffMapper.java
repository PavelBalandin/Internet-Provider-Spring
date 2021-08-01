package com.provider.mapper;

import com.provider.dto.TariffDto;
import com.provider.entity.Tariff;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffMapper {

    public TariffDto toDto(Tariff tariff) {
        TariffDto tariffDto = new TariffDto();
        tariffDto.setId(tariff.getId());
        tariffDto.setName(tariff.getName());
        tariffDto.setDescription(tariff.getDescription());
        tariffDto.setDuration(tariff.getDuration());
        tariffDto.setPrice(tariff.getPrice());
        tariffDto.setService(tariff.getService());

        return tariffDto;
    }

    public Tariff toEntity(TariffDto tariffDto) {
        Tariff tariff = new Tariff();
        tariff.setId(tariffDto.getId());
        tariff.setName(tariffDto.getName());
        tariff.setDescription(tariffDto.getDescription());
        tariff.setDuration(tariffDto.getDuration());
        tariff.setPrice(tariffDto.getPrice());
        tariff.setService(tariffDto.getService());

        return tariff;
    }

    public List<TariffDto> toDtoList(List<Tariff> tariffList) {
        return tariffList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Tariff> toEntityList(List<TariffDto> tariffDTOListList) {
        return tariffDTOListList.stream().map(this::toEntity).collect(Collectors.toList());
    }


}
