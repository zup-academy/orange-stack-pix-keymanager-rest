package br.com.zup.edu

import io.micronaut.runtime.Micronaut.*
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
		info = Info(
				title = "keymanager",
				version = "1.0"
		)
)
object Aplication

fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zup.edu")
		.start()
}

