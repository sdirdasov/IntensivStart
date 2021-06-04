package ru.androidschool.intensiv.ui.tvshows

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_tv_show.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.TvShow

class TvShowItem(
    private val content: TvShow
) : Item() {

    override fun getLayout() = R.layout.item_tv_show

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.description.text = content.title
        viewHolder.tv_show_rating.rating = content.rating

        // TODO Получать из модели
        Picasso.get()
            .load("https://avatars.mds.yandex.net/get-kinopoisk-post-img/1362954/19a50bcb5685f37a5f78b07312a2d818/960x540")
            .into(viewHolder.image_preview)
    }
}
