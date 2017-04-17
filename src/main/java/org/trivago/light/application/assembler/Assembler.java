package org.trivago.light.application.assembler;

import org.modelmapper.ModelMapper;
import org.trivago.light.application.domain.Entity;

public abstract class Assembler<DTO, E extends Entity> {

    final protected ModelMapper modelMapper = new ModelMapper();
    final Class<DTO> typeParameterClassDto;
    final Class<E> typeParameterClassEntity;

    protected Assembler(Class<DTO> typeParameterClassDto, Class<E> typeParameterClassEntity) {
        this.typeParameterClassDto = typeParameterClassDto;
        this.typeParameterClassEntity = typeParameterClassEntity;
    }

    public DTO dto(E entity) {
        return modelMapper.map(entity, typeParameterClassDto);
    }

    public E entity(DTO dto) {
        return modelMapper.map(dto, typeParameterClassEntity);
    }
}
