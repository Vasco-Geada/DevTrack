package tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import devtrack.DevTrackApplication;
import devtrack.model.Enums.Priority;
import devtrack.model.Task;
import devtrack.service.IssueService;

@SpringBootTest(classes = DevTrackApplication.class)
@AutoConfigureMockMvc
public class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IssueService issueService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearIssues() {
        issueService.findAll()
                .forEach(issue -> issueService.deleteIssue(issue.getId()));
    }

    @Test
    void getIssuesReturnsOk() throws Exception {
        mockMvc.perform(get("/api/issues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(print());
    }

    @Test
    void getIssuesByIdReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/issues/{id}", 1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void getSavedIssueReturnsOk() throws Exception {
        Task task = new Task(
                "Aprender REST",
                "Testar a consulta de uma Task",
                Priority.HIGH
        );

        issueService.createIssue(task);

        // 2. Consultar o ID da Issue que acabámos de guardar.
        mockMvc.perform(get("/api/issues/{id}", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Aprender REST"))
                .andExpect(jsonPath("$.description")
                        .value("Testar a consulta de uma Task"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.type").value("TASK"));
    }

    @Test
    void postValidTaskReturnsCreated() throws Exception {
        // 1. Enviar o JSON e verificar o estado e o DTO devolvido.
        var result = mockMvc.perform(post("/api/issues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                            {
                                "title": "Aprender REST",
                                "description": "Testar a criação de uma Task",
                                "priority": "HIGH"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Aprender REST"))
                .andExpect(jsonPath("$.description")
                        .value("Testar a criação de uma Task"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.type").value("TASK"))
                .andReturn();

        // 2. Ler o ID gerado, que não conhecíamos antes do pedido.
        String responseJson = result.getResponse().getContentAsString();
        String id = objectMapper.readTree(responseJson).get("id").asText();

        // 3. Verificar o endereço e confirmar que a Task ficou guardada.
        header().string("Location", "http://localhost/api/issues/" + id)
                .match(result);

        assertTrue(issueService.findById(id).isPresent());
    }

    @Test
    void postIssuesReturnsUnprocessableEntity() throws Exception {
        mockMvc.perform(post("/api/issues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "   ",
                            "description": "Testar a criacao de uma Task",
                            "priority": "HIGH"
                        }
                        """
                )).andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.title").value("must not be blank")).andDo(print());
    }
}
