package ru.netology.nerecipe

import org.junit.Test
import org.junit.Assert.*

//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import ru.netology.nerecipe.dto.RecipesFilled
//import ru.netology.nerecipe.viewmodel.RecipeViewModel

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

//class FeedFragmentUnitTest: Fragment() {
//    private val viewModel: RecipeViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )
//    @Test
//    fun search() {
//        val data = viewModel.data.value
//        val recipe = RecipesFilled.empty
//        viewModel.getByFilterOnName("%asia%")
//        assertNotEquals(listOf(recipe),data)
//    }
//}