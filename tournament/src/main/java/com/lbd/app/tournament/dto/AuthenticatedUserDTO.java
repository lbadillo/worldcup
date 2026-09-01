package com.lbd.app.tournament.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticatedUserDTO {
    private Long id;
    private String email;
    private String name;
    private String sub;
    private String providerUserId;
    private String providerId;
    private Long roleId;
    private String roleName;


}

