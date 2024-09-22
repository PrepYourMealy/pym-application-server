package app.prepmymealy.application.acceptance

import app.prepmymealy.application.controller.payload.ShoppingListItemPayload
import app.prepmymealy.application.controller.payload.ShoppingListPayload
import app.prepmymealy.application.domain.menu.ShoppingList
import app.prepmymealy.application.domain.menu.ShoppingListItem
import app.prepmymealy.application.repository.ShoppingListRepository
import app.prepmymealy.application.testsupport.AbstractSpringTest
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class ShoppingListControllerAcceptanceTest : AbstractSpringTest() {

    @Autowired
    private lateinit var listRepository: ShoppingListRepository

    @BeforeMethod
    fun setUp() {
        listRepository.deleteAll()
    }

    @Test
    fun `should return shopping list`() {
        // given
        val menu = listRepository.save(ShoppingList(
            id = "user_qweqweqwe",
            list = listOf(
                ShoppingListItem(
                    name = "ingredient1",
                    quantity = 1,
                    unit = "unit1",
                    price = 12.0,
                    origin = "ALDI",
                    bought = false
                )
            )
        ))

        // when
        val response = api.getListById(menu.id)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo("{" +
                "\"total\":12.0," +
                "\"items\":[" +
                    "{" +
                        "\"name\":\"ingredient1\"," +
                        "\"price\":12.0," +
                        "\"quantity\":1," +
                        "\"unit\":\"unit1\"," +
                        "\"origin\":\"ALDI\"," +
                        "\"bought\":false" +
                    "}" +
                "]" +
        "}")
    }

    @Test
    fun `should update bought status`() {
        // given
        val menu = listRepository.save(ShoppingList(
            id = "user_qweqweqwe",
            list = listOf(
                ShoppingListItem(
                    name = "ingredient1",
                    quantity = 1,
                    unit = "unit1",
                    price = 12.0,
                    origin = "ALDI",
                    bought = false
                )
            )
        ))

        // when
        val response = api.putList(menu.id, ShoppingListPayload(
            items = listOf(
                ShoppingListItemPayload(
                    name = "ingredient1",
                    quantity = 1,
                    unit = "unit1",
                    price = 12.0,
                    origin = "ALDI",
                    bought = true
                )
            )
        ))

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(listRepository.findById(menu.id).get().list[0].bought).isTrue()
    }

}