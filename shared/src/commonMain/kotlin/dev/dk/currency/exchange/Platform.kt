package dev.dk.currency.exchange

expect val platform: String

class Greeting {
    fun greeting() = "Hello, $platform!"
}