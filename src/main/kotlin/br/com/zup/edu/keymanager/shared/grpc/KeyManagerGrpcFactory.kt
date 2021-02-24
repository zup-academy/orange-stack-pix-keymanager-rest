package br.com.zup.edu.keymanager.shared.grpc

import br.com.zup.edu.grpc.KeymanagerCarregaGrpcServiceGrpc
import br.com.zup.edu.grpc.KeymanagerListaGrpcServiceGrpc
import br.com.zup.edu.grpc.KeymanagerRegistraGrpcServiceGrpc
import br.com.zup.edu.grpc.KeymanagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeymanagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deletaChave() = KeymanagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = KeymanagerListaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun carregaChave() = KeymanagerCarregaGrpcServiceGrpc.newBlockingStub(channel)


}
