package com.softz.identity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PermissionControllerTest {
        @Autowired
        MockMvc mockMvc;

        @MockBean
        PermissionService permissionService;

        private NewPermissionRequest request;

        private PermissionDto permissionsDto;

        // @Test
        // @WithMockUser("admin")
        // void createPermission_validateData_success() throws Exception {
        //         request = NewPermissionRequest.builder()
        //                         .name("admin")
        //                         .description("dessdsf")
        //                         .build();
        //         permissionsDto = permissionsDto.builder()
        //                         .id(123)
        //                         .name("thuan3f")
        //                         .description("def")
        //                         .build();
        //         ObjectMapper objectMapper = new ObjectMapper();
        //         Mockito.when(permissionService.createPermission(ArgumentMatchers.any()))
        //                         .thenReturn(permissionsDto);

        //         String content = objectMapper.writeValueAsString(request);

        //         // WHEN - THEN
        //         mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
        //                         .contentType(MediaType.APPLICATION_JSON_VALUE)
        //                         .content(content))
        //                         .andExpect(MockMvcResultMatchers.status().isOk())
        //                         .andExpect(MockMvcResultMatchers
        //                                         .jsonPath("code").value("1000"))
        //                         .andExpect(MockMvcResultMatchers
        //                                         .jsonPath("result.id").value(123));

         //}

        // @Test
        // @WithMockUser("admin")
        // void createPermission_invalidData_fail() throws Exception {
                // GIVEN
        //         request = NewPermissionRequest.builder()
        //                         .name("")
        //                         .description("fffffff")
        //                         .build();

        //         permissionsDto = PermissionDto.builder()
        //                         .id(18)
        //                         .name("")
        //                         .description("fffffffff")
        //                         .build();
        //         ObjectMapper objectMapper = new ObjectMapper();
        //         String content = objectMapper.writeValueAsString(request);

        //         Mockito.when(permissionService.createPermission(ArgumentMatchers.any()))
        //                         .thenReturn(permissionsDto);

        //         // WHEN - THEN
        //         mockMvc.perform(MockMvcRequestBuilders.post("/permissions")
        //                         .contentType(MediaType.APPLICATION_JSON_VALUE)
        //                         .content(content))
        //                         .andExpect(MockMvcResultMatchers.status().isBadRequest())
        //                         .andExpect(MockMvcResultMatchers
        //                                         .jsonPath("code").value("100106"))
        //                         .andExpect(MockMvcResultMatchers
        //                                         .jsonPath("message").value("Field is required"));
        // }
}
