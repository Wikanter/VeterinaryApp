package pl.gr.veterinaryapp.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gr.veterinaryapp.mapper.AnimalMapper;
import pl.gr.veterinaryapp.model.dto.AnimalRequestDto;
import pl.gr.veterinaryapp.model.dto.AnimalResponseDto;
import pl.gr.veterinaryapp.model.entity.Animal;
import pl.gr.veterinaryapp.service.AnimalService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/animals")
@Slf4j
public class AnimalRestController {

    private final AnimalService animalService;
    private final AnimalMapper mapper;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    public AnimalResponseDto getAnimal(@PathVariable Long id) {
        log.info("received request on GET api/animals/{}", id);
        Animal animalById = animalService.getAnimalById(id);
        return mapper.toAnimalResponseDto(animalById);
    }

    @PostMapping
    public AnimalResponseDto createAnimal(@RequestBody AnimalRequestDto animalRequestDto) throws JsonProcessingException {
        log.info("received request on POST api/animals with body: {}", objectMapper.writeValueAsString(animalRequestDto));
        Animal animal = animalService.createAnimal(animalRequestDto);
        return mapper.toAnimalResponseDto(animal);
    }

    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable Long id) {
        log.info("received request on DELETE api/animals/{}", id);
        animalService.deleteAnimal(id);
    }

    @GetMapping
    public List<AnimalResponseDto> getAllAnimals() {
        log.info("received request on GET api/animals");
        List<Animal> allAnimals = animalService.getAllAnimals();
        return mapper.toAnimalsResponseDto(allAnimals);
    }
}
