package org.example.service;

import org.example.exception.FileProperlyReadException;
import org.example.model.BookMarket;
import org.example.model.Detail;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataProcessedService {
    private BookMarket bookMarket;
    private List<String> requiredForPrinting;
    private static final String BID_TYPE = "bid";
    private static final String ASK_TYPE = "ask";
    private static final String BEST_BID_REQUEST = "best_bid";
    private static final String BEST_ASK_REQUEST = "best_ask";
    private static final String UPDATE_OPTION = "u";
    private static final String PRINT_OPTION = "q";
    private static final String ORDER_OPTION = "o";
    private static final String BUY_ACTION_TYPE = "buy";
    private static final String SELL_ACTION_TYPE = "sell";
    private static final String RESULT_FILE = "output.txt";

    private static final String ERROR_PROPERLY_READ = "Can't read data properly";

    public void readAndWriteResult(String fileName) throws IOException {
        bookMarket = new BookMarket();
        requiredForPrinting = new ArrayList<>();
        String[] lines = readTheFile(fileName);
        proceedData(lines);
        writeFile();
    }

    private String[] readTheFile(String fileName) throws IOException {
        File file = new File(fileName);
        Reader reader = new FileReader(file);
        String[] content;
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            content = bufferedReader.lines()
                    .collect(Collectors.joining(System.lineSeparator())).split("\n");
        }
        return content;
    }

    private void writeFile() throws IOException {
        try (BufferedWriter br = new BufferedWriter(new FileWriter(RESULT_FILE))) {
            for (String str : requiredForPrinting) {
                br.write(str + System.lineSeparator());
            }
        }
    }

    private void proceedData(String[] line) {
        for (String elem : line) {
            String[] parsedLine = Arrays.stream(elem.split(","))
                    .filter(el -> el.trim().length() > 0)
                    .toArray(String[]::new);
            String requiredOperation = parsedLine[0];
            switch (requiredOperation) {
                case UPDATE_OPTION:
                    updateOrderBook(parsedLine[1], parsedLine[2], parsedLine[3]);
                    break;
                case PRINT_OPTION:
                    findAndPrint(elem);
                    break;
                case ORDER_OPTION:
                    handlingOrder(elem);
                    break;
                default:
                    throw new FileProperlyReadException(ERROR_PROPERLY_READ);
            }
        }
    }

    private void handlingOrder(String line) {
        String[] parsedLine = Arrays.stream(line.split(","))
                .filter(el -> el.trim().length() > 0)
                .toArray(String[]::new);
        if (parsedLine[1].equals(BUY_ACTION_TYPE)) {
            Optional<Detail> bestAsk = getBestAskDetail();
            if (bestAsk.isPresent()) {
                Integer currentSize = bestAsk.get().getSize();
                bestAsk.get().setSize(currentSize - Integer.valueOf(parsedLine[2]));
            }
        } else if (parsedLine[1].equals(SELL_ACTION_TYPE)) {
            Optional<Detail> bestAsk = getBestAskDetail();
            Optional<Detail> bestBid = Optional.ofNullable(bookMarket.getBid().stream()
                    .filter(bid -> bid.getSize() >= 0 && bid.getSize() < bestAsk.get().getSize())
                    .sorted(Comparator.comparing(Detail::getPrice)
                            .thenComparing(Detail::getSize)
                            .reversed())
                    .findFirst()
                    .orElseThrow(() -> new FileProperlyReadException(ERROR_PROPERLY_READ)));
            if (bestBid.isPresent()) {
                Integer currentSize = bestBid.get().getSize();
                bestBid.get().setSize(currentSize - Integer.valueOf(parsedLine[2]));
            }
        }
    }

    private Optional<Detail> getBestAskDetail() {
        return Optional.ofNullable(bookMarket.getAsk().stream()
                .filter(ask -> ask.getSize() >= 0)
                .sorted(Comparator.comparing(Detail::getPrice)
                        .thenComparing(Detail::getSize))
                .findFirst()
                .orElseThrow(() -> new FileProperlyReadException(ERROR_PROPERLY_READ)));

    }

    private void updateOrderBook(String price, String size, String type) {
        Detail detail = new Detail(Integer.valueOf(price), Integer.valueOf(size));
        if (type.equals(ASK_TYPE)) {
            bookMarket.getAsk().add(detail);
        } else if (type.equals(BID_TYPE)) {
            bookMarket.getBid().add(detail);
        } else {
            throw new FileProperlyReadException(ERROR_PROPERLY_READ);
        }
    }

    private void findAndPrint(String line) {
        String[] parsedLine = Arrays.stream(line.split(","))
                .filter(el -> el.trim().length() > 0)
                .toArray(String[]::new);
        if (parsedLine.length == 3) {
            printSizeAtSpecifiedPrice(parsedLine);
        } else if (parsedLine[1].equals(BEST_BID_REQUEST)) {
            printBestBid();
        } else if (parsedLine[1].equals(BEST_ASK_REQUEST)) {
            printBestAsk();
        } else {
            throw new FileProperlyReadException(ERROR_PROPERLY_READ);
        }

    }

    private void printBestAsk() {
        Optional<Detail> bestAskResult = getBestAskDetail();
        if (bestAskResult.isPresent()) {
            requiredForPrinting.add(bestAskResult.get().getPrice() + "," + bestAskResult.get().getSize());
        }
    }

    private Optional<Detail> getBestBid() {
        return Optional.ofNullable(bookMarket.getBid().stream()
                .sorted(Comparator.comparing(Detail::getPrice)
                        .thenComparing(Detail::getSize)
                        .reversed())
                .findFirst()
                .orElseThrow(() -> new FileProperlyReadException(ERROR_PROPERLY_READ)));
    }

    private void printBestBid() {
        Optional<Detail> bestBidResult = getBestBid();
        bestBidResult.ifPresent(detail -> requiredForPrinting.add(detail.getPrice() + "," + detail.getSize()));
    }

    private void printSizeAtSpecifiedPrice(String[] parsedLine) {
        determineSpread();
        Integer requiredPrice = Integer.valueOf(parsedLine[2]);
        Optional<Detail> foundedAsk = bookMarket.getAsk().stream()
                .filter(el -> el.getPrice().equals(requiredPrice))
                .findFirst();

        Optional<Detail> foundedBid = bookMarket.getBid().stream()
                .filter(el -> el.getPrice().equals(requiredPrice))
                .findFirst();

        Optional<Detail> foundedSpread = bookMarket.getBid().stream()
                .filter(el -> el.getPrice().equals(requiredPrice))
                .findFirst();


        foundedBid.ifPresent(detail -> requiredForPrinting.add(String.valueOf(detail.getSize())));
        foundedAsk.ifPresent(detail -> requiredForPrinting.add(String.valueOf(detail.getSize())));
        foundedSpread.ifPresent(detail -> requiredForPrinting.add(String.valueOf(detail.getSize())));
    }

    private void determineSpread() {
        List<Detail> sortedAsk = bookMarket.getBid().stream()
                .sorted(Comparator.comparing(Detail::getPrice)
                        .thenComparing(Comparator.comparing(Detail::getSize)))
                .collect(Collectors.toList());

        Optional<Detail> bestBid = getBestBid();
        if (bestBid.isPresent() && !sortedAsk.isEmpty()) {
            Integer askMin = sortedAsk.get(0).getPrice();
            Integer bitMaxPrise = bestBid.get().getPrice();
            for (int i = bitMaxPrise + 1; i < askMin; i++) {
                bookMarket.getSpread().add(new Detail(i, 0));
            }
        }
    }
}
