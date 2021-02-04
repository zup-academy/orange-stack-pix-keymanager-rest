package br.com.zup.edu.keymanager

import br.com.zup.edu.grpc.*
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class CadastraChavePixController(val grpcClient : KeymanagerGrpcServiceGrpc.KeymanagerGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(CadastraChavePixController::class.java)

    @Post("/pix")
    fun create(clienteId: UUID,
               @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {

        LOGGER.info("[$clienteId] criando uma nova chave pix com $request")

        val grpcResponse = grpcClient.registra(request.paraModeloGrpc(clienteId))

       // TODO: Caso der erro na chamada gRpc capturar aqui

        val location = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${grpcResponse.pixId}")
        return HttpResponse.created(location)
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