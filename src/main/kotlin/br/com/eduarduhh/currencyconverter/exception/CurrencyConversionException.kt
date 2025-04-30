package br.com.eduarduhh.currencyconverter.exception

class CurrencyConversionException(
    val currencyEnum: CurrencyEnum,
    override val message: String,
    private val exception: Exception?) :
    RuntimeException(){
        constructor(currencyEnum: CurrencyEnum,  message: String) :this(currencyEnum, message, null)

}