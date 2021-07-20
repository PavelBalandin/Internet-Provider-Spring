package com.provider.mapper;

import com.provider.dto.TariffDTO;
import com.provider.entity.Tariff;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TariffMapper {

    public TariffDTO entityToDTO(Tariff tariff) {
        TariffDTO tariffDTO = new TariffDTO();
        tariffDTO.setId(tariff.getId());
        tariffDTO.setName(tariff.getName());
        tariffDTO.setDescription(tariff.getDescription());
        tariffDTO.setDuration(tariff.getDuration());
        tariffDTO.setPrice(tariff.getPrice());
        tariffDTO.setService(tariff.getService());

        return tariffDTO;
    }

    public Tariff DTOtoEntity(TariffDTO tariffDTO) {
        Tariff tariff = new Tariff();
        tariff.setId(tariffDTO.getId());
        tariff.setName(tariffDTO.getName());
        tariff.setDescription(tariffDTO.getDescription());
        tariff.setDuration(tariffDTO.getDuration());
        tariff.setPrice(tariffDTO.getPrice());
        tariff.setService(tariffDTO.getService());

        return tariff;
    }

    public List<TariffDTO> listEntityToDTOList(List<Tariff> tariffList) {
        return tariffList.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    public List<Tariff> listDTOtoEntityList(List<TariffDTO> tariffDTOListList) {
        return tariffDTOListList.stream().map(this::DTOtoEntity).collect(Collectors.toList());
    }


}
