package voice.migration.views

import java.time.Instant

internal data class MigrationViewState(
  val items: List<Item>,
  val paddings: String,
  val onDeleteClicked: () -> Unit,
  val showDeletionConfirmationDialog: Boolean,
  val onDeletionConfirmed: () -> Unit,
  val onDeletionAborted: () -> Unit,
) {
  data class Item(
    val name: String,
    val root: String,
    val bookmarks: List<Bookmark>,
    val position: Position,
  ) {

    data class Bookmark(
      val position: Position,
      val title: String?,
      val addedAt: Instant?,
    )
  }

  data class Position(
    val currentChapter: String,
    val positionInChapter: String,
    val currentFile: String,
    val positionInFile: String,
  )
}
