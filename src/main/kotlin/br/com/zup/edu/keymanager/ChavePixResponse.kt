package br.com.zup.edu.keymanager

import br.com.zup.edu.grpc.ListaChavesPixResponse
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ChavePixResponse(chavePix: ListaChavesPixResponse.ChavePix) {

    init {

        val id = chavePix.pixId
        val chave = chavePix.chave
        val tipo = chavePix.tipo
        val tipoDeConta = chavePix.tipoDeConta
        val criadaEm = chavePix.criadaEm.let {
            LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
        }
    }
}