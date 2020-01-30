package lt.seb.homework.form;

import java.math.BigDecimal;

public class CurrencyExchangeForm {

	private String currencyCodeFrom;
	
	private String currencyCodeTo;
	
	private BigDecimal amount;

	public String getCurrencyCodeFrom() {
		return currencyCodeFrom;
	}

	public void setCurrencyCodeFrom(String currencyCodeFrom) {
		this.currencyCodeFrom = currencyCodeFrom;
	}

	public String getCurrencyCodeTo() {
		return currencyCodeTo;
	}

	public void setCurrencyCodeTo(String currencyCodeTo) {
		this.currencyCodeTo = currencyCodeTo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
