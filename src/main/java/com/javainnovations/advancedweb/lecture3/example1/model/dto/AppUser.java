package com.javainnovations.advancedweb.lecture3.example1.model.dto;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table("app_user")
public class AppUser {
    @Id
    @Column("id")
    private Long id;

    @Column("email") //TODO max 100
    private String email;

    @Column("password") //TODO max 100
    private String password;

    @Column("status_id")
    private Integer statusId;

    @Column("role_id")
    private Integer roleId;

    @Column("name")
    private String name;

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", statusId=" + statusId +
                ", roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
