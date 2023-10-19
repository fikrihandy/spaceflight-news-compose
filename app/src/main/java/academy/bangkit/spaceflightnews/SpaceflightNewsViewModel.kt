package academy.bangkit.spaceflightnews

import academy.bangkit.spaceflightnews.data.repository.ArticleRepository
import academy.bangkit.spaceflightnews.data.response.Article
import academy.bangkit.spaceflightnews.data.response.GetArticlesResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SpaceflightNewsViewModel(private val repository: ArticleRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    var nextArticle: String? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getArticles() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val articles: GetArticlesResponse = repository.getArticles()
                _articles.value = articles.results //List of Article
                nextArticle = articles.next //Next Article (String)
                _isLoading.value = false
            } catch (e: Exception) {
                // Handle error
                _isLoading.value = false
            }
        }
    }

    fun loadMoreArticles(limit: Int, offset: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            val newArticles = repository.getArticles(limit, offset)
            val newListArticles = newArticles.results
            val articles = _articles.value
            if (articles != null) {
                _articles.value = articles + newListArticles
            }
            nextArticle = newArticles.next
            _isLoading.value = false
        }
    }

}

class ViewModelFactory(private val repository: ArticleRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SpaceflightNewsViewModel::class.java)) {
            return SpaceflightNewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}