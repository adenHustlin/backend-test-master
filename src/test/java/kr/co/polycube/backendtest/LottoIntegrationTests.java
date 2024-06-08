package kr.co.polycube.backendtest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import kr.co.polycube.backendtest.batch.LottoBatch;
import kr.co.polycube.backendtest.model.Lotto;
import kr.co.polycube.backendtest.model.Winner;
import kr.co.polycube.backendtest.repository.LottoRepository;
import kr.co.polycube.backendtest.repository.WinnerRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LottoIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LottoRepository lottoRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @Autowired
    private LottoBatch lottoBatch;

    @BeforeEach
    public void setUp() {
        lottoRepository.deleteAll();
        winnerRepository.deleteAll();
    }

    @Test
    void testGenerateLottoNumbers() {
        ResponseEntity<Lotto> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/lottos", null,
                Lotto.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Lotto lotto = responseEntity.getBody();
        assertThat(lotto).isNotNull();
        assertThat(lotto.getNumber1()).isBetween(1, 45);
        assertThat(lotto.getNumber2()).isBetween(1, 45);
        assertThat(lotto.getNumber3()).isBetween(1, 45);
        assertThat(lotto.getNumber4()).isBetween(1, 45);
        assertThat(lotto.getNumber5()).isBetween(1, 45);
        assertThat(lotto.getNumber6()).isBetween(1, 45);
    }

    @Test
    void testCheckWinners() {
        Lotto lotto1 = new Lotto();
        lotto1.setNumber1(1);
        lotto1.setNumber2(2);
        lotto1.setNumber3(3);
        lotto1.setNumber4(4);
        lotto1.setNumber5(5);
        lotto1.setNumber6(6);
        lottoRepository.save(lotto1);

        Lotto lotto2 = new Lotto();
        lotto2.setNumber1(7);
        lotto2.setNumber2(8);
        lotto2.setNumber3(9);
        lotto2.setNumber4(10);
        lotto2.setNumber5(11);
        lotto2.setNumber6(12);
        lottoRepository.save(lotto2);

        lottoBatch.checkWinners();

        List<Winner> winners = winnerRepository.findAll();
        assertThat(winners.size()).isEqualTo(2); // 2명의 로또 구매자가 있으므로 당첨자도 2명이어야 함

        // 랭크가 1부터 2까지 있는지 확인
        assertThat(winners.stream().map(Winner::getRank).collect(Collectors.toSet())).containsExactlyInAnyOrder(1, 2);
    }
}
