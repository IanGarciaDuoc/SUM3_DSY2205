package com.tienda.tienda.converter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BooleanToNumberConverterTest {

    @Test
    void testConvertToDatabaseColumn() {
        BooleanToNumberConverter converter = new BooleanToNumberConverter();

        // Caso: atributo null
        assertEquals(1, converter.convertToDatabaseColumn(null), 
            "Si es null debería devolver 1 por defecto.");

        // Caso: atributo true
        assertEquals(1, converter.convertToDatabaseColumn(true), 
            "Si es true debería devolver 1.");

        // Caso: atributo false
        assertEquals(0, converter.convertToDatabaseColumn(false), 
            "Si es false debería devolver 0.");
    }

    @Test
    void testConvertToEntityAttribute() {
        BooleanToNumberConverter converter = new BooleanToNumberConverter();

        // Caso: dbData null
        assertTrue(converter.convertToEntityAttribute(null), 
            "Si es null debería devolver true por defecto.");

        // Caso: dbData = 1 (equivale a true)
        assertTrue(converter.convertToEntityAttribute(1), 
            "Si es 1 debería devolver true.");

        // Caso: dbData = 0 (equivale a false)
        assertFalse(converter.convertToEntityAttribute(0), 
            "Si es 0 debería devolver false.");
    }
}
