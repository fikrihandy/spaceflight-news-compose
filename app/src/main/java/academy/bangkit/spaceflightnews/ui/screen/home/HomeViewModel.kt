package academy.bangkit.spaceflightnews.ui.screen.home

import academy.bangkit.spaceflightnews.data.repository.ArticleRepository
import academy.bangkit.spaceflightnews.data.response.Article
import academy.bangkit.spaceflightnews.data.response.GetArticlesResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    var nextArticle: String? = null
    var count: Int = 0

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    var snackBarMessage: String = ""

    fun getArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val articles: GetArticlesResponse = repository.getArticles()
                count = articles.count
                _articles.value = articles.results
                nextArticle = articles.next
                snackBarMessage = "Success, total articles: $count"
                _isLoading.value = false
            } catch (e: Exception) {
                snackBarMessage = "Error, ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun loadMoreArticles(limit: Int, offset: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val newArticles = repository.getArticles(limit, offset)
                if (newArticles.count != count) {
                    count = newArticles.count
                    _articles.value = newArticles.results
                    nextArticle = newArticles.next
                    _isLoading.value = false
                    snackBarMessage =
                        "${newArticles.count - count} new articles have been detected. Retrieve the new articles instead of loading the previous ones"
                    return@launch
                }
                val newListArticles = newArticles.results
                val articles = _articles.value
                if (articles != null) {
                    _articles.value = articles + newListArticles
                }
                nextArticle = newArticles.next
                _isLoading.value = false
                snackBarMessage = "Previous articles loaded"
            } catch (e: Exception) {
                snackBarMessage = "Error, ${e.message}"
                _isLoading.value = false
            }
        }
    }
}