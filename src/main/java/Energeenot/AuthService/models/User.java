package Energeenot.AuthService.models;

import Energeenot.AuthService.models.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false, name = "id")
    private long id;

    @Email(message = "Введите корректный почтовый адрес")
    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty(message = "Поле пароль должно быть заполнено")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Role role;

    @Column(name = "refresh_token")
    private String refreshToken;
}
