package lt.seb.homework.validator;

import java.math.BigDecimal;

import javax.money.UnknownCurrencyException;
import javax.money.convert.CurrencyConversionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import lt.seb.homework.form.CurrencyExchangeForm;
import lt.seb.homework.service.CurrencyService;

@Component("currencyExchangeFormValidator")
public class CurrencyExchangeFormValidator implements Validator {

	private static final Logger _LOG = LoggerFactory.getLogger(CurrencyExchangeFormValidator.class);

	@Autowired
	private CurrencyService currencyService;

	@Override
	public boolean supports(Class<?> clazz) {
		return CurrencyExchangeForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		_LOG.debug("validating");
		CurrencyExchangeForm form = (CurrencyExchangeForm) target;
		if (StringUtils.isEmpty(form.getCurrencyCodeFrom())) {
			errors.reject("error.emptyCurrencyFrom");
		}
		if (StringUtils.isEmpty(form.getCurrencyCodeTo())) {
			errors.reject("error.emptyCurrencyTo");
		}
		if (!errors.hasErrors()) {
			try {
				if (!currencyService.isAvailableCurrency(form.getCurrencyCodeFrom(), form.getCurrencyCodeTo())) {
					addNotAvailableError(errors);
				} else {
					try {
						// isAvailable, but sometimes returns CurrencyConversionException or NPE??
						if (currencyService.getExchangeRate(form.getCurrencyCodeFrom(),
								form.getCurrencyCodeTo()) == null) {
							addNotAvailableError(errors);
						}
					} catch (CurrencyConversionException | NullPointerException ex) {
						addNotAvailableError(errors);
					}
				}
			} catch (UnknownCurrencyException e) {
				addNotAvailableError(errors);
			}
		}
		if (form.getAmount() == null || BigDecimal.ZERO.compareTo(form.getAmount()) > -1) {
			errors.reject("error.amount");
		}
	}

	private void addNotAvailableError(Errors errors) {
		errors.reject("error.notAvailable");
	}

}
