package ru.rmntim.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rmntim.web.entity.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {
    @Min(0)
    private long id;

    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username must be alphanumeric with underscores")
    private String username;

    @Size(min = 3, max = 20, message = "Email must be between 3 and 20 characters")
    @Email(message = "Invalid email format")
    private String email;

    private String avatarUrl;

    public static UserInfoDTO fromEntity(UserEntity entity) {
        return UserInfoDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .avatarUrl(entity.getAvatarUrl())
                .build();
    }
}
