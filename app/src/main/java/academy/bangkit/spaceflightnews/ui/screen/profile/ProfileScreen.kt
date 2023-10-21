package academy.bangkit.spaceflightnews.ui.screen.profile

import academy.bangkit.spaceflightnews.OpenUrl
import academy.bangkit.spaceflightnews.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    val author = stringResource(id = R.string.author)
    val authorEmail = stringResource(id = R.string.author_email)

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Avatar(author = author)
            Text(
                text = author,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = authorEmail,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            )
            OpenUrl(
                title = "Dicoding",
                url = "https://www.dicoding.com/users/fikrihandy",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun Avatar(author: String) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = author,
            modifier = Modifier
                .clip(CircleShape)
        )
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}