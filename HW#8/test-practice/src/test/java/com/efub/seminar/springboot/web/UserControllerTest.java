package com.efub.seminar.springboot.web;

import com.efub.seminar.springboot.domain.User;
import com.efub.seminar.springboot.domain.UserType;
import com.efub.seminar.springboot.service.UserService;
import com.efub.seminar.springboot.web.dto.UserController;
import com.efub.seminar.springboot.web.dto.UserResponseDto;
import com.efub.seminar.springboot.web.dto.UserSaveRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Test
    public void 사용자를_생성한다() throws Exception{
        //given
        String testName = "test_name";
        given(userService.save(any(UserSaveRequestDto.class))).willReturn(User.builder().name(testName).type(UserType.NORMAL).build());

        //then
        mvc.perform(MockMvcRequestBuilders.post("/test/"+testName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(testName));
    }

    @Test
    public void 사용자를_가져온다() throws Exception{
        //given
        User entity = User.builder()
                .name("test_name")
                .type(UserType.NORMAL)
                .build();
        given(userService.findById(anyLong())).willReturn(new UserResponseDto(entity));
        //then
        mvc.perform(MockMvcRequestBuilders.get("/test1/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("test_name"));
    }

    @Test
    public void 사용자없음_예외처리를한다() throws Exception{
        //given
        given(userService.findById(anyLong())).willThrow(new IllegalArgumentException());

        //then
        mvc.perform(MockMvcRequestBuilders.get("/test/1"))
                .andExpect(status().isNotFound());
    }

}
