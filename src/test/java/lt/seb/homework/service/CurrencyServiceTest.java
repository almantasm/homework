package lt.seb.homework.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Currency;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import lt.seb.homework.service.impl.CurrencyServiceImpl;

@RunWith(SpringRunner.class)
public class CurrencyServiceTest {

	@TestConfiguration
    static class CurrencyServiceImplTestContextConfiguration {
  
        @Bean
        public CurrencyService currencyService() {
            return new CurrencyServiceImpl();
        }
    }
 
    @Autowired
    private CurrencyService currencyService;
    
    @Test
    public void test_getCurrencies() {
    	List<Currency> currencies = currencyService.getCurrencies();
    	assertNotNull(currencies);
    	assertNotEquals(currencies.size(), 0);
    }
    
    @Test
    public void test_mainCurrenciesConversionAvailable() {
    	assertTrue(currencyService.isAvailableCurrency("EUR", "USD"));
    }
	
}
