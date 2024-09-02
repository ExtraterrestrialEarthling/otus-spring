package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.Order;
import ru.otus.hw.domain.OrderType;
import ru.otus.hw.domain.Receipt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderGenerationServiceImpl implements OrderGenerationService {

    private static final List<String> STOCK = List.of("smartphone", "computer", "car",
            "speakers", "keyboard", "headphones", "smart watch", "mouse");

    private static final Random RANDOM = new Random();

    private final OrderGateway shop;

    @Override
    public void startGenerateOrdersLoop() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            int num = i + 1;
            pool.execute(() -> {
                List<Item> items = generateItems();
                log.info("{}, Creating order with items: {}", num,
                        items.stream().map(Item::getName)
                                .collect(Collectors.joining(", ")));
                OrderType randomType = OrderType.values()[RANDOM.nextInt(2)];
                Receipt receipt = shop.process(new Order(items, randomType));
                log.info("{}, Receipt: {}", num, receipt.toString());
            });
            delay();
        }
    }

    private static Item generateItem() {
        return new Item(STOCK.get(RANDOM.nextInt(0, STOCK.size())),
                new BigDecimal(RANDOM.nextInt(100000)));
    }

    private static List<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < RANDOM.nextInt(1, 5); ++i) {
            items.add(generateItem());
        }
        return items;
    }

    private void delay() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
