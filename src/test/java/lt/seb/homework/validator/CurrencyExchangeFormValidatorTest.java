package lt.seb.homework.validator;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import javax.money.CurrencyUnit;
import javax.money.NumberValue;
import javax.money.convert.ConversionContext;
import javax.money.convert.ExchangeRate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import lt.seb.homework.form.CurrencyExchangeForm;
import lt.seb.homework.service.CurrencyService;

@RunWith(SpringRunner.class)
public class CurrencyExchangeFormValidatorTest {

	@TestConfiguration
	static class CurrencyExchangeFormValidatorTestContextConfiguration {

		@Bean
		public Validator validator() {
			return new CurrencyExchangeFormValidator();
		}
	}

	@Autowired
	private Validator validator;

	@MockBean
	private CurrencyService currencyService;

	@Test
	public void test_emptyForm() {
		CurrencyExchangeForm form = new CurrencyExchangeForm();
		Mockito.when(currencyService.isAvailableCurrency(form.getCurrencyCodeFrom(), form.getCurrencyCodeTo()))
				.thenReturn(false);
		BindingResult bindingResult = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, bindingResult);
		assertTrue(bindingResult.hasErrors());
	}

	@Test
	public void test_fakeCurrencyCodes() {
		CurrencyExchangeForm form = new CurrencyExchangeForm();
		form.setCurrencyCodeFrom("AAA");
		form.setCurrencyCodeTo("BBB");
		Mockito.when(currencyService.isAvailableCurrency(form.getCurrencyCodeFrom(), form.getCurrencyCodeTo()))
				.thenReturn(false);
		BindingResult bindingResult = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, bindingResult);
		assertTrue(bindingResult.hasErrors());
	}

	@Test
	public void test_nullAmount() {
		CurrencyExchangeForm form = new CurrencyExchangeForm();
		form.setCurrencyCodeFrom("EUR");
		form.setCurrencyCodeTo("USD");
		Mockito.when(currencyService.isAvailableCurrency(form.getCurrencyCodeFrom(), form.getCurrencyCodeTo()))
				.thenReturn(true);
		mockExchangeRate(currencyService, form);
		BindingResult bindingResult = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, bindingResult);
		assertTrue(bindingResult.hasErrors());
	}

	@Test
	public void test_validForm() {
		CurrencyExchangeForm form = new CurrencyExchangeForm();
		form.setCurrencyCodeFrom("EUR");
		form.setCurrencyCodeTo("USD");
		form.setAmount(BigDecimal.TEN);
		Mockito.when(currencyService.isAvailableCurrency(form.getCurrencyCodeFrom(), form.getCurrencyCodeTo()))
				.thenReturn(true);
		mockExchangeRate(currencyService, form);
		BindingResult bindingResult = new BeanPropertyBindingResult(form, "form");
		validator.validate(form, bindingResult);
		assertTrue(!bindingResult.hasErrors());
	}

	private void mockExchangeRate(CurrencyService currencyService, CurrencyExchangeForm form) {
		Mockito.when(currencyService.getExchangeRate(form.getCurrencyCodeFrom(), form.getCurrencyCodeTo()))
				.thenReturn(new ExchangeRate() {

					@Override
					public NumberValue getFactor() {
						return null;
					}

					@Override
					public List<ExchangeRate> getExchangeRateChain() {
						return null;
					}

					@Override
					public CurrencyUnit getCurrency() {
						return null;
					}

					@Override
					public ConversionContext getContext() {
						return null;
					}

					@Override
					public CurrencyUnit getBaseCurrency() {
						return null;
					}
				});
	}

}
