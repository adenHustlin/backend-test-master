package kr.co.polycube.backendtest.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.polycube.backendtest.model.Lotto;
import kr.co.polycube.backendtest.model.Winner;
import kr.co.polycube.backendtest.repository.LottoRepository;
import kr.co.polycube.backendtest.repository.WinnerRepository;

@Component
public class LottoBatch {

    @Autowired
    private LottoRepository lottoRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @Scheduled(cron = "0 0 0 * * SUN")
    public void checkWinners() {
        int[] winningNumbers = generateWinningNumbers();
        List<Lotto> lottos = lottoRepository.findAll();

        Map<Lotto, Integer> lottoMatchCounts = new HashMap<>();
        for (Lotto lotto : lottos) {
            int matchCount = getMatchCount(lotto, winningNumbers);
            lottoMatchCounts.put(lotto, matchCount);
        }

        List<Map.Entry<Lotto, Integer>> sortedLottos = lottoMatchCounts.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toList());

        int rank = 1;
        for (Map.Entry<Lotto, Integer> entry : sortedLottos) {
            Winner winner = new Winner();
            winner.setLottoId(entry.getKey().getId());
            winner.setRank(rank);
            winnerRepository.save(winner);
            rank++;
        }
    }

    private int[] generateWinningNumbers() {
        Random random = new Random();
        return random.ints(1, 46).distinct().limit(6).toArray();
    }

    private int getMatchCount(Lotto lotto, int[] winningNumbers) {
        int[] lottoNumbers = { lotto.getNumber1(), lotto.getNumber2(), lotto.getNumber3(), lotto.getNumber4(),
                lotto.getNumber5(), lotto.getNumber6() };
        int matchCount = 0;
        for (int num : lottoNumbers) {
            for (int winNum : winningNumbers) {
                if (num == winNum) {
                    matchCount++;
                }
            }
        }
        return matchCount;
    }
}
