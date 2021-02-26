package br.com.zup.edu.keymanager

import br.com.zup.edu.grpc.CarregaChavePixRequest
import br.com.zup.edu.grpc.KeymanagerCarregaGrpcServiceGrpc
import br.com.zup.edu.grpc.KeymanagerListaGrpcServiceGrpc
import br.com.zup.edu.grpc.ListaChavesPixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class CarregaChavePixController(val carregaChavePixClient: KeymanagerCarregaGrpcServiceGrpc.KeymanagerCarregaGrpcServiceBlockingStub,
                                val listaChavesPixClient: KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceBlockingStub)  {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carrega(clienteId: UUID,
                pixId: UUID) : HttpResponse<Any> {

        val chaveResponse = carregaChavePixClient.carrega(CarregaChavePixRequest.newBuilder()
                                                            .setPixId(CarregaChavePixRequest.FiltroPorPixId.newBuilder()
                                                                    .setClienteId(clienteId.toString())
                                                                    .setPixId(pixId.toString())
                                                                    .build()).
                                                            build())


        return HttpResponse.ok(DetalheChavePixResponse(chaveResponse))
    }


    @Get("/pix/")
    fun lista(clienteId: UUID) : HttpResponse<Any> {

        LOGGER.info("[$clienteId] listando chaves pix com")

        val pix = listaChavesPixClient.lista(ListaChavesPixRequest.newBuilder()
                                                        .setClienteId(clienteId.toString())
                                                        .build())

        val chaves = pix.chavesList.map { ChavePixResponse(it) }
        return HttpResponse.ok(chaves)
    }
}
