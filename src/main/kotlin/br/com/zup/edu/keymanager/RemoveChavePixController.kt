package br.com.zup.edu.keymanager

import br.com.zup.edu.grpc.KeymanagerRemoveGrpcServiceGrpc
import br.com.zup.edu.grpc.RemoveChavePixRequest
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Error
import io.micronaut.http.hateoas.JsonError
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController(private val removeChavePixClient: KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun delete(clienteId: UUID,
               pixId: UUID) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] removendo uma chave pix com $pixId")

        removeChavePixClient.remove(RemoveChavePixRequest.newBuilder()
                                                         .setClienteId(clienteId.toString())
                                                         .setPixId(pixId.toString())
                                                         .build())

        return HttpResponse.ok()
    }

    @Error(exception = StatusRuntimeException::class)
    fun handleGRpcError(request: HttpRequest<Any>,
                        ex: StatusRuntimeException): HttpResponse<Any>? {

        val jsonError = JsonError("Nao foi possivel completar a request devido ao: ${ex.status.description} (${ex.status.code})")

        return HttpResponse
                .unprocessableEntity<JsonError>()
                .body(jsonError)
    }
}

