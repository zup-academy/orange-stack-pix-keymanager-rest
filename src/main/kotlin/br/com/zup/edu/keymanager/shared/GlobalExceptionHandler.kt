package br.com.zup.edu.keymanager.shared

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class GlobalExceptionHandler : ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>, exception: StatusRuntimeException): HttpResponse<Any> {

        val statusCode = exception.status.code
        val statusDescription = exception.status.description ?: ""
        val (httpStatus, message) = when (statusCode) {
            Status.NOT_FOUND.code -> Pair(HttpStatus.NOT_FOUND, statusDescription)
            Status.INVALID_ARGUMENT.code -> Pair(HttpStatus.BAD_REQUEST, "Dados da requisição estão inválidos") // TODO: melhoria: extrair detalhes do erro
            Status.ALREADY_EXISTS.code -> Pair(HttpStatus.UNPROCESSABLE_ENTITY, statusDescription)
            else ->  {
                LOGGER.error("Erro inesperado '${exception.javaClass.name}' ao processar requisição", exception)
                Pair(HttpStatus.INTERNAL_SERVER_ERROR, "Nao foi possivel completar a requisição devido ao erro: ${statusDescription} (${statusCode})")
            }
        }

        return HttpResponse
                .status<JsonError>(httpStatus)
                .body(JsonError(message))
    }
}