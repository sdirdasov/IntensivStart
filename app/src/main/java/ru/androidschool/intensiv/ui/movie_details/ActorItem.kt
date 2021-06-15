package ru.androidschool.intensiv.ui.movie_details

import com.squareup.picasso.Picasso
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.item_actor.*
import ru.androidschool.intensiv.R
import ru.androidschool.intensiv.data.Actor

class ActorItem(
    private val content: Actor
) : Item() {

    override fun getLayout() = R.layout.item_actor

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.actor_name.text = content.name
            .split(" ", limit = 2)
            .joinToString("\n")
        Picasso.get()
            .load(content.profilePath)
            .transform(CropCircleTransformation())
            .into(viewHolder.actor_preview)
    }
}
