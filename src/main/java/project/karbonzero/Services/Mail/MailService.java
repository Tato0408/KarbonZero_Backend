    package project.karbonzero.Services.Mail;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.mail.SimpleMailMessage;
    import org.springframework.mail.javamail.JavaMailSender;
    import org.springframework.stereotype.Service;
    import project.karbonzero.Models.DTO.Mail.MailDTO;
    @Service
    public class MailService {
        //Objeto de tipoJavaMailSender que se encargad e enviar los correos
        @Autowired
        private JavaMailSender javaMailSender;

        public void sendEmail(MailDTO mail){
            //Crea un mensaje de correo simple, solamente texto
            SimpleMailMessage message = new SimpleMailMessage();

            //Asignacion de valores
            //A quien se le envia el correo
            message.setTo(mail.getToEmail());

            //Asunto del correo
            message.setSubject(mail.getSubject());

            //Cuerpo del correo
            message.setText(mail.getText());

            //De donde viene el correo
            message.setFrom("karbonzeroadmim@gmail.com");

            //Envia el correo usando javaMailSender
            javaMailSender.send(message);
        }
        public boolean sendRecoveryCode(String toEmail, String code) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject("Código de recuperación de contraseña");
                message.setText("Hola,\n\n" +
                        "Tu código de recuperación de contraseña es:\n\n" +
                        code + "\n\n" +
                        "Ingresa este código en la app para restablecer tu contraseña.\n\n" +
                        "Saludos,\nKarbon Zero Admins.");

                javaMailSender.send(message);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
