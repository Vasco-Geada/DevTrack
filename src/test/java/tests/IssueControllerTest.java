package tests;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import devtrack.DevTrackApplication;
import devtrack.model.Enums.ActivityType;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;
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
    void getIssuesFiltersReturnsOk() throws Exception {
        mockMvc.perform(get("/api/issues").param("status", Status.OPEN.toString()).param("priority", Priority.HIGH.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(print());
    }

    @Test
    void getIssuesFiltersReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/issues").param("status", "HIGH").param("priority", Priority.HIGH.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.path").value("/api/issues"))
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
        //Empty Title
        mockMvc.perform(post("/api/issues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "   ",
                            "description": "Testar a criacao de uma Task",
                            "priority": "HIGH"
                        }
                        """
                )).andExpect(status().isUnprocessableEntity()).andExpect(jsonPath("$.fieldErrors.title").value("must not be blank"))
                .andExpect(jsonPath("$.status").value("UNPROCESSABLE_ENTITY"))
                .andExpect(jsonPath("$.path").value("/api/issues"))
                .andExpect(jsonPath("$.message").value("Invalid data")).andDo(print());

        // Invalid JSON
        mockMvc.perform(post("/api/issues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "   ",
                            "description": "",
                        }
                        """
                )).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.path").value("/api/issues"))
                .andDo(print());

        //Invalid Enum
        mockMvc.perform(post("/api/issues")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "   ",
                            "description": "Testar a criacao de uma Task",
                            "priority": "OPEN"
                        }
                        """
                )).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.path").value("/api/issues"))
                .andDo(print());
    }

    @Test
    void patchIssuesReturnsOkay() throws Exception {

        Task task = new Task(
                "Aprender REST",
                "Testar a consulta de uma Task",
                Priority.HIGH
        );

        issueService.createIssue(task);

        mockMvc.perform(patch("/api/issues/{id}/start", task.getId()))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void patchIssuesReturnsConflict() throws Exception {

        Task task = new Task(
                "Aprender REST",
                "Testar a consulta de uma Task",
                Priority.HIGH,
                Status.IN_PROGRESS
        );

        issueService.createIssue(task);

        mockMvc.perform(patch("/api/issues/{id}/start", task.getId()))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.fieldErrors.error").value("Only an open issue can start")).andDo(print());
    }

    @Test
    void deleteIssuesReturnsOkay() throws Exception {

        Task task = new Task(
                "Aprender REST",
                "Testar a consulta de uma Task",
                Priority.HIGH,
                Status.IN_PROGRESS
        );

        issueService.createIssue(task);

        mockMvc.perform(delete("/api/issues/{id}", task.getId()))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    void JSONIssuesReturnsConflict() throws Exception {

        Task task = new Task(
                "Aprender REST",
                "Testar a consulta de uma Task",
                Priority.HIGH,
                Status.IN_PROGRESS
        );

        issueService.createIssue(task);

        mockMvc.perform(delete("/api/issues/{id}", task.getId()))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    void FiltersIssuesReturnsOkay() throws Exception {

        Task task = new Task(
                "Aprender REST",
                "Testar a consulta de uma Task",
                Priority.HIGH,
                Status.IN_PROGRESS
        );
        Task task2 = new Task(
                "Título de teste",
                "Testar a consulta de uma Task",
                Priority.LOW,
                Status.OPEN
        );

        issueService.createIssue(task);
        issueService.createIssue(task2);

        //Status
        mockMvc.perform(get("/api/issues", task.getId()).param("status", Status.IN_PROGRESS.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].id").value(task.getId())).andDo(print());

        // Priority
        mockMvc.perform(get("/api/issues", task.getId()).param("priority", Priority.LOW.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].id").value(task2.getId())).andDo(print());

        //Title 
        mockMvc.perform(get("/api/issues", task.getId()).param("q", " aprender REST"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].id").value(task.getId())).andDo(print());
        //Status + Priority
        mockMvc.perform(get("/api/issues", task.getId()).param("status", Status.OPEN.toString()).param("priority", Priority.LOW.toString()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].id").value(task2.getId())).andDo(print());

        //Status + Title
        mockMvc.perform(get("/api/issues", task.getId()).param("status", Status.OPEN.toString()).param("q", "Título "))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(task2.getId())).andDo(print());

        //Priority + Title
        mockMvc.perform(get("/api/issues", task.getId()).param("priority", Priority.HIGH.toString()).param("q", "resT           "))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(task.getId())).andDo(print());

        //Status + Priority + Title
        mockMvc.perform(get("/api/issues", task.getId()).param("priority", Priority.HIGH.toString()).param("status", Status.IN_PROGRESS.toString()).param("q", " aprender "))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(task.getId())).andDo(print());
    }

    @Test
    void startIssueUpdatesStateDateAndActivityLog() throws Exception {
        // Preparar a Task e guardar os valores anteriores.
        Task task = new Task("Testar início");
        issueService.createIssue(task);

        var originalCreatedAt = task.getCreatedAt();
        int originalLogSize = task.getActivityLog().size();

        LocalDateTime beforeRequest = LocalDateTime.now();

        // Executar a operação através da API.
        mockMvc.perform(patch("/api/issues/{id}/start", task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        LocalDateTime afterRequest = LocalDateTime.now();
        var updatedIssue = issueService.getIssueById(task.getId());

        // O estado mudou; a data de criação foi conservada.
        assertEquals(Status.IN_PROGRESS, updatedIssue.getStatus());
        assertEquals(originalCreatedAt, updatedIssue.getCreatedAt());

        // updatedAt corresponde ao intervalo em que ocorreu a operação.
        assertTrue(!updatedIssue.getUpdatedAt().isBefore(beforeRequest));
        assertTrue(!updatedIssue.getUpdatedAt().isAfter(afterRequest));

        // Foi acrescentada uma atividade com os dados da transição.
        assertEquals(originalLogSize + 1, updatedIssue.getActivityLog().size());

        var activity = updatedIssue.getActivityLog().getLast();

        assertEquals(ActivityType.STATUS_CHANGED, activity.getType());
        assertEquals("OPEN -> IN_PROGRESS", activity.getDescription());
        assertEquals(updatedIssue.getUpdatedAt(), activity.getCreatedAt());
    }

    @Test
    void invalidStartPreservesStateDatesAndActivityLog() throws Exception {
        // Preparar uma Task já iniciada, com uma atividade no histórico.
        Task task = new Task("Testar início inválido");
        issueService.createIssue(task);
        issueService.startIssue(task.getId());

        // Guardar uma fotografia dos valores anteriores ao pedido inválido.
        var originalStatus = task.getStatus();
        var originalCreatedAt = task.getCreatedAt();
        var originalUpdatedAt = task.getUpdatedAt();
        var originalLog = List.copyOf(task.getActivityLog());

        // Iniciar novamente não é permitido.
        mockMvc.perform(patch("/api/issues/{id}/start", task.getId()))
                .andExpect(status().isConflict());

        var unchangedIssue = issueService.getIssueById(task.getId());

        // A operação rejeitada não deve alterar nenhum destes valores.
        assertEquals(originalStatus, unchangedIssue.getStatus());
        assertEquals(originalCreatedAt, unchangedIssue.getCreatedAt());
        assertEquals(originalUpdatedAt, unchangedIssue.getUpdatedAt());
        assertEquals(originalLog, unchangedIssue.getActivityLog());
    }
}
