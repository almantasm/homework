package lt.seb.homework.controller;

import javax.money.convert.ExchangeRate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lt.seb.homework.form.CurrencyExchangeForm;
import lt.seb.homework.service.CurrencyService;
import lt.seb.homework.util.Constants;

@Controller
public class CurrencyExchangeController {

	private static final Logger _LOG = LoggerFactory.getLogger(CurrencyExchangeController.class);

	@Autowired
	@Qualifier("currencyExchangeFormValidator")
	private Validator validator;

	@Autowired
	private CurrencyService currencyService;

	@GetMapping(Constants.MAPPING_HOMEPAGE)
	public String get(@ModelAttribute("currencyExchangeForm") CurrencyExchangeForm form, Model model) {
		addBaseAttributes(model, new CurrencyExchangeForm());
		return Constants.TEMPLATE_HOMEPAGE;
	}

	@PostMapping(Constants.MAPPING_HOMEPAGE)
	public String post(@ModelAttribute("currencyExchangeForm") CurrencyExchangeForm form,
			BindingResult bindingResult, Model model) {

		validator.validate(form, bindingResult);
		if (bindingResult.hasErrors()) {
			_LOG.error("Error in validation");
		} else {
			currencyService.getExchangeRate(form.getCurrencyCodeFrom(), form.getCurrencyCodeTo());
			ExchangeRate exchangeRate = currencyService.getExchangeRate(form.getCurrencyCodeFrom(),
					form.getCurrencyCodeTo());
			model.addAttribute("exchangeRate", exchangeRate);
			model.addAttribute("convertedAmount",
					currencyService.getConvertedCurrencies(exchangeRate, form.getAmount()));
		}
		addBaseAttributes(model, form);
		return Constants.TEMPLATE_HOMEPAGE;
	}

	private void addBaseAttributes(Model model, CurrencyExchangeForm form) {
		model.addAttribute("currencies", currencyService.getCurrencies());
		model.addAttribute("currencyExchangeForm", form);
	}

}
