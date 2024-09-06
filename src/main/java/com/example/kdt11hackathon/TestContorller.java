package com.example.kdt11hackathon;

import com.example.kdt11hackathon.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestContorller {
    private final ProblemService problemService;

    @GetMapping("/test")
    public void test(){
        problemService.simpletest();
    }
}
