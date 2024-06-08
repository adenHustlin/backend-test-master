package kr.co.polycube.backendtest.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.polycube.backendtest.model.Lotto;
import kr.co.polycube.backendtest.repository.LottoRepository;

@Service
public class LottoService {
    @Autowired
    private LottoRepository lottoRepository;

    public Lotto generateLotto() {
        Random random = new Random();
        Lotto lotto = new Lotto();
        lotto.setNumber1(random.nextInt(45) + 1);
        lotto.setNumber2(random.nextInt(45) + 1);
        lotto.setNumber3(random.nextInt(45) + 1);
        lotto.setNumber4(random.nextInt(45) + 1);
        lotto.setNumber5(random.nextInt(45) + 1);
        lotto.setNumber6(random.nextInt(45) + 1);
        return lottoRepository.save(lotto);
    }

    public List<Lotto> getAllLottos() {
        return lottoRepository.findAll();
    }
}
