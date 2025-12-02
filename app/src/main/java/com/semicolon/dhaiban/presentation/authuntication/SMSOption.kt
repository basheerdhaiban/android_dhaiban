package com.semicolon.dhaiban.presentation.authuntication

//enum class SMSOption(val value: Int) {
//    Email(0),
//    SMS(1),
//    WhatsApp(2)
//}
enum class SMSOption(val label: String, val value: Int) {
    WhatsApp("WhatsApp", 2),
    Email("Email", 0),
    SMS("SMS", 1)
}