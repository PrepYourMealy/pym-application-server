package app.prepmymealy.application.converter

interface Converter<IN, OUT> {
    fun convert(input: IN): OUT
}
