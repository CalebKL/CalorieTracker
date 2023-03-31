package com.example.core.domain.use_case

//Created this following clean Architecture

class FilterOutDigits {

    operator fun invoke(text:String):String{
        return text.filter { it.isDigit() }
    }
}