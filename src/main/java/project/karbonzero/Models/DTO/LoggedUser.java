package project.karbonzero.Models.DTO;

import java.util.List;

public record LoggedUser(
        String id,
        String username,
        String email,
        String firstName,
        String lastName,
        Integer roleName,
        List<String> authorities
) {}
