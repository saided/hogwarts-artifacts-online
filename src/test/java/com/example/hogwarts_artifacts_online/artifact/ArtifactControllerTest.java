package com.example.hogwarts_artifacts_online.artifact;

import com.example.hogwarts_artifacts_online.artifact.dto.ArtifactDto;
import com.example.hogwarts_artifacts_online.system.StatusCode;
import com.example.hogwarts_artifacts_online.wizard.Wizard;
import com.example.hogwarts_artifacts_online.wizard.dto.WizardDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockmvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ArtifactService artifactService;



    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactsByIdSuccess() throws Exception {
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setName("Invisibility Cloak");
        a.setImageURL("ImageUrl");
        a.setOwner(w);
       //given

        when(artifactService.findById("1250808601744904192")).thenReturn(a);

        //main method calling in controller
        mockmvc.perform(get("/api/v1/artifacts/1250808601744904192"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data.name").value("Invisibility Cloak"));


    }

    @Test
    void testFindArtifactsById_NotFound() throws Exception {
        when(artifactService.findById("1250808601744904193")).thenThrow(new ArtifactNotFoundException("1250808601744904193"));

        mockmvc.perform(get("/api/v1/artifacts/1250808601744904193"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with the id:1250808601744904193"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception{
        List<ArtifactDto> artifactsList = new ArrayList<>();
        Wizard w = new Wizard();
        w.setId(2);
        w.setName("Harry Potter");
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setName("Invisibility Cloak");
        a.setImageURL("ImageUrl");
        a.setOwner(w);
        System.out.println(a.toString());
        WizardDto wizDto = new WizardDto(1,"Harry Potter", 2);
        ArtifactDto expectedDto = new ArtifactDto(a.getId(),a.getName(),a.getDescription(),a.getImageURL(),wizDto);
        artifactsList.add(expectedDto);
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904194");
        a1.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a1.setName("The Marauder's Map");
        a1.setImageURL("ImageUrl");
        a1.setOwner(w);
        ArtifactDto expectedDto1 = new ArtifactDto(a1.getId(),a1.getName(),a1.getDescription(),a1.getImageURL(),wizDto);
        artifactsList.add(expectedDto1);
        when(artifactService.findAllArtifacts()).thenReturn(artifactsList);

        //call the controller method
        mockmvc.perform(get("/api/v1/artifacts"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(artifactsList.size())))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904192"))
                .andExpect(jsonPath("$.data[1].id").value("1250808601744904194"));
    }

    @Test
    void testAddArtifact() throws Exception{

        ArtifactDto newArtifactDto = new ArtifactDto(null,"TestArtifact","Artifact testing","image.jpg",null);
        //for the post request this dto needs to beconverted into json object
        String jsonObj = this.objectMapper.writeValueAsString(newArtifactDto);

        Artifact a = new Artifact();
        a.setId("1250808601744904197");
        a.setName("TestArtifact");
        a.setDescription("Artifact testing");
        a.setImageURL("image.jpg");

        //call the service method
        when(artifactService.saveArtifact(Mockito.any(Artifact.class))).thenReturn(a);

        //controller method and asserts

        mockmvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(jsonObj))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("TestArtifact"));

    }
    @Test
    void testUpdateArtifactSuccess() throws Exception{
        String artifactId = "1250808601744904191";
        ArtifactDto aDto = new ArtifactDto(null,"Deluminator-update",
                                           "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update",
                                            "imagerurl-updated",null);
        String jsonObj = this.objectMapper.writeValueAsString(aDto);
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId(artifactId);
        updatedArtifact.setName(aDto.name());
        updatedArtifact.setDescription(aDto.description());
        updatedArtifact.setImageURL(aDto.imageURL());

        //call the service method
        when(artifactService.updateArtifact(aDto,artifactId)).thenReturn(updatedArtifact);

        mockmvc.perform(put("/api/v1/artifacts/1250808601744904191").contentType(MediaType.APPLICATION_JSON)
                .content(jsonObj))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(artifactId))
                .andExpect(jsonPath("$.data.name").value(aDto.name()))
                .andExpect(jsonPath("$.data.imageURL").value(aDto.imageURL()));
    }

    @Test
    void testUpdateArtifacts_NotFound() throws Exception {
        ArtifactDto aDto = new ArtifactDto(null,"Deluminator-update","A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update","ImageUrl-update",null);
        String jsonObj = this.objectMapper.writeValueAsString(aDto);
        when(artifactService.updateArtifact(aDto,"1250808601744904198")).thenThrow(new ArtifactNotFoundException("1250808601744904198"));
       // when(artifactService.updateArtifact(invalidDto,"1250808601744904191")).thenThrow(new MethodArgumentNotValidException(null));



        mockmvc.perform(put("/api/v1/artifacts/1250808601744904198").contentType(MediaType.APPLICATION_JSON).content(jsonObj))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with the id:1250808601744904198"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testUpdateArtifact_InvalidArguments_ShouldReturnBadRequest() throws Exception {
        ArtifactDto invalidDto = new ArtifactDto(null,"Deluminator-update","","image-url-udated",null);
        String invalidDtoJson = this.objectMapper.writeValueAsString(invalidDto);

        mockmvc.perform(put("/api/v1/artifacts/1250808601744904191").contentType(MediaType.APPLICATION_JSON).content(invalidDtoJson))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."))
                .andExpect(jsonPath("$.data.description").value("description is required"));

    }

    @Test
    void testDeleteArtifactById_Success() throws Exception{
        String artifactId = "1895942083416559616";

        //call the service method
        doNothing().when(this.artifactService).deleteArtifactById("1895942083416559616");
       // when(artifactService.deleteArtifactById(artifactId))

        mockmvc.perform(delete("/api/v1/artifacts/1895942083416559616"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(artifactService,times(1)).deleteArtifactById(artifactId);
    }

    @Test
    void testDeleteArtifactById_NotFound() throws Exception{
        //Given
        doThrow(new ArtifactNotFoundException("1895942083416559619")).when(this.artifactService).deleteArtifactById("1895942083416559619");


        mockmvc.perform(delete("/api/v1/artifacts/1895942083416559619"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with the id:1895942083416559619"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

}