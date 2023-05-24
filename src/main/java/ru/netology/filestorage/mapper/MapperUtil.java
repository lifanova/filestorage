package ru.netology.filestorage.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {
    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T, D> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        Page<D> map = entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
        return map;
    }

    public <T, D> D mapEntityIntoDto(T t, Class<D> dtoClass) {
        return modelMapper.map(t, dtoClass);
    }
}
