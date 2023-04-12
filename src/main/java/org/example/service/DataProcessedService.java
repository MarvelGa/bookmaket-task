package org.example.service;

import org.example.exception.FileProperlyReadException;
import org.example.model.BookMarket;
import org.example.model.Detail;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
                    .collect(Collectors.joining(System.lineSeparator())).split("\r\n");
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
            String[] parsedLine = elem.split(",");
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
        String[] parsedLine = line.split(",");
        if (parsedLine[1].equals(BUY_ACTION_TYPE)) {
            if (bookMarket.getAsk().isEmpty()) {
                requiredForPrinting.add("0");
            } else {
                List<Detail> sortedDetail = bookMarket.getAsk().stream()
                        .sorted(Comparator.comparing(Detail::getPrice)
                                .thenComparing(Detail::getSize))
                        .collect(Collectors.toList());
                Integer currentSize = sortedDetail.get(0).getSize();
                if (currentSize > 0) {
                    sortedDetail.get(0).setSize(currentSize - Integer.valueOf(parsedLine[2]));
                    bookMarket.setAsk(sortedDetail);
                }
            }
        } else if (parsedLine[1].equals(SELL_ACTION_TYPE)) {
            if (bookMarket.getAsk().isEmpty()) {
                requiredForPrinting.add("0");
            } else {
                List<Detail> sortedDetail = bookMarket.getBid().stream()
                        .sorted(Comparator.comparing(Detail::getPrice)
                                .thenComparing(Detail::getSize)
                                .reversed())
                        .collect(Collectors.toList());
                Integer currentSize = sortedDetail.get(0).getSize();
                if (currentSize > 0) {
                    sortedDetail.get(0).setSize(currentSize - Integer.valueOf(parsedLine[2]));
                    bookMarket.setBid(sortedDetail);
                }
            }
        } else {
            throw new FileProperlyReadException(ERROR_PROPERLY_READ);
        }
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
        String[] parsedLine = line.split(",");
        if (parsedLine.length == 3) {
            printSizeAtSpecifiedPrice(parsedLine);
        } else if (parsedLine[1].equals(BEST_BID_REQUEST)) {
            printBestBitSizeAndPrice();
        } else if (parsedLine[1].equals(BEST_ASK_REQUEST)) {
            printBestAskSizeAndPrice();
        } else {
            throw new FileProperlyReadException(ERROR_PROPERLY_READ);
        }

    }

    private void printBestAskSizeAndPrice() {
        if (bookMarket.getAsk().isEmpty()) {
            requiredForPrinting.add("0");
        } else {
            Optional<Detail> bestAskResult = bookMarket.getAsk().stream()
                    .sorted(Comparator.comparing(Detail::getPrice)
                            .thenComparing(Detail::getSize)
                            .reversed())
                    .findFirst();
            if (bestAskResult.isPresent()) {
                requiredForPrinting.add(bestAskResult.get().getPrice() + "," + bestAskResult.get().getSize());
            } else {
                requiredForPrinting.add("0");
            }
        }
    }

    private void printBestBitSizeAndPrice() {
        if (bookMarket.getBid().isEmpty()) {
            requiredForPrinting.add("0");
        } else {
            Optional<Detail> bestBidResult = bookMarket.getBid().stream()
                    .sorted(Comparator.comparing(Detail::getPrice)
                            .thenComparing(Detail::getSize)
                            .reversed())
                    .findFirst();
            if (bestBidResult.isPresent()) {
                requiredForPrinting.add(bestBidResult.get().getPrice() + "," + bestBidResult.get().getSize());
            } else {
                requiredForPrinting.add("0");
            }
        }
    }

    private void printSizeAtSpecifiedPrice(String[] parsedLine) {
        Integer requiredPrice = Integer.valueOf(parsedLine[2]);
        int[] resultOfAsk = bookMarket.getAsk().stream()
                .filter(el -> el.getPrice().equals(requiredPrice))
                .mapToInt(Detail::getSize)
                .toArray();

        int[] resultOfBid = bookMarket.getBid().stream()
                .filter(el -> el.getPrice().equals(requiredPrice))
                .mapToInt(Detail::getSize)
                .toArray();

        if (resultOfBid.length != 0 && resultOfAsk.length != 0) {
            int totalSize = resultOfBid[0] + resultOfAsk[0];
            requiredForPrinting.add(String.valueOf(totalSize));
        } else if (resultOfBid.length != 0) {
            int bidSize = resultOfBid[0];
            requiredForPrinting.add(String.valueOf(bidSize));
        } else if (resultOfAsk.length != 0) {
            int askSize = resultOfAsk[0];
            requiredForPrinting.add(String.valueOf(askSize));
        }
    }
}
