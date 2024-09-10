package app.prepmymealy.application_server.converter

interface Converter<IN, OUT> {
    fun convert(input: IN): OUT
}