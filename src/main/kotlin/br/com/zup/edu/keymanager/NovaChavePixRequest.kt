package br.com.zup.edu.keymanager

import br.com.caelum.stella.validation.CPFValidator
import br.com.zup.edu.grpc.RegistraChavePixRequest
import br.com.zup.edu.grpc.TipoDeChave
import br.com.zup.edu.grpc.TipoDeConta
import br.com.zup.edu.keymanager.shared.validations.ValidPixKey
import io.micronaut.core.annotation.Introspected
import io.micronaut.validation.validator.constraints.EmailValidator
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
class NovaChavePixRequest(@field:NotBlank val tipoDeConta: TipoDeContaRequest,
                          @field:Size(max = 77) val chave: String,
                          @field:NotBlank val tipoDeChave: TipoDeChaveRequest) {

    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest {

        return RegistraChavePixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .setTipoDeConta(tipoDeConta.atributoGrpc)
                .setTipoDeChave(tipoDeChave.atributoGrpc)
                .setChave(chave)
                .build()


    }
}

enum class TipoDeChaveRequest(val atributoGrpc: TipoDeChave) {

    CPF(TipoDeChave.CPF) {

        override fun valida(chave: String?): Boolean {
            if (chave.isNullOrBlank()) {
                return false
            }

            return CPFValidator(false)
                           .invalidMessagesFor(chave)
                           .isEmpty()
        }

    },

    CELULAR(TipoDeChave.CELULAR) {
        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return chave.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },

    EMAIL(TipoDeChave.EMAIL) {

        override fun valida(chave: String?): Boolean {

            if (chave.isNullOrBlank()) {
                return false
            }
            return EmailValidator().run {
                initialize(null)
                isValid(chave, null)
            }

        }
    },

    ALEATORIA(TipoDeChave.ALEATORIA) {
        override fun valida(chave: String?) = chave.isNullOrBlank() // não deve se preenchida
    };

    abstract fun valida(chave: String?): Boolean
}

enum class TipoDeContaRequest(val atributoGrpc: TipoDeConta) {

    CONTA_CORRENTE(TipoDeConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoDeConta.CONTA_POUPANCA)
}
