package project.karbonzero.Config.Cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        //Creamos un objeto que sirve para leer las variables del archivo .env

        //Creamos un Map para almacenar la configuración necesaria para Cloudinary
        Map<String,  String> config = new HashMap<>();

        config.put("cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"));
        config.put("api_key", System.getenv("CLOUDINARY_API_KEY"));
        config.put("api_secret", System.getenv("CLOUDINARY_API_SECRET"));

        return new Cloudinary(config);
    }
}

