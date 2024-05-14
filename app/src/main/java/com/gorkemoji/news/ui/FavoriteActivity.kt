package com.gorkemoji.news.ui

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gorkemoji.news.R
import com.gorkemoji.news.adapter.FavoriteAdapter
import com.gorkemoji.news.databinding.ActivityFavoriteBinding
import com.gorkemoji.news.viewmodel.FavoriteViewModel
import com.gorkemoji.news.viewmodel.FavoriteViewModelFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private val viewModel: FavoriteViewModel by viewModels {
        FavoriteViewModelFactory(applicationContext)
    }

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

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FavoriteAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = DefaultItemAnimator()

        viewModel.favoriteNewsLiveData.observe(this) { favoriteNewsList ->
            adapter.setData(favoriteNewsList)
        }

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
                            viewModel.deleteFavoriteNews(favoriteNews)
                        }
                        showToast(resources.getString(R.string.deleted))
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
            xfermode = android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR)
        }

        val itemView: View = viewHolder.itemView

        ColorDrawable().apply {
            color = Color.parseColor("#b80f0a")
            setBounds((itemView.right + dX).toInt(), itemView.top, itemView.right, itemView.bottom)
            draw(c)
        }

        val deleteDrawable: Drawable? = ContextCompat.getDrawable(this@FavoriteActivity, R.drawable.ic_delete)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
