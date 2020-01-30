package lt.seb.homework.service;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import javax.money.MonetaryAmount;
import javax.money.convert.ExchangeRate;

public interface CurrencyService {

	List<Currency> getCurrencies(); 
	
	Boolean isAvailableCurrency(String currencyCodeFrom, String currencyCodeTo);
	
	ExchangeRate getExchangeRate(String currencyCodeFrom, String currencyCodeTo);
	
	MonetaryAmount getConvertedCurrencies(ExchangeRate exchangeRate, BigDecimal amount);
	
}
