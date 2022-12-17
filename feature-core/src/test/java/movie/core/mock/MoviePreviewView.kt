package movie.core.mock

import movie.core.db.model.MoviePreviewView
import java.util.Date

fun MoviePreviewView() = mockFinal<MoviePreviewView> {
    override { Date(0) }
    set(MoviePreviewView::id, "id")
}