package com.goodwy.audiobook.features.bookOverview

import android.app.Dialog
import android.os.Bundle
import androidx.core.view.doOnLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.bluelinelabs.conductor.Controller
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.goodwy.audiobook.R
import com.goodwy.audiobook.data.Book
import com.goodwy.audiobook.data.repo.BookRepository
import com.goodwy.audiobook.databinding.BookMoreBottomSheetBinding
import com.goodwy.audiobook.features.bookmarks.BookmarkController
import com.goodwy.audiobook.injection.appComponent
import com.goodwy.audiobook.misc.DialogController
import com.goodwy.audiobook.misc.RouterProvider
import com.goodwy.audiobook.misc.conductor.asTransaction
import com.goodwy.audiobook.misc.getUUID
import com.goodwy.audiobook.misc.putUUID
import timber.log.Timber
import javax.inject.Inject

/**
 * Bottom sheet dialog fragment that will be displayed when a book edit was requested
 */
class EditBookBottomSheetController(args: Bundle) : DialogController(args) {

  @Inject
  lateinit var repo: BookRepository

  private val bookId = args.getUUID(NI_BOOK)

  override fun onCreateDialog(savedViewState: Bundle?): Dialog {
    appComponent.inject(this)

    val dialog = BottomSheetDialog(activity!!)

    // if there is no book, skip here
    val book = repo.bookById(bookId)
    if (book == null) {
      Timber.e("book is null. Return early")
      return dialog
    }

    val binding = BookMoreBottomSheetBinding.inflate(activity!!.layoutInflater)
    dialog.setContentView(binding.root)

    BottomSheetBehavior.from(dialog.findViewById(R.id.design_bottom_sheet)!!).apply {
      binding.root.doOnLayout {
        peekHeight = it.height
      }
    }

    binding.title.setOnClickListener {
      val router = (activity as RouterProvider).provideRouter()
      EditBookTitleDialogController(book).showDialog(router)
      dismissDialog()
    }
    binding.author.setOnClickListener {
      val router = (activity as RouterProvider).provideRouter()
      EditBookAuthorDialogController(book).showDialog(router)
      dismissDialog()
    }
    binding.internetCover.setOnClickListener {
      callback().onInternetCoverRequested(book)
      dismissDialog()
    }
    binding.fileCover.setOnClickListener {
      callback().onFileCoverRequested(book)
      dismissDialog()
    }
    binding.bookmark.setOnClickListener {
      val router = (activity as RouterProvider).provideRouter()
      val controller = BookmarkController(book.id)
      router.pushController(controller.asTransaction())

      dismissDialog()
    }
    binding.deleteFile.setOnClickListener {
      showDeleteFileDialog()
      dismissDialog()
    }

    return dialog
  }

  private fun callback() = targetController as Callback

  companion object {
    private const val NI_BOOK = "ni#book"
    operator fun <T> invoke(
      target: T,
      book: Book
    ): EditBookBottomSheetController where T : Controller, T : Callback {
      val args = Bundle().apply {
        putUUID(NI_BOOK, book.id)
      }
      return EditBookBottomSheetController(args).apply {
        targetController = target
      }
    }
  }

  interface Callback {
    fun onInternetCoverRequested(book: Book)
    fun onFileCoverRequested(book: Book)
    fun onFileDeletionRequested(book: Book)
  }

  // Dialogs Special Thanks
  private fun showDeleteFileDialog() {
    val book = repo.bookById(bookId)
    if (book == null) {
      Timber.e("book is null. Return early")
    }
    val bookContent = book?.content
    val currentFile = bookContent?.currentFile
    MaterialDialog(activity!!)
      .title(R.string.delete_file)
      .message(text = currentFile.toString()) {
        html()
        lineSpacing(1.2f)
      }
      .negativeButton(R.string.dialog_cancel)
      .positiveButton(R.string.dialog_ok) {
        if (book != null) {
          callback().onFileDeletionRequested(book)
          activity!!.recreate()
        } else Timber.e("book is null. Return early")
      }
       .icon(R.drawable.delete)
      .show()
  }
}
