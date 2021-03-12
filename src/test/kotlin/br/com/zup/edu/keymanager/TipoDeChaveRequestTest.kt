package br.com.zup.edu.keymanager

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class TipoDeChaveRequestTest {

    @Nested
    inner class ChaveAleatoriaTest {

        @Test
        fun `deve ser valido quando chave aleatoria for nula ou vazia`() {

            val tipoDechave = TipoDeChaveRequest.ALEATORIA

            assertTrue(tipoDechave.valida(null))
            assertTrue(tipoDechave.valida(""))
        }

        @Test
        fun `nao deve ser valido quando chave aleatoria possuir um valor`() {

            val tipoDechave = TipoDeChaveRequest.ALEATORIA

            assertFalse(tipoDechave.valida("um valor qualquer"))
        }
    }

    @Nested
    inner class CpfTest {

        @Test
        fun `deve ser valido quando cpf for um numero valido`() {

            val tipoDechave = TipoDeChaveRequest.CPF

            assertTrue(tipoDechave.valida("35060731332"))
        }

        @Test
        fun `nao deve ser valido quando cpf for um numero invalido`() {

            val tipoDechave = TipoDeChaveRequest.CPF

            assertFalse(tipoDechave.valida("35060731331"))
        }

        @Test
        fun `nao deve ser valido quando cpf nao for informado`() {

            val tipoDechave = TipoDeChaveRequest.CPF

            assertFalse(tipoDechave.valida(null))
            assertFalse(tipoDechave.valida(""))
        }
    }

    @Nested
    inner class CelularTest {

        @Test
        fun `deve ser valido quando celular for um numero valido`() {

            val tipoDechave = TipoDeChaveRequest.CELULAR

            assertTrue(tipoDechave.valida("+5511987654321"))
        }

        @Test
        fun `nao deve ser valido quando celular for um numero invalido`() {

            val tipoDechave = TipoDeChaveRequest.CELULAR

            assertFalse(tipoDechave.valida("11987654321"))
            assertFalse(tipoDechave.valida("+55a11987654321"))
        }

        @Test
        fun `nao deve ser valido quando celular for um numero nao for informado`() {

            val tipoDechave = TipoDeChaveRequest.CELULAR

            assertFalse(tipoDechave.valida(null))
            assertFalse(tipoDechave.valida(""))
        }
    }

    @Nested
    inner class EmailTest {

        @Test
        fun `deve ser valido quando email for endereco valido`() {

            val tipoDechave = TipoDeChaveRequest.EMAIL

            assertTrue(tipoDechave.valida("zup.edu@zup.com.br"))
        }

        @Test
        fun `nao deve ser valido quando email estiver em um formato invalido`() {

            val tipoDechave = TipoDeChaveRequest.EMAIL

            assertFalse(tipoDechave.valida("zup.eduzup.com.br"))
            assertFalse(tipoDechave.valida("zup.edu@zup.com."))
        }

        @Test
        fun `nao deve ser valido quando email nao for informado`() {

            val tipoDechave = TipoDeChaveRequest.EMAIL

            assertFalse(tipoDechave.valida(null))
            assertFalse(tipoDechave.valida(""))
        }
    }

}
