package com.example.hogwarts_artifacts_online.wizard;

import com.example.hogwarts_artifacts_online.artifact.Artifact;
import com.example.hogwarts_artifacts_online.system.StatusCode;
import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {

    @Autowired
    MockMvc mockmvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    WizardService wizardService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindWizardById_Success() throws Exception {
        //Mock the final data to compare
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");

        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setName("Invisibility Cloak");
        a.setImageURL("ImageUrl");
        a.setOwner(w);
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904194");
        a1.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a1.setName("The Marauder's Map");
        a1.setImageURL("ImageUrl");
        a1.setOwner(w);
        //Mock the wizard data
        List<Artifact> artifactList = List.of(a,a1);
        w.setArtifacts(artifactList);

        //call the service method
        when(wizardService.getWizardById(2)).thenReturn(w);
        //perform assertions
        mockmvc.perform(get("/api/v1/wizards/2"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(w.getId()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(w.getNumofArtifacts()));
    }
}