package com.appboy.sample.imageloading

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import com.appboy.IAppboyImageLoader
import com.appboy.enums.AppboyViewBounds
import com.appboy.models.IInAppMessage
import com.appboy.models.cards.Card
import com.appboy.support.AppboyLogger
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GlideAppboyImageLoader : IAppboyImageLoader {
  private var mRequestOptions = RequestOptions()
  override fun renderUrlIntoCardView(context: Context, card: Card?, imageUrl: String, imageView: ImageView, viewBounds: AppboyViewBounds?) {
    renderUrlIntoView(context, imageUrl, imageView, viewBounds)
  }

  override fun renderUrlIntoInAppMessageView(context: Context,
                                             inAppMessage: IInAppMessage,
                                             imageUrl: String,
                                             imageView: ImageView,
                                             viewBounds: AppboyViewBounds?) {
    renderUrlIntoView(context, imageUrl, imageView, viewBounds)
  }

  override fun getPushBitmapFromUrl(context: Context, extras: Bundle?, imageUrl: String, viewBounds: AppboyViewBounds?): Bitmap {
    return getBitmapFromUrl(context, imageUrl, viewBounds)!!
  }

  override fun getInAppMessageBitmapFromUrl(context: Context, inAppMessage: IInAppMessage, imageUrl: String, viewBounds: AppboyViewBounds?): Bitmap {
    return getBitmapFromUrl(context, imageUrl, viewBounds)!!
  }

  private fun renderUrlIntoView(context: Context, imageUrl: String, imageView: ImageView, viewBounds: AppboyViewBounds?) {
    Glide.with(context)
        .load(imageUrl)
        .apply(mRequestOptions)
        .into(imageView)
  }

  private fun getBitmapFromUrl(context: Context, imageUrl: String, viewBounds: AppboyViewBounds?): Bitmap? {
    try {
      return Glide.with(context)
          .asBitmap()
          .apply(mRequestOptions)
          .load(imageUrl).submit().get()
    } catch (e: Exception) {
      AppboyLogger.e(TAG, "Failed to retrieve bitmap at url: $imageUrl", e)
    }
    return null
  }

  override fun setOffline(isOffline: Boolean) {
    // If the loader is offline, then we should only be retrieving from the cache
    mRequestOptions = mRequestOptions.onlyRetrieveFromCache(isOffline)
  }

  companion object {
    private val TAG = GlideAppboyImageLoader::class.java.name
  }
}
