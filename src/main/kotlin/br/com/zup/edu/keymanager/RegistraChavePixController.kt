package br.com.zup.edu.keymanager

import br.com.zup.edu.grpc.KeymanagerRegistraGrpcServiceGrpc
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.annotation.Post
import io.micronaut.http.hateoas.JsonError
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes/{clienteId}") // 1
class RegistraChavePixController(private val registraChavePixClient: KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceBlockingStub) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun create(clienteId: UUID,
               @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {

        LOGGER.info("[$clienteId] criando uma nova chave pix com $request")

        val grpcResponse = registraChavePixClient.registra(request.paraModeloGrpc(clienteId))

        val location = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${grpcResponse.pixId}")

        return HttpResponse.created(location)
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
