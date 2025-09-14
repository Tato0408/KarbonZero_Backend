package project.karbonzero.Models.DTO.Mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Genera automáticamente getters, setters, toString(), equals() y hashCode()
@Data

// Crea un constructor sin parámetros
@NoArgsConstructor

// Crea un constructor con todos los parámetros (toEmail, subject, text)
@AllArgsConstructor

///Se usan ambas anotaciones para:
//1. Crear el objeto vacío: primero se crea y luego se le da los valores uno por uno usando setters.
//2. Crear el objeto lleno: se creas y se le da todos los valores de una vez usando el constructor con parámetros.
public class MailDTO {
    //Variable que contendrá el correo al que le mandaremos el correo electrónico
    private String toEmail;
    //Variable que contendrá el asunto del correo
    private String subject;
    //Variable que contendrá el cuerpo del correo
    private String text;
}
