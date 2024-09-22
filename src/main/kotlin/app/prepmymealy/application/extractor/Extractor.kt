package app.prepmymealy.application.extractor

interface Extractor<IN, OUT> {
    fun extract(input: IN, id: String): OUT
}