package pl.gr.veterinaryapp.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.gr.veterinaryapp.mapper.VetMapper;
import pl.gr.veterinaryapp.model.dto.VetRequestDto;
import pl.gr.veterinaryapp.model.dto.VetResponseDto;
import pl.gr.veterinaryapp.model.entity.Vet;
import pl.gr.veterinaryapp.service.VetService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/vets")
public class VetRestController {

    private final VetService vetService;
    private final VetMapper mapper;

    @GetMapping("/{id}")
    public VetResponseDto getVet(@PathVariable Long id) {
        Vet vetById = vetService.getVetById(id);
        return mapper.toVetResponseDto(vetById);
    }

    @PostMapping
    public VetResponseDto addVet(@RequestBody VetRequestDto vetRequestDto) {
        Vet vet = vetService.createVet(vetRequestDto);
        return mapper.toVetResponseDto(vet);
    }

    @GetMapping
    public List<VetResponseDto> getAllVets() {
        List<Vet> allVets = vetService.getAllVets();
        return mapper.toVetsResponseDto(allVets);
    }

    @DeleteMapping("/{id}")
    public void deleteVet(@PathVariable Long id) {
        vetService.deleteVet(id);
    }
}
