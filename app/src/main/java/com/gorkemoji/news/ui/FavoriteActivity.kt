package com.gorkemoji.news.ui

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.gorkemoji.news.R
import com.gorkemoji.news.adapter.FavoriteAdapter
import com.gorkemoji.news.database.AppDatabase
import com.gorkemoji.news.databinding.ActivityFavoriteBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.selectedItemId = R.id.favorites

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, NewsActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        database = AppDatabase.getDatabase(this)
        val favoriteNewsLiveData = database.favoriteNewsDao().getAll()

        favoriteNewsLiveData.observe(this, Observer { favoriteNewsList ->
            adapter = FavoriteAdapter(favoriteNewsList.reversed().toMutableList())
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@FavoriteActivity)
                adapter = this@FavoriteActivity.adapter
                itemAnimator = DefaultItemAnimator()
            }
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition

                when (swipeDir) {
                    ItemTouchHelper.LEFT -> {
                        MainScope().launch {
                            val favoriteNews = adapter.favoriteNewsList[position]
                            database.favoriteNewsDao().delete(favoriteNews)
                            adapter.favoriteNewsList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                        }
                    }
                }
            }

            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.5f
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    setDeleteIcon(c, viewHolder, dX)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }).attachToRecyclerView(binding.recyclerView)

    }

    private fun setDeleteIcon(c: Canvas, viewHolder: RecyclerView.ViewHolder, dX: Float) {
        Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        val itemView : View = viewHolder.itemView

        val mBackground = ColorDrawable().apply {
            color = Color.parseColor("#b80f0a")
            setBounds((itemView.right + dX).toInt(), itemView.top, itemView.right, itemView.bottom)
            draw(c)
        }

        val deleteDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.ic_delete)
        val width: Int = deleteDrawable?.intrinsicWidth ?: 0
        val height: Int = deleteDrawable?.intrinsicHeight ?: 0

        val itemHeight: Int = itemView.height
        val deleteIconTop: Int = itemView.top + (itemHeight - height) / 2
        val deleteIconMargin: Int = (itemHeight - height) / 2
        val deleteIconLeft: Int = itemView.right - deleteIconMargin - width
        val deleteIconRight: Int = itemView.right - deleteIconMargin
        val deleteIconBottom: Int = deleteIconTop + height

        deleteDrawable?.bounds = Rect(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteDrawable?.draw(c)
    }
}
