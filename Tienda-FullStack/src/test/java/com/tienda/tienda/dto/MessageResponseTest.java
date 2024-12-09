package com.tienda.tienda.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void testMessageResponse_AllArgsConstructor() {
        // Crear un MessageResponse usando el constructor con argumentos
        String mensaje = "Este es un mensaje de prueba";
        MessageResponse response = new MessageResponse(mensaje);

        // Verificar que el mensaje se haya configurado correctamente
        assertEquals(mensaje, response.getMensaje());
    }

    @Test
    void testMessageResponse_NoArgsConstructor() {
        // Crear un MessageResponse usando el constructor vacío
        MessageResponse response = new MessageResponse();

        // Verificar que el mensaje sea inicialmente null
        assertNull(response.getMensaje());

        // Establecer un mensaje
        String mensaje = "Mensaje modificado";
        response.setMensaje(mensaje);

        // Verificar que el mensaje se haya configurado correctamente
        assertEquals(mensaje, response.getMensaje());
    }

    @Test
    void testMessageResponse_SetterAndGetter() {
        // Crear un MessageResponse vacío
        MessageResponse response = new MessageResponse();

        // Configurar un mensaje
        String mensaje = "Probando setters y getters";
        response.setMensaje(mensaje);

        // Verificar que el getter devuelve el mensaje configurado
        assertEquals(mensaje, response.getMensaje());
    }

    @Test
    void testMessageResponse_EqualsAndHashCode() {
        // Crear dos objetos con el mismo mensaje
        MessageResponse response1 = new MessageResponse("Mensaje");
        MessageResponse response2 = new MessageResponse("Mensaje");

        // Verificar que sean iguales
        assertEquals(response1, response2);

        // Verificar que tengan el mismo hashcode
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testMessageResponse_ToString() {
        // Crear un MessageResponse
        MessageResponse response = new MessageResponse("Mensaje de prueba");

        // Verificar que el método toString genera la salida esperada
        String expected = "MessageResponse(mensaje=Mensaje de prueba)";
        assertEquals(expected, response.toString());
    }

    @Test
    void testMessageResponse_NullValue() {
        // Crear un MessageResponse con mensaje null
        MessageResponse response = new MessageResponse(null);
    
        // Verificar que el mensaje sea null
        assertNull(response.getMensaje());
    
        // Modificar el mensaje a null y verificar
        response.setMensaje(null);
        assertNull(response.getMensaje());
    }
    @Test
void testMessageResponse_EmptyString() {
    // Crear un MessageResponse con una cadena vacía
    MessageResponse response = new MessageResponse("");

    // Verificar que el mensaje sea una cadena vacía
    assertEquals("", response.getMensaje());

    // Modificar el mensaje a otra cadena vacía y verificar
    response.setMensaje("");
    assertEquals("", response.getMensaje());
}

@Test
void testMessageResponse_LongString() {
    // Crear un mensaje muy largo
    String longMessage = "A".repeat(1000); // 1000 caracteres 'A'

    // Crear un MessageResponse con el mensaje largo
    MessageResponse response = new MessageResponse(longMessage);

    // Verificar que el mensaje sea igual al mensaje largo
    assertEquals(longMessage, response.getMensaje());
}
@Test
void testMessageResponse_NotEquals() {
    // Crear dos objetos con mensajes diferentes
    MessageResponse response1 = new MessageResponse("Mensaje 1");
    MessageResponse response2 = new MessageResponse("Mensaje 2");

    // Verificar que no sean iguales
    assertNotEquals(response1, response2);

    // Verificar que sus hashcodes sean diferentes
    assertNotEquals(response1.hashCode(), response2.hashCode());
}
@Test
void testMessageResponse_EqualsWithNull() {
    // Crear un MessageResponse con un mensaje
    MessageResponse response = new MessageResponse("Mensaje");

    // Verificar que no sea igual a null
    assertNotEquals(response, null);
}
@Test
void testMessageResponse_EqualsWithSelf() {
    // Crear un MessageResponse
    MessageResponse response = new MessageResponse("Mensaje");

    // Verificar que sea igual a sí mismo
    assertEquals(response, response);
}
@Test
void testMessageResponse_EqualsConsistency() {
    // Crear dos objetos con el mismo mensaje
    MessageResponse response1 = new MessageResponse("Mensaje");
    MessageResponse response2 = new MessageResponse("Mensaje");

    // Verificar que sean iguales en llamadas consecutivas
    assertEquals(response1, response2);
    assertEquals(response1, response2);
    assertEquals(response1, response2);
}
@Test
void testMessageResponse_StateChange() {
    // Crear dos objetos con el mismo mensaje
    MessageResponse response1 = new MessageResponse("Mensaje");
    MessageResponse response2 = new MessageResponse("Mensaje");

    // Verificar que inicialmente sean iguales
    assertEquals(response1, response2);

    // Cambiar el mensaje de uno de ellos
    response1.setMensaje("Mensaje Modificado");

    // Verificar que ahora sean diferentes
    assertNotEquals(response1, response2);
}


}
