package pl.gr.veterinaryapp.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.gr.veterinaryapp.mapper.VisitMapper;
import pl.gr.veterinaryapp.model.dto.AvailableVisitDto;
import pl.gr.veterinaryapp.model.dto.VisitEditDto;
import pl.gr.veterinaryapp.model.dto.VisitRequestDto;
import pl.gr.veterinaryapp.model.dto.VisitResponseDto;
import pl.gr.veterinaryapp.service.VisitService;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@RequestMapping("api/visits")
@RestController
public class VisitRestController {

    private final VisitService visitService;
    private final VisitMapper mapper;

    @DeleteMapping(path = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public void delete(@PathVariable Long id) {
        visitService.deleteVisit(id);
    }

    @GetMapping(path = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public VisitResponseDto getVisit(@AuthenticationPrincipal User user, @PathVariable Long id) {
        var visit = mapper.toVisitResponseDto(visitService.getVisitById(user, id));
        addLinks(visit);
        return visit;
    }

    @GetMapping(path = "/available", produces = MediaTypes.HAL_JSON_VALUE)
    public List<AvailableVisitDto> getAvailableVisits(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") OffsetDateTime startDateTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") OffsetDateTime endDateTime,
            @RequestParam(required = false) List<Long> vetIds) {
        Set<Long> vetIdsSet = Optional.ofNullable(vetIds)
                .map(ids -> ids.stream().filter(Objects::nonNull).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        var availableVisits = visitService
                .getAvailableVisits(startDateTime, endDateTime, vetIdsSet);

        availableVisits.forEach(availableVisit -> availableVisit.getVetIds()
                .forEach(vetId -> availableVisit.add(createVetLink(vetId))));

        return availableVisits;
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public List<VisitResponseDto> getAllVisits(@AuthenticationPrincipal User user) {
        var visits = mapper.toVisitsResponseDto(visitService.getAllVisits(user));

        for (var visit : visits) {
            addLinks(visit);
            var link = linkTo(methodOn(VisitRestController.class).getVisit(user, visit.getId()))
                    .withSelfRel();
            visit.add(link);
        }
        return visits;
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public VisitResponseDto createVisit(@AuthenticationPrincipal User user,
                                        @RequestBody VisitRequestDto visitRequestDto) {
        var visit = mapper.toVisitResponseDto(visitService.createVisit(user, visitRequestDto));
        addLinks(visit);
        return visit;
    }

    @PatchMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public VisitResponseDto finalizeVisit(@RequestBody VisitEditDto visitEditDto) {
        var visit = mapper.toVisitResponseDto(visitService.finalizeVisit(visitEditDto));
        addLinks(visit);
        return visit;
    }

    public Link createVetLink(Long id) {
        return linkTo(VetRestController.class)
                .slash(id)
                .withRel("vet");
    }

    public Link createPetLink(Long id) {
        return linkTo(PetRestController.class)
                .slash(id)
                .withRel("pet");
    }

    public void addLinks(VisitResponseDto visitResponseDto) {
        visitResponseDto.add(createVetLink(visitResponseDto.getVetId()),
                createPetLink(visitResponseDto.getPetId()));
    }
}
