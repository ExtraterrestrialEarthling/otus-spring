package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.otus.hw.dto.request.SkillDtoRq;
import ru.otus.hw.dto.response.SkillDtoRs;
import ru.otus.hw.mapper.SkillMapper;
import ru.otus.hw.models.Skill;
import ru.otus.hw.repositories.SkillRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/skills")
public class SkillController {

    private final SkillMapper skillMapper;

    private final SkillRepository skillRepository;


    @GetMapping("/{id}")
    public Mono<ResponseEntity<SkillDtoRs>> getSkillById(@PathVariable String id) {
        return skillRepository.findById(id)
                .map(skill -> skillMapper.entityToDtoRs(skill))
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<List<SkillDtoRs>>> getAllSkills() {
        return skillRepository.findAll()
                .map(skillMapper::entityToDtoRs)
                .collectList()
                .map(ResponseEntity::ok);
    }

    @PostMapping()
    public Mono<ResponseEntity<SkillDtoRs>> createSkill(@Valid @RequestBody SkillDtoRq skillDtoRq) {
        return skillRepository.save(new Skill(null, skillDtoRq.getName()))
                .map(savedSkill -> ResponseEntity.status(201).body(skillMapper.entityToDtoRs(savedSkill)))
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }
}