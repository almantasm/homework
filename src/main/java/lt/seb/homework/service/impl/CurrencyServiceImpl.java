package lt.seb.homework.service.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Currency;
import java.util.List;

import javax.money.MonetaryAmount;
import javax.money.convert.ConversionQuery;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.javamoney.moneta.FastMoney;
import org.springframework.stereotype.Service;

import lt.seb.homework.service.CurrencyService;
import lt.seb.homework.util.Constants;

@Service
public class CurrencyServiceImpl implements CurrencyService {
		
	private List<Currency> currencies;
	
	private ExchangeRateProvider exchangeRateProvider = MonetaryConversions.getExchangeRateProvider(Constants.EXCHANGE_RATE_PROVIDER);
	
	{
	  currencies = Currency.getAvailableCurrencies()
	      .stream()
	      .sorted(Comparator.comparing(Currency::getDisplayName))
	      .collect(Collectors.toList());
	}
	
	@Override
	public List<Currency> getCurrencies() {
		return currencies;
	}

	@Override
	public Boolean isAvailableCurrency(String currencyCodeFrom, String currencyCodeTo) {
		return exchangeRateProvider.isAvailable(currencyCodeFrom, currencyCodeTo);
	}

	@Override
	public ExchangeRate getExchangeRate(String currencyCodeFrom, String currencyCodeTo) {
		return exchangeRateProvider.getExchangeRate(currencyCodeFrom, currencyCodeTo);
	}

	@Override
	public MonetaryAmount getConvertedCurrencies(ExchangeRate exchangeRate, BigDecimal amount) {
		MonetaryAmount base = FastMoney.of(amount, exchangeRate.getBaseCurrency());
		ConversionQuery query = ConversionQueryBuilder.of().setTermCurrency(exchangeRate.getCurrency()).build();
		CurrencyConversion currencyConversion = exchangeRateProvider.getCurrencyConversion(query);
		return currencyConversion.apply(base);
	}
	
}
