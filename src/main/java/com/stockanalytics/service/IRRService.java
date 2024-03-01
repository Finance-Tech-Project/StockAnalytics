package com.stockanalytics.service;

import com.stockanalytics.dto.IrrDto;
import com.stockanalytics.dto.StockQuoteDto;
import com.stockanalytics.model.Dividend;
import com.stockanalytics.model.StockQuote;
import com.stockanalytics.model.Symbol;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IRRService {

    private final DividendService dividendService;
    private final StockQuoteService stockQuoteService;
    private final SymbolService symbolService;

    /**
     * Calculates the historical Internal Rate of Return (IRR) for a given stock ticker,
     * over a specified date range, starting with a specified amount.
     *
     * @param dateFrom    The start date of the period for the IRR calculation.
     * @param dateTo      The end date of the period for the IRR calculation.
     * @param startAmount The initial investment amount.
     * @param ticker      The stock ticker symbol for which to calculate the IRR.
     * @return The calculated historical IRR as a double.
     */
    public double calculateHistoricalIRR(LocalDate dateFrom, LocalDate dateTo, Double startAmount, String ticker) {
        // Inside the calculateHistoricalIRR method
        Symbol symbol = symbolService.getSymbol(ticker);

        List<Double> cashFlows = new ArrayList<>();
        cashFlows.add(-startAmount); // Initial investment as a negative cash flow

        // Calculate number of shares
        StockQuote firstQuote = stockQuoteService.getSingleDate(symbol, dateFrom);
        Double numberOfShares = startAmount / firstQuote.getOpen().doubleValue();

        // Add dividends
//        List<Dividend> dividends = dividendService.getData(symbol, dateFrom.plusDays(1), dateTo);
//        for (int i = 0; i < dividends.size(); i++) {
//            cashFlows.add(numberOfShares * dividends.get(i).getDividendRate().doubleValue()); // Add all dividends as positive cash flows
//        }

        // Calculate final amount
        StockQuote lastQuote = stockQuoteService.getSingleDate(symbol, dateTo);
        Double finalAmount = numberOfShares * lastQuote.getClose().doubleValue();

        // Add the final sale value as a positive cash flow
        cashFlows.add(finalAmount);

        double irr = calculateIRR(cashFlows) * 100; // Leverage an existing IRR calculation method
        return irr;
    }


    public static double calculateIRR(List<Double> cashFlows) {
        final double TOLERANCE = 1e-7;
        double x0 = 0.0;
        double x1 = 0.1; // Initial guess
        double npv = npv(x1, cashFlows);

        while (Math.abs(npv) > TOLERANCE) {
            x1 = x1 - npv / npvDerivative(x1, cashFlows);
            npv = npv(x1, cashFlows);
        }

        return x1;
    }


    private static double npv(double rate, List<Double> cashFlows) {
        double npv = 0.0;
        for (int t = 0; t < cashFlows.size(); t++) {
            npv += cashFlows.get(t) / Math.pow(1 + rate, t);
        }
        return npv;
    }

    private static double npvDerivative(double rate, List<Double> cashFlows) {
        double npvDerivative = 0.0;
        for (int t = 1; t < cashFlows.size(); t++) {
            npvDerivative += -t * cashFlows.get(t) / Math.pow(1 + rate, t + 1);
        }
        return npvDerivative;
    }

    public List<IrrDto> calculateHistoricalIRRList(LocalDate dateFrom, LocalDate dateTo, Double startAmount, String ticker) {
        // Inside the calculateHistoricalIRR method
        Symbol symbol = symbolService.getSymbol(ticker);

        List<IrrDto> irrDtos = new ArrayList<>();

        // Calculate number of shares
        List<StockQuote> quotes = stockQuoteService.getListByIdAndDateBetween(symbol, dateFrom, dateTo);

        Double numberOfShares = startAmount / quotes.get(0).getOpen().doubleValue();

        for (int i = 1; i < quotes.size(); i++) {
            IrrDto irrDto = new IrrDto();
            StockQuote lastQuote = quotes.get(i);
            irrDto.setDate(lastQuote.getDate());

            List<Double> cashFlows = new ArrayList<>();
            cashFlows.add(-startAmount); // Initial investment as a negative cash flow

            // Calculate final amount
            Double finalAmount = numberOfShares * lastQuote.getClose().doubleValue();

            // Add the final sale value as a positive cash flow
            cashFlows.add(finalAmount);

            double irr = calculateIRR(cashFlows) * 100; // Leverage an existing IRR calculation method

            irrDto.setValue(irr);

            irrDtos.add(irrDto);
        }


        // Add dividends
//        List<Dividend> dividends = dividendService.getData(symbol, dateFrom.plusDays(1), dateTo);
//        for (int i = 0; i < dividends.size(); i++) {
//            cashFlows.add(numberOfShares * dividends.get(i).getDividendRate().doubleValue()); // Add all dividends as positive cash flows
//        }


        return irrDtos;
    }
}
