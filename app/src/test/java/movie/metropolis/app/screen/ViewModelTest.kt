package movie.metropolis.app.screen

import org.junit.Before

abstract class ViewModelTest {

    abstract fun prepare()

    @Before
    fun prepareInternal() {
        prepare()
    }

}