package kr.co.polycube.backendtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.polycube.backendtest.model.Lotto;
import kr.co.polycube.backendtest.service.LottoService;

@RestController
@RequestMapping("/lottos")
public class LottoController {

    @Autowired
    private LottoService lottoService;

    @PostMapping
    public Lotto createLotto() {
        return lottoService.generateLotto();
    }
}
