package ua.com.alevel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.com.alevel.web.controller.ImageController;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImageController controller;

    @Test
    public void shouldReturnDefaultMessage() throws Exception{
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Search")))
                .andExpect(content().string(containsString("Войти")));;
    }

    @Test
    public void accessDeniedTest() throws Exception{
        this.mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void correctLoginTest() throws  Exception{
        this.mockMvc.perform(formLogin().user("dru").password("123456789"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void accessTest() throws  Exception{
        this.mockMvc.perform(get("/top.html"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        this.mockMvc.perform(get("/lk.html"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }
}
