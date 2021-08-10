package com.provider.mapper;

import com.provider.dto.TariffDto;
import com.provider.entity.Tariff;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public TariffMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TariffDto toDto(Tariff tariff) {
        return modelMapper.map(tariff, TariffDto.class);
    }

    public Tariff toEntity(TariffDto tariffDto) {
        return modelMapper.map(tariffDto, Tariff.class);
    }

    public List<TariffDto> toDtoList(List<Tariff> tariffList) {
        return tariffList.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Tariff> toEntityList(List<TariffDto> tariffDTOListList) {
        return tariffDTOListList.stream().map(this::toEntity).collect(Collectors.toList());
    }


}
