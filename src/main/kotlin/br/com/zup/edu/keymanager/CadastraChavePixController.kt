package br.com.zup.edu.keymanager

import br.com.zup.edu.grpc.*
import br.com.zup.edu.grpc.KeymanagerGrpcServiceGrpc.KeymanagerGrpcServiceBlockingStub
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.core.annotation.Introspected
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class CadastraChavePixController(val grpcClient : KeymanagerGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(CadastraChavePixController::class.java)

    @Post("/pix")
    fun create(clienteId: UUID,
               @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {

        LOGGER.info("[$clienteId] criando uma nova chave pix com $request")

        try {
            val grpcResponse = grpcClient.registra(request.paraModeloGrpc(clienteId))

            val location = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${grpcResponse.pixId}")

            return HttpResponse.created(location)

        } catch (e: StatusRuntimeException) {
            return when (e.status.code) {
                Status.Code.INVALID_ARGUMENT -> HttpResponse.badRequest()

                else -> HttpResponse.serverError()
            }
        }
    }

}

@Introspected
class NovaChavePixRequest(@field:NotBlank val tipoDeConta: TipoDeContaRequest,
                          @field:Size(max = 77) val chave: String,
                          @field:NotBlank  val tipoDeChave: TipoDeChaveRequest) {

    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest {

        return RegistraChavePixRequest.newBuilder()
                                      .setClienteId(clienteId.toString())
                                      .setTipoDeConta(tipoDeConta.paraRequestGrpc())
                                      .setTipoDeChave(tipoDeChave.paraRequestGrpc())
                                      .setChave(chave)
                                      .build()


    }
}

enum class TipoDeChaveRequest {
    CPF {
        override fun paraRequestGrpc() = TipoDeChave.CPF
    },
    CELULAR {
        override fun paraRequestGrpc() = TipoDeChave.CELULAR
    },
    EMAIL {
        override fun paraRequestGrpc() = TipoDeChave.EMAIL
    },
    ALEATORIA {
        override fun paraRequestGrpc() = TipoDeChave.ALEATORIA
    };

    abstract fun paraRequestGrpc() : TipoDeChave
}

enum class TipoDeContaRequest {
    CONTA_CORRENTE {
        override fun paraRequestGrpc() = TipoDeConta.CONTA_CORRENTE
    },
    CONTA_POUPANCA {
        override fun paraRequestGrpc() = TipoDeConta.CONTA_POUPANCA
    };

    abstract fun paraRequestGrpc() : TipoDeConta
}


@Factory
class KeymanagerGrpcFactory {

    @Singleton
    fun keymanagerService(@GrpcChannel("keyManager") channel: ManagedChannel) : KeymanagerGrpcServiceBlockingStub {

        return KeymanagerGrpcServiceGrpc.newBlockingStub(channel)
    }
}
