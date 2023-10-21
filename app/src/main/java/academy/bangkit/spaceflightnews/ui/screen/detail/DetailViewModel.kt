package academy.bangkit.spaceflightnews.ui.screen.detail

import academy.bangkit.spaceflightnews.data.repository.ArticleRepository
import academy.bangkit.spaceflightnews.data.response.Article
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _article = MutableLiveData<Article>()
    val article: LiveData<Article> = _article

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    fun getArticle(articleId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val article = repository.getArticle(articleId)
                Log.d("article", "getArticle: $article")
                _article.value = article
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }
}