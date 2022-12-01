package dev.dk.currency.exchange.data.model

import kotlinx.serialization.Serializable

class OnResultObtained<T>(val result:T?,val isLoaded:Boolean,error:String? = null)

@Serializable
class ExchangeRate(val base: String, val rates : Map<String?,Double>?)

//@Serializable
//class Currency(val currencies : Map<String,String>)