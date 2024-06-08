package kr.co.polycube.backendtest;

import kr.co.polycube.backendtest.model.User;
import kr.co.polycube.backendtest.repository.UserRepository;
import kr.co.polycube.backendtest.filter.SpecialCharacterFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .addFilter(new SpecialCharacterFilter())
                .build();
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setName("test");

        ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/users", user, User.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getId()).isNotNull();
    }

    @Test
    void testGetUser() {
        User user = new User();
        user.setName("test");
        user = userRepository.save(user);

        ResponseEntity<User> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/users/" + user.getId(), User.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getName()).isEqualTo("test");
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("test");
        user = userRepository.save(user);

        User updatedUser = new User();
        updatedUser.setName("newname");

        restTemplate.put("http://localhost:" + port + "/users/" + user.getId(), updatedUser);

        ResponseEntity<User> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/users/" + user.getId(), User.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getName()).isEqualTo("newname");
    }

    @Test
    void testFilterWithValidCharacters() throws Exception {
        mockMvc.perform(get("/users/1?name=test"))
                .andExpect(status().isOk());
    }

    @Test
    void testFilterWithInvalidCharacters() throws Exception {
        mockMvc.perform(get("/users/1?name=test!!"))
                .andExpect(status().isBadRequest());
    }
}
