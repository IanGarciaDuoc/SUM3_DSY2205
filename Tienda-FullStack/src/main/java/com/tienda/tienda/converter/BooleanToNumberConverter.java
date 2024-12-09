package com.tienda.tienda.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToNumberConverter implements AttributeConverter<Boolean, Integer> {
    
    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return 1; // valor por defecto
        }
        return attribute ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return true; // valor por defecto
        }
        return dbData.equals(1);
    }
}