package project.karbonzero.Models.DTO.Mail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString   @EqualsAndHashCode
@Getter @Setter
public class ChannelDTO {
    private String idChannel;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotBlank
    @Size(max = 200)
    private String descript;

    @NotBlank
    private String imagePath;
}
