package com.learnsystem.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String displayName;
    private String role;
    private String status;
    private List<Long> categoryIds;
}
