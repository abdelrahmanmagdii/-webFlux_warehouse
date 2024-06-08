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
@Table("user_role_list")
public class UserRole {
    @Id
    @Column("user_id")
    private Long userId;

    @Column("role_id")
    private String roleId;
}
